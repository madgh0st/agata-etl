package com.kpmg.agata.processors.validation;

import com.kpmg.agata.config.Environment;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.constant.database.CollisionsReportTableConstants;
import com.kpmg.agata.constant.database.ValidationReportsTablesConstants;
import com.kpmg.agata.constant.database.ValidationTablesConstants;
import com.kpmg.agata.utils.filesystem.FileFinder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.types.DataTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kpmg.agata.constant.LogConstants.END_OF;
import static com.kpmg.agata.constant.LogConstants.START_OF;
import static com.kpmg.agata.constant.LogConstants.VALIDATION_REPORT;
import static com.kpmg.agata.constant.database.BaseConstants.EVENTDATE;
import static com.kpmg.agata.constant.database.BaseConstants.FILENAME;
import static com.kpmg.agata.constant.database.BaseConstants.FILEPATH;
import static com.kpmg.agata.constant.database.BaseConstants.NAME_DO;
import static com.kpmg.agata.constant.database.BaseConstants.PARQUET;
import static com.kpmg.agata.constant.database.BaseConstants.TABLE_NAME;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_CREDIT_CORRECTION_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_CREDIT_LIMIT_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_EXPRESS_ANALYSIS_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_FNS_ARREARS_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_FNS_TAX_VIOLATION_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_LEGAL_CASES_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_PAYMENT_FROM_CUSTOMER_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_PDZ_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_REALIZATION_SERVICES_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_SPARK_INTERFAX_SUMMARY_TABLE;
import static com.kpmg.agata.constant.database.CollisionsReportTableConstants.COLLISIONS_PREFIX;
import static com.kpmg.agata.constant.database.MdTablesConstants.MD_CONTRACTOR_DICT_TABLE;
import static com.kpmg.agata.constant.database.UnionTableConstants.UNION_CREDIT_LIMIT_TABLE;
import static com.kpmg.agata.constant.database.UnionTableConstants.UNION_PDZ_TABLE;
import static com.kpmg.agata.utils.Utils.withSchema;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.spark.sql.expressions.Window.partitionBy;
import static org.apache.spark.sql.functions.callUDF;
import static org.apache.spark.sql.functions.coalesce;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.round;
import static org.apache.spark.sql.functions.row_number;
import static org.apache.spark.sql.functions.sum;
import static org.apache.spark.sql.functions.trim;
import static org.apache.spark.sql.functions.when;

public class ValidationReportProcessor implements Serializable {
    private static final String SRC_CHECK_SUM = "src_check_sum";
    private static final String COUNT_EMPTY_EVENT_DATE = "count_empty_eventdate";
    private static final String MODIFICATION_DATE = "modificationDate";
    private static final String CHECK_GLOB = "check_glob";
    private static final String SOURCE_AND_DO_FOR_CHECKSUM = "source_and_do_for_src_checksum";
    private static final String SOURCE_CATEGORY = "sourceCategory";
    private static final String RAW_COUNT = "rawCount";
    private static final String HIVE_COUNT = "hiveCount";
    private static final String ERROR_STATUS = "error_status";
    private static final String USE_DO = "use_do";
    private static final String DO_NAME = "doname";
    private static final String AVAILABLE_DO = "available_do";

    private static final Logger log = LoggerFactory.getLogger(ValidationReportProcessor.class);
    private static String pattern = "/\\d{4}-\\d{2}-\\d{2}(/.*)/";
    private static Pattern regexPattern = Pattern.compile(pattern);
    private final String hiveSchema;
    private final String outputDir;
    private final String currentDatetime = LocalDateTime.now().format(ofPattern("yyyy-MM-dd_HH-mm-ss"));
    private final SparkSession sparkSession;
    private List<ModelTypeConfiguration> config;

    public ValidationReportProcessor(SparkSession sparkSession, String outputDir, String hiveSchema,
                                     List<ModelTypeConfiguration> config) {
        this.hiveSchema = hiveSchema;
        this.outputDir = outputDir;
        this.sparkSession = sparkSession;
        this.config = config;
    }

    public static String getParsedSourceAndDoForSrcChecksum(String filePath, String nameFile) {
        String cleanString;
        try {
            Matcher m = regexPattern.matcher(filePath.replace(nameFile, ""));
            cleanString = m.find() ? m.group(1) : "";
        } catch (Exception ex) {
            cleanString = "";
        }
        return cleanString;
    }

