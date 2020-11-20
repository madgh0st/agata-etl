package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanSectoralIndexModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.sql.Date;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.SECTORAL_INDICES_AVERAGES;
import static org.junit.Assert.*;

public class SectoralIndicesCounterTest {

    private SectoralIndicesCounter counter;

    @Before
    public void setUp() {
        counter = new SectoralIndicesCounter();
    }

    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        return counter.process(pdz);
    }

    private void processSectoralIndex(String date, String id, double close) {
        CleanSectoralIndexModel index = new CleanSectoralIndexModel();
        index.setEventDate(Date.valueOf(date));
        index.setId(id);
        index.setClose(close);
        counter.process(index);
    }
    private void processSectoralIndex(String date, double close) {
        processSectoralIndex(date, "FIXED-ID", close);
    }


    @Test
    public void oneIndexInTheMiddlePerPeriod() {
        processSectoralIndex("1970-01-15", 1);
        processSectoralIndex("1970-02-15", 2);
        processSectoralIndex("1970-03-15", 3);
        assertEquals("3.0;2.0;1.0", processPdz("1970-04-01").get(SECTORAL_INDICES_AVERAGES));
    }

    @Test
    public void noIndicesAppears() {
        assertNull(processPdz("1970-01-01").get(SECTORAL_INDICES_AVERAGES));
    }

    @Test
    public void averageIsCorrect() {
        processSectoralIndex("1970-01-15", 1);
        processSectoralIndex("1970-01-16", 2);

        processSectoralIndex("1970-02-15", 3);
        processSectoralIndex("1970-02-16", 4);
        processSectoralIndex("1970-02-17", 5);

        processSectoralIndex("1970-03-15", 1);
        processSectoralIndex("1970-03-16", 1);
        assertEquals("1.0;4.0;1.5", processPdz("1970-04-01").get(SECTORAL_INDICES_AVERAGES));
    }

    @Test
    public void anotherIndexIsSkipped() {
        processSectoralIndex("1970-01-01", "ID",1);
        processSectoralIndex("1970-01-02", "ANOTHER-ID", 999);
        assertEquals("1.0", processPdz("1970-01-03").get(SECTORAL_INDICES_AVERAGES));
    }

    @Test
    public void ifOneDayIndicesThenTakeTheLast() {
        processSectoralIndex("1970-01-01",1);
        processSectoralIndex("1970-01-01", 3);
        assertEquals("3.0", processPdz("1970-01-03").get(SECTORAL_INDICES_AVERAGES));
    }

    @Test
    public void boundaryValues() {
        processSectoralIndex("1970-01-01",1);       // outside the 1-st period, but inside the 2-nd
        processSectoralIndex("1970-01-02", 3);      // inside the 1-st period
        assertEquals("3.0;1.0", processPdz("1970-01-31").get(SECTORAL_INDICES_AVERAGES));
    }

    @Test
    public void allBoundariesValues() {
        // boundaries are
        // 1-st: [04-01;03-03], 2-nd: [03-02;02-01], 3-rd [31-01;01-02]
        processSectoralIndex("1970-01-01",1);       // outside the 3-rd period

        processSectoralIndex("1970-01-02",2);       // inside the 3-rd period, near to leave
        processSectoralIndex("1970-01-31",3);       // inside the 3-nd period, near to the 2-nd

        processSectoralIndex("1970-02-01",4);       // inside the 2-st period, near to the 3-rd
        processSectoralIndex("1970-03-02",5);       // inside the 2-st period, near to the 1-st

        processSectoralIndex("1970-03-03", 6);      // inside the 1-st period, near to the 2-nd
        processSectoralIndex("1970-04-01", 7);      // inside the 1-st period

        assertEquals("6.5;4.5;2.5", processPdz("1970-04-01").get(SECTORAL_INDICES_AVERAGES));
    }
}
