package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanSparkInterfaxSummaryModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptyMap;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_HAS_EFF_LIMIT;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_LIMIT_IN_RUB;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_CAUTION;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_CL_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_DISHONEST_SUPPLIERS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_FIN_RISK;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_LC_CLAIMS_2Y;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_LC_COUNT_2Y;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_LC_DECISIONS_2Y;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_NEWS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_PAYMENT_DISC;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_PLEDGES;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_RISK_FACTORS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SP_IFAX_STATUS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SparkInterfaxSummaryCounterTest {

    private SparkInterfaxSummaryCounter counter;

    @Before
    public void setUp() {
        counter = new SparkInterfaxSummaryCounter();
    }

    @Test
    public void eventNeverAppearsTest() {
        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals(0, reports.size());
    }

    @Test
    public void eventAppearsTest() {
        CleanSparkInterfaxSummaryModel spIfax = new CleanSparkInterfaxSummaryModel();
        spIfax.setStatus("status");
        spIfax.setCautionIndex(101);
        spIfax.setFinancialRiskIndex(201);
        spIfax.setPaymentDisciplineIndex(301);
        spIfax.setRiskFactors("LOW");
        spIfax.setPledges(false);
        spIfax.setLegalCasesCountTwoYears(401);
        spIfax.setLegalCasesClaimsSumTwoYears(501.1);
        spIfax.setLegalCasesDecisionsSumTwoYears(601.1);
        spIfax.setNews("news");
        spIfax.setIsDishonestSupplier(false);
        spIfax.setNegativeRegistries("Реестр дилеров");
        counter.process(spIfax);

        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals(12, reports.size());
        assertEquals("status", reports.get(SP_IFAX_STATUS));
        assertEquals("101", reports.get(SP_IFAX_CAUTION));
        assertEquals("201", reports.get(SP_IFAX_FIN_RISK));
        assertEquals("301", reports.get(SP_IFAX_PAYMENT_DISC));
        assertEquals("LOW", reports.get(SP_IFAX_RISK_FACTORS));
        assertEquals("false", reports.get(SP_IFAX_PLEDGES));
        assertEquals("401", reports.get(SP_IFAX_LC_COUNT_2Y));
        assertEquals("501.1", reports.get(SP_IFAX_LC_CLAIMS_2Y));
        assertEquals("601.1", reports.get(SP_IFAX_LC_DECISIONS_2Y));
        assertEquals("news", reports.get(SP_IFAX_NEWS));
        assertEquals("false", reports.get(SP_IFAX_DISHONEST_SUPPLIERS));
        assertEquals("1", reports.get("sp_ifax_reg_диллеры"));
        assertNull(reports.get(SP_IFAX_CL_RATIO));
    }

    @Test
    public void emptyEventAppearsTest() {
        CleanSparkInterfaxSummaryModel spIfax = new CleanSparkInterfaxSummaryModel();
        counter.process(spIfax);

        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals(0, reports.size());
    }

    @Test
    public void takeLastEventDataTest() {
        CleanSparkInterfaxSummaryModel firstSpIfax = new CleanSparkInterfaxSummaryModel();
        firstSpIfax.setStatus("old-status");
        firstSpIfax.setCautionIndex(101);
        firstSpIfax.setFinancialRiskIndex(201);
        firstSpIfax.setPaymentDisciplineIndex(301);
        firstSpIfax.setRiskFactors("LOW");
        firstSpIfax.setPledges(false);
        firstSpIfax.setLegalCasesCountTwoYears(401);
        firstSpIfax.setLegalCasesClaimsSumTwoYears(501.1);
        firstSpIfax.setLegalCasesDecisionsSumTwoYears(601.1);
        firstSpIfax.setNews("old-news");
        firstSpIfax.setIsDishonestSupplier(false);
        firstSpIfax.setNegativeRegistries("Реестр дилеров");
        counter.process(firstSpIfax);

        CleanSparkInterfaxSummaryModel secondSpIfax = new CleanSparkInterfaxSummaryModel();
        secondSpIfax.setStatus("new-status");
        secondSpIfax.setCautionIndex(102);
        secondSpIfax.setFinancialRiskIndex(202);
        secondSpIfax.setPaymentDisciplineIndex(302);
        secondSpIfax.setRiskFactors("MEDIUM");
        secondSpIfax.setPledges(true);
        secondSpIfax.setLegalCasesCountTwoYears(402);
        secondSpIfax.setLegalCasesClaimsSumTwoYears(502.2);
        secondSpIfax.setLegalCasesDecisionsSumTwoYears(602.2);
        secondSpIfax.setNews("actual-news");
        secondSpIfax.setIsDishonestSupplier(true);
        secondSpIfax.setNegativeRegistries("Реестр брокеров");
        counter.process(secondSpIfax);

        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals(12, reports.size());
        assertEquals("new-status", reports.get(SP_IFAX_STATUS));
        assertEquals("102", reports.get(SP_IFAX_CAUTION));
        assertEquals("202", reports.get(SP_IFAX_FIN_RISK));
        assertEquals("302", reports.get(SP_IFAX_PAYMENT_DISC));
        assertEquals("MEDIUM", reports.get(SP_IFAX_RISK_FACTORS));
        assertEquals("true", reports.get(SP_IFAX_PLEDGES));
        assertEquals("402", reports.get(SP_IFAX_LC_COUNT_2Y));
        assertEquals("502.2", reports.get(SP_IFAX_LC_CLAIMS_2Y));
        assertEquals("602.2", reports.get(SP_IFAX_LC_DECISIONS_2Y));
        assertEquals("actual-news", reports.get(SP_IFAX_NEWS));
        assertEquals("true", reports.get(SP_IFAX_DISHONEST_SUPPLIERS));
        assertEquals("1", reports.get("sp_ifax_reg_брокеры"));
        assertNull(reports.get(SP_IFAX_CL_RATIO));
    }

    @Test
    public void storeLastEventDataForNextReports() {
        CleanSparkInterfaxSummaryModel spIfax = new CleanSparkInterfaxSummaryModel();
        spIfax.setStatus("status");
        spIfax.setCautionIndex(101);
        spIfax.setFinancialRiskIndex(201);
        spIfax.setPaymentDisciplineIndex(301);
        spIfax.setRiskFactors("LOW");
        spIfax.setPledges(false);
        spIfax.setLegalCasesCountTwoYears(401);
        spIfax.setLegalCasesClaimsSumTwoYears(501.1);
        spIfax.setLegalCasesDecisionsSumTwoYears(601.1);
        spIfax.setNews("news");
        spIfax.setIsDishonestSupplier(false);
        spIfax.setNegativeRegistries("Реестр дилеров");
        counter.process(spIfax);

        Map<String, String> firstReports = processPdz("1970-01-01");
        assertEquals(12, firstReports.size());
        assertEquals("status", firstReports.get(SP_IFAX_STATUS));
        assertEquals("101", firstReports.get(SP_IFAX_CAUTION));
        assertEquals("201", firstReports.get(SP_IFAX_FIN_RISK));
        assertEquals("301", firstReports.get(SP_IFAX_PAYMENT_DISC));
        assertEquals("LOW", firstReports.get(SP_IFAX_RISK_FACTORS));
        assertEquals("false", firstReports.get(SP_IFAX_PLEDGES));
        assertEquals("401", firstReports.get(SP_IFAX_LC_COUNT_2Y));
        assertEquals("501.1", firstReports.get(SP_IFAX_LC_CLAIMS_2Y));
        assertEquals("601.1", firstReports.get(SP_IFAX_LC_DECISIONS_2Y));
        assertEquals("news", firstReports.get(SP_IFAX_NEWS));
        assertEquals("false", firstReports.get(SP_IFAX_DISHONEST_SUPPLIERS));
        assertEquals("1", firstReports.get("sp_ifax_reg_диллеры"));
        assertNull(firstReports.get(SP_IFAX_CL_RATIO));

        Map<String, String> secondReports = processPdz("1970-01-02");
        assertEquals(12, secondReports.size());
        assertEquals("status", secondReports.get(SP_IFAX_STATUS));
        assertEquals("101", secondReports.get(SP_IFAX_CAUTION));
        assertEquals("201", secondReports.get(SP_IFAX_FIN_RISK));
        assertEquals("301", secondReports.get(SP_IFAX_PAYMENT_DISC));
        assertEquals("LOW", secondReports.get(SP_IFAX_RISK_FACTORS));
        assertEquals("false", secondReports.get(SP_IFAX_PLEDGES));
        assertEquals("401", secondReports.get(SP_IFAX_LC_COUNT_2Y));
        assertEquals("501.1", secondReports.get(SP_IFAX_LC_CLAIMS_2Y));
        assertEquals("601.1", secondReports.get(SP_IFAX_LC_DECISIONS_2Y));
        assertEquals("news", secondReports.get(SP_IFAX_NEWS));
        assertEquals("false", secondReports.get(SP_IFAX_DISHONEST_SUPPLIERS));
        assertEquals("1", secondReports.get("sp_ifax_reg_диллеры"));
        assertNull(secondReports.get(SP_IFAX_CL_RATIO));
    }

    @Test
    public void creditLimitSparkInterfaxTest() {
        processSparkInterfax(20.0);
        processCreditLimit(10.0);
        Map<String, String> reports = processPdz("1970-01-01");

        assertEquals("2.0", reports.get(SP_IFAX_CL_RATIO));
    }

    @Test
    public void takeLastCreditLimitSparkInterfaxTest() {
        processSparkInterfax(20.0);
        processSparkInterfax(40.0);
        processCreditLimit(10.0);
        Map<String, String> reports = processPdz("1970-01-01");

        assertEquals("4.0", reports.get(SP_IFAX_CL_RATIO));
    }

    @Test
    public void creditLimitSparkInterfaxWhenCreditLimitIsZeroTest() {
        processSparkInterfax(20.0);
        processCreditLimit(0.0);
        Map<String, String> reports = processPdz("1970-01-01");

        assertNull(reports.get(SP_IFAX_CL_RATIO));
    }

    @Test
    public void creditLimitSparkInterfaxNoCreditLimitTest() {
        processSparkInterfax(20.0);
        processCreditLimit(null);
        Map<String, String> reports = processPdz("1970-01-01");

        assertNull(reports.get(SP_IFAX_CL_RATIO));
    }

    @Test
    public void severalNegativeRegistriesTest() {
        CleanSparkInterfaxSummaryModel spIfax = new CleanSparkInterfaxSummaryModel();
        spIfax.setNegativeRegistries("\"Реестр дилеров (дата 1970-01-01)\"," +
                "\"Реестр брокеров\",\"Реестр реестродержателей\"");
        counter.process(spIfax);

        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals(3, reports.size());
        assertEquals("1", reports.get("sp_ifax_reg_диллеры"));
        assertEquals("1", reports.get("sp_ifax_reg_брокеры"));
        assertEquals("1", reports.get("sp_ifax_reg_реестродержатели"));
    }

    @Test
    public void unknownNegativeRegistryTest() {
        CleanSparkInterfaxSummaryModel spIfax = new CleanSparkInterfaxSummaryModel();
        spIfax.setNegativeRegistries("unknown-registry");
        counter.process(spIfax);

        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals(0, reports.size());
    }

    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        return counter.process(pdz);
    }

    private void processSparkInterfax(double creditLimit) {
        CleanSparkInterfaxSummaryModel spIfax = new CleanSparkInterfaxSummaryModel();
        spIfax.setSparkInterfaxCreditLimit(creditLimit);
        counter.process(spIfax);
    }

    private void processCreditLimit(Double creditLimit) {
        CleanCreditLimitModel clModel = new CleanCreditLimitModel();
        clModel.setLimitInRub(creditLimit);
        counter.process(clModel);
    }
}
