package com.kpmg.agata.controllers;

import com.kpmg.agata.config.Environment;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import static com.kpmg.agata.config.Environment.getSchemaPrefix;
import static com.kpmg.agata.constant.database.BaseConstants.AVAILABLE_LIST_DO_TABLE;

public class AvailableListDoController extends AbstractController {

    private SparkSession sparkSession;

    public AvailableListDoController(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    @Override
    protected void startAction() {
        sparkSession
                .sql("SELECT * FROM " + getSchemaPrefix() + AVAILABLE_LIST_DO_TABLE)
                .repartition(1)
                .write()
                .format("csv")
                .mode(SaveMode.Overwrite)
                .option("header", "true")
                .save(Environment.getConfig().getProperty("hdfs.dir.available_list_do"));
    }

}
