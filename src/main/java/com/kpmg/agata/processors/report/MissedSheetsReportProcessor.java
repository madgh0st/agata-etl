package com.kpmg.agata.processors.report;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;

import java.time.LocalDateTime;

import static com.kpmg.agata.constant.database.ValidationReportsTablesConstants.WITHOUT_TARGET_SHEET;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.regexp_extract;
import static org.apache.spark.sql.functions.sum;
import static org.apache.spark.sql.functions.when;

public class MissedSheetsReportProcessor extends ReportProcessor {

    private static final String FILE = "filepath";

    public MissedSheetsReportProcessor(SparkSession sparkSession, String logsTableName, LocalDateTime start) {
        super(sparkSession, logsTableName, start);
    }

    @Override
    public void createReport() {
        Dataset<Row> logs = sparkSession.table(logsTableName).filter(col(RAW_LOGS_COL).contains("Excel file:"));

        Dataset<Row> brokenFiles = logs.withColumn(FILE,
                regexp_extract(col(RAW_LOGS_COL), "Excel file: (.*?) Sheet with name", 1))
                .withColumn("isPresent", when(col(RAW_LOGS_COL).contains("is present"), 1)
                        .when(col(RAW_LOGS_COL).contains("is not present"), 0))
                .withColumn("sum", sum("isPresent").over(Window.partitionBy(col(FILE))))
                .filter(col("sum").lt(1))
                .select(FILE)
                .distinct();

        writeDf(brokenFiles, WITHOUT_TARGET_SHEET);
    }
}
