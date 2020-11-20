package com.kpmg.agata.processors.eventlog;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanLegalCasesModel;
import com.kpmg.agata.models.clean.CleanSparkInterfaxSummaryModel;
import com.kpmg.agata.test.utils.TestSqlLoader;
import com.kpmg.agata.test.utils.TestUtils;
import com.kpmg.agata.test.utils.spark.SparkTestUtils;
import java.sql.Date;
import java.util.List;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static java.lang.String.format;

import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_LEGAL_CASES_TABLE;
import static com.kpmg.agata.constant.database.CleanTablesConstants.CLEAN_SPARK_INTERFAX_SUMMARY_TABLE;
import static org.apache.spark.sql.functions.col;
import static org.junit.Assert.assertEquals;

public class EventLogProcessorTest {

    private static SparkSession spark;
    private static EventLogProcessor processor;

    @BeforeClass
    public static void beforeClass() {
        TestUtils.loadTestConfig();
        spark = SparkTestUtils.getSparkSession();
        processor = new EventLogProcessor(spark);
    }

    @Test
    public void readSparkInterfaxFromTableTest() throws Exception {
        SparkTestUtils.csv(spark, "sql/clean/spark_interfax_summary/3_after_clean_table.csv")

                      .withColumn("eventdate", col("eventdate").cast("date"))
                      .withColumn("cautionIndex", col("cautionIndex").cast("int"))
                      .withColumn("financialRiskIndex", col("financialRiskIndex").cast("int"))
                      .withColumn("paymentDisciplineIndex", col("paymentDisciplineIndex").cast("int"))
                      .withColumn("sparkInterfaxCreditLimit",
                              col("sparkInterfaxCreditLimit").cast("double"))
                      .withColumn("legalCasesCountTwoYears",
                              col("legalCasesCountTwoYears").cast("int"))
                      .withColumn("legalCasesClaimsSumTwoYears",
                              col("legalCasesClaimsSumTwoYears").cast("double"))
                      .withColumn("legalCasesDecisionsSumTwoYears",
                              col("legalCasesDecisionsSumTwoYears").cast("double"))
                      .withColumn("pledges", col("pledges").cast("boolean"))
                      .withColumn("isDishonestSupplier",
                              col("isDishonestSupplier").cast("boolean"))

                      .createOrReplaceTempView(CLEAN_SPARK_INTERFAX_SUMMARY_TABLE);

        String selectSqlPattern = Whitebox.getInternalState(processor, "selectSqlPattern");
        String sql = new TestSqlLoader().fromString(format(selectSqlPattern, CLEAN_SPARK_INTERFAX_SUMMARY_TABLE))
                                        .tablesToViews("agataTest")
                                        .get();

        Class<?> clazz = Whitebox.getInnerClassType(EventLogProcessor.class, "TableLoadingModel");
        Object innerModel = Whitebox.getConstructor(clazz, String.class, Class.class)
                                    .newInstance(sql, CleanSparkInterfaxSummaryModel.class);

        JavaRDD<? extends AbstractCleanDataModel> rdd =
                Whitebox.invokeMethod(processor, "loadCleanTable", innerModel);
        List<? extends AbstractCleanDataModel> list = rdd.collect();

        assertEquals(3, list.size());

        CleanSparkInterfaxSummaryModel actual = (CleanSparkInterfaxSummaryModel) list.get(0);
        CleanSparkInterfaxSummaryModel expected = new CleanSparkInterfaxSummaryModel();
        expected.setCode("code1");
        expected.setEventDate(Date.valueOf("1970-01-01"));
        expected.setName_do("do1");
        expected.setCounterpartyName("«ООО company1»");
        expected.setCounterpartyInn("001");
        expected.setStatus("ACTIVE");
        expected.setSparkInterfaxCreditLimit(1000.0);
        expected.setCautionIndex(1);
        expected.setFinancialRiskIndex(2);
        expected.setPaymentDisciplineIndex(3);
        expected.setRiskFactors("LOW");
        expected.setNegativeRegistries("reg1,reg2");
        expected.setPledges(false);
        expected.setLegalCasesCountTwoYears(4);
        expected.setLegalCasesClaimsSumTwoYears(5.5);
        expected.setLegalCasesDecisionsSumTwoYears(6.6);
        expected.setNews("news01,news02,news03,news04");
        expected.setIsDishonestSupplier(false);

        assertEquals(expected, actual);
    }

    @Test
    public void readLegalCasesFromTableTest() throws Exception {
        SparkTestUtils.csv(spark, "sql/clean/legal_cases/3_after_clean_table.csv")
                      .withColumn("eventdate", col("eventdate").cast("date"))
                      .withColumn("claimDate", col("claimDate").cast("date"))
                      .withColumn("outcomeDate", col("outcomeDate").cast("date"))
                      .withColumn("claimAmount", col("claimAmount").cast("double"))
                      .withColumn("outcomeAmount", col("outcomeAmount").cast("double"))
                      .createOrReplaceTempView(CLEAN_LEGAL_CASES_TABLE);

        String selectSqlPattern = Whitebox.getInternalState(processor, "selectSqlPattern");
        String sql = new TestSqlLoader().fromString(format(selectSqlPattern, CLEAN_LEGAL_CASES_TABLE))
                                        .tablesToViews("agataTest")
                                        .get();

        Class<?> clazz = Whitebox.getInnerClassType(EventLogProcessor.class, "TableLoadingModel");
        Object innerModel = Whitebox.getConstructor(clazz, String.class, Class.class)
                                    .newInstance(sql, CleanLegalCasesModel.class);

        JavaRDD<? extends AbstractCleanDataModel> rdd =
                Whitebox.invokeMethod(processor, "loadCleanTable", innerModel);
        List<? extends AbstractCleanDataModel> list = rdd.collect();

        assertEquals(3, list.size());

        CleanLegalCasesModel actual = (CleanLegalCasesModel) list.get(0);
        CleanLegalCasesModel expected = new CleanLegalCasesModel();
        expected.setCode("code1");
        expected.setEventDate(Date.valueOf("1970-01-01"));
        expected.setName_do("do1");
        expected.setCounterpartyName("«company1»");
        expected.setCounterpartyInn("001");
        expected.setStatus("ACTIVE");
        expected.setCaseNumber("101");
        expected.setCategory("Прочее");
        expected.setStatus("Завершено");
        expected.setOutcome("Иск не удовлетворен");
        expected.setClaimDate(Date.valueOf("1970-01-21"));
        expected.setOutcomeDate(Date.valueOf("1970-01-22"));
        expected.setClaimAmount(201.0);
        expected.setOutcomeAmount(401.0);
        expected.setClaimCharge("о взыскании 201 рубля");
        expected.setDictum("В удовлетворении исковых требований отказать.");

        assertEquals(expected, actual);
    }
}
