package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.PDZ_21;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.TARGET_1M;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.TARGET_3M;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.TARGET_6M;
import static org.junit.Assert.assertEquals;

public class TargetCounterTest {
    private static final String JAN_01 = "1970-01-01";
    private TargetCounter counter;

    @Before
    public void setUp() {
        counter = new TargetCounter();
    }

    private static Map<String, String> consumePdz21(TargetCounter counter, String value, String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        Map<String, String> oldReports = new HashMap<>();
        oldReports.put(PDZ_21, value);
        pdz.setReports(oldReports);

        return counter.process(pdz);
    }

    @Test
    public void noFutureValuesTest() {
        Map<String, String> reports = consumePdz21(counter, "1", "1970-06-01");

        assertEquals("0", reports.get(TARGET_1M));
        assertEquals("0", reports.get(TARGET_3M));
        assertEquals("0", reports.get(TARGET_6M));
    }

    // 1970-02-01 lies inside 1 month
    @Test
    public void onePositiveFutureValueTest() {
        consumePdz21(counter, "1", "1970-02-01");
        Map<String, String> reports = consumePdz21(counter, "1", JAN_01);

        assertEquals("1", reports.get(TARGET_1M));
        assertEquals("1", reports.get(TARGET_3M));
        assertEquals("1", reports.get(TARGET_6M));
    }

    // 1970-02-02 lies outside 1 month range, but inside 3 and 6 month ranges
    @Test
    public void onePositive31DaysFutureValueTest() {
        consumePdz21(counter, "1", "1970-02-02");
        Map<String, String> reports = consumePdz21(counter, "1", JAN_01);

        assertEquals("0", reports.get(TARGET_1M));
        assertEquals("1", reports.get(TARGET_3M));
        assertEquals("1", reports.get(TARGET_6M));
    }

    // 1970-04-01 lies outside 1 month range, but inside 3 and 6 month ranges
    @Test
    public void onePositive60DaysFutureValueTest() {
        consumePdz21(counter, "1", "1970-04-01");
        Map<String, String> reports = consumePdz21(counter, "1", JAN_01);

        assertEquals("0", reports.get(TARGET_1M));
        assertEquals("1", reports.get(TARGET_3M));
        assertEquals("1", reports.get(TARGET_6M));
    }

    // 1970-04-02 lies outside 1 and 3 month ranges, but inside 6 month range
    @Test
    public void onePositive61DaysFutureValueTest() {
        consumePdz21(counter, "1", "1970-04-02");
        Map<String, String> reports = consumePdz21(counter, "1", JAN_01);

        assertEquals("0", reports.get(TARGET_1M));
        assertEquals("0", reports.get(TARGET_3M));
        assertEquals("1", reports.get(TARGET_6M));
    }

    // 1970-07-01 lies outside 1 and 3 month ranges, but inside 6 month range
    @Test
    public void onePositive180DaysFutureValueTest() {
        consumePdz21(counter, "1", "1970-07-01");
        Map<String, String> reports = consumePdz21(counter, "1", JAN_01);

        assertEquals("0", reports.get(TARGET_1M));
        assertEquals("0", reports.get(TARGET_3M));
        assertEquals("1", reports.get(TARGET_6M));
    }

    // 1970-07-02 lies outside 1, 3 and 6 month ranges
    @Test
    public void onePositive181DaysFutureValueTest() {
        consumePdz21(counter, "1", "1970-07-02");
        Map<String, String> reports = consumePdz21(counter, "1", JAN_01);

        assertEquals("0", reports.get(TARGET_1M));
        assertEquals("0", reports.get(TARGET_3M));
        assertEquals("0", reports.get(TARGET_6M));
    }

    // 1970-06-30 lies outside 1, 3 and 6 month ranges
    @Test
    public void severalValuesTest() {
        // out of 6 month
        consumePdz21(counter, "0", "1970-07-20");
        consumePdz21(counter, "1", "1970-07-10");
        // within 6 month
        consumePdz21(counter, "0", "1970-06-10");
        consumePdz21(counter, "0", "1970-04-25");
        // within 3 month
        consumePdz21(counter, "0", "1970-02-25");
        consumePdz21(counter, "1", "1970-02-15");
        // within 1 month
        consumePdz21(counter, "0", "1970-01-20");
        consumePdz21(counter, "0", "1970-01-02");

        Map<String, String> reports = consumePdz21(counter, "1", JAN_01);

        assertEquals("0", reports.get(TARGET_1M));
        assertEquals("1", reports.get(TARGET_3M));
        assertEquals("1", reports.get(TARGET_6M));
    }
}
