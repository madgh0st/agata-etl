package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.AffilationModel;
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

public class AffilationSqlTest {

    private static final String RES_DIR = "sql/clean/affilation/";
    private static final String SRC_DIR = "src/main/resources/sql/clean/affilation/";
    private static SparkSession sparkSession;

    @BeforeClass
    public static void beforeClass() {
        sparkSession = getSparkSession();
        UdfRegistrar.register(sparkSession);
        SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_clean.csv")
                      .createOrReplaceTempView("affilation_sort_do");
        SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_clean.csv")
                      .createOrReplaceTempView("clean_affilation");
    }

    @Test
    public void beforeUnionTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_clean.csv");

        AffilationModel firstAffilation = new AffilationModel();
        firstAffilation.setFullName("Компания 1, ОАО");
        firstAffilation.setOgrn("101");
        firstAffilation.setShortName("ОАО Компания 1");
        firstAffilation.setName("ОТКРЫТОЕ АКЦИОНЕРНОЕ ОБЩЕСТВО Компания 1");
        firstAffilation.setChief("Иванов Иван Иванович");
        firstAffilation.setChiefPosition("руководитель");
        firstAffilation.setChiefInn("201");
        firstAffilation.setAge("5");
        firstAffilation.setStatus("Действующая");
        firstAffilation.setInn("001");
        firstAffilation.setActivitySector("Деятельность по обеспечению общественного порядка и безопасности");
        firstAffilation.setOkved("84.24");
        firstAffilation.setPreviousInns("401");
        firstAffilation.setInfo("some important info");

        firstAffilation.setFileName("file1");
        firstAffilation.setSheetName("report");
        firstAffilation.setModificationDate("1970-01-01");
        firstAffilation.setLoadDate("1970-01-02");
        firstAffilation.setName_do("sort_do");

        AffilationModel secondAffilation = new AffilationModel();
        secondAffilation.setFileName("file1");
        secondAffilation.setSheetName("report");
        secondAffilation.setModificationDate("1970-02-01");
        secondAffilation.setLoadDate("1970-02-02");
        secondAffilation.setName_do("sort_do");


        Dataset<Row> actual = sparkSession.createDataFrame(asList(firstAffilation, secondAffilation), AffilationModel.class);

        assertEquals(expected, actual);
    }

    @Test
    public void cleanSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_clean.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "1_clean.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }
}
