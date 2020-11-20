package com.kpmg.agata.config;

import com.kpmg.agata.controllers.RawDataProcessingController;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

// todo: make this non-static
public class Environment {

    private static Logger log = LoggerFactory.getLogger(Environment.class);
    private static Properties config;

    public static SparkSession getSparkSession() {
        log.debug("Creating spark session");

        return SparkSession
                .builder()
                .config("hive.metastore.uris", config.getProperty("hive.metastore.uris"))
                .enableHiveSupport()
                .getOrCreate();
    }

    public static List<String> getListPrettyPrintAllProperties() {
        return config
                .entrySet()
                .stream()
                .map(x -> String.format("Property: %s -> %s", x.getKey(), x.getValue()))
                .collect(Collectors.toList());
    }

    public static Properties getConfig() {
        if (config == null) throw new IllegalStateException("Properties were not set");
        return config;
    }

    public static void setConfig(String path) {
        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error occurred during reading property file " + path, e);
        }

        Environment.config = properties;
    }


    private static String getResourceAsString(String configResource) {
        log.debug("Loading resource: {}", configResource);

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        RawDataProcessingController.class.getClassLoader().getResourceAsStream(configResource))))
        ) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error("Invalid configResource argument string");
            throw new IllegalArgumentException("Invalid configResourceString argument", e);
        }
    }

    public static String getSqlResource(String configResource, Object... sqlStringArgs) {
        return String.format(getResourceAsString(configResource), sqlStringArgs);
    }

    public static String getSqlResource(String configResource) {
        return getResourceAsString(configResource);
    }

    public static String getSchemaPrefix() {
        return getConfig().getProperty("db.schema") + ".";
    }
}
