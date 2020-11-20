package com.kpmg.agata.processors.report;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.time.LocalDateTime;

import static com.kpmg.agata.constant.LogConstants.VALIDATION_REPORT;
import static com.kpmg.agata.constant.database.ValidationReportsTablesConstants.BUILD_VALIDATION_REPORT;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.min;
import static org.apache.spark.sql.functions.regexp_extract;

public class ReportCreateReportProcessor extends ReportProcessor {

    public ReportCreateReportProcessor(SparkSession sparkSession, String logsTableName, LocalDateTime start) {
        super(sparkSession, logsTableName, start);
    }

    @Override
    public void createReport() {
        Dataset<Row> reportsDf = filterLog(VALIDATION_REPORT);
        reportsDf = reportsDf.withColumn("report_name",
                regexp_extract(col(RAW_LOGS_COL), VALIDATION_REPORT + ": (.*?)$", 1))
                .groupBy(col("report_name"))
                .agg(min(col(TIME)).alias(START),
                        max(col(TIME)).alias(END));

        reportsDf = countTime(reportsDf);
        writeDf(reportsDf, BUILD_VALIDATION_REPORT);
    }
}
