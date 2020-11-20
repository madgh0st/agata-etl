package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.FnsTaxViolationModel;
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
import static com.kpmg.agata.test.utils.spark.SparkTestUtils.getSparkSession;

public class FnsTaxViolationSqlTest {

    private static final String RES_DIR = "sql/clean/fns_tax_violation/";
    private static final String SRC_DIR = "src/main/resources/sql/clean/fns_tax_violation/";
    private static SparkSession sparkSession;

    @BeforeClass
    public static void beforeClass() {
        sparkSession = getSparkSession();
        UdfRegistrar.register(sparkSession);
        SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union.csv")
                      .createOrReplaceTempView("fns_tax_violation_sort_do");
        SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv")
                      .createOrReplaceTempView("union_fns_tax_violation");
        SparkTestUtils.csv(sparkSession, RES_DIR + "2_after_collisions_report.csv")
                      .createOrReplaceTempView("collisions_report_fns_tax_violation");
        SparkTestUtils.csv(sparkSession, RES_DIR + "3_after_clean_table.csv")
                      .createOrReplaceTempView("clean_fns_tax_violation");
        SparkTestUtils.csv(sparkSession, "sql/clean/md_contractor_dict.csv")
                      .createOrReplaceTempView("md_contractor_dict");
    }

    @Test
    public void beforeUnionTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union.csv");

        FnsTaxViolationModel firstTaxViolation = new FnsTaxViolationModel();
        firstTaxViolation.setCreationDate("02-01-1970");
        firstTaxViolation.setDocDate("01.01.1970");
        firstTaxViolation.setDocId("docId0");
        firstTaxViolation.setEventDate("01-01-1970");
        firstTaxViolation.setFileName("src/test/resources/xml/fns-tax-violation-test.xml");
        firstTaxViolation.setFine("42.0000");
        firstTaxViolation.setInnul("001");
        firstTaxViolation.setLoadDate("02-01-1970");
        firstTaxViolation.setModificationDate("01-01-1970");
        firstTaxViolation.setName_do("do1");
        firstTaxViolation.setOrgName("company1");
        firstTaxViolation.setSheetName("sheet1");

        FnsTaxViolationModel secondTaxViolation = new FnsTaxViolationModel();
        secondTaxViolation.setCreationDate("01-01-1970");
        secondTaxViolation.setDocDate("31.12.1970");
        secondTaxViolation.setDocId("docId1");
        secondTaxViolation.setEventDate("02-01-1970");
        secondTaxViolation.setFileName("src/test/resources/xml/fns-tax-violation-test.xml");
        secondTaxViolation.setFine("100500.0000");
        secondTaxViolation.setInnul("003");
        secondTaxViolation.setLoadDate("02-01-1970");
        secondTaxViolation.setModificationDate("01-01-1970");
        secondTaxViolation.setName_do("do1");
        secondTaxViolation.setOrgName("«ООО company2»");
        secondTaxViolation.setSheetName("sheet1");

        Dataset<Row> actual =
                sparkSession.createDataFrame(asList(firstTaxViolation, secondTaxViolation), FnsTaxViolationModel.class);

        assertEquals(expected, actual);
    }

    @Test
    public void unionSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "1_union_fns_tax_violation.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void collisionsReportSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "2_after_collisions_report.csv");

        String sql = new TestSqlLoader().fromPath(
                SRC_DIR + "2_collisions_report_fns_tax_violation.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void cleanTableSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "3_after_clean_table.csv");

        String unionReplacer = "select 'do1' as name_do " +
                "UNION select 'do2' as name_do " +
                "UNION select 'do3' as name_do";
        String sql = new TestSqlLoader().fromPath(SRC_DIR + "3_clean_table_fns_tax_violation.sql")
                                        .inject("union_replacer", unionReplacer)
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void validationSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "4_after_validation.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "4_validation_fns_tax_violation.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }
}
