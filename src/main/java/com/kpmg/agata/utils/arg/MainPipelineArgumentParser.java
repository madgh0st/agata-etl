package com.kpmg.agata.utils.arg;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static java.util.Arrays.asList;

public class MainPipelineArgumentParser implements ArgumentParser {

    private static final List<String> POSSIBLE_STEPS = asList("raw-files", "json-to-hive", "clean-tables",
            "available-list-do", "write-eventlog", "write-eventlog", "validation");

    private String dateKey;
    private String step;
    private String rawFilesConfigPath;
    private String appPropertiesPath;

    public String getDateKey() {
        return dateKey;
    }

    public String getStep() {
        return step;
    }

    public String getRawFilesConfigPath() {
        return rawFilesConfigPath;
    }

    public String getAppPropertiesPath() {
        return appPropertiesPath;
    }

    public void run(String[] args) {
        if (args == null || args.length != 4) {
            throw new IllegalArgumentException("4 arguments required");
        }

        this.dateKey = args[0];
        this.step = args[1];
        this.rawFilesConfigPath = args[2];
        this.appPropertiesPath = args[3];

        validateDateKey();
        validateStep();
        validateRawFilesConfigPath();
        validateAppPropertiesPath();
    }

    private void validateDateKey() {
        if (StringUtils.isBlank(dateKey)) {
            throw new IllegalArgumentException("Argument dateKey must be empty");
        }

        if (!"all".equals(dateKey) && !"latest".equals(dateKey)) {
            try {
                LocalDate.parse(dateKey);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Argument dateKey must be \"all\" or \"latest\" or yyyy-MM-dd date");
            }
        }
    }

    private void validateStep() {
        if (StringUtils.isBlank(step)) {
            throw new IllegalArgumentException("Argument step must be empty");
        }

        if (!POSSIBLE_STEPS.contains(step)) {
            throw new IllegalArgumentException("Argument dateKey must be one of " + POSSIBLE_STEPS);
        }
    }

    private void validateRawFilesConfigPath() {
        if (StringUtils.isBlank(rawFilesConfigPath)) {
            throw new IllegalArgumentException("Argument rawFilesConfigPath must be empty");
        }

        if (Files.notExists(Paths.get(rawFilesConfigPath))) {
            throw new IllegalArgumentException("Argument rawFilesConfigPath file must exist");
        }
    }

    private void validateAppPropertiesPath() {
        if (StringUtils.isBlank(appPropertiesPath)) {
            throw new IllegalArgumentException("Argument appPropertiesPath must be empty");
        }

        if (Files.notExists(Paths.get(appPropertiesPath))) {
            throw new IllegalArgumentException("Argument appPropertiesPath file must exist");
        }
    }
}
