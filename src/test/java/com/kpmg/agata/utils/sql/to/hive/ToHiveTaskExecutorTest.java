package com.kpmg.agata.utils.sql.to.hive;

import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.parser.ConfigType;
import com.kpmg.agata.test.utils.TestUtils;
import com.kpmg.agata.utils.sql.ReplaceModel;
import java.io.IOException;
import java.util.List;
import org.apache.spark.sql.SparkSession;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static java.util.Arrays.asList;

import static com.kpmg.agata.utils.Utils.restoreLineSeparators;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ToHiveTaskExecutorTest {

    private static ToHiveTaskExecutor toHiveTaskExecutor;

    @BeforeClass
    public static void beforeClass() throws IOException {
        TestUtils.loadTestConfig();
        List<ModelTypeConfiguration> rawFilesConfig = new ConfigParser()
                .getRawConfigAgataXls("./src/test/resources/raw/test_ConfigAgata.xlsx")
                .withDate("01-01-1970");
        toHiveTaskExecutor = new ToHiveTaskExecutor(null, rawFilesConfig);
    }
    @Test
    public void getSqlMdUnionDictTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.COUNTERPARTY_DICTIONARY);
        assertEquals("SELECT 'aero' AS name_do_table, * FROM agataTest.counterparty_dictionary_aero\n" +
                        " UNION \n" +
                        "SELECT 'bm' AS name_do_table, * FROM agataTest.counterparty_dictionary_bm\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.counterparty_dictionary_kp\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.counterparty_dictionary_mb\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.counterparty_dictionary_rp\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.counterparty_dictionary_sm",
                actual);
    }


    @Test
    public void getSqlUnionCreditCorrectionTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.CREDIT_CORRECTION);
        assertEquals(
                "SELECT 'aero' AS name_do_table, * FROM agataTest.credit_correction_aero\n" +
                        " UNION \n" +
                        "SELECT 'bm' AS name_do_table, * FROM agataTest.credit_correction_bm\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.credit_correction_rp\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.credit_correction_kp\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.credit_correction_mb\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.credit_correction_sm",
                actual);
    }


    @Test
    public void getSqlUnionCreditLimitTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.CREDIT_LIMIT);
        assertEquals("SELECT 'aero' AS name_do_table, * FROM agataTest.credit_limit_aero\n" +
                        " UNION \n" +
                        "SELECT 'bm' AS name_do_table, * FROM agataTest.credit_limit_bm\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.credit_limit_kp\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.credit_limit_mb\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.credit_limit_rp\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.credit_limit_sm\n" +
                        " UNION \n" +
                        "SELECT 'sort_do' AS name_do_table, * FROM agataTest.credit_limit_sort_do",
                actual);
    }


    @Test
    public void getSqlUnionRsbuLimitTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.EXPRESS_ANALYSIS_RSBU_LIMIT);
        assertEquals(
                "SELECT 'bm' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_bm\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_mb\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_rp\n" +
                        " UNION \n" +
                        "SELECT 'sort_do' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_sort_do\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_kp\n" +
                        " UNION \n" +
                        "SELECT 'aero' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_aero\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_sm",
                actual);
    }

    @Test
    public void getSqlUnionMsfoLimitTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.EXPRESS_ANALYSIS_MSFO_LIMIT);
        assertEquals(
                "SELECT 'bm' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_bm\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_mb\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_rp\n" +
                        " UNION \n" +
                        "SELECT 'sort_do' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_sort_do\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_kp\n" +
                        " UNION \n" +
                        "SELECT 'aero' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_aero\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_sm",
                actual);
    }


    @Test
    public void getSqlUnionRsbuReportTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.EXPRESS_ANALYSIS_RSBU_REPORT);
        assertEquals(
                "SELECT 'bm' AS name_do_table, * FROM agataTest.express_analysis_rsbu_report_bm\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.express_analysis_rsbu_report_mb\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.express_analysis_rsbu_report_rp\n" +
                        " UNION \n" +
                        "SELECT 'sort_do' AS name_do_table, * FROM agataTest.express_analysis_rsbu_report_sort_do\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.express_analysis_rsbu_report_kp\n" +
                        " UNION \n" +
                        "SELECT 'aero' AS name_do_table, * FROM agataTest.express_analysis_rsbu_report_aero\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.express_analysis_rsbu_report_sm",
                actual);
    }


    @Test
    public void getSqlUnionLegalCasesTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.LEGAL_CASES);
        assertEquals("SELECT 'aero' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'bm' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do",
                actual);
    }


    @Test
    public void getSqlUnionPaymentFromCustomerTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.PAYMENT_FROM_CUSTOMER);
        assertEquals("SELECT 'aero' AS name_do_table, * FROM agataTest.payment_from_customer_aero\n" +
                        " UNION \n" +
                        "SELECT 'bm' AS name_do_table, * FROM agataTest.payment_from_customer_bm\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.payment_from_customer_kp\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.payment_from_customer_mb\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.payment_from_customer_rp\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.payment_from_customer_sm",
                actual);
    }


    @Test
    public void getSqlUnionPdzMonthlyTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.MONTHLY_STATUS_PDZ);
        assertEquals("SELECT 'aero' AS name_do_table, * FROM agataTest.monthly_status_pdz_aero\n" +
                        " UNION \n" +
                        "SELECT 'bm' AS name_do_table, * FROM agataTest.monthly_status_pdz_bm\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.monthly_status_pdz_mb\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.monthly_status_pdz_sm\n" +
                        " UNION \n" +
                        "SELECT 'sort_do' AS name_do_table, * FROM agataTest.monthly_status_pdz_sort_do\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.monthly_status_pdz_rp\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.monthly_status_pdz_kp",
                actual);
    }


    @Test
    public void getSqlUnionPdzWeeklyTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.WEEKLY_STATUS_PDZ);
        assertEquals(
                "SELECT 'aero' AS name_do_table, * FROM agataTest.weekly_status_pdz_aero\n" +
                        " UNION \n" +
                        "SELECT 'bm' AS name_do_table, * FROM agataTest.weekly_status_pdz_bm\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.weekly_status_pdz_sm\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.weekly_status_pdz_kp\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.weekly_status_pdz_rp\n" +
                        " UNION \n" +
                        "SELECT 'sort_do' AS name_do_table, * FROM agataTest.weekly_status_pdz_sort_do",
                actual);
    }


    @Test
    public void getSqlUnionRealizationServicesTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.REALIZATION_SERVICES);
        assertEquals(
                "SELECT 'aero' AS name_do_table, * FROM agataTest.realization_services_aero\n" +
                        " UNION \n" +
                        "SELECT 'bm' AS name_do_table, * FROM agataTest.realization_services_bm\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.realization_services_kp\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.realization_services_mb\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.realization_services_rp\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.realization_services_sm",
                actual);
    }

    @Test
    public void cleanSqlAfterReplacePlaceholderTest() throws Exception {
        SqlToHiveTaskModel flatRsbuLimitModel = new SqlToHiveTaskModel(
                "sql/test_express_analysis.sql",
                "some_table",
                asList(new ReplaceModel("replacer_union_rsbu_limit", ConfigType.EXPRESS_ANALYSIS_RSBU_LIMIT),
                        new ReplaceModel("replacer_union_msfo_limit", ConfigType.EXPRESS_ANALYSIS_MSFO_LIMIT))
        );

        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "loadSql",
                flatRsbuLimitModel);
        actual = restoreLineSeparators(actual);

        assertEquals("start_sql...\n" +
                        "SELECT 'bm' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_bm\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_mb\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_rp\n" +
                        " UNION \n" +
                        "SELECT 'sort_do' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_sort_do\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_kp\n" +
                        " UNION \n" +
                        "SELECT 'aero' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_aero\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.express_analysis_rsbu_limit_sm\n" +
                        "\t\tunion\n" +
                        "some sql...\n" +
                        "SELECT 'bm' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_bm\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_mb\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_rp\n" +
                        " UNION \n" +
                        "SELECT 'sort_do' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_sort_do\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_kp\n" +
                        " UNION \n" +
                        "SELECT 'aero' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_aero\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do_table, * FROM agataTest.express_analysis_msfo_limit_sm\n" +
                        "end_sql;\n",
                actual);
    }

    @Test
    public void getSqlUnionGenprocTest() throws Exception {
        String actual = Whitebox.invokeMethod(toHiveTaskExecutor,
                "getSqlUnionForSource",
                ConfigType.GENPROC);
        assertEquals("SELECT 'aero' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'bm' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'kp' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'mb' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'rp' AS name_do\n" +
                        " UNION \n" +
                        "SELECT 'sm' AS name_do",
                actual);
    }
}
