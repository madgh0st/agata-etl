package com.kpmg.agata.processors.report;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.time.LocalDateTime;

import static com.kpmg.agata.constant.LogConstants.PIPELINE;
import static com.kpmg.agata.constant.LogConstants.STAGE;
import static com.kpmg.agata.constant.database.ValidationReportsTablesConstants.VAL_PIPELINE;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.min;
import static org.apache.spark.sql.functions.regexp_extract;

public class PipelineTimeReportProcessor extends ReportProcessor {

    public PipelineTimeReportProcessor(SparkSession sparkSession, String logsTableName, LocalDateTime start) {
        super(sparkSession, logsTableName, start);
    }

    public void createReport() {
        Dataset<Row> stagesDf = filterLog(STAGE);
        stagesDf = stagesDf.withColumn(STAGE_TYPE,
                regexp_extract(col(RAW_LOGS_COL), STAGE + ": (.*?)$", 1))
                .groupBy(col(STAGE_TYPE))
                .agg(min(col(TIME)).alias(START),
                        max(col(TIME)).alias(END))
                .filter(col(STAGE_TYPE).isNotNull());

        Dataset<Row> pipelineTime = filterLog(PIPELINE);
        pipelineTime = aggregateByTime(pipelineTime);
        pipelineTime = pipelineTime.withColumn(STAGE_TYPE, lit(PIPELINE));

        Dataset<Row> totalTime = sparkSession
                .table(logsTableName)
                .filter(col(TIME).notEqual(""));
        totalTime = aggregateByTime(totalTime);
        totalTime = totalTime.withColumn(STAGE_TYPE, lit("total"));

        Dataset<Row> writeableDf = stagesDf;
        writeableDf = unionWithStage(writeableDf, totalTime);
        writeableDf = unionWithStage(writeableDf, pipelineTime);

        writeableDf = countTime(writeableDf);
        writeDf(writeableDf, VAL_PIPELINE);
    }
}
