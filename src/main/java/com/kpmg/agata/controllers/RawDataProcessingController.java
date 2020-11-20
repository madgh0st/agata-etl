package com.kpmg.agata.controllers;

import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.Environment;
import com.kpmg.agata.processors.raw.RawDataToJsonProcessor;
import com.kpmg.agata.processors.raw.RawFileSystemProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RawDataProcessingController extends AbstractController {
    private static Logger log = LoggerFactory.getLogger(RawDataProcessingController.class);
    private SparkSession sparkSession;
    private String dateKey;
    private ConfigParser config;

    private final String hdfsDirJsonDeltas = Environment.getConfig().getProperty("hdfs.dir.json_deltas");
    private final String hdfsDirSrcXls = Environment.getConfig().getProperty("hdfs.dir.src.xls");
    private final String hdfsDirResultJson = Environment.getConfig().getProperty("hdfs.dir.result_jsons");


    public RawDataProcessingController(String dateKey, ConfigParser config, SparkSession sparkSession) {
        this.dateKey = dateKey;
        this.config = config;
        this.sparkSession = sparkSession;
    }

    @Override
    protected void startAction() {

        if (StringUtils.isBlank(hdfsDirJsonDeltas)) {
            throw new IllegalArgumentException("hdfsDirJsonDeltas property can't be empty");
        }

        if (StringUtils.isBlank(hdfsDirSrcXls)) {
            throw new IllegalArgumentException("hdfsDirSrcXls property can't be empty");
        }

        if (StringUtils.isBlank(hdfsDirResultJson)) {
            throw new IllegalArgumentException("hdfsDirResultJson property can't be empty");
        }

        RawFileSystemProcessor
                fsProcessor = new RawFileSystemProcessor(hdfsDirJsonDeltas, hdfsDirSrcXls, hdfsDirResultJson, sparkSession);

        List<String> availableDateListXls = fsProcessor.getAvailableDateListXls();

        List<String> dateList = new ArrayList<>();

        // dateRaw: (latest | all | 2019-12-28)
        switch (dateKey) {
            case "all":
                log.info("Handling xls for all date");
                dateList = availableDateListXls;
                break;
            case "latest":
                log.info("Handling xls for latest date");
                String maxDate = availableDateListXls
                        .stream()
                        .max(Comparator.comparing(String::valueOf))
                        .orElseThrow(() -> new RuntimeException("Incorrect max date"));
                dateList.add(maxDate);
                break;
            default:
                String inputDate = availableDateListXls
                        .stream()
                        .filter(availableDate -> availableDate.equals(dateKey))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException(
                                String.format("Input date (%s) is incorrect. Available date: {%s}"
                                        , dateKey
                                        , String.join(", ", availableDateListXls)))
                        );
                log.info("Handling xls for input date: {}", inputDate);
                dateList.add(inputDate);
        }

        fsProcessor.createTableCheckSumToHive();

        if (this.dateKey.equals("all")) {
            fsProcessor.removeDirsJsonDeltasAndResultJson();
        } else {
            fsProcessor.removeInconsistentFilesJsonDeltas();
            fsProcessor.removeDirsJsonDeltas(dateList);
            fsProcessor.removeFilesInResultJsons(dateList);
        }

        RawDataToJsonProcessor rawDataToJsonProcessor = new RawDataToJsonProcessor();
        rawDataToJsonProcessor.parseWhiteList();
        rawDataToJsonProcessor.parseData(dateList, config);

        fsProcessor.copyJsonDeltasToResultDir(dateList);
    }

}
