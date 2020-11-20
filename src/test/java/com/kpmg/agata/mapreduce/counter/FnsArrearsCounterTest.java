package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanFnsArrearsModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.sql.Date;
import java.util.Calendar;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class FnsArrearsCounterTest {

    private FnsArrearsCounter counter = new FnsArrearsCounter();

    @Test
    public void oneArrearsCatch() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.valueOf("1970-01-01"));
        Date date1 = new Date(calendar.getTimeInMillis());

        CleanFnsArrearsModel model = new CleanFnsArrearsModel();
        model.setEventDate(date1);
        model.setTaxName(FnsArrearsCounter.ArrearsTaxName.NON_TAX_INCOME.getRawName());
        model.setTotalArrears("101.0");
        counter.process(model);

        calendar.add(Calendar.DAY_OF_MONTH, -179);
        Date date2 = new Date(calendar.getTimeInMillis());

        model = new CleanFnsArrearsModel();
        model.setEventDate(date2);
        model.setTaxName(FnsArrearsCounter.ArrearsTaxName.GAMBLING.getRawName());
        model.setTotalArrears("11111.0");
        counter.process(model);

        calendar.add(Calendar.DAY_OF_MONTH, 179);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date3 = new Date(calendar.getTimeInMillis());

        CleanWeeklyStatusPDZFlatModel pdzModel = new CleanWeeklyStatusPDZFlatModel();
        pdzModel.setEventDate(date3);

        Map<String, String> reports = counter.process(pdzModel);

        assertEquals("101.0",
                reports.get("fns_arrears_" + FnsArrearsCounter.ArrearsTaxName.NON_TAX_INCOME.name().toLowerCase()));
        assertNotEquals("11111.0",
                reports.get("fns_arrears_" + FnsArrearsCounter.ArrearsTaxName.GAMBLING.name().toLowerCase()));
    }

    @Test
    public void twoArrearsCatch() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.valueOf("1970-01-01"));
        Date date1 = new Date(calendar.getTimeInMillis());

        CleanFnsArrearsModel model = new CleanFnsArrearsModel();
        model.setEventDate(date1);
        model.setTaxName(FnsArrearsCounter.ArrearsTaxName.GAMBLING.getRawName());
        model.setTotalArrears("101.0");
        counter.process(model);

        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date date2 = new Date(calendar.getTimeInMillis());

        model = new CleanFnsArrearsModel();
        model.setEventDate(date2);
        model.setTaxName(FnsArrearsCounter.ArrearsTaxName.GAMBLING.getRawName());
        model.setTotalArrears("11111.0");
        counter.process(model);


        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date3 = new Date(calendar.getTimeInMillis());

        CleanWeeklyStatusPDZFlatModel pdzModel = new CleanWeeklyStatusPDZFlatModel();
        pdzModel.setEventDate(date3);

        Map<String, String> reports = counter.process(pdzModel);

        assertEquals("11111.0",
                reports.get("fns_arrears_" + FnsArrearsCounter.ArrearsTaxName.GAMBLING.name().toLowerCase()));
    }

    @Test
    public void pdzOnly() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.valueOf("1970-01-01"));
        Date date1 = new Date(calendar.getTimeInMillis());

        CleanWeeklyStatusPDZFlatModel pdzModel = new CleanWeeklyStatusPDZFlatModel();
        pdzModel.setEventDate(date1);
        Map<String, String> reports = counter.process(pdzModel);

        for (FnsArrearsCounter.ArrearsTaxName value : FnsArrearsCounter.ArrearsTaxName.values()) {
            String signal = FnsArrearsCounter.ArrearsTaxName.getSignal(value.getRawName());
            assertNull(reports.get(signal));
        }
    }
}