    public static RawConfigGlobModel getParsedSourceAndDoForConfig(ModelTypeConfiguration config) {
        return new RawConfigGlobModel(
                config.getInputDir()
                        .replace(Environment.getConfig().getProperty("hdfs.dir.src.xls"), "")
                        .replace("${date}", "")
                        .trim(),
                config.getGlob());
    }

    private void saveReport(String name, Dataset<Row> dataset) {
        log.info("{}{}: {}", START_OF, VALIDATION_REPORT, name);

        dataset.cache();

        dataset
                .write()
                .format(PARQUET)
                .mode(SaveMode.Overwrite)
                .option("path", format("%s/parquet/%s", this.outputDir, name))
                .saveAsTable(hiveSchema + "." + name);

        dataset
                .repartition(1)
                .write()
                .format("csv")
                .mode(SaveMode.Overwrite)
                .option("header", "true")
                .save(format("%s/csv/%s/%s", this.outputDir, currentDatetime, name));

        log.info("{}{}: {}", END_OF, VALIDATION_REPORT, name);
    }

    private void createDataImportFullReport() {
        log.info("{}{}: {}", START_OF, VALIDATION_REPORT, "createDataImportFullReport");

        Dataset<Row> downloadedToHiveRawTables = config.stream()
                .map(ModelTypeConfiguration::getHiveTableName)
                .distinct()
                .map(tableName -> sparkSession.table(withSchema(hiveSchema, tableName))
                        .select(lit(tableName).as(TABLE_NAME),
                                col(FILENAME).as(FILEPATH),
                                col(MODIFICATION_DATE))
                        .distinct())
                .reduce(Dataset::union)
                .orElseThrow(() -> new RuntimeException("Incorrect DataImportReportSQL"));

        Dataset<Row> srcCheckSum = this.sparkSession.table(withSchema(hiveSchema, SRC_CHECK_SUM));
        Dataset<Row> globForInputDir = sparkSession
                .createDataFrame(config
                        .stream()
                        .map(ValidationReportProcessor::getParsedSourceAndDoForConfig)
                        .distinct()
                        .collect(Collectors.toList()), RawConfigGlobModel.class)
                .toDF();

        FileFinder fileFinder = new FileFinder();
        sparkSession.udf().register(CHECK_GLOB,
                (UDF2<String, String, String>) (x, y) -> Boolean.toString(fileFinder.checkGlob(x, y)),
                DataTypes.StringType);
        sparkSession.udf().register(SOURCE_AND_DO_FOR_CHECKSUM,
                (UDF2<String, String, String>) ValidationReportProcessor::getParsedSourceAndDoForSrcChecksum,
                DataTypes.StringType);

        srcCheckSum
                .join(downloadedToHiveRawTables,
                        srcCheckSum.col(FILEPATH)
                                .equalTo(downloadedToHiveRawTables.col(FILEPATH)), "left_outer")
                .withColumn(SOURCE_AND_DO_FOR_CHECKSUM,
                        callUDF(SOURCE_AND_DO_FOR_CHECKSUM,
                                srcCheckSum.col(FILEPATH),
                                srcCheckSum.col(FILENAME)))
                .select(srcCheckSum.col(FILEPATH),
                        srcCheckSum.col(FILENAME),
                        srcCheckSum.col("checkSum"),
                        srcCheckSum.col(SOURCE_CATEGORY),
                        downloadedToHiveRawTables.col(TABLE_NAME),
                        downloadedToHiveRawTables.col(MODIFICATION_DATE),
                        col(SOURCE_AND_DO_FOR_CHECKSUM))
                .createOrReplaceTempView("downloaded_checksum_tables");

        Dataset<Row> downloadedChecksumTables = sparkSession.table("downloaded_checksum_tables");

        saveReport(
                ValidationReportsTablesConstants.DATA_IMPORT_FULL_REPORT,
                downloadedChecksumTables
                        .join(globForInputDir,
                                col(SOURCE_AND_DO_FOR_CHECKSUM)
                                        .equalTo(globForInputDir.col("inputDir")), "left_outer")
                        .select(
                                downloadedChecksumTables.col(FILEPATH),
                                downloadedChecksumTables.col(FILENAME),
                                downloadedChecksumTables.col("checkSum"),
                                downloadedChecksumTables.col(SOURCE_CATEGORY),
                                downloadedChecksumTables.col(TABLE_NAME),
                                downloadedChecksumTables.col(MODIFICATION_DATE),
                                globForInputDir.col("inputDir"),
                                globForInputDir.col("glob"),
                                callUDF(CHECK_GLOB, col("glob"), col(FILENAME)).as(CHECK_GLOB)
                        )
        );

        log.info("{}{}: {}", END_OF, VALIDATION_REPORT, "createDataImportFullReport");
    }

