package com.kpmg.agata.processors.report;

import org.apache.hadoop.hive.ql.parse.JoinType;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.time.LocalDateTime;

import static com.kpmg.agata.constant.database.ValidationReportsTablesConstants.STACKTRACE;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.collect_list;
import static org.apache.spark.sql.functions.concat_ws;

public class StackTraceReportProcessor extends ReportProcessor {

    private static final String BLOCK_NUMBER = "blockNum";

    public StackTraceReportProcessor(SparkSession sparkSession, String logsTableName, LocalDateTime start) {
        super(sparkSession, logsTableName, start);
    }

    @Override
    public void createReport() {
        Dataset<Row> logs = sparkSession.table(logsTableName);
        Dataset<Row> errorBlockNames = logs.where(col(ERROR).equalTo(Boolean.TRUE.toString()))
                .select(BLOCK_NUMBER)
                .distinct();

        Dataset<Row> stacktrace = logs.join(errorBlockNames,
                logs.col(BLOCK_NUMBER).equalTo(errorBlockNames.col(BLOCK_NUMBER)),
                JoinType.INNER.name())
                .select(logs.col(BLOCK_NUMBER), logs.col(RAW_LOGS_COL))
                .groupBy(logs.col(BLOCK_NUMBER))
                .agg(concat_ws("\n", collect_list(logs.col(RAW_LOGS_COL))).alias("stacktrace"));

        writeDf(stacktrace, STACKTRACE);
    }
}
