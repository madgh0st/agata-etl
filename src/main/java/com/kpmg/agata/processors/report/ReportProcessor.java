package com.kpmg.agata.processors.report;

import com.kpmg.agata.config.Environment;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.kpmg.agata.constant.LogConstants.END_OF;
import static com.kpmg.agata.constant.LogConstants.START_OF;
import static com.kpmg.agata.constant.database.BaseConstants.PARQUET;
import static com.kpmg.agata.config.Environment.getSchemaPrefix;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.desc;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.min;
import static org.apache.spark.sql.functions.unix_timestamp;

abstract class ReportProcessor {

    static final String RAW_LOGS_COL = "rawLogs";
    static final String TIME = "time";
    static final String START = "start";
    static final String END = "end";
    static final String STAGE_TYPE = "stage_type";
    static final String ERROR = "error";

    private static final String SECONDS = "processing_time_in_seconds";
    private static final String MINUTES = "processing_time_in_minutes";

    final SparkSession sparkSession;
    final String logsTableName;

    private final LocalDateTime startOfProcessing;

    ReportProcessor(SparkSession sparkSession, String logsTableName, LocalDateTime startOfProcessing) {
        this.sparkSession = sparkSession;
        this.logsTableName = logsTableName;
        this.startOfProcessing = startOfProcessing;
    }

    public abstract void createReport();

    void writeDf(Dataset<Row> writeableDf, String tableName) {
        writeableDf.toDF().write()
                .format(PARQUET)
                .mode(SaveMode.Overwrite)
                .option("path",
                        Environment.getConfig()
                                .getProperty("hdfs.dir.validation") + "parquet/" + tableName)
                .saveAsTable(getSchemaPrefix() + tableName);

        writeableDf.repartition(1)
                .toDF()
                .write()
                .format("csv")
                .mode(SaveMode.Overwrite)
                .option("header", "true")
                .save(Environment.getConfig().getProperty("hdfs.dir.logs") +
                        getModificationDate() + "/" +
                        "csv_reports_" + getModificationDateTime() + "/" +
                        tableName);
    }

    Dataset<Row> countTime(Dataset<Row> xlsParsingDf) {
        return xlsParsingDf.withColumn(SECONDS,
                unix_timestamp(col(END)).minus(unix_timestamp(col(START))))
                .withColumn(MINUTES, col(SECONDS).divide(lit(60))
                        .cast(DataTypes.IntegerType))
                .drop(START, END)
                .sort(desc(SECONDS));
    }

    Dataset<Row> unionWithStage(Dataset<Row> first, Dataset<Row> second) {
        return first.select(STAGE_TYPE, START, END).union(second.select(STAGE_TYPE, START, END));
    }

    Dataset<Row> aggregateByTime(Dataset<Row> stagesDf) {
        return stagesDf.agg(min(col(TIME)).alias(START),
                max(col(TIME)).alias(END));
    }

    Dataset<Row> filterLog(String searchValue) {
        return sparkSession
                .table(logsTableName)
                .select(RAW_LOGS_COL, TIME)
                .where(col(RAW_LOGS_COL)
                        .contains(START_OF + searchValue)
                        .or(col(RAW_LOGS_COL).contains(END_OF + searchValue)));
    }

    private String getModificationDate() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(startOfProcessing);
    }

    private String getModificationDateTime() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(startOfProcessing);
    }
}