    public void start() {
        createDataImportFullReport();

        saveReport(ValidationReportsTablesConstants.DATA_IMPORT_SUMMARY_REPORT, dataImportSummaryDf());
        saveReport(ValidationReportsTablesConstants.DATA_IMPORT_MISSING_REPORT, dataImportMissingSummaryDf());
        saveReport(ValidationReportsTablesConstants.INTERSECTION_CL_WITH_CATEGORIES_COUNTERPARTY,
                intersectionClWithCategoriesCounterparty());
        saveReport(ValidationReportsTablesConstants.INTERSECTION_CL_WITH_CATEGORIES_ROWS,
                intersectionCreditLimitWithCategoriesRows());
        saveReport(ValidationReportsTablesConstants.AVAILABLE_DO_FROM_CREDIT_LIMIT_AND_PDZ,
                availableDoFromCreditLimitAndPdzDf());
        saveReport(ValidationReportsTablesConstants.CLEAN_TABLE_WITHOUT_EVENT_DATE, cleanTablesWithoutEventDateDf());
        saveReport(ValidationReportsTablesConstants.RAW_DUPLICATE_FILES, rawDuplicateFilesDf());
        saveReport(ValidationReportsTablesConstants.MAPPING_STATUS_CREDIT_LIMIT_REPORT, mappingStatusCreditLimitDf());
        saveReport(ValidationReportsTablesConstants.VALIDATION_DATA_FOR_ALL_STEPS, validationDataForAllSteps());
    }

    private Dataset<Row> dataImportSummaryDf() {
        return sparkSession.table(withSchema(hiveSchema, ValidationReportsTablesConstants.DATA_IMPORT_FULL_REPORT))
                .groupBy(SOURCE_CATEGORY)
                .agg(count(FILENAME).as(RAW_COUNT),
                        count(TABLE_NAME).as(HIVE_COUNT))
                .withColumn("successPercentage",
                        round(col(HIVE_COUNT).divide(col(RAW_COUNT)).multiply(100), 2))
                .withColumn("loaded", col(HIVE_COUNT))
                .withColumn("missing", col(RAW_COUNT).minus(col(HIVE_COUNT)))
                .select(SOURCE_CATEGORY, "successPercentage", "loaded", "missing");
    }

    private Dataset<Row> dataImportMissingSummaryDf() {
        return sparkSession.table(withSchema(hiveSchema, ValidationReportsTablesConstants.DATA_IMPORT_FULL_REPORT))
                .withColumn("reason",
                        when(col("glob").isNull(), lit("Категория для данного ДО не представлена в конфиге"))
                                .when(col("glob").isNotNull().and(col(CHECK_GLOB).equalTo("false")),
                                        lit("Файл не попал под паттерн поиска"))
                                .when(col("glob").isNotNull().and(col(CHECK_GLOB).equalTo("true")),
                                        lit("Ошибка парсинга. Некорректный Sheet-name и/или структура заголовка"))
                                .otherwise(lit("Неизвестная причина")))
                .select(col(FILEPATH).as(FILENAME),
                        col(SOURCE_CATEGORY),
                        col("glob"),
                        col(CHECK_GLOB),
                        col("reason"))
                .where(col(TABLE_NAME).isNull());
    }

