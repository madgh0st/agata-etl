package com.kpmg.agata.processors.clean;

import com.kpmg.agata.models.CounterpartyContractModel;
import com.kpmg.agata.test.utils.TestSqlLoader;
import com.kpmg.agata.test.utils.spark.SparkTestUtils;
import com.kpmg.agata.utils.sql.UdfRegistrar;
import java.util.Collections;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.kpmg.agata.test.utils.spark.SparkTestUtils.assertEquals;
import static com.kpmg.agata.test.utils.spark.SparkTestUtils.getSparkSession;

public class CounterpartyContractSqlTest {

    private static final String RES_DIR = "sql/clean/counterparty_contract/";
    private static final String SRC_DIR = "src/main/resources/sql/clean/counterparty_contract/";
    private static SparkSession sparkSession;

    @BeforeClass
    public static void beforeClass() {
        sparkSession = getSparkSession();
        UdfRegistrar.register(sparkSession);
        SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union_sort_do.csv")
                      .createOrReplaceTempView("counterparty_contract_sort_do");
        SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union_sm.csv")
                      .createOrReplaceTempView("counterparty_contract_sm");
        SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv")
                      .createOrReplaceTempView("union_counterparty_contract");
        SparkTestUtils.csv(sparkSession, RES_DIR + "2_after_collisions_report.csv")
                      .createOrReplaceTempView("collisions_report_counterparty_contract");
        SparkTestUtils.csv(sparkSession, RES_DIR + "3_after_clean_table.csv")
                      .createOrReplaceTempView("clean_counterparty_contract");
        SparkTestUtils.csv(sparkSession, "sql/clean/md_contractor_dict.csv")
                      .createOrReplaceTempView("md_contractor_dict");
    }

    @Test
    public void beforeUnionTest() {
        Dataset<Row> expectedContractSortDo = SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union_sort_do.csv");
        Dataset<Row> expectedContractSm = SparkTestUtils.csv(sparkSession, RES_DIR + "0_before_union_sm.csv");

        CounterpartyContractModel contractSortDo = new CounterpartyContractModel();
        contractSortDo.setOwnerCode("ownerCode1");
        contractSortDo.setErpCode("erpCode1");
        contractSortDo.setNumber("number1");
        contractSortDo.setName("company1");
        contractSortDo.setDate("30.01.1970");
        contractSortDo.setInn("001");
        contractSortDo.setValidityPeriod("validityPeriod1");
        contractSortDo.setContractKind("contractKind1");
        contractSortDo.setSettlementCurrency("settlementCurrency1");
        contractSortDo.setSettlementManagement("settlementManagement1");
        contractSortDo.setMarkRemove("markRemove1");
        contractSortDo.setPaymentTerm("paymentTerm1");
        contractSortDo.setFileName("filename1");
        contractSortDo.setSheetName("sheet1");
        contractSortDo.setLoadDate("1970-01-03");
        contractSortDo.setModificationDate("1970-01-04");

        CounterpartyContractModel contractSm = new CounterpartyContractModel();
        contractSm.setOwnerCode("ownerCode3");
        contractSm.setErpCode("erpCode3");
        contractSm.setNumber("number3");
        contractSm.setName("company3");
        contractSm.setDate("30.01.1970");
        contractSm.setInn("003");
        contractSm.setValidityPeriod("validityPeriod3");
        contractSm.setContractKind("contractKind3");
        contractSm.setSettlementCurrency("settlementCurrency3");
        contractSm.setSettlementManagement("settlementManagement3");
        contractSm.setMarkRemove("markRemove3");
        contractSm.setPaymentTerm("paymentTerm3");
        contractSm.setFileName("filename3");
        contractSm.setSheetName("sheet3");
        contractSm.setLoadDate("1970-01-03");
        contractSm.setModificationDate("1970-01-04");


        Dataset<Row> actualContractSortDo =
                sparkSession
                        .createDataFrame(Collections.singletonList(contractSortDo), CounterpartyContractModel.class);
        Dataset<Row> actualContractSm =
                sparkSession.createDataFrame(Collections.singletonList(contractSm), CounterpartyContractModel.class);

        assertEquals(expectedContractSortDo, actualContractSortDo);
        assertEquals(expectedContractSm, actualContractSm);
    }

    @Test
    public void unionSqlTest() {
        Dataset<Row> expected = SparkTestUtils.csv(sparkSession, RES_DIR + "1_after_union.csv");

        String unionReplacer = "SELECT 'sm' AS name_do_table, * FROM {dbSchema}.counterparty_contract_sm"
                + " UNION " +
                "SELECT 'sort_do' AS name_do_table, * FROM {dbSchema}.counterparty_contract_sort_do";
        String sql = new TestSqlLoader().fromPath(SRC_DIR + "1_union_counterparty_contract.sql")
                                        .inject("union_replacer", unionReplacer)
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
        String sql = new TestSqlLoader().fromPath(SRC_DIR + "3_clean_counterparty_contract.sql")
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
