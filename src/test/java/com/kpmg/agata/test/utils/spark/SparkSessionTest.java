package com.kpmg.agata.test.utils.spark;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.min;
import static org.junit.Assert.assertEquals;

public class SparkSessionTest {

    private static SparkSession sparkSession;
    private static Dataset<Row> csvDf;
    private final String testTableName = "testTable";

    @BeforeClass
    public static void setup() {
        sparkSession = SparkTestUtils.getSparkSession();
        csvDf = sparkSession.read().csv("src/test/resources/csv/spark_session_test_data.csv");
    }

    @Test(expected = AnalysisException.class)
    public void wrongColNameSelect() {
        csvDf.select("wrongColName");
    }

    @Test
    public void readValidation() {
        List<Row> csvList = csvDf.collectAsList();
        assertEquals("1", csvList.get(0).get(0));
    }

    @Test
    public void inMemoryCreation() {
        List<String> data = Arrays.asList("hello", "world");
        Dataset<String> df = sparkSession.createDataset(data, Encoders.STRING());
        List<String> dfList = df.collectAsList();
        assertEquals("hello", dfList.get(0));
        assertEquals("world", dfList.get(1));
    }

    @Test
    public void dfTransformation(){
        Dataset<Row> aggDf = csvDf.agg(min(col("_c0")).alias("min"), max(col("_c0")).alias("max"));
        List<Row> aggList = aggDf.collectAsList();
        assertEquals("1", aggList.get(0).get(0));
        assertEquals("3", aggList.get(0).get(1));
    }

    @Test
    public void tempViewWriteRead() {
        csvDf.createOrReplaceTempView(testTableName);
        Dataset<Row> tableDf = sparkSession.read().table(testTableName);
        List<Row> tableList = tableDf.collectAsList();
        assertEquals("1", tableList.get(0).get(0));
        assertEquals("4", tableList.get(0).get(1));
    }

    @Test
    public void sqlExecutionTest() {
        csvDf.createOrReplaceTempView(testTableName);
        Dataset<Row> sqlDf = sparkSession.sql("select * from " + testTableName);
        List<Row> sqlList = sqlDf.collectAsList();
        assertEquals("1", sqlList.get(0).get(0));
        assertEquals("4", sqlList.get(0).get(1));
    }
}
