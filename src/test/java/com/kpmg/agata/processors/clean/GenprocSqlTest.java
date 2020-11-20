package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.GenprocModel;
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

public class GenprocSqlTest {

    private static final String RES_DIR = "sql/clean/genproc/";
    private static final String SRC_DIR = "src/main/resources/sql/clean/genproc/";
    private static SparkSession sparkSession;

    @BeforeClass
    public static void beforeClass() {
        sparkSession = getSparkSession();
        UdfRegistrar.register(sparkSession);
        SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union.csv")
                      .createOrReplaceTempView("genproc_sort_do");
        SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv")
                      .createOrReplaceTempView("union_genproc");
        SparkTestUtils.csv(sparkSession, RES_DIR + "2_after_collisions_report.csv")
                      .createOrReplaceTempView("collisions_report_genproc");
        SparkTestUtils.csv(sparkSession, RES_DIR + "3_after_clean_table.csv")
                      .createOrReplaceTempView("clean_genproc");
        SparkTestUtils.csv(sparkSession, "sql/clean/md_contractor_dict.csv")
                      .createOrReplaceTempView("md_contractor_dict");
    }

    @Test
    public void beforeUnionTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union.csv");

        GenprocModel firstGenproc = new GenprocModel();
        firstGenproc.setName_do("do1");
        firstGenproc.setFileName("file1");
        firstGenproc.setSheetName("sheet1");
        firstGenproc.setModificationDate("01-01-1970");
        firstGenproc.setLoadDate("02-01-1970");
        firstGenproc.setCompanyName("company1");
        firstGenproc.setInn("001");
        firstGenproc.setOgrn("101");
        firstGenproc.setKpp("201");
        firstGenproc.setDecisionDate("03.01.1970");

        GenprocModel secondGenproc = new GenprocModel();
        secondGenproc.setName_do("do1");
        secondGenproc.setFileName("file1");
        secondGenproc.setSheetName("sheet1");
        secondGenproc.setModificationDate("04-01-1970");
        secondGenproc.setLoadDate("05-01-1970");
        secondGenproc.setCompanyName("«ООО company3»");
        secondGenproc.setInn("003");
        secondGenproc.setOgrn("103");
        secondGenproc.setKpp("203");
        secondGenproc.setDecisionDate("06.01.1970");

        Dataset<Row> actual = sparkSession.createDataFrame(asList(firstGenproc, secondGenproc), GenprocModel.class);

        assertEquals(expected, actual);
    }

    @Test
    public void unionSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "1_union_genproc.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void collisionsReportSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "2_after_collisions_report.csv");

        String sql = new TestSqlLoader().fromPath(
                SRC_DIR + "2_collisions_report_genproc.sql")
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
        String sql = new TestSqlLoader().fromPath(SRC_DIR + "3_clean_table_genproc.sql")
                                        .inject("union_replacer", unionReplacer)
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void validationSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "4_after_validation.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "4_validation_genproc.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }
}
