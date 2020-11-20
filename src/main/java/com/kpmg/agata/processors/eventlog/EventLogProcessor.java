package com.kpmg.agata.processors.eventlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.config.Environment;
import com.kpmg.agata.mapreduce.EventLogLine;
import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanCounterpartyContractModel;
import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanExpressAnalysisModel;
import com.kpmg.agata.models.clean.CleanFnsArrearsModel;
import com.kpmg.agata.models.clean.CleanFnsDisqModel;
import com.kpmg.agata.models.clean.CleanFnsTaxViolationModel;
import com.kpmg.agata.models.clean.CleanFsspModel;
import com.kpmg.agata.models.clean.CleanGenprocModel;
import com.kpmg.agata.models.clean.CleanLegalCasesModel;
import com.kpmg.agata.models.clean.CleanPaymentFromCustomerModel;
import com.kpmg.agata.models.clean.CleanRealizationServicesModel;
import com.kpmg.agata.models.clean.CleanSectoralIndexModel;
import com.kpmg.agata.models.clean.CleanSparkInterfaxSummaryModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.models.eventlog.EventModel;
import java.io.Serializable;
import java.util.stream.Stream;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_COUNTERPARTY_CONTRACT_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_CREDIT_LIMIT_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_EXPRESS_ANALYSIS_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_FNS_ARREARS_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_FNS_DISQ_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_FNS_TAX_VIOLATION_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_FSSP_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_GENPROC_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_LEGAL_CASES_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_PAYMENT_FROM_CUSTOMER_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_REALIZATION_SERVICES_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_SECTORAL_INDICES_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_SPARK_INTERFAX_SUMMARY_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_WEEKLY_PDZ_FLAT_TABLE;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class EventLogProcessor implements Serializable, IEventLog {

    private static final Logger log = LoggerFactory.getLogger(EventLogProcessor.class);
    private final String schemaName = Environment.getConfig().getProperty("db.schema");
    private final String selectSqlPattern = format("SELECT * FROM %s.%s", schemaName, "%s");

    private SparkSession sparkSession;
    private String outputFolder;
    private ObjectMapper objectMapper;

    public EventLogProcessor(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
        this.outputFolder = format("%s/counterparty/", Environment.getConfig().getProperty("hdfs.dir.eventlog"));
        this.objectMapper = new ObjectMapper();
    }

    private JavaRDD<? extends AbstractCleanDataModel> loadCleanTable(TableLoadingModel model) {
        log.warn("Executing query: '{}'", model.sqlQuery);
        return sparkSession.sql(model.sqlQuery)
                           .as(Encoders.bean(model.outputClass))
                           .toJavaRDD();
    }

    private JavaRDD<EventModel> castRowsToEvents(JavaRDD<? extends AbstractCleanDataModel> cleanTableRDD) {
        return cleanTableRDD
                .filter(model -> isNotBlank(model.getCode()))
                .filter(model -> model.getEventDate() != null)
                .filter(model -> isNotBlank(model.getName_do()))
                .map(model -> new EventModel(
                        model.getCode(),
                        model.getName_do(),
                        model.getEventDate(),
                        model)
                );
    }

    @Override
    public void putToHdfs() {
        Stream.of(
                // todo: replace sql building with dataFrames
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_CREDIT_LIMIT_TABLE),
                        CleanCreditLimitModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_PAYMENT_FROM_CUSTOMER_TABLE),
                        CleanPaymentFromCustomerModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_REALIZATION_SERVICES_TABLE),
                        CleanRealizationServicesModel.class
                ),
                new TableLoadingModel(
                        format("SELECT *, map('a','b') as reports FROM %s.%s", schemaName, CLEAN_WEEKLY_PDZ_FLAT_TABLE),
                        CleanWeeklyStatusPDZFlatModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_EXPRESS_ANALYSIS_TABLE),
                        CleanExpressAnalysisModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_LEGAL_CASES_TABLE),
                        CleanLegalCasesModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_SPARK_INTERFAX_SUMMARY_TABLE),
                        CleanSparkInterfaxSummaryModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_FNS_TAX_VIOLATION_TABLE),
                        CleanFnsTaxViolationModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_FNS_ARREARS_TABLE),
                        CleanFnsArrearsModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_GENPROC_TABLE),
                        CleanGenprocModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_FNS_DISQ_TABLE),
                        CleanFnsDisqModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_FSSP_TABLE),
                        CleanFsspModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_SECTORAL_INDICES_TABLE),
                        CleanSectoralIndexModel.class
                ),
                new TableLoadingModel(
                        format(selectSqlPattern, CLEAN_COUNTERPARTY_CONTRACT_TABLE),
                        CleanCounterpartyContractModel.class
                )
        )
              .map(this::loadCleanTable)
              .map(this::castRowsToEvents)
              .reduce(JavaRDD::union)
              .orElseThrow(() -> new RuntimeException("Empty EventLog RDD"))
              .map(event -> EventLogLine.serialize(event, objectMapper))
              .distinct()
              .coalesce(1, true)
              .saveAsTextFile(this.outputFolder);
    }

    private static class TableLoadingModel implements Serializable {

        private final String sqlQuery;
        private final Class<? extends AbstractCleanDataModel> outputClass;

        private TableLoadingModel(String sqlQuery, Class<? extends AbstractCleanDataModel> outputClass) {
            this.sqlQuery = sqlQuery;
            this.outputClass = outputClass;
        }
    }
}
