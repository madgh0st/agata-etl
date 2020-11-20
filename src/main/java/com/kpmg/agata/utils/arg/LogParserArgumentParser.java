package com.kpmg.agata.utils.arg;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class LogParserArgumentParser implements ArgumentParser {
    private String appPropertiesPath;
    private String inputLogPath;
    private String outputLogPath;

    public String getAppPropertiesPath() {
        return appPropertiesPath;
    }

    public String getInputLogPath() {
        return inputLogPath;
    }

    public String getOutputLogPath() {
        return outputLogPath;
    }

    public void run(String[] args) {
        if (args == null || args.length != 3) {
            throw new IllegalArgumentException("3 arguments required");
        }

        this.appPropertiesPath = args[0];
        this.inputLogPath = args[1];
        this.outputLogPath = args[2];

        validateAppPropertiesPath();
        validateInputLogPath();
        validateOutputLogPath();
    }

    private void validateAppPropertiesPath() {
        if (StringUtils.isBlank(appPropertiesPath)) {
            throw new IllegalArgumentException("Argument appPropertiesPath must be empty");
        }

        if (Files.notExists(Paths.get(appPropertiesPath))) {
            throw new IllegalArgumentException("Argument appPropertiesPath file must exist");
        }
    }

    private void validateInputLogPath() {
        if (StringUtils.isBlank(inputLogPath)) {
            throw new IllegalArgumentException("Argument inputLogPath must be empty");
        }
    }

    private void validateOutputLogPath() {
        if (StringUtils.isBlank(outputLogPath)) {
            throw new IllegalArgumentException("Argument outputLogPath must be empty");
        }
    }
}
