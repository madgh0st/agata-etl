package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import org.junit.Test;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_HAS_EFF_LIMIT;
import static org.junit.Assert.assertEquals;

public class ReverseCreditLimitCounterTest {
    ReverseCreditLimitCounter counter = new ReverseCreditLimitCounter();

    @Test
    public void counterTest() {
        Map<String, String> reports = processPdz("1970-01-30");
        assertEquals("0", reports.get(CL_HAS_EFF_LIMIT));

        processCreditLimit("1970-01-29", "1970-01-28", "1970-01-29");

        reports = processPdz("1970-01-29");
        assertEquals("0", reports.get(CL_HAS_EFF_LIMIT));

        reports = processPdz("1970-01-28");
        assertEquals("1", reports.get(CL_HAS_EFF_LIMIT));

        reports = processPdz("1970-01-28");
        assertEquals("1", reports.get(CL_HAS_EFF_LIMIT));

        reports = processPdz("1970-01-27");
        assertEquals("0", reports.get(CL_HAS_EFF_LIMIT));

    }

    private void processCreditLimit(String date, String approval, String expiration) {
        CleanCreditLimitModel cl = new CleanCreditLimitModel();
        cl.setEventDate(Date.valueOf(date));
        cl.setApprovalDate(Date.valueOf(approval));
        cl.setExpirationDate(Date.valueOf(expiration));
        counter.process(cl);
    }

    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        pdz.setReports(new HashMap<>());
        return counter.process(pdz);
    }
}
