package com.kpmg.agata.controllers;

import com.kpmg.agata.processors.logs.HandlingLogsToSparkProcessor;
import com.kpmg.agata.processors.logs.LogParserProcessor;
import com.kpmg.agata.processors.report.MissedSheetsReportProcessor;
import com.kpmg.agata.processors.report.PipelineTimeReportProcessor;
import com.kpmg.agata.processors.report.ReportCreateReportProcessor;
import com.kpmg.agata.processors.report.SqlScriptsReportProcessor;
import com.kpmg.agata.processors.report.StackTraceReportProcessor;
import com.kpmg.agata.processors.report.XlsParsingReportProcessor;
import com.kpmg.agata.utils.filesystem.HDFSFacade;
import org.apache.spark.sql.SparkSession;

import java.time.LocalDateTime;

import static com.kpmg.agata.config.Environment.getSchemaPrefix;

public class ParseLogController extends AbstractController {

    private static final String LOGS_TABLE_NAME = getSchemaPrefix() + "logs";

    private SparkSession sparkSession;
    private String inputFile;
    private String outputFile;

    public ParseLogController(SparkSession sparkSession, String inputFile, String outputFile) {
        this.sparkSession = sparkSession;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @Override
    protected void startAction() {
        new LogParserProcessor(new HDFSFacade()).writeToFile(inputFile, outputFile);
        new HandlingLogsToSparkProcessor(sparkSession, outputFile, LOGS_TABLE_NAME).createTableLogsToHive();

        LocalDateTime start = LocalDateTime.now();
        sparkSession.table(LOGS_TABLE_NAME).cache();
        new PipelineTimeReportProcessor(sparkSession, LOGS_TABLE_NAME, start).createReport();
        new ReportCreateReportProcessor(sparkSession, LOGS_TABLE_NAME, start).createReport();
        new XlsParsingReportProcessor(sparkSession, LOGS_TABLE_NAME, start).createReport();
        new StackTraceReportProcessor(sparkSession, LOGS_TABLE_NAME, start).createReport();
        new MissedSheetsReportProcessor(sparkSession, LOGS_TABLE_NAME, start).createReport();
        new SqlScriptsReportProcessor(sparkSession, LOGS_TABLE_NAME, start).createReport();
    }
}
