package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanExpressAnalysisModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.sql.Date;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_BUS_COOP_EXP_CATEGORY;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_BUS_COOP_EXP_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_BUS_PAY_DISCIPLINE_CATEGORY;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_BUS_PAY_DISCIPLINE_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CALC_CURR_ASSETS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CALC_CURR_LIABILITIES;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CALC_NET_ASSETS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CALC_NON_CURR_ASSETS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_CONTRACTOR_CLASS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_DAYS_SINCE_PUB;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_ASSETS_PROFIT_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_CAPITAL_TURNOVER_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_COVERAGE_COEF_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_HOT_LIQ_COEF_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_SELL_PROFIT_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_FIN_STABILITY_COEF_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_INDUSTRY;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_POINTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.EXP_SOLVENCY_GROUP;
import static org.junit.Assert.assertEquals;

public class ExpressAnalysisCounterTest {
    private ExpressAnalysisCounter counter;

    @Before
    public void setUp() {
        counter = new ExpressAnalysisCounter();
    }

    @Test
    public void noReportExpressAnalysisTest() {
        Map<String, String> reports = counter.process(new CleanExpressAnalysisModel());
        assertEquals(0, reports.size());
    }

    @Test
    public void noReportsWithoutExpressAnalysisTest() {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-01"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals(0, reports.size());
    }

    @Test
    public void reportTwoExpressAnalysisTest() {
        CleanExpressAnalysisModel ea1 = new CleanExpressAnalysisModel();
        ea1.setEventDate(Date.valueOf("1970-01-01"));
        ea1.setIndustry("value");
        counter.process(ea1);

        CleanExpressAnalysisModel ea2 = new CleanExpressAnalysisModel();
        ea2.setEventDate(Date.valueOf("1970-01-02"));
        ea2.setIndustry("another value");
        counter.process(ea2);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-03"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("another value", reports.get(EXP_INDUSTRY));
    }

    @Test
    public void reportTwoExpressAnalysisWithRsbuTest() {
        CleanExpressAnalysisModel ea1 = new CleanExpressAnalysisModel();
        ea1.setEventDate(Date.valueOf("1970-01-01"));
        ea1.setIndustry("value");
        ea1.setLimit_estimation_rsbu(Date.valueOf("1970-01-02"));
        counter.process(ea1);

        CleanExpressAnalysisModel ea2 = new CleanExpressAnalysisModel();
        ea2.setEventDate(Date.valueOf("1970-01-01"));
        ea2.setIndustry("another value");
        ea2.setLimit_estimation_rsbu(Date.valueOf("1970-01-01"));
        counter.process(ea2);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-03"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("value", reports.get(EXP_INDUSTRY));
    }

    @Test
    public void reportTwoExpressAnalysisWithNullRsbuAndYearTest() {
        CleanExpressAnalysisModel ea1 = new CleanExpressAnalysisModel();
        ea1.setEventDate(Date.valueOf("1970-01-01"));
        ea1.setIndustry("value");
        ea1.setLimit_estimation_rsbu(null);
        ea1.setYear("year_3");
        counter.process(ea1);

        CleanExpressAnalysisModel ea2 = new CleanExpressAnalysisModel();
        ea2.setEventDate(Date.valueOf("1970-01-01"));
        ea2.setIndustry("another value");
        ea2.setLimit_estimation_rsbu(Date.valueOf("1970-01-01"));
        ea2.setYear("year_1");
        counter.process(ea2);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-03"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("value", reports.get(EXP_INDUSTRY));
    }

    @Test
    public void reportTwoExpressAnalysisWithNullRsbuAndNullYearTest() {
        CleanExpressAnalysisModel ea1 = new CleanExpressAnalysisModel();
        ea1.setEventDate(Date.valueOf("1970-01-01"));
        ea1.setIndustry("value");
        ea1.setLimit_estimation_rsbu(Date.valueOf("1970-01-01"));
        ea1.setYear(null);
        counter.process(ea1);

        CleanExpressAnalysisModel ea2 = new CleanExpressAnalysisModel();
        ea2.setEventDate(Date.valueOf("1970-01-01"));
        ea2.setIndustry("another value");
        ea2.setLimit_estimation_rsbu(null);
        ea2.setYear("year_1");
        counter.process(ea2);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-03"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("another value", reports.get(EXP_INDUSTRY));
    }

    @Test
    public void putSimpleReportsTest() {
        CleanExpressAnalysisModel ea = new CleanExpressAnalysisModel();
        ea.setEventDate(Date.valueOf("1970-01-01"));

        ea.setIndustry("1");
        ea.setContractor_class("2");
        ea.setSolvency_group("3");
        ea.setPoints("4");

        ea.setFin_metr_coverage_coefficient_points("5");
        ea.setFin_metr_hot_liquidity_coefficient_points("6");
        ea.setFin_metr_capital_turnover_points("7");
        ea.setFin_metr_selling_profitability_points("8");
        ea.setFin_metr_assets_profitability_points("9");
        ea.setFin_metr_financial_stability_coefficient_points("10");

        ea.setBus_rep_cooperation_experience_category("11");
        ea.setBus_rep_cooperation_experience_points("12");
        ea.setBus_rep_payment_discipline_category("13");
        ea.setBus_rep_payment_discipline_points("14");

        ea.setCalculation_non_current_assets("15");
        ea.setCalculation_net_assets("16");
        ea.setCalculation_current_assets("17");
        ea.setCalculation_current_liabilities("18");

        counter.process(ea);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-02"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals(19, reports.size());
        assertEquals("1", reports.get(EXP_DAYS_SINCE_PUB));

        assertEquals("1", reports.get(EXP_INDUSTRY));
        assertEquals("2", reports.get(EXP_CONTRACTOR_CLASS));
        assertEquals("3", reports.get(EXP_SOLVENCY_GROUP));
        assertEquals("4", reports.get(EXP_POINTS));

        assertEquals("5", reports.get(EXP_FIN_COVERAGE_COEF_POINTS));
        assertEquals("6", reports.get(EXP_FIN_HOT_LIQ_COEF_POINTS));
        assertEquals("7", reports.get(EXP_FIN_CAPITAL_TURNOVER_POINTS));
        assertEquals("8", reports.get(EXP_FIN_SELL_PROFIT_POINTS));
        assertEquals("9", reports.get(EXP_FIN_ASSETS_PROFIT_POINTS));
        assertEquals("10", reports.get(EXP_FIN_STABILITY_COEF_POINTS));

        assertEquals("11", reports.get(EXP_BUS_COOP_EXP_CATEGORY));
        assertEquals("12", reports.get(EXP_BUS_COOP_EXP_POINTS));
        assertEquals("13", reports.get(EXP_BUS_PAY_DISCIPLINE_CATEGORY));
        assertEquals("14", reports.get(EXP_BUS_PAY_DISCIPLINE_POINTS));

        assertEquals("15", reports.get(EXP_CALC_NON_CURR_ASSETS));
        assertEquals("16", reports.get(EXP_CALC_NET_ASSETS));
        assertEquals("17", reports.get(EXP_CALC_CURR_ASSETS));
        assertEquals("18", reports.get(EXP_CALC_CURR_LIABILITIES));
    }
}
