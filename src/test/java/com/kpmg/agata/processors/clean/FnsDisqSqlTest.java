package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.FnsDisqModel;
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

public class FnsDisqSqlTest {

    private static final String CSV_CLEAN_FNS_DISQ = "sql/clean/fns_disq/";
    private static final String SQL_CLEAN_FNS_DISQ = "src/main/resources/sql/clean/fns_disq/";
    private static SparkSession sparkSession;

    @BeforeClass
    public static void setup() {
        sparkSession = SparkTestUtils.getSparkSession();
        UdfRegistrar.register(sparkSession);
        SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_DISQ + "0_before_union.csv")
                      .createOrReplaceTempView("fns_disq_sort_do");
        SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_DISQ + "1_after_union.csv")
                      .createOrReplaceTempView("union_fns_disq");
        SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_DISQ + "2_after_collisions_report.csv")
                      .createOrReplaceTempView("collisions_report_fns_disq");
        SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_DISQ + "3_after_clean_table.csv")
                      .createOrReplaceTempView("clean_fns_disq");
        SparkTestUtils.csv(sparkSession, "sql/clean/md_contractor_dict.csv")
                      .createOrReplaceTempView("md_contractor_dict");
    }

    @Test
    public void beforeUnionTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_DISQ + "0_before_union.csv");

        FnsDisqModel firstFnsDisq = new FnsDisqModel();
        firstFnsDisq.setAddress("address1");
        firstFnsDisq.setFileName("data-01011970-structure-02011970.csv");
        firstFnsDisq.setInn("001");
        firstFnsDisq.setKpp("101");
        firstFnsDisq.setName_do("do1");
        firstFnsDisq.setOgrn("201");
        firstFnsDisq.setOrganizationFullName("company1");
        firstFnsDisq.setSheetName("sheet1");
        firstFnsDisq.setModificationDate("03-01-1970");
        firstFnsDisq.setLoadDate("04-01-1970");

        FnsDisqModel secondFnsDisq = new FnsDisqModel();
        secondFnsDisq.setAddress("address3");
        secondFnsDisq.setFileName("data-01031970-structure-02031970.csv");
        secondFnsDisq.setInn("003");
        secondFnsDisq.setKpp("103");
        secondFnsDisq.setName_do("do1");
        secondFnsDisq.setOgrn("203");
        secondFnsDisq.setOrganizationFullName("«ООО company3»");
        secondFnsDisq.setSheetName("sheet1");
        secondFnsDisq.setModificationDate("03-03-1970");
        secondFnsDisq.setLoadDate("04-03-1970");

        Dataset<Row> actual = sparkSession.createDataFrame(asList(firstFnsDisq, secondFnsDisq), FnsDisqModel.class);

        assertEquals(expected, actual);
    }

    @Test
    public void unionSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_DISQ + "1_after_union.csv");

        String sql = new TestSqlLoader()
                .fromPath(SQL_CLEAN_FNS_DISQ + "1_union_fns_disq.sql")
                .tablesToViews("{dbSchema}")
                .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void collisionsReportSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_DISQ + "2_after_collisions_report.csv");

        String sql = new TestSqlLoader().fromPath(
                SQL_CLEAN_FNS_DISQ + "2_collisions_report_fns_disq.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void cleanTableSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_DISQ + "3_after_clean_table.csv");

        String unionReplacer = "select 'do1' as name_do " +
                "UNION select 'do2' as name_do " +
                "UNION select 'do3' as name_do";
        String sql = new TestSqlLoader().fromPath(SQL_CLEAN_FNS_DISQ + "3_clean_table_fns_disq.sql")
                                        .inject("union_replacer", unionReplacer)
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void validationSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, CSV_CLEAN_FNS_DISQ + "4_after_validation.csv");

        String sql = new TestSqlLoader().fromPath(SQL_CLEAN_FNS_DISQ + "4_validation_fns_disq.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }
}