    private Dataset<Row> intersectionClWithCategoriesCounterparty() {
        Dataset<Row> clDf = sparkSession.table(withSchema(hiveSchema, CLEAN_CREDIT_LIMIT_TABLE))
                .where(col("code").isNotNull())
                .groupBy("code", "counterpartyInn")
                .agg(count("*").as("cnt"), max(DO_NAME).as("do_name"));

        Dataset<Row> eaDf = sparkSession.table(withSchema(hiveSchema, CLEAN_EXPRESS_ANALYSIS_TABLE))
                .where(col("code").isNotNull().and(col("year").equalTo("year_1")))
                .groupBy("code")
                .agg(count("*").as("cnt"));

        Dataset<Row> pdzDf = sparkSession.table(withSchema(hiveSchema, CLEAN_PDZ_TABLE))
                .where(col("code").isNotNull())
                .groupBy("code")
                .agg(count("*").as("cnt"));

        Dataset<Row> ccDf = sparkSession.table(withSchema(hiveSchema, CLEAN_CREDIT_CORRECTION_TABLE))
                .where(col("code").isNotNull())
                .groupBy("code")
                .agg(count("*").as("cnt"));

        Dataset<Row> lcDf = sparkSession.table(withSchema(hiveSchema, CLEAN_LEGAL_CASES_TABLE))
                .where(col("code").isNotNull().and(col("name_do").equalTo("kp")))
                .groupBy("code")
                .agg(count("*").as("cnt"));

        Dataset<Row> tvDf = sparkSession.table(withSchema(hiveSchema, CLEAN_FNS_TAX_VIOLATION_TABLE))
                .where(col("code").isNotNull().and(col("name_do").equalTo("kp")))
                .groupBy("code")
                .agg(count("*").as("cnt"));

        Dataset<Row> arDf = sparkSession.table(withSchema(hiveSchema, CLEAN_FNS_ARREARS_TABLE))
                .where(col("code").isNotNull().and(col("name_do").equalTo("kp")))
                .groupBy("code")
                .agg(count("*").as("cnt"));

        Dataset<Row> spInterfaxDf = sparkSession.table(withSchema(hiveSchema, CLEAN_SPARK_INTERFAX_SUMMARY_TABLE))
                .where(col("code").isNotNull().and(col("name_do").equalTo("kp")))
                .groupBy("code")
                .agg(count("*").as("cnt"));

        Dataset<Row> pfcDf = sparkSession.table(withSchema(hiveSchema, CLEAN_PAYMENT_FROM_CUSTOMER_TABLE))
                .where(col("code").isNotNull())
                .groupBy("code")
                .agg(count("*").as("cnt"));

        Dataset<Row> rsDf = sparkSession.table(withSchema(hiveSchema, CLEAN_REALIZATION_SERVICES_TABLE))
                .where(col("code").isNotNull())
                .groupBy("code")
                .agg(count("*").as("cnt"));

        Dataset<Row> mdcdDf = sparkSession.table(withSchema(hiveSchema, MD_CONTRACTOR_DICT_TABLE))
                .where(col("code").isNotNull())
                .groupBy("code")
                .agg(max("name_orig").as("name"));

        return clDf.join(eaDf, clDf.col("code").equalTo(eaDf.col("code")), "left")
                .join(pdzDf, clDf.col("code").equalTo(pdzDf.col("code")), "left")
                .join(ccDf, clDf.col("code").equalTo(ccDf.col("code")), "left")
                .join(lcDf, clDf.col("code").equalTo(lcDf.col("code")), "left")
                .join(tvDf, clDf.col("code").equalTo(tvDf.col("code")), "left")
                .join(arDf, clDf.col("code").equalTo(arDf.col("code")), "left")
                .join(spInterfaxDf, clDf.col("code").equalTo(spInterfaxDf.col("code")), "left")
                .join(pfcDf, clDf.col("code").equalTo(pfcDf.col("code")), "left")
                .join(rsDf, clDf.col("code").equalTo(rsDf.col("code")), "left")
                .join(mdcdDf, clDf.col("code").equalTo(mdcdDf.col("code")), "left")
                .select(coalesce(clDf.col("code"), lit(0)).as("code"),
                        clDf.col("counterpartyInn").as("inn"),
                        clDf.col("do_name"),
                        coalesce(clDf.col("cnt"), lit(0)).as("limits"),
                        coalesce(eaDf.col("cnt"), lit(0)).as("express_analysis"),
                        coalesce(pdzDf.col("cnt"), lit(0)).as("pdz"),
                        coalesce(ccDf.col("cnt"), lit(0)).as("credit_correction"),
                        coalesce(lcDf.col("cnt"), lit(0)).as("legal_cases"),
                        coalesce(tvDf.col("cnt"), lit(0)).as("fns_tax_violation"),
                        coalesce(arDf.col("cnt"), lit(0)).as("fns_arrears"),
                        coalesce(spInterfaxDf.col("cnt"), lit(0)).as("spark_interfax_summary"),
                        coalesce(pfcDf.col("cnt"), lit(0)).as("payment_from_customer"),
                        coalesce(rsDf.col("cnt"), lit(0)).as("realization_services"),
                        mdcdDf.col("name"))
                .orderBy("limits");
    }

