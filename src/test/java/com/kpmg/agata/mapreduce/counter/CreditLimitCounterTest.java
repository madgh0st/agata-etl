package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanExpressAnalysisModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.sql.Date;
import java.util.Map;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_EA_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_HAS_EFF_LIMIT;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_LIMIT_IN_RUB;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_LIMIT_UTIL;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_OVERDUE_LIMIT_UTIL;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_PAY_DEFER_AVG_IN_90_DAYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CreditLimitCounterTest {

    private static double significantDebt;
    private CreditLimitCounter counter;

    @BeforeClass
    public static void beforeClass() throws Exception {
        significantDebt = Whitebox.getField(OverdueDebtHandler.class, "SIGNIFICANT_OVERDUE_DEBT")
                                  .getDouble(null);
    }

    @Before
    public void setUp() {
        counter = new CreditLimitCounter();
    }

    @Test
    public void intervalTest() {
        assertInterval("1", "1970-01-05", "1970-01-01", "1970-01-10");
        assertInterval("1", "1970-01-02", "1970-01-01", "1970-01-03");
        // date == approval within, because interval is [approval; expiration)
        assertInterval("1", "1970-01-01", "1970-01-01", "1970-01-02");

        // date after interval
        assertInterval("0", "1970-01-10", "1970-01-01", "1970-01-05");
        // date == expiration, but interval is [approval; expiration)
        assertInterval("0", "1970-01-02", "1970-01-01", "1970-01-02");
        // date before interval
        assertInterval("0", "1970-01-01", "1970-01-02", "1970-01-03");
        // approval after expiration - invalid data - just skip
        assertInterval("0", "1970-01-01", "1970-01-02", "1970-01-01");
    }

    private void assertInterval(String expected, String date, String approval, String expiration) {
        counter = new CreditLimitCounter();
        CleanCreditLimitModel cl = new CleanCreditLimitModel();
        cl.setEventDate(Date.valueOf(date));
        cl.setApprovalDate(Date.valueOf(approval));
        cl.setExpirationDate(Date.valueOf(expiration));
        counter.process(cl);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        Map<String, String> reports = counter.process(pdz);

        assertEquals(2, reports.size());
        assertEquals(expected, reports.get(CL_HAS_EFF_LIMIT));
    }

    @Test
    public void utilizationTest() {
        CleanCreditLimitModel cl = new CleanCreditLimitModel();
        cl.setEventDate(Date.valueOf("1970-01-01"));
        cl.setLimitInRub(2.0 * significantDebt);
        counter.process(cl);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-02"));
        pdz.setTotalDebtWithoutReserve(5.0 * significantDebt);
        pdz.setOverdueDebt(4.0 * significantDebt);
        Map<String, String> reports = counter.process(pdz);

        assertEquals("2.5", reports.get(CL_LIMIT_UTIL));
        assertEquals("2.0", reports.get(CL_OVERDUE_LIMIT_UTIL));
    }

    @Test
    public void twoCreditLimitsUtilizationTest() {
        CleanCreditLimitModel cl1 = new CleanCreditLimitModel();
        cl1.setEventDate(Date.valueOf("1970-01-01"));
        cl1.setLimitInRub(2.0 * significantDebt);
        counter.process(cl1);

        CleanCreditLimitModel cl2 = new CleanCreditLimitModel();
        cl2.setEventDate(Date.valueOf("1970-01-01"));
        cl2.setLimitInRub(1.0 * significantDebt);
        counter.process(cl2);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-02"));
        pdz.setTotalDebtWithoutReserve(5.0 * significantDebt);
        pdz.setOverdueDebt(4.0 * significantDebt);
        Map<String, String> reports = counter.process(pdz);

        assertEquals("5.0", reports.get(CL_LIMIT_UTIL));
        assertEquals("4.0", reports.get(CL_OVERDUE_LIMIT_UTIL));
    }

    @Test
    public void noLimitInRubUtilizationTest() {
        CleanCreditLimitModel cl = new CleanCreditLimitModel();
        cl.setEventDate(Date.valueOf("1970-01-01"));
        cl.setLimitInRub(null);
        counter.process(cl);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-02"));
        pdz.setTotalDebtWithoutReserve(5.0);
        pdz.setOverdueDebt(4.0);
        Map<String, String> reports = counter.process(pdz);

        assertNull(reports.get(CL_LIMIT_UTIL));
        assertNull(reports.get(CL_OVERDUE_LIMIT_UTIL));
    }

    @Test
    public void noTotalDebtUtilizationTest() {
        CleanCreditLimitModel cl = new CleanCreditLimitModel();
        cl.setEventDate(Date.valueOf("1970-01-01"));
        cl.setLimitInRub(2.0 * significantDebt);
        counter.process(cl);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-02"));
        pdz.setTotalDebtWithoutReserve(null);
        pdz.setOverdueDebt(4.0 * significantDebt);
        Map<String, String> reports = counter.process(pdz);

        assertNull(reports.get(CL_LIMIT_UTIL));
        assertEquals("2.0", reports.get(CL_OVERDUE_LIMIT_UTIL));
    }

    @Test
    public void noOverdueDebtUtilizationTest() {
        CleanCreditLimitModel cl = new CleanCreditLimitModel();
        cl.setEventDate(Date.valueOf("1970-01-01"));
        cl.setLimitInRub(2.0);
        counter.process(cl);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-02"));
        pdz.setTotalDebtWithoutReserve(5.0);
        pdz.setOverdueDebt(null);
        Map<String, String> reports = counter.process(pdz);

        assertEquals("2.5", reports.get(CL_LIMIT_UTIL));
        assertNull(reports.get(CL_OVERDUE_LIMIT_UTIL));
    }

    @Test
    public void noReportsForCreditLimit() {
        Map<String, String> reports = counter.process(new CleanCreditLimitModel());
        assertEquals(0, reports.size());
    }

    @Test
    public void noReportsWithoutCreditLimit() {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-01"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals(2, reports.size());
        assertEquals("0", reports.get(CL_HAS_EFF_LIMIT));
        assertNull(reports.get(CL_LIMIT_IN_RUB));
    }

    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        return counter.process(pdz);
    }

    private void processCreditLimit(String date, Double limitInRub) {
        CleanCreditLimitModel cl = new CleanCreditLimitModel();
        cl.setEventDate(Date.valueOf(date));
        cl.setLimitInRub(limitInRub);
        counter.process(cl);
    }

    private void processCreditLimitWithPayDeferment(String date, Integer payDeferment) {
        CleanCreditLimitModel cl = new CleanCreditLimitModel();
        cl.setEventDate(Date.valueOf(date));
        cl.setLimitInRub(1.0);
        cl.setPaymentDefermentDays(payDeferment);
        counter.process(cl);
    }

    private void processExpressAnalysis(String date, Double creditLimit, Date limitRsbu, String year) {
        CleanExpressAnalysisModel ea = new CleanExpressAnalysisModel();
        ea.setEventDate(Date.valueOf(date));
        ea.setCredit_limit(creditLimit == null ? null : Double.toString(creditLimit));
        ea.setLimit_estimation_rsbu(limitRsbu);
        ea.setYear(year);
        counter.process(ea);
    }

    private void processExpressAnalysis(String date, Double creditLimit, String year) {
        processExpressAnalysis(date, creditLimit, null, year);
    }

    private void processExpressAnalysis(String date, Double creditLimit, Date limitRsbu) {
        processExpressAnalysis(date, creditLimit, limitRsbu, null);
    }

    private void processExpressAnalysis(String date, Double creditLimit) {
        processExpressAnalysis(date, creditLimit, null, null);
    }

    @Test
    public void noExpressAnalysis() {
        processCreditLimit("1970-01-01", 1.0);
        assertNull(processPdz("1970-01-02").get(CL_EA_RATIO));
    }

    @Test
    public void expressAnalysisAppears() {
        processCreditLimit("1970-01-01", 10.0);
        processExpressAnalysis("1970-01-02", 20.0);
        assertEquals("2.0", processPdz("1970-01-03").get(CL_EA_RATIO));
    }

    @Test
    public void twoExpressAnalysisAppears() {
        processCreditLimit("1970-01-01", 10.0);
        processExpressAnalysis("1970-01-02", 20.0);
        processExpressAnalysis("1970-01-02", 30.0);
        assertEquals("3.0", processPdz("1970-01-03").get(CL_EA_RATIO));
    }

    @Test
    public void secondExpressAnalysisHasNullCreditLimit() {
        processCreditLimit("1970-01-01", 10.0);
        processExpressAnalysis("1970-01-02", 20.0);
        processExpressAnalysis("1970-01-02", null);
        assertNull(processPdz("1970-01-03").get(CL_EA_RATIO));
    }

    @Test
    public void preferTheNewestExpressAnalysisByLimitRsbu() {
        processCreditLimit("1970-01-01", 10.0);
        processExpressAnalysis("1970-01-02", 20.0, Date.valueOf("1971-01-02"));
        processExpressAnalysis("1970-01-02", 30.0, Date.valueOf("1971-01-01"));
        assertEquals("2.0", processPdz("1970-01-03").get(CL_EA_RATIO));
    }

    @Test
    public void preferTheNewestExpressAnalysisByYear() {
        processCreditLimit("1970-01-01", 10.0);
        processExpressAnalysis("1970-01-02", 20.0, "1970");
        processExpressAnalysis("1970-01-02", 30.0, "1969");
        assertEquals("2.0", processPdz("1970-01-03").get(CL_EA_RATIO));
    }

    @Test
    public void paymentDefermentAverageIn90Days() {
        processCreditLimitWithPayDeferment("1970-01-01", 1);
        processCreditLimitWithPayDeferment("1970-01-02", 2);
        assertEquals("1.5", processPdz("1970-01-03").get(CL_PAY_DEFER_AVG_IN_90_DAYS));
    }

    @Test
    public void noCreditLimitAppearsForPayDefermentAvg() {
        assertNull(processPdz("1970-01-03").get(CL_PAY_DEFER_AVG_IN_90_DAYS));
    }

    @Test
    public void severalCreditLimitsOneDayForPayDefermentAvg() {
        processCreditLimitWithPayDeferment("1970-01-01", 1);
        processCreditLimitWithPayDeferment("1970-01-01", 2);
        assertEquals("1.5", processPdz("1970-01-01").get(CL_PAY_DEFER_AVG_IN_90_DAYS));
    }

    @Test
    public void boundariesForOneDayForPayDefermentAvg() {
        processCreditLimitWithPayDeferment("1970-01-01", 1);    // outside 90 days range
        processCreditLimitWithPayDeferment("1970-01-02", 2);    // inside 90 days range
        assertEquals("2.0", processPdz("1970-04-01").get(CL_PAY_DEFER_AVG_IN_90_DAYS));
    }

    @Test
    public void roundingOfOneDayForPayDefermentAvg() {
        processCreditLimitWithPayDeferment("1970-01-01", 1);
        processCreditLimitWithPayDeferment("1970-01-02", 1);
        processCreditLimitWithPayDeferment("1970-01-03", 2);
        assertEquals("1.333", processPdz("1970-01-04").get(CL_PAY_DEFER_AVG_IN_90_DAYS));
    }
}
