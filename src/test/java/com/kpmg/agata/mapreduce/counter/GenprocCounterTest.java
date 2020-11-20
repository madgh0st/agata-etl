package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanGenprocModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.GENPROC_DAYS_IN_REGISTRY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GenprocCounterTest {

    private GenprocCounter counter;

    @Before
    public void setUp() {
        counter = new GenprocCounter();
    }

    @Test
    public void neverWasInRegistryTest() {
        Map<String, String> reports = processPdz("1970-01-01");
        assertNull(reports.get(GENPROC_DAYS_IN_REGISTRY));
    }

    @Test
    public void appearsInRegistryTest() {
        processGenproc("1970-01-01");
        Map<String, String> reports = processPdz("1970-01-01");
        assertEquals("0", reports.get(GENPROC_DAYS_IN_REGISTRY));
    }

    @Test
    public void threeDaysInRegistryTest() {
        processGenproc("1970-01-01");
        Map<String, String> reports = processPdz("1970-01-04");
        assertEquals("3", reports.get(GENPROC_DAYS_IN_REGISTRY));
    }

    @Test
    public void thirtyDaysInRegistryTest() {
        processGenproc("1970-01-01");
        Map<String, String> reports = processPdz("1970-02-01");
        assertEquals("31", reports.get(GENPROC_DAYS_IN_REGISTRY));
    }

    @Test
    public void appearsInRegistryAgainTest() {
        processGenproc("1970-01-01");
        processGenproc("1970-01-02");
        Map<String, String> reports = processPdz("1970-01-02");
        assertEquals("0", reports.get(GENPROC_DAYS_IN_REGISTRY));
    }

    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        pdz.setReports(new HashMap<>());
        return counter.process(pdz);
    }

    private void processGenproc(String date) {
        CleanGenprocModel genproc = new CleanGenprocModel();
        genproc.setEventDate(Date.valueOf(date));
        counter.process(genproc);
    }
}
