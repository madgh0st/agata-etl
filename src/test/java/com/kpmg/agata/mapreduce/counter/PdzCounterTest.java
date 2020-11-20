package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.sql.Date;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.DAYS_SINCE_LAST_OVERDUE_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.DIST_21D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.IS_OVERDUE_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.OVERDUE_NUM_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PCT_21D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PDZ_COMMENTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PDZ_SEASONALITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PdzCounterTest {
    private static double significantDebt;
    private static double notSignificantDebt;
    private PdzCounter counter;

    @BeforeClass
    public static void beforeClass() throws Exception {
        significantDebt = Whitebox.getField(OverdueDebtHandler.class, "SIGNIFICANT_OVERDUE_DEBT")
                .getDouble(null);
        notSignificantDebt = significantDebt - 1.0;
    }

    @Before
    public void setUp() {
        counter = new PdzCounter();
    }


    private CleanWeeklyStatusPDZFlatModel buildCleanPdz(Double debt, String date, String comments, String seasonality) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setOverdueDebt(debt);
        pdz.setEventDate(Date.valueOf(date));
        pdz.setComments(comments);
        pdz.setSeasonality(seasonality);
        return pdz;
    }

    @Test
    public void test100pct() {
        processPdz(significantDebt, "1970-01-01");
        processPdz(significantDebt, "1970-01-02");
        processPdz(significantDebt, "1970-01-03");
        Map<String, String> result = processPdz(significantDebt, "1970-01-04");
        assertEquals("1.0", result.get(PCT_21D));
    }

    @Test
    public void test75pct() {
        processPdz(significantDebt, "1970-01-01");
        processPdz(significantDebt, "1970-01-02");
        processPdz(0.0, "1970-01-03");
        Map<String, String> result = processPdz(significantDebt, "1970-01-04");
        assertEquals("0.75", result.get(PCT_21D));
    }

    @Test
    public void test() {
        // DIST_21D
        Map<String, String> result = processPdz(significantDebt, "1970-01-01");
        assertEquals("1.0", result.get(DIST_21D));

        result = processPdz(significantDebt, "1970-01-03");
        assertEquals("1.0", result.get(DIST_21D));

        result = processPdz(0.0, "1970-01-05");
        assertEquals("0.0", result.get(DIST_21D));

        result = processPdz(significantDebt, "1970-01-10");
        assertEquals("0.75", result.get(DIST_21D));

        result = processPdz(0.0, "1970-01-25");
        assertEquals("0.0", result.get(DIST_21D));

        result = processPdz(significantDebt, "1970-01-26");
        assertEquals("0.66", result.get(DIST_21D).substring(0, 4));

        // PCT_21D
        result = processPdz(0.0, "1970-02-01");
        assertEquals("0.33", result.get(PCT_21D).substring(0, 4));

        result = processPdz(significantDebt, "1970-02-05");
        assertEquals("0.5", result.get(PCT_21D));

        result = processPdz(significantDebt, "1970-02-08");
        assertEquals("0.6", result.get(PCT_21D));

        result = processPdz(significantDebt, "1970-02-12");
        assertEquals("0.66", result.get(PCT_21D).substring(0, 4));

        result = processPdz(significantDebt, "1970-02-15");
        assertEquals("0.83", result.get(PCT_21D).substring(0, 4));

        result = processPdz(significantDebt, "1970-02-16");
        assertEquals("0.83", result.get(PCT_21D).substring(0, 4));

        // DAYS_SINCE_LAST_OVERDUE_90D
        result = processPdz(0.0, "1970-02-20");
        assertEquals("4", result.get(DAYS_SINCE_LAST_OVERDUE_90D));

        // OVERDUE_NUM_90D
        result = processPdz(0.0, "1970-02-21");
        assertEquals("9", result.get(OVERDUE_NUM_90D));

        // IS_OVERDUE_90D
        result = processPdz(significantDebt, "1970-02-22");
        assertEquals("1", result.get(IS_OVERDUE_90D));

        result = processPdz(0.0, "1970-05-23");
        assertEquals("0", result.get(IS_OVERDUE_90D));

        // OVERDUE_NUM_90D
        assertEquals("0", result.get(OVERDUE_NUM_90D));
        assertEquals("90", result.get(DAYS_SINCE_LAST_OVERDUE_90D));
    }

    private Map<String, String> processPdz(Double debt, String date) {
        return counter.process(buildCleanPdz(debt, date, null, null));
    }

    private Map<String, String> processPdzWithComments(String date, String comments) {
        return counter.process(buildCleanPdz(0.0, date, comments, null));
    }

    @Test
    public void testOneDayPdzBothNotSignificantPct() {
        processPdz(notSignificantDebt, "1970-01-01");
        Map<String, String> noOneSignificant = processPdz(notSignificantDebt, "1970-01-01");
        assertEquals("0.0", noOneSignificant.get(PCT_21D));
    }

    @Test
    public void testOneDayPdzFirstSignificantPct() {
        processPdz(significantDebt, "1970-01-01");
        Map<String, String> firstSignificant = processPdz(notSignificantDebt, "1970-01-01");
        assertEquals("1.0", firstSignificant.get(PCT_21D));
    }

    @Test
    public void testOneDayPdzSecondSignificantPct() {

        processPdz(notSignificantDebt, "1970-01-01");
        Map<String, String> secondSignificant = processPdz(significantDebt, "1970-01-01");
        assertEquals("1.0", secondSignificant.get(PCT_21D));
    }

    @Test
    public void testOneDayPdzBothSignificantPct() {
        processPdz(significantDebt, "1970-01-01");
        Map<String, String> noOneSignificant = processPdz(significantDebt, "1970-01-01");
        assertEquals("1.0", noOneSignificant.get(PCT_21D));
    }

    @Test
    public void commentsAppear() {
        Map<String, String> report = processPdzWithComments("1970-01-01", "some comments");
        assertEquals("some comments", report.get(PDZ_COMMENTS));
    }

    @Test
    public void nullCommentsAppear() {
        Map<String, String> report = processPdzWithComments("1970-01-01", null);
        assertNull(report.get(PDZ_COMMENTS));
    }

    @Test
    public void emptyCommentsAppear() {
        Map<String, String> report = processPdzWithComments("1970-01-01", "");
        assertNull(report.get(PDZ_COMMENTS));
    }

    @Test
    public void commentsAndThenAnotherCommentsAppear() {
        Map<String, String> firstReport = processPdzWithComments("1970-01-01", "some comments");
        assertEquals("some comments", firstReport.get(PDZ_COMMENTS));

        Map<String, String> secondReport = processPdzWithComments("1970-01-01", "another comments");
        assertEquals("another comments", secondReport.get(PDZ_COMMENTS));
    }

    private Map<String, String> processPdzWithSeasonality(String date, String seasonality) {
        return counter.process(buildCleanPdz(0.0, date, null, seasonality));
    }

    @Test
    public void seasonality() {
        assertEquals("0", processPdzWithSeasonality("1970-01-01", "0").get(PDZ_SEASONALITY));
        assertEquals("1", processPdzWithSeasonality("1970-01-01", "1").get(PDZ_SEASONALITY));

        assertNull(processPdzWithSeasonality("1970-01-01", null).get(PDZ_SEASONALITY));
        assertNull(processPdzWithSeasonality("1970-01-01", "123").get(PDZ_SEASONALITY));
        assertNull(processPdzWithSeasonality("1970-01-01", "str").get(PDZ_SEASONALITY));
    }
}
