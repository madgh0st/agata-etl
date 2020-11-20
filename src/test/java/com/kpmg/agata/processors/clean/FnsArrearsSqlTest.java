package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.FnsArrearsModel;
import com.kpmg.agata.test.utils.TestSqlLoader;
import com.kpmg.agata.test.utils.spark.SparkTestUtils;
import com.kpmg.agata.utils.sql.UdfRegistrar;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.BeforeClass;
import org.junit.Test;

import static java.util.Arrays.asList;

import static com.kpmg.agata.test.utils.spark.SparkTestUtils.assertEquals;

public class FnsArrearsSqlTest {

    private static final String CSV_CLEAN_FNS_ARREARS = "sql/clean/fns_arrears/";
    private static final String SQL_CLEAN_FNS_ARREARS = "src/main/resources/sql/clean/fns_arrears/";
    private static SparkSession sparkSession;

    @BeforeClass
    public static void setup() {
        sparkSession = SparkTestUtils.getSparkSession();
        UdfRegistrar.register(sparkSession);
        SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_ARREARS + "0_before_union.csv")
                      .createOrReplaceTempView("fns_arrears_sort_do");
        SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_ARREARS + "1_after_union.csv")
                      .createOrReplaceTempView("union_fns_arrears");
        SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_ARREARS + "2_after_collisions_report.csv")
                      .createOrReplaceTempView("collisions_report_fns_arrears");
        SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_ARREARS + "3_after_clean_table.csv")
                      .createOrReplaceTempView("clean_fns_arrears");

        SparkTestUtils.csv(sparkSession, "sql/clean/md_contractor_dict.csv")
                      .createOrReplaceTempView("md_contractor_dict");
    }

    @Test
    public void beforeUnionTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_ARREARS + "0_before_union.csv");

        FnsArrearsModel firstFnsArrears = new FnsArrearsModel();
        firstFnsArrears.setArrearsFine("2.00");
        firstFnsArrears.setCreationDate("01.01.1971");
        firstFnsArrears.setDocDate("01.01.1970");
        firstFnsArrears.setDocId("docId0");
        firstFnsArrears.setFileName("fns-arrears-test.xml");
        firstFnsArrears.setInnul("001");
        firstFnsArrears.setLoadDate("01.02.1970");
        firstFnsArrears.setModificationDate("01.02.1970");
        firstFnsArrears.setName_do("sort_do");
        firstFnsArrears.setOrgName("company1");
        firstFnsArrears.setPenalties("1.00");
        firstFnsArrears.setSheetName("null");
        firstFnsArrears.setTaxArrears("0.00");
        firstFnsArrears.setTaxName("taxName1");
        firstFnsArrears.setTotalArrears("3.00");

        FnsArrearsModel secondFnsArrears = new FnsArrearsModel();
        secondFnsArrears.setArrearsFine("2.00");
        secondFnsArrears.setCreationDate("02.01.1971");
        secondFnsArrears.setDocDate("02.01.1970");
        secondFnsArrears.setDocId("docId0");
        secondFnsArrears.setFileName("fns-arrears-test.xml");
        secondFnsArrears.setInnul("003");
        secondFnsArrears.setLoadDate("01.02.1970");
        secondFnsArrears.setModificationDate("01.02.1970");
        secondFnsArrears.setName_do("sort_do");
        secondFnsArrears.setOrgName("«ООО company2»");
        secondFnsArrears.setPenalties("1.00");
        secondFnsArrears.setSheetName("null");
        secondFnsArrears.setTaxArrears("0.00");
        secondFnsArrears.setTaxName("taxName2");
        secondFnsArrears.setTotalArrears("3.00");

        Dataset<Row> actual =
                sparkSession.createDataFrame(asList(firstFnsArrears, secondFnsArrears), FnsArrearsModel.class);

        assertEquals(expected, actual);
    }

    @Test
    public void unionSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_ARREARS + "1_after_union.csv");
        String sql = new TestSqlLoader()
                .fromPath(SQL_CLEAN_FNS_ARREARS + "1_union_fns_arrears.sql")
                .tablesToViews("{dbSchema}")
                .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void collisionsReportSqlTest() {
        Dataset<Row> expected =
                SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_ARREARS + "2_after_collisions_report.csv");

        String sql = new TestSqlLoader().fromPath(
                SQL_CLEAN_FNS_ARREARS + "2_collisions_report_fns_arrears.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void cleanTableSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_ARREARS + "3_after_clean_table.csv");

        String unionReplacer = "select 'do1' as name_do " +
                "UNION select 'do2' as name_do " +
                "UNION select 'do3' as name_do";
        String sql = new TestSqlLoader().fromPath(SQL_CLEAN_FNS_ARREARS + "3_clean_table_fns_arrears.sql")
                                        .inject("union_replacer", unionReplacer)
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void validationSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_ARREARS + "4_after_validation.csv");

        String sql = new TestSqlLoader().fromPath(SQL_CLEAN_FNS_ARREARS + "4_validation_fns_arrears.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }
}
