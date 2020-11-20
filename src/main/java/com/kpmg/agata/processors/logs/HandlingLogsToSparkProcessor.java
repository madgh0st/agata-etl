package com.kpmg.agata.processors.logs;

import com.kpmg.agata.config.Environment;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import static com.kpmg.agata.constant.database.BaseConstants.PARQUET;

public class HandlingLogsToSparkProcessor {

    private SparkSession sparkSession;
    private String outputFile;
    private String tableName;

    public HandlingLogsToSparkProcessor(SparkSession sparkSession, String outputFile, String tableName) {
        this.sparkSession = sparkSession;
        this.outputFile = outputFile;
        this.tableName = tableName;
    }

    public void createTableLogsToHive() {
        sparkSession.sqlContext()
                .read()
                .format("json")
                .load(outputFile)
                .toDF()
                .write()
                .format(PARQUET)
                .mode(SaveMode.Overwrite)
                .option("path", Environment.getConfig().getProperty("hdfs.dir.logs") + PARQUET)
                .saveAsTable(tableName);
    }
}
