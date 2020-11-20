package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.LegalCasesModel;
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

public class LegalCasesSqlTest {

    private static final String RES_DIR = "sql/clean/legal_cases/";
    private static final String SRC_DIR = "src/main/resources/sql/clean/legal_cases/";
    private static SparkSession sparkSession;

    @BeforeClass
    public static void beforeClass() {
        sparkSession = getSparkSession();
        UdfRegistrar.register(sparkSession);
        SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union.csv")
                      .createOrReplaceTempView("legal_cases_sort_do");
        SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv")
                      .createOrReplaceTempView("union_legal_cases");
        SparkTestUtils.csv(sparkSession, RES_DIR + "2_after_collisions_report.csv")
                      .createOrReplaceTempView("collisions_report_legal_cases");
        SparkTestUtils.csv(sparkSession, RES_DIR + "3_after_clean_table.csv")
                      .createOrReplaceTempView("clean_legal_cases");
        SparkTestUtils.csv(sparkSession, "sql/clean/md_contractor_dict.csv")
                      .createOrReplaceTempView("md_contractor_dict");
    }

    @Test
    public void beforeUnionTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union.csv");

        LegalCasesModel firstLegalCase = new LegalCasesModel();
        firstLegalCase.setClaimDate("1/21/1970");
        firstLegalCase.setInn("001");
        firstLegalCase.setName("«company1»");
        firstLegalCase.setNameSpark("company1 ооо");
        firstLegalCase.setOutcomeDate("1/22/1970");
        firstLegalCase.setCaseNumber("101");
        firstLegalCase.setCategory("Прочее");
        firstLegalCase.setClaimAmount("201");
        firstLegalCase.setClaimCharge("о взыскании 201 рубля");
        firstLegalCase.setContractorID("301");
        firstLegalCase.setDictum("В удовлетворении исковых требований отказать.");
        firstLegalCase.setOutcome("Иск не удовлетворен");
        firstLegalCase.setOutcomeAmount("401");
        firstLegalCase.setSide("side1");
        firstLegalCase.setStatus("Завершено");
        firstLegalCase.setSheetName("Арбитражные дела");
        firstLegalCase.setLoadDate("1970-01-03");
        firstLegalCase.setModificationDate("1970-01-04");
        firstLegalCase.setFileName("filename1");

        LegalCasesModel secondLegalCase = new LegalCasesModel();
        secondLegalCase.setClaimDate("3/21/1970");
        secondLegalCase.setInn("003");
        secondLegalCase.setName("«company3»");
        secondLegalCase.setNameSpark("company3 ооо");
        secondLegalCase.setOutcomeDate("3/22/1970");
        secondLegalCase.setCaseNumber("103");
        secondLegalCase.setCategory("Прочее");
        secondLegalCase.setClaimAmount("203");
        secondLegalCase.setClaimCharge(null);
        secondLegalCase.setContractorID("303");
        secondLegalCase.setDictum(null);
        secondLegalCase.setOutcome(null);
        secondLegalCase.setOutcomeAmount("403");
        secondLegalCase.setSide("side3");
        secondLegalCase.setStatus("Рассматривается в 1-й инстанции");
        secondLegalCase.setSheetName("Арбитражные дела");
        secondLegalCase.setLoadDate("1970-03-03");
        secondLegalCase.setModificationDate("1970-03-04");
        secondLegalCase.setFileName("filename3");

        Dataset<Row> actual =
                sparkSession.createDataFrame(asList(firstLegalCase, secondLegalCase), LegalCasesModel.class);

        assertEquals(expected, actual);
    }

    @Test
    public void unionSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "1_union_legal_cases.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void collisionsReportSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "2_after_collisions_report.csv");

        String sql = new TestSqlLoader().fromPath(
                SRC_DIR + "2_collisions_report.sql")
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
        String sql = new TestSqlLoader().fromPath(SRC_DIR + "3_clean_table.sql")
                                        .inject("union_replacer", unionReplacer)
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void validationSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "4_after_validation.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "4_validation.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }
}
