package com.kpmg.agata.controllers;

import com.kpmg.agata.config.Environment;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.models.OkvedSectoralIndexModel;
import com.kpmg.agata.models.UnstructureModel;
import com.kpmg.agata.utils.filesystem.HDFSFacade;
import java.util.List;
import java.util.stream.Stream;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.toList;

import static com.kpmg.agata.config.Environment.getSchemaPrefix;
import static com.kpmg.agata.constant.LogConstants.END_OF;
import static com.kpmg.agata.constant.LogConstants.START_OF;
import static com.kpmg.agata.constant.database.BaseConstants.PARQUET;

public class SparkJsonToHiveController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(SparkJsonToHiveController.class);
    private final List<ModelTypeConfiguration> config;
    private final SparkSession sparkSession;
    private final JavaSparkContext jsc;
    private final String hdfsDirJsonDeltas = Environment.getConfig().getProperty("hdfs.dir.json_deltas");
    private final String hdfsDirResultJson = Environment.getConfig().getProperty("hdfs.dir.result_jsons");
    private final String schemaPrefix = getSchemaPrefix();
    private final HDFSFacade hdfs = new HDFSFacade();

    public SparkJsonToHiveController(SparkSession sparkSession, List<ModelTypeConfiguration> config) {
        this.config = config;
        this.sparkSession = sparkSession;
        jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
    }

    private void createEmptyTable(String parquetPath, String hiveTableName,
                                  Class<? extends AbstractClientDataModel> modelClass) {
        sparkSession.createDataFrame(jsc.emptyRDD(), modelClass)
                    .toDF()
                    .write()
                    .format(PARQUET)
                    .mode(SaveMode.Overwrite)
                    .option("path", parquetPath)
                    .saveAsTable(schemaPrefix + hiveTableName);
    }

    private void createWhiteListDo() {
        sparkSession.sqlContext()
                    .read()
                    .format("json")
                    .load(Environment.getConfig().getProperty("hdfs.dir.list_do.whitelist") + "json/")
                    .distinct()
                    .toDF()
                    .write()
                    .format(PARQUET)
                    .mode(SaveMode.Overwrite)
                    .option("path", Environment.getConfig().getProperty("hdfs.dir.list_do.whitelist") + PARQUET)
                    .saveAsTable(schemaPrefix + "white_list_do");
    }

    private void configToHive() {
        sparkSession
                .sqlContext()
                .createDataFrame(config
                        .stream()
                        .map(y -> new RawConfigModel(
                                        y.getNameDo(),
                                        y.getRawType(),
                                        y.getGlob(),
                                        y.getInputDir(),
                                        y.getSheetName(),
                                        y.getHiveTableName(),
                                        y.getPathToParquet(),
                                        String.valueOf(y.getHeaderRowNumber()),
                                        y.getOutputDir()
                                )
                        )
                        .collect(toList()), RawConfigModel.class
                )
                .toDF()
                .write()
                .format(PARQUET)
                .mode(SaveMode.Overwrite)
                .option("path", Environment.getConfig().getProperty("hdfs.dir.parquet.raw") + "raw_config")
                .saveAsTable(schemaPrefix + "raw_config");
    }

    @Override
    protected void startAction() {
        log.info(START_OF + "Create config to Hive");
        configToHive();
        log.info(END_OF + "Create config to Hive");

        log.info(START_OF + "Create white-list");
        createWhiteListDo();
        log.info(END_OF + "Create white-list");

        log.info(START_OF + "Create okved_sectoral_indices table");
        createOkvedSectoralIndicesTable();
        log.info(END_OF + "Create okved_sectoral_indices table");

        log.info(START_OF + "JSON TO HIVE");
        this.config.forEach(this::processConfig);
        log.info(END_OF + "JSON TO HIVE");
    }

    private void createOkvedSectoralIndicesTable() {
        Stream<OkvedSectoralIndexModel> content = Stream.of(
                Stream.of("06", "09.1", "19")
                      .map(okved -> new OkvedSectoralIndexModel(okved, "MEOGTR")),

                Stream.of("35", "36", "37", "38", "39", "42.21", "49.50.1", "49.50.2")
                      .map(okved -> new OkvedSectoralIndexModel(okved, "MEEUTR")),

                Stream.of("60", "61")
                      .map(okved -> new OkvedSectoralIndexModel(okved, "METLTR")),

                Stream.of("05", "07", "08", "09.2", "23", "24")
                      .map(okved -> new OkvedSectoralIndexModel(okved, "MEMMTR")),

                Stream.of("64", "65", "66")
                      .map(okved -> new OkvedSectoralIndexModel(okved, "MEFNTR")),

                Stream.of("10", "11", "12", "13", "14", "15", "16", "17", "18", "31", "32", "45", "46",
                        "47", "56", "58", "59", "86", "87", "88", "90", "91", "92", "93", "95", "96")
                      .map(okved -> new OkvedSectoralIndexModel(okved, "MECNTR")),

                Stream.of("20", "21", "22", "46")
                      .map(okved -> new OkvedSectoralIndexModel(okved, "MECHTR")),

                Stream.of("28", "29", "30", "49.1", "49.2", "49.3", "49.4", "49.50.3", "50", "51", "52", "53")
                      .map(okved -> new OkvedSectoralIndexModel(okved, "METNTR")))
                                                        .flatMap(stream -> stream);

        sparkSession.createDataFrame(content.collect(toList()), OkvedSectoralIndexModel.class)
                    .write()
                    .format(PARQUET)
                    .mode(SaveMode.Overwrite)
                    .option("path", Environment.getConfig().getProperty("hdfs.dir.parquet.raw") +
                            "okved_sectoral_indices")
                    .saveAsTable(schemaPrefix + "okved_sectoral_indices");
    }

    private void processConfig(ModelTypeConfiguration config) {
        String tableName = config.getHiveTableName();
        AbstractClientDataModel model = config.getDataModelInstance();
        Class<? extends AbstractClientDataModel> modelClass = model == null ? UnstructureModel.class : model.getClass();
        String inputPath = config.getOutputDir().replace(hdfsDirJsonDeltas + "${date}/", hdfsDirResultJson);
        String outputPath = config.getPathToParquet();
        boolean isInputDirEmpty = hdfs.globMatch(inputPath, "*").isEmpty();

        try {
            log.info("FullPathToHiveParquet:{}, FullPathToJsonHdfs:{}", outputPath, inputPath);
            if (isInputDirEmpty) {
                log.info("Not found json input directory:{}; Create empty table:{}", inputPath, tableName);
                createEmptyTable(outputPath, tableName, modelClass);
                return;
            }

            createTable(inputPath, outputPath, tableName, modelClass);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private void createTable(String inputPath, String outputPath, String tableName,
                             Class<? extends AbstractClientDataModel> modelClass) {
        sparkSession.sqlContext()
                    .read()
                    .schema(Encoders.bean(modelClass).schema())
                    .format("json")
                    .load(inputPath)
                    .toDF()
                    .write()
                    .format(PARQUET)
                    .mode(SaveMode.Overwrite)
                    .option("path", outputPath)
                    .saveAsTable(schemaPrefix + tableName);
    }

    public static class RawConfigModel {

        private String nameDo;
        private String rawType;
        private String glob;
        private String inputDir;
        private String sheetName;
        private String hiveTableName;
        private String pathToParquet;
        private String headerRowNumber;
        private String outputDir;

        public RawConfigModel(String nameDo, String rawType, String glob, String inputDir, String sheetName,
                              String hiveTableName, String pathToParquet, String headerRowNumber, String outputDir) {
            this.nameDo = nameDo;
            this.rawType = rawType;
            this.glob = glob;
            this.inputDir = inputDir;
            this.sheetName = sheetName;
            this.hiveTableName = hiveTableName;
            this.pathToParquet = pathToParquet;
            this.headerRowNumber = headerRowNumber;
            this.outputDir = outputDir;
        }

        public String getNameDo() {
            return nameDo;
        }

        public String getRawType() {
            return rawType;
        }

        public String getGlob() {
            return glob;
        }

        public String getInputDir() {
            return inputDir;
        }

        public String getSheetName() {
            return sheetName;
        }

        public String getHiveTableName() {
            return hiveTableName;
        }

        public String getPathToParquet() {
            return pathToParquet;
        }

        public String getHeaderRowNumber() {
            return headerRowNumber;
        }

        public String getOutputDir() {
            return outputDir;
        }
    }
}
