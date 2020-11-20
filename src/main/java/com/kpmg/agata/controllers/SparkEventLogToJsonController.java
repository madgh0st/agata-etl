package com.kpmg.agata.controllers;

import com.kpmg.agata.processors.eventlog.EventLogProcessor;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class SparkEventLogToJsonController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(SparkEventLogToJsonController.class);
    private final SparkSession sparkSession;

    public SparkEventLogToJsonController(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    @SuppressWarnings({"Convert2MethodRef", "squid:S1612"})
    @Override
    protected void startAction() {
        log.info("Starting write to HDFS");
        Stream.of(
                new EventLogProcessor(sparkSession)
        )
              // Spark serializer doesn't work properly with method reference, so leave this line as-is
              .forEach(service -> service.putToHdfs());

    }

}
