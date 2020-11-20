package com.kpmg.agata.controllers;

import com.kpmg.agata.config.Environment;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.processors.validation.ValidationReportProcessor;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ValidationReportController extends AbstractController {
    private static Logger log = LoggerFactory.getLogger(ValidationReportController.class);
    private final String outputDirectory;
    private final String hiveSchema;
    private SparkSession sparkSession;
    private List<ModelTypeConfiguration> config;

    public ValidationReportController(SparkSession sparkSession, List<ModelTypeConfiguration> config) {
        this.outputDirectory = Environment.getConfig().getProperty("hdfs.dir.validation");
        this.hiveSchema = Environment.getConfig().getProperty("db.schema");
        this.sparkSession = sparkSession;
        this.config = config;
    }

    @Override
    protected void startAction() {
        log.info("Starting validation processor");
        new ValidationReportProcessor(sparkSession, outputDirectory, hiveSchema, config).start();
    }

}
