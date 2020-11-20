package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.SparkInterfaxSummaryModel;
import com.kpmg.agata.test.utils.TestSqlLoader;
import com.kpmg.agata.test.utils.spark.SparkTestUtils;
import com.kpmg.agata.utils.sql.UdfRegistrar;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.BeforeClass;
import org.junit.Test;

import static java.util.Collections.singletonList;

import static com.kpmg.agata.test.utils.spark.SparkTestUtils.assertEquals;
import static com.kpmg.agata.test.utils.spark.SparkTestUtils.getSparkSession;

public class SparkInterfaxSummarySqlTest {

    private static final String RES_DIR = "sql/clean/spark_interfax_summary/";
    private static final String SRC_DIR = "src/main/resources/sql/clean/spark_interfax_summary/";
    private static SparkSession spark;

    @BeforeClass
    public static void beforeClass() {
        spark = getSparkSession();
        UdfRegistrar.register(spark);
        SparkTestUtils.csv(spark, RES_DIR + "0_before_union.csv")
                      .createOrReplaceTempView("spark_interfax_summary_sort_do ");
        SparkTestUtils.csv(spark, RES_DIR + "1_after_union.csv")
                      .createOrReplaceTempView("union_spark_interfax_summary");
        SparkTestUtils.csv(spark, RES_DIR + "2_after_collisions_report.csv")
                      .createOrReplaceTempView("collisions_report_spark_interfax_summary");
        SparkTestUtils.csv(spark, RES_DIR + "3_after_clean_table.csv")
                      .createOrReplaceTempView("clean_spark_interfax_summary");
        SparkTestUtils.csv(spark, "sql/clean/md_contractor_dict.csv")
                      .createOrReplaceTempView("md_contractor_dict");
    }


    @Test
    public void beforeUnionTest() {
        Dataset<Row> expected = SparkTestUtils.csv(spark, RES_DIR + "0_before_union.csv");

        SparkInterfaxSummaryModel spIfx = new SparkInterfaxSummaryModel();
        spIfx.setFileName("/1970.01.01/file");
        spIfx.setSheetName(null);
        spIfx.setLoadDate(null);
        spIfx.setModificationDate(null);
        spIfx.setName("«ООО company1»");
        spIfx.setInn("001");
        spIfx.setStatus("Действующая");
        spIfx.setSparkInterfaxCreditLimit("1 000");
        spIfx.setCautionIndex("1");
        spIfx.setFinancialRiskIndex("2");
        spIfx.setPaymentDisciplineIndex("3");
        spIfx.setRiskFactors("Низкий риск");
        spIfx.setNegativeRegistries("reg1,reg2");
        spIfx.setPledges("Нет");
        spIfx.setLegalCasesCountTwoYears("4");
        spIfx.setLegalCasesClaimsSumTwoYears("5.5");
        spIfx.setLegalCasesDecisionsSumTwoYears("6.6");
        spIfx.setNews1("news01");
        spIfx.setNews2("news02");
        spIfx.setNews3("news03");
        spIfx.setNews4("news04");
        spIfx.setIsDishonestSupplier("В реестре не значится");

        Dataset<Row> actual = spark.createDataFrame(singletonList(spIfx), SparkInterfaxSummaryModel.class);

        assertEquals(expected, actual);
    }

    @Test
    public void unionSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(spark, RES_DIR + "1_after_union.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "1_union_spark_interfax_summary.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = spark.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void collisionsReportSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(spark, RES_DIR + "2_after_collisions_report.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "2_collision_report.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = spark.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void cleanTableSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(spark, RES_DIR + "3_after_clean_table.csv");

        String unionReplacer = "select 'do1' as name_do " +
                "UNION select 'do2' as name_do " +
                "UNION select 'do3' as name_do";
        String sql = new TestSqlLoader().fromPath(SRC_DIR + "3_clean_table.sql")
                                        .inject("union_replacer", unionReplacer)
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = spark.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void validationSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(spark, RES_DIR + "4_after_validation.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "4_validation.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = spark.sql(sql);

        assertEquals(expected, actual);
    }
}
