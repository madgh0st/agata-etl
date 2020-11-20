package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanPaymentFromCustomerModel;
import com.kpmg.agata.models.clean.CleanRealizationServicesModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_CUR_TO_LAST_MONTH_REAL_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_DIFF_90;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_PAY_SUM_30;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_PAY_SUM_60_30;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_PAY_SUM_90;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_PAY_SUM_90_60;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REAL_SUM_30;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REAL_SUM_60_30;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REAL_SUM_90;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REAL_SUM_90_60;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REL_DIFF_90;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PaymentsRealizationsCounterTest {
    private PaymentsRealizationsCounter counter = new PaymentsRealizationsCounter();

    @Before
    public void setUp() {
        counter = new PaymentsRealizationsCounter();
    }

    @Test
    public void paymentsTest() {
        // out of 90 days
        processAmountOnePayment("1969-12-31");
        // within 90 days
        processAmountOnePayment("1970-01-01");
        // double Jan 10 process check - means amount == 2.0
        processAmountOnePayment("1970-01-10");
        processAmountOnePayment("1970-01-10");
        processAmountOnePayment("1970-01-30");
        //within 60 days
        processAmountOnePayment("1970-01-31");
        processAmountOnePayment("1970-02-10");
        processAmountOnePayment("1970-03-01");
        //within 30 days
        processAmountOnePayment("1970-03-02");
        processAmountOnePayment("1970-03-31");

        Map<String, String> reports = processPdz("1970-03-31");

        assertEquals("9.0", reports.get(PR_PAY_SUM_90));
        assertEquals("4.0", reports.get(PR_PAY_SUM_90_60));
        assertEquals("3.0", reports.get(PR_PAY_SUM_60_30));
        assertEquals("2.0", reports.get(PR_PAY_SUM_30));

        assertEquals("-9.0", reports.get(PR_DIFF_90));
        assertNull(reports.get(PR_REL_DIFF_90));
    }

    @Test
    public void noPaymentsTest() {
        Map<String, String> reports = processPdz("1970-01-01");

        assertEquals("0.0", reports.get(PR_PAY_SUM_90));
        assertEquals("0.0", reports.get(PR_PAY_SUM_90_60));
        assertEquals("0.0", reports.get(PR_PAY_SUM_60_30));
        assertEquals("0.0", reports.get(PR_PAY_SUM_30));

        assertEquals("0.0", reports.get(PR_DIFF_90));
        assertNull(reports.get(PR_REL_DIFF_90));
    }

    @Test
    public void realizationsTest() {
        // out of 90 days
        processAmountOneRealization("1969-12-31");
        // within 90 days
        processAmountOneRealization("1970-01-01");
        // double Jan 10 process check - means amount == 2.0
        processAmountOneRealization("1970-01-10");
        processAmountOneRealization("1970-01-10");
        processAmountOneRealization("1970-01-30");
        //within 60 days
        processAmountOneRealization("1970-01-31");
        processAmountOneRealization("1970-02-10");
        processAmountOneRealization("1970-03-01");
        //within 30 days
        processAmountOneRealization("1970-03-02");
        processAmountOneRealization("1970-03-31");

        Map<String, String> reports = processPdz("1970-03-31");

        assertEquals("9.0", reports.get(PR_REAL_SUM_90));
        assertEquals("4.0", reports.get(PR_REAL_SUM_90_60));
        assertEquals("3.0", reports.get(PR_REAL_SUM_60_30));
        assertEquals("2.0", reports.get(PR_REAL_SUM_30));

        assertEquals("9.0", reports.get(PR_DIFF_90));
        assertNull(reports.get(PR_REL_DIFF_90));
    }

    @Test
    public void noRealizationsTest() {
        Map<String, String> reports = processPdz("1970-01-01");

        assertEquals("0.0", reports.get(PR_REAL_SUM_90));
        assertEquals("0.0", reports.get(PR_REAL_SUM_90_60));
        assertEquals("0.0", reports.get(PR_REAL_SUM_60_30));
        assertEquals("0.0", reports.get(PR_REAL_SUM_30));

        assertEquals("0.0", reports.get(PR_DIFF_90));
        assertNull(reports.get(PR_REL_DIFF_90));
    }

    @Test
    public void paymentsRealizationsWithCreditLimitTest() {
        processCreditLimit("1970-01-05", 2.0);

        processAmountPayment("1970-01-10", 1.0);
        processAmountPayment("1970-01-11", 2.0);

        processCreditLimit("1970-01-04", null);
        // this is actual credit limit, drop others credit limits
        processCreditLimit("1970-03-05", 3.0);

        processAmountRealization("1970-03-12", 1.0);
        processAmountRealization("1970-03-13", 2.0);
        processAmountRealization("1970-03-14", 3.0);

        Map<String, String> reports = processPdz("1970-03-31");

        assertEquals("3.0", reports.get(PR_DIFF_90));
        assertEquals("1.0", reports.get(PR_REL_DIFF_90));
    }

    private void processAmountPayment(String date, Double amount) {
        CleanPaymentFromCustomerModel pay = new CleanPaymentFromCustomerModel();
        pay.setEventDate(Date.valueOf(date));
        pay.setAmount(amount);
        Map<String, String> reports = counter.process(pay);
        assertEquals(0, reports.size());
    }

    private void processAmountOnePayment(String date) {
        processAmountPayment(date, 1.0);
    }

    private void processAmountRealization(String date, Double amount) {
        CleanRealizationServicesModel pay = new CleanRealizationServicesModel();
        pay.setEventDate(Date.valueOf(date));
        pay.setAmount(amount);
        Map<String, String> reports = counter.process(pay);
        assertEquals(0, reports.size());
    }

    private void processAmountOneRealization(String date) {
        processAmountRealization(date, 1.0);
    }

    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        return counter.process(pdz);
    }

    private void processCreditLimit(String date, Double amount) {
        CleanCreditLimitModel pay = new CleanCreditLimitModel();
        pay.setEventDate(Date.valueOf(date));
        pay.setLimitInRub(amount);
        Map<String, String> reports = counter.process(pay);

        assertEquals(0, reports.size());
    }

    @Test
    public void currentToLastMonthRealizationRatio() {
        processAmountRealization("1970-01-15", 1.1);
        processAmountRealization("1970-02-15", 2.2);
        assertEquals("2.0", processPdz("1970-03-01").get(PR_CUR_TO_LAST_MONTH_REAL_RATIO));
    }

    @Test
    public void whenNoRealizationsThenCurToLastMonthRealRatioNull() {
        assertNull(processPdz("1970-03-01").get(PR_CUR_TO_LAST_MONTH_REAL_RATIO));
    }

    @Test
    public void whenNoLastMonthRealizationsThenCurToLastMonthRealRatioNull() {
        processAmountRealization("1970-02-15", 2.2);
        assertNull(processPdz("1970-03-01").get(PR_CUR_TO_LAST_MONTH_REAL_RATIO));
    }

    @Test
    public void whenNoLastMonthRealizationsIsZeroThenCurToLastMonthRealRatioNull() {
        processAmountRealization("1970-01-15", 0.0);
        processAmountRealization("1970-02-15", 2.2);
        assertNull(processPdz("1970-03-01").get(PR_CUR_TO_LAST_MONTH_REAL_RATIO));
    }

    @Test
    public void severalRealizationsForCurrentToLastMonthRealizationRatio() {
        processAmountRealization("1970-01-15", 1.1);
        processAmountRealization("1970-01-16", 1.1);
        processAmountRealization("1970-01-17", 1.1);

        processAmountRealization("1970-02-15", 4.4);
        processAmountRealization("1970-02-15", 5.5);
        assertEquals("3.0", processPdz("1970-03-01").get(PR_CUR_TO_LAST_MONTH_REAL_RATIO));
    }

    @Test
    public void boundariesCheckForCurrentToLastMonthRealizationRatio() {
        // current month: [03-01;01-31], last month: [30-01;01-01]
        processAmountRealization("1969-12-31", 1.1);    // outside of

        processAmountRealization("1970-01-01", 2.2);    // inside the last month, near to leave
        processAmountRealization("1970-01-30", 3.3);    // inside the last month, near to the current month

        processAmountRealization("1970-01-31", 4.4);    // inside the current month, near to the last month
        processAmountRealization("1970-03-01", 6.6);    // inside the current month, current day
        assertEquals("2.0", processPdz("1970-03-01").get(PR_CUR_TO_LAST_MONTH_REAL_RATIO));
    }

    @Test
    public void roundingOfCurToLastMonthRealRatioNull() {
        processAmountRealization("1970-01-15", 3.3);
        processAmountRealization("1970-02-15", 1.1);
        assertEquals("0.333", processPdz("1970-03-01").get(PR_CUR_TO_LAST_MONTH_REAL_RATIO));
    }
}
