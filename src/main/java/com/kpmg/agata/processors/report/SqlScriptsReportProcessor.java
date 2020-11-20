package com.kpmg.agata.processors.report;

import java.time.LocalDateTime;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static com.kpmg.agata.constant.LogConstants.TO_HIVE_EXEC;
import static com.kpmg.agata.constant.LogConstants.TO_HIVE_MODEL;
import static com.kpmg.agata.constant.database.ValidationReportsTablesConstants.CLEAN_TABLE_REPORT_TIME;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.min;
import static org.apache.spark.sql.functions.regexp_extract;

public class SqlScriptsReportProcessor extends ReportProcessor {

    private static final String HIVE_MODEL_COL_NAME = "hiveModel";

    public SqlScriptsReportProcessor(SparkSession sparkSession, String logsTableName, LocalDateTime start) {
        super(sparkSession, logsTableName, start);
    }

    @Override
    public void createReport() {
        Dataset<Row> sqlReport = filterLog(TO_HIVE_EXEC);
        sqlReport = sqlReport
                .withColumn(HIVE_MODEL_COL_NAME,
                        regexp_extract(col(RAW_LOGS_COL), TO_HIVE_MODEL + "=(.*?)$", 1))
                .groupBy(HIVE_MODEL_COL_NAME)
                .agg(min(col(TIME)).alias(START),
                        max(col(TIME)).alias(END));

        sqlReport = countTime(sqlReport);
        writeDf(sqlReport, CLEAN_TABLE_REPORT_TIME);
    }
}
