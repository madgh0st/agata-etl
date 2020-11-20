package com.kpmg.agata.test.utils.spark;

import com.kpmg.agata.mapreduce.EventLogDriver;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;

import static org.junit.Assert.assertTrue;

public class SparkTestUtils {

    private static final Logger log = LoggerFactory.getLogger(SparkTestUtils.class);
    private static final StringPlainDatasetEquator DATA_FRAME_EQUATOR = new StringPlainDatasetEquator();

    private SparkTestUtils() {
    }

    public static SparkSession getSparkSession() {
        return SparkSession.builder()
                           .master("local[*]")
                           .config(new SparkConf().set("fs.defaultFS", "file:///"))
                           .appName("agata-test")
                           .getOrCreate();
    }

    public static Dataset<Row> csv(SparkSession sparkSession, String resourcePath) {
        URL resource = SparkTestUtils.class.getClassLoader()
                                           .getResource(resourcePath);
        if (resource == null) throw new IllegalArgumentException("Resource not found");

        return sparkSession.read()
                           .option("header", "true")
                           .csv(resource.getPath());
    }

    public static void assertEquals(Dataset<Row> expected, Dataset<Row> actual) {
        try {
            assertTrue(DATA_FRAME_EQUATOR.equals(expected, actual));
        } catch (AssertionError e) {
            List<String> expectedHeaders = asList(expected.schema().fieldNames());
            List<String> actualHeaders = asList(actual.schema().fieldNames());

            log.info("subtract(expectedHeaders, actualHeaders): " + CollectionUtils.subtract(expectedHeaders,
                    actualHeaders));
            log.info("subtract(actualHeaders, expectedHeaders):" + CollectionUtils.subtract(actualHeaders,
                    expectedHeaders));

            expected.show(false);
            actual.show(false);
            throw e;
        }
    }
}