    private Dataset<Row> availableDoFromCreditLimitAndPdzDf() {
        Dataset<Row> wlDf = sparkSession.table(withSchema(hiveSchema, "white_list_do"))
                .withColumnRenamed("string_name_do", USE_DO);

        Dataset<Row> clDf = sparkSession.table(withSchema(hiveSchema, UNION_CREDIT_LIMIT_TABLE))
                .select(trim(col(DO_NAME).as(AVAILABLE_DO)));

        return sparkSession.table(withSchema(hiveSchema, UNION_PDZ_TABLE))
                .select(trim(col("groupdo")).as(AVAILABLE_DO))
                .union(clDf)
                .distinct()
                .where(col(AVAILABLE_DO).isNotNull().and(col(AVAILABLE_DO).notEqual("")))
                .join(wlDf, col(AVAILABLE_DO).equalTo(col(USE_DO)), "left")
                .select(AVAILABLE_DO, USE_DO);
    }

    private Dataset<Row> validationDataForAllSteps() {

        Dataset<Row> credCor = sparkSession.table(withSchema(hiveSchema,
                ValidationTablesConstants.VALIDATION_CREDIT_CORRECTION_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_CREDIT_CORRECTION_TABLE));

        Dataset<Row> credLimit = sparkSession.table(withSchema(hiveSchema,
                ValidationTablesConstants.VALIDATION_CREDIT_LIMIT_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_CREDIT_LIMIT_TABLE));

        Dataset<Row> express = sparkSession.table(withSchema(hiveSchema,
                ValidationTablesConstants.VALIDATION_EXPRESS_ANALYSIS_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_EXPRESS_ANALYSIS_TABLE));

        Dataset<Row> legal = sparkSession.table(withSchema(hiveSchema,
                ValidationTablesConstants.VALIDATION_LEGAL_CASES_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_LEGAL_CASES_TABLE));

        Dataset<Row> fnsTax = sparkSession.table(withSchema(hiveSchema,
                ValidationTablesConstants.VALIDATION_FNS_TAX_VIOLATION_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_FNS_TAX_VIOLATION_TABLE));

        Dataset<Row> fnsArrears = sparkSession.table(withSchema(hiveSchema,
                ValidationTablesConstants.VALIDATION_FNS_ARREARS_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_FNS_ARREARS_TABLE));

        Dataset<Row> payments = sparkSession.table(withSchema(hiveSchema,
                ValidationTablesConstants.VALIDATION_PAYMENT_FROM_CUSTOMER_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_PAYMENT_FROM_CUSTOMER_TABLE));

        Dataset<Row> pdz = sparkSession.table(withSchema(hiveSchema, ValidationTablesConstants.VALIDATION_PDZ_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_PDZ_TABLE));

        Dataset<Row> realiz = sparkSession.table(withSchema(hiveSchema,
                ValidationTablesConstants.VALIDATION_REALIZATION_SERVICES_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_REALIZATION_SERVICES_TABLE));

        Dataset<Row> interfax = sparkSession.table(withSchema(hiveSchema,
                ValidationTablesConstants.VALIDATION_SPARK_INTERFAX_SUMMARY_TABLE))
                .withColumn("source", lit(ValidationTablesConstants.VALIDATION_SPARK_INTERFAX_SUMMARY_TABLE));


        Dataset<Row> unionDf = credCor
                .union(credLimit)
                .union(express)
                .union(legal)
                .union(fnsTax)
                .union(fnsArrears)
                .union(payments)
                .union(pdz)
                .union(realiz)
                .union(interfax);


        return unionDf
                .withColumn("union_step", lit(1))
                .withColumn("collision_step", when(col("collision_step").equalTo(true), lit(1)).otherwise(lit(0)))
                .withColumn("clean_step", when(col("clean_step").equalTo(true), lit(1)).otherwise(lit(0)))
                .withColumn("eventlog_step", when(col("eventlog_step").equalTo(true), lit(1)).otherwise(lit(0)))
                .select(
                        col("source"),
                        col("union_step"),
                        col("collision_step"),
                        col("clean_step"),
                        col("eventlog_step"),
                        col("reason_filtered")
                )
                .withColumn("black_list_reason",
                        when(col("reason_filtered").equalTo("black_list_reason"), lit(1)).otherwise(lit(0)))
                .withColumn("md_mapping_reason",
                        when(col("reason_filtered").equalTo("md_mapping_reason"), lit(1)).otherwise(lit(0)))
                .withColumn("field_validation_reason",
                        when(col("reason_filtered").equalTo("field_validation_reason"), lit(1)).otherwise(lit(0)))
                .withColumn("white_list_reason",
                        when(col("reason_filtered").equalTo("white_list_reason"), lit(1)).otherwise(lit(0)))
                .groupBy(col("source"))
                .agg(
                        sum(col("union_step")).as("union_step"),
                        sum(col("collision_step")).as("collision_step"),
                        sum(col("clean_step")).as("clean_step"),
                        sum(col("eventlog_step")).as("eventlog_step"),
                        sum(col("black_list_reason")).as("black_list_reason"),
                        sum(col("md_mapping_reason")).as("md_mapping_reason"),
                        sum(col("field_validation_reason")).as("field_validation_reason"),
                        sum(col("white_list_reason")).as("white_list_reason")
                )
                .select(
                        col("source"),
                        col("union_step"),
                        col("collision_step"),
                        col("clean_step"),
                        col("eventlog_step"),
                        col("black_list_reason"),
                        col("md_mapping_reason"),
                        col("field_validation_reason"),
                        col("white_list_reason")
                );
    }

