package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.FsspModel;
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

public class FsspSqlTest {

    private static final String RES_DIR = "sql/clean/fssp/";
    private static final String SRC_DIR = "src/main/resources/sql/clean/fssp/";
    private static SparkSession sparkSession;

    @BeforeClass
    public static void beforeClass() {
        sparkSession = SparkTestUtils.getSparkSession();
        UdfRegistrar.register(sparkSession);
        SparkTestUtils
                .csv(sparkSession, RES_DIR + "0_before_union.csv")
                .createOrReplaceTempView("fssp_sort_do");
        SparkTestUtils
                .csv(sparkSession, RES_DIR + "1_after_union.csv")
                .createOrReplaceTempView("union_fssp");
        SparkTestUtils
                .csv(sparkSession, RES_DIR + "2_after_collisions_report.csv")
                .createOrReplaceTempView("collisions_report_fssp");
        SparkTestUtils
                .csv(sparkSession, RES_DIR + "3_after_clean_table.csv")
                .createOrReplaceTempView("clean_fssp");
        SparkTestUtils
                .csv(sparkSession, "sql/clean/md_contractor_dict.csv")
                .createOrReplaceTempView("md_contractor_dict");
    }

    @Test
    public void beforeUnionTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union.csv");

        FsspModel firstFssp = new FsspModel();
        firstFssp.setName_do("do1");
        firstFssp.setFileName("file1");
        firstFssp.setSheetName("sheet1");
        firstFssp.setModificationDate("1970-01-01");
        firstFssp.setLoadDate("1970-01-02");
        firstFssp.setDebtorName("company1");
        firstFssp.setProceedingInstitutionDate("1970-01-23");
        firstFssp.setExecutiveDocumentDate("1970-01-24");
        firstFssp.setExecutiveDocumentType("type1");
        firstFssp.setExecutiveDocumentObject("obj1");
        firstFssp.setExecutionObject("obj1");
        firstFssp.setDebt("123");
        firstFssp.setRemainingDebt("123");
        firstFssp.setDateAndCompletionReason("smth");

        FsspModel secondFssp = new FsspModel();
        secondFssp.setName_do("do1");
        secondFssp.setFileName("file1");
        secondFssp.setSheetName("sheet1");
        secondFssp.setModificationDate("1970-01-01");
        secondFssp.setLoadDate("1970-01-02");
        secondFssp.setDebtorName("«ООО company3»");
        secondFssp.setProceedingInstitutionDate("1970-01-23");
        secondFssp.setExecutiveDocumentDate("1970-01-24");
        secondFssp.setExecutiveDocumentType("type2");
        secondFssp.setExecutiveDocumentObject("obj2");
        secondFssp.setExecutionObject("obj2");
        secondFssp.setDebt("123");
        secondFssp.setRemainingDebt("123");
        secondFssp.setDateAndCompletionReason("smth");

        Dataset<Row> actual = sparkSession.createDataFrame(asList(firstFssp, secondFssp), FsspModel.class);

        assertEquals(expected, actual);
    }

    @Test
    public void unionSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv");

        String sql = new TestSqlLoader()
                .fromPath(SRC_DIR + "1_union_fssp.sql")
                .tablesToViews("{dbSchema}")
                .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void collisionsReportSqlTest() {
        Dataset<Row> expected = SparkTestUtils
                .csv(sparkSession, RES_DIR + "2_after_collisions_report.csv");

        String sql = new TestSqlLoader()
                .fromPath(SRC_DIR + "2_collisions_report_fssp.sql")
                .tablesToViews("{dbSchema}")
                .get();
        Dataset<Row> actual = sparkSession.sql(sql);
        assertEquals(expected, actual);
    }

    @Test
    public void cleanTableSqlTest() {
        Dataset<Row> expected = SparkTestUtils
                .csv(sparkSession, RES_DIR + "3_after_clean_table.csv");

        String unionReplacer = "SELECT 'do1' AS name_do " +
                "UNION select 'do2' as name_do " +
                "UNION select 'do3' as name_do";
        String sql = new TestSqlLoader()
                .fromPath(SRC_DIR + "3_clean_table_fssp.sql")
                .inject("union_replacer", unionReplacer)
                .tablesToViews("{dbSchema}")
                .get();
        Dataset<Row> actual = sparkSession.sql(sql);
        assertEquals(expected, actual);
    }

    @Test
    public void validationSqlTest() {
        Dataset<Row> expected = SparkTestUtils
                .csv(sparkSession, RES_DIR + "4_after_validation.csv");

        String sql = new TestSqlLoader()
                .fromPath(SRC_DIR + "4_validation_fssp.sql")
                .tablesToViews("{dbSchema}")
                .get();
        Dataset<Row> actual = sparkSession.sql(sql);
        assertEquals(expected, actual);
    }
}
