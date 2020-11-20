package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanFnsDisqModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.FNS_DISQ;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.FNS_DISQ_DAYS_SINCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FnsDisqCounterTest {

    private FnsDisqCounter counter;

    @Before
    public void setUp() {
        counter = new FnsDisqCounter();
    }

    @Test
    public void neverWasInRegistryTest() {
        Map<String, String> reports = processPdz("1970-01-01");
        assertNull(reports.get(FNS_DISQ_DAYS_SINCE));
        assertEquals("0", reports.get(FNS_DISQ));
    }

    @Test
    public void appearsInRegistryTest() {
        processCounter("1970-01-01");
        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals("0", reports.get(FNS_DISQ_DAYS_SINCE));
        assertEquals("1", reports.get(FNS_DISQ));
    }

    @Test
    public void threeDaysInRegistryTest() {
        processCounter("1970-01-01");
        Map<String, String> reports = processPdz("1970-01-04");
        assertEquals("3", reports.get(FNS_DISQ_DAYS_SINCE));
        assertEquals("1", reports.get(FNS_DISQ));
    }

    @Test
    public void thirtyDaysInRegistryTest() {
        processCounter("1970-01-01");
        Map<String, String> reports = processPdz("1970-02-01");
        assertEquals("31", reports.get(FNS_DISQ_DAYS_SINCE));
        assertEquals("1", reports.get(FNS_DISQ));
    }

    @Test
    public void appearsInRegistryAgainTest() {
        processCounter("1970-01-01");
        processCounter("1970-01-02");
        Map<String, String> reports = processPdz("1970-01-02");
        assertEquals("0", reports.get(FNS_DISQ_DAYS_SINCE));
        assertEquals("1", reports.get(FNS_DISQ));
    }

    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        pdz.setReports(new HashMap<>());
        return counter.process(pdz);
    }

    private void processCounter(String date) {
        CleanFnsDisqModel cleanModel = new CleanFnsDisqModel();
        cleanModel.setEventDate(Date.valueOf(date));
        counter.process(cleanModel);
    }
}
