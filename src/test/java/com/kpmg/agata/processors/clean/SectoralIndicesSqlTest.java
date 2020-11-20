package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.SectoralIndexModel;
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

public class SectoralIndicesSqlTest {

    private static final String RES_DIR = "sql/clean/sectoral_indices/";
    private static final String SRC_DIR = "src/main/resources/sql/clean/sectoral_indices/";
    private static SparkSession sparkSession;

    @BeforeClass
    public static void beforeClass() {
        sparkSession = getSparkSession();
        UdfRegistrar.register(sparkSession);
        SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union.csv")
                      .createOrReplaceTempView("sectoral_indices_sort_do");
        SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv")
                      .createOrReplaceTempView("union_sectoral_indices");
        SparkTestUtils.csv(sparkSession, RES_DIR + "okved_sectoral_indices.csv")
                      .createOrReplaceTempView("okved_sectoral_indices");
        SparkTestUtils.csv(sparkSession, "sql/clean/affilation/1_after_clean.csv")
                      .createOrReplaceTempView("clean_affilation");
        SparkTestUtils.csv(sparkSession, "sql/clean/md_contractor_dict.csv")
                      .createOrReplaceTempView("md_contractor_dict");
    }

    @Test
    public void beforeUnion() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union.csv");

        SectoralIndexModel firstSectoralIndex = new SectoralIndexModel();
        firstSectoralIndex.setName_do("sort_do");
        firstSectoralIndex.setFileName("file1");
        firstSectoralIndex.setSheetName("sheet1");
        firstSectoralIndex.setModificationDate("1970-01-02");
        firstSectoralIndex.setLoadDate("1970-01-03");
        firstSectoralIndex.setId("ID-1");
        firstSectoralIndex.setName("Индекс 1");
        firstSectoralIndex.setTradeDate("1970-01-01");
        firstSectoralIndex.setOpen("1.1");
        firstSectoralIndex.setHigh("2.1");
        firstSectoralIndex.setLow("3.1");
        firstSectoralIndex.setClose("4.1");
        firstSectoralIndex.setValue("5.1");
        firstSectoralIndex.setDuration("duration-1");
        firstSectoralIndex.setYield("yield-1");

        SectoralIndexModel secondSectoralIndex = new SectoralIndexModel();
        secondSectoralIndex.setName_do("sort_do");
        secondSectoralIndex.setFileName("file1");
        secondSectoralIndex.setSheetName("sheet1");
        secondSectoralIndex.setModificationDate("1970-02-02");
        secondSectoralIndex.setLoadDate("1970-02-03");
        secondSectoralIndex.setId("ID-2");
        secondSectoralIndex.setName("Индекс 2");
        secondSectoralIndex.setTradeDate("1970-02-01");
        secondSectoralIndex.setOpen("1.2");
        secondSectoralIndex.setHigh("2.2");
        secondSectoralIndex.setLow("3.2");
        secondSectoralIndex.setClose("4.2");
        secondSectoralIndex.setValue("5.2");
        secondSectoralIndex.setDuration("duration-2");
        secondSectoralIndex.setYield("yield-2");

        Dataset<Row> actual = sparkSession.createDataFrame(asList(firstSectoralIndex, secondSectoralIndex),
                SectoralIndexModel.class);

        assertEquals(expected, actual);
    }

    @Test
    public void unionSql() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv");

        String sql = new TestSqlLoader().fromPath(SRC_DIR + "1_union.sql")
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }

    @Test
    public void cleanSql() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "2_after_clean.csv");

        String unionReplacer = "select 'do1' as name_do " +
                "UNION select 'do2' as name_do " +
                "UNION select 'do3' as name_do";
        String sql = new TestSqlLoader().fromPath(SRC_DIR + "2_clean.sql")
                                        .inject("union_replacer", unionReplacer)
                                        .tablesToViews("{dbSchema}")
                                        .get();
        Dataset<Row> actual = sparkSession.sql(sql);

        assertEquals(expected, actual);
    }
}
