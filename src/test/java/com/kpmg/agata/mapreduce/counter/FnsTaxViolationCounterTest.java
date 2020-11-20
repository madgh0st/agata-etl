package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanFnsTaxViolationModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import org.junit.Test;

import java.sql.Date;
import java.util.Calendar;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FnsTaxViolationCounterTest {

    private FnsTaxViolationCounter counter = new FnsTaxViolationCounter();

    @Test
    public void oneViolationCatch() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.valueOf("1970-01-01"));
        Date date1 = new Date(calendar.getTimeInMillis());

        CleanFnsTaxViolationModel model = new CleanFnsTaxViolationModel();
        model.setEventDate(date1);
        model.setFine("101.0");
        counter.process(model);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date2 = new Date(calendar.getTimeInMillis());

        model = new CleanFnsTaxViolationModel();
        model.setEventDate(date2);
        model.setFine("11111.0");
        counter.process(model);

        calendar.add(Calendar.DAY_OF_MONTH, 179);
        Date date3 = new Date(calendar.getTimeInMillis());

        CleanWeeklyStatusPDZFlatModel pdzModel = new CleanWeeklyStatusPDZFlatModel();
        pdzModel.setEventDate(date3);

        Map<String, String> reports = counter.process(pdzModel);

        assertEquals("11111.0", reports.get("fns_tax_violation_fine"));
    }

    @Test
    public void zeroValueCatch() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.valueOf("1970-01-01"));
        Date date1 = new Date(calendar.getTimeInMillis());

        CleanFnsTaxViolationModel model = new CleanFnsTaxViolationModel();
        model.setEventDate(date1);
        model.setFine("101.0");
        counter.process(model);

        calendar.add(Calendar.DAY_OF_MONTH, 180);
        Date date2 = new Date(calendar.getTimeInMillis());

        CleanWeeklyStatusPDZFlatModel pdzModel = new CleanWeeklyStatusPDZFlatModel();
        pdzModel.setEventDate(date2);
        Map<String, String> reports = counter.process(pdzModel);

        assertEquals("0", reports.get("fns_tax_violation_fine"));
    }

    @Test
    public void pdzOnly() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.valueOf("1970-01-01"));
        Date date1 = new Date(calendar.getTimeInMillis());

        CleanWeeklyStatusPDZFlatModel pdzModel = new CleanWeeklyStatusPDZFlatModel();
        pdzModel.setEventDate(date1);
        Map<String, String> reports = counter.process(pdzModel);

        assertEquals("0", reports.get("fns_tax_violation_fine"));
    }
}
