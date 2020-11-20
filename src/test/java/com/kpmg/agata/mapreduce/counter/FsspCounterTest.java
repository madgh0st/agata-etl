package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanFsspModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.FSSP_NUM_IN_PAST;
import static org.junit.Assert.assertEquals;

public class FsspCounterTest {

    private FsspCounter counter;

    @Before
    public void setUp() {
        counter = new FsspCounter();
    }

    @Test
    public void neverSeenTest() {
        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals("0", reports.get(FSSP_NUM_IN_PAST));
    }

    @Test
    public void incrementCounterTest() {
        processCounter("1970-01-01");
        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals("1", reports.get(FSSP_NUM_IN_PAST));
        processCounter("1970-01-02");
        reports = processPdz("1970-01-02");
        assertEquals("2", reports.get(FSSP_NUM_IN_PAST));
    }

    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        pdz.setReports(new HashMap<>());
        return counter.process(pdz);
    }

    private void processCounter(String date) {
        CleanFsspModel cleanModel = new CleanFsspModel();
        cleanModel.setEventDate(Date.valueOf(date));
        counter.process(cleanModel);
    }
}
