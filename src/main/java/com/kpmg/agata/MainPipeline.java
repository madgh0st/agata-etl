package com.kpmg.agata;

import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.Environment;
import com.kpmg.agata.controllers.AvailableListDoController;
import com.kpmg.agata.controllers.RawDataProcessingController;
import com.kpmg.agata.controllers.SparkCleanDataController;
import com.kpmg.agata.controllers.SparkEventLogToJsonController;
import com.kpmg.agata.controllers.SparkJsonToHiveController;
import com.kpmg.agata.controllers.ValidationReportController;
import com.kpmg.agata.utils.arg.MainPipelineArgumentParser;
import com.kpmg.agata.utils.filesystem.HDFSFacade;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

import static com.kpmg.agata.constant.LogConstants.END_OF;
import static com.kpmg.agata.constant.LogConstants.PIPELINE;
import static com.kpmg.agata.constant.LogConstants.START_OF;
import static com.kpmg.agata.constant.database.BaseConstants.PARQUET;

public class MainPipeline {
    private static final Logger log = LoggerFactory.getLogger(MainPipeline.class);

    public static void main(String[] args) throws IOException {

        log.info("Input RAW params: {}", Arrays.toString(args));

        MainPipelineArgumentParser argParser = new MainPipelineArgumentParser();
        argParser.run(args);

        Environment.setConfig(argParser.getAppPropertiesPath());
        Environment.getListPrettyPrintAllProperties().forEach(log::info);

        String hdfsDirEventLog = Environment.getConfig().getProperty("hdfs.dir.eventlog");
        if (StringUtils.isBlank(hdfsDirEventLog)) {
            throw new IllegalArgumentException("hdfsDirEventLog property can't be empty");
        }

        SparkSession sparkSession = Environment.getSparkSession();
        ConfigParser configAgata = new ConfigParser().getRawConfigAgataXls(argParser.getRawFilesConfigPath());

        log.info(START_OF + PIPELINE);

        switch (argParser.getStep()) {
            case "raw-files":
                if (argParser.getDateKey().equals("all")) {
                    HDFSFacade hdfsFacade = new HDFSFacade();
                    hdfsFacade.deleteFolderOrFile(Environment.getConfig().getProperty("hdfs.dir.parquet.raw"));
                    hdfsFacade.deleteFolderOrFile(Environment.getConfig().getProperty("hdfs.dir.parquet.aggregate"));
                    hdfsFacade.createFolder(Environment.getConfig().getProperty("hdfs.dir.parquet.raw"));
                    hdfsFacade.createFolder(Environment.getConfig().getProperty("hdfs.dir.parquet.aggregate"));
                    hdfsFacade.deleteFolderOrFile(hdfsDirEventLog);
                    hdfsFacade.createFolder(hdfsDirEventLog);
                    hdfsFacade
                            .deleteFolderOrFile(Environment.getConfig().getProperty("hdfs.dir.validation") + PARQUET);
                    hdfsFacade.createFolder(Environment.getConfig().getProperty("hdfs.dir.validation") + PARQUET);
                }
                new RawDataProcessingController(argParser.getDateKey(), configAgata, sparkSession).start();
            case "json-to-hive":
                new SparkJsonToHiveController(sparkSession, configAgata.getModelTypeConfiguration()).start();
            case "clean-tables":
                new SparkCleanDataController(sparkSession, configAgata.getModelTypeConfiguration()).start();
            case "available-list-do":
                new AvailableListDoController(sparkSession).start();
            case "write-eventlog":
                new SparkEventLogToJsonController(sparkSession).start();
            case "validation":
                new ValidationReportController(sparkSession, configAgata.getModelTypeConfiguration()).start();
                break;
            default:
                throw new IllegalArgumentException(
                        String.format(
                                "Incorrect starting step argument (%s). Can be one of the following: [raw-files, json-to-hive, clean-tables, write-eventlog, validation]",
                                argParser.getStep())
                );
        }
        log.info(END_OF + PIPELINE);
        sparkSession.close();
    }
}
