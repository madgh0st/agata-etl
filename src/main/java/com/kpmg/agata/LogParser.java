package com.kpmg.agata;

import com.kpmg.agata.config.Environment;
import com.kpmg.agata.controllers.ParseLogController;
import com.kpmg.agata.utils.arg.LogParserArgumentParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.kpmg.agata.constant.LogConstants.END_OF;
import static com.kpmg.agata.constant.LogConstants.PIPELINE;
import static com.kpmg.agata.constant.LogConstants.START_OF;

public class LogParser {
    private static final Logger log = LoggerFactory.getLogger(LogParser.class);

    public static void main(String[] args) {

        log.info("Input RAW params: {}", Arrays.toString(args));
        LogParserArgumentParser argParser = new LogParserArgumentParser();
        argParser.run(args);

        Environment.setConfig(argParser.getAppPropertiesPath());
        Environment.getListPrettyPrintAllProperties().forEach(log::info);

        String hdfsDirEventLog = Environment.getConfig().getProperty("hdfs.dir.eventlog");
        if (StringUtils.isBlank(hdfsDirEventLog)) {
            throw new IllegalArgumentException("hdfsDirEventLog property can't be empty");
        }

        SparkSession sparkSession = Environment.getSparkSession();

        log.info(START_OF + PIPELINE);

        new ParseLogController(sparkSession, argParser.getInputLogPath(), argParser.getOutputLogPath()).start();

        log.info(END_OF + PIPELINE);
        sparkSession.close();
    }
}
