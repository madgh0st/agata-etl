package com.kpmg.agata.processors.report;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.time.LocalDateTime;

import static com.kpmg.agata.constant.LogConstants.RAW_DATA_PROCESSING;
import static com.kpmg.agata.constant.database.ValidationReportsTablesConstants.XLS_PARSING_TIME;
import static org.apache.spark.sql.functions.asc;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.min;
import static org.apache.spark.sql.functions.regexp_extract;

public class XlsParsingReportProcessor extends ReportProcessor {

    private static final String DATE = "date";

    public XlsParsingReportProcessor(SparkSession sparkSession, String logsTableName, LocalDateTime start) {
        super(sparkSession, logsTableName, start);
    }

    @Override
    public void createReport() {
        Dataset<Row> xlsParsing = filterLog(RAW_DATA_PROCESSING);
        xlsParsing = xlsParsing
                .withColumn(DATE,
                        regexp_extract(col(RAW_LOGS_COL), RAW_DATA_PROCESSING + ": (\\d{4}-\\d{2}-\\d{2}).*", 1))
                .groupBy(col(DATE))
                .agg(min(col(TIME)).alias(START),
                        max(col(TIME)).alias(END))
                .orderBy(asc(DATE));

        xlsParsing = countTime(xlsParsing);
        writeDf(xlsParsing, XLS_PARSING_TIME);
    }
}