    private Dataset<Row> cleanTablesWithoutEventDateDf() {
        Dataset<Row> clDf = sparkSession.table(withSchema(hiveSchema, CLEAN_CREDIT_LIMIT_TABLE))
                .where(col(EVENTDATE).isNull())
                .agg(count("*").as(COUNT_EMPTY_EVENT_DATE))
                .withColumn(TABLE_NAME, lit(CLEAN_CREDIT_LIMIT_TABLE));

        Dataset<Row> eaDf = sparkSession.table(withSchema(hiveSchema, CLEAN_EXPRESS_ANALYSIS_TABLE))
                .where(col(EVENTDATE).isNull())
                .agg(count("*").as(COUNT_EMPTY_EVENT_DATE))
                .withColumn(TABLE_NAME, lit(CLEAN_EXPRESS_ANALYSIS_TABLE));

        Dataset<Row> pdzDf = sparkSession.table(withSchema(hiveSchema, CLEAN_PDZ_TABLE))
                .where(col(EVENTDATE).isNull())
                .agg(count("*").as(COUNT_EMPTY_EVENT_DATE))
                .withColumn(TABLE_NAME, lit(CLEAN_PDZ_TABLE));

        Dataset<Row> ccDf = sparkSession.table(withSchema(hiveSchema, CLEAN_CREDIT_CORRECTION_TABLE))
                .where(col(EVENTDATE).isNull())
                .agg(count("*").as(COUNT_EMPTY_EVENT_DATE))
                .withColumn(TABLE_NAME, lit(CLEAN_CREDIT_CORRECTION_TABLE));

        Dataset<Row> lcDf = sparkSession.table(withSchema(hiveSchema, CLEAN_LEGAL_CASES_TABLE))
                .where(col(EVENTDATE).isNull())
                .agg(count("*").as(COUNT_EMPTY_EVENT_DATE))
                .withColumn(TABLE_NAME, lit(CLEAN_LEGAL_CASES_TABLE));

        Dataset<Row> tvDf = sparkSession.table(withSchema(hiveSchema, CLEAN_FNS_TAX_VIOLATION_TABLE))
                .where(col(EVENTDATE).isNull())
                .agg(count("*").as(COUNT_EMPTY_EVENT_DATE))
                .withColumn(TABLE_NAME, lit(CLEAN_FNS_TAX_VIOLATION_TABLE));

        Dataset<Row> arDf = sparkSession.table(withSchema(hiveSchema, CLEAN_FNS_ARREARS_TABLE))
                .where(col(EVENTDATE).isNull())
                .agg(count("*").as(COUNT_EMPTY_EVENT_DATE))
                .withColumn(TABLE_NAME, lit(CLEAN_FNS_ARREARS_TABLE));

        Dataset<Row> pfcDf = sparkSession.table(withSchema(hiveSchema, CLEAN_PAYMENT_FROM_CUSTOMER_TABLE))
                .where(col(EVENTDATE).isNull())
                .agg(count("*").as(COUNT_EMPTY_EVENT_DATE))
                .withColumn(TABLE_NAME, lit(CLEAN_PAYMENT_FROM_CUSTOMER_TABLE));

        Dataset<Row> rsDf = sparkSession.table(withSchema(hiveSchema, CLEAN_REALIZATION_SERVICES_TABLE))
                .where(col(EVENTDATE).isNull())
                .agg(count("*").as(COUNT_EMPTY_EVENT_DATE))
                .withColumn(TABLE_NAME, lit(CLEAN_REALIZATION_SERVICES_TABLE));

        return clDf.union(eaDf)
                .union(pdzDf)
                .union(ccDf)
                .union(lcDf)
                .union(tvDf)
                .union(arDf)
                .union(pfcDf)
                .union(rsDf)
                .select(TABLE_NAME, COUNT_EMPTY_EVENT_DATE);
    }

