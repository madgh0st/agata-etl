package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanFnsTaxViolationModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_180_DAYS;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.DESC;

public class FnsTaxViolationCounter implements EventLogCounter {

    private WindowBuffer<String> buffer = new WindowBuffer<>(RANGE_180_DAYS, ASC);

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        Map<String, String> result;
        LocalDate currentDate = model.getEventDate().toLocalDate();
        if (model instanceof CleanFnsTaxViolationModel) {
            CleanFnsTaxViolationModel taxViolationModel = (CleanFnsTaxViolationModel) model;
            buffer.put(currentDate, taxViolationModel.getFine());
            result = Collections.emptyMap();
        } else if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            result = populateResult(currentDate);
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    private Map<String, String> populateResult(LocalDate currentDate) {
        Map<String, String> result = new HashMap<>();
        LocalDate from = currentDate.minusDays(RANGE_180_DAYS).plusDays(1);
        String lastTaxViolationValue = buffer.streamWithinRange(from, currentDate, DESC).findFirst().orElse("0");
        result.put(CounterConstants.FNS_TAX_VIOLATION, lastTaxViolationValue);
        return result;
    }
}