    private Dataset<Row> rawDuplicateFilesDf() {
        WindowSpec rankW = partitionBy(col("checksum")).orderBy(FILEPATH);
        WindowSpec countW = partitionBy(col("checksum"));

        return sparkSession.table(withSchema(hiveSchema, SRC_CHECK_SUM))
                .withColumn("rnk", row_number().over(rankW))
                .withColumn("count_group", count("*").over(countW))
                .where(col("count_group").gt(1));
    }

    private Dataset<Row> intersectionCreditLimitWithCategoriesRows() {
        Stream<String> tableNames = sparkSession
                .sql(format("SHOW TABLES IN %s", hiveSchema))
                .select(TABLE_NAME)
                .as(Encoders.STRING())
                .collectAsList()
                .stream()
                .filter(tableName -> tableName.startsWith(COLLISIONS_PREFIX))
                .filter(tableName -> !tableName.equals(CollisionsReportTableConstants.COLLISIONS_REPORT_CREDIT_LIMIT_TABLE));

        return tableNames.map(tableName -> {
            long total = sparkSession.table(withSchema(hiveSchema, tableName)).count();
            return sparkSession.table(withSchema(hiveSchema, tableName))
                    .where(col(NAME_DO).isNotNull())
                    .groupBy(col(NAME_DO).as("do"),
                            col(ERROR_STATUS).as("status"))
                    .agg(count(ERROR_STATUS).as("rows_count"),
                            round(lit(100)
                                    .multiply(count(col(ERROR_STATUS)))
                                    .divide(lit(total)))
                                    .as("percentage"))

                    .select(lit(tableName).as("table_name"),
                            col("do"),
                            col("status"),
                            col("rows_count"),
                            col("percentage"));
        }).reduce(Dataset::union)
                .orElseThrow(() -> new RuntimeException(
                        format("Can't find necessary tables for validation report: %s",
                                ValidationReportsTablesConstants.INTERSECTION_CL_WITH_CATEGORIES_ROWS)));

    }

    private Dataset<Row> mappingStatusCreditLimitDf() {

        return sparkSession
                .table(withSchema(hiveSchema, CollisionsReportTableConstants.COLLISIONS_REPORT_CREDIT_LIMIT_TABLE))
                .select(
                        sum(lit(1)).as("total"),
                        sum(when(col("error_status").equalTo("OK"), lit(1)).otherwise(lit(0))).as("OK"),
                        sum(when(col("error_status").equalTo("NOT_MAPPING"), lit(1)).otherwise(lit(0))).as("NOT_MAPPING"),
                        sum(when(col("in_white_list").equalTo(false)
                                        .and(col("in_black_list").equalTo(false)), lit(1)).otherwise(lit(0))).as(
                                "not_white_list"),
                        sum(when(col("in_black_list").equalTo(true), lit(1)).otherwise(lit(0))).as("in_black_list"),
                        sum(when(col("is_corrupted_field").equalTo(true)
                                .and(col("in_black_list").equalTo(false))
                                .and(col("in_white_list").equalTo(true)), lit(1)
                        ).otherwise(lit(0))).as(
                                "is_corrupted_field")
                );
    }

    public static class RawConfigGlobModel implements Serializable {
        private String inputDir;
        private String glob;

        RawConfigGlobModel(String inputDir, String glob) {
            this.inputDir = inputDir;
            this.glob = glob;
        }

        public String getInputDir() {
            return inputDir;
        }

        public String getGlob() {
            return glob;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RawConfigGlobModel that = (RawConfigGlobModel) o;
            return Objects.equals(inputDir, that.inputDir) &&
                    Objects.equals(glob, that.glob);
        }

        @Override
        public int hashCode() {
            return Objects.hash(inputDir, glob);
        }
    }
}
