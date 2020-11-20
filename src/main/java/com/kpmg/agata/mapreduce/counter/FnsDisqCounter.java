package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanFnsDisqModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyMap;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.FNS_DISQ;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.FNS_DISQ_DAYS_SINCE;

public class FnsDisqCounter implements EventLogCounter {

    private LocalDate lastSeen = null;

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (model instanceof CleanFnsDisqModel) {
            return processByFnsDisq(model);
        }

        if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
            return processByPdz(pdz);
        }

        return emptyMap();
    }

    private Map<String, String> processByFnsDisq(AbstractCleanDataModel cleanModel) {
        lastSeen = cleanModel.getEventDate().toLocalDate();
        return emptyMap();
    }

    protected Map<String, String> processByPdz(AbstractCleanDataModel pdz) {
        Map<String, String> reports = new HashMap<>();
        if (lastSeen == null) {
            reports.put(FNS_DISQ, "0");
        } else {
            reports.put(FNS_DISQ, "1");
            long days = DAYS.between(lastSeen, pdz.getEventDate().toLocalDate());
            reports.put(FNS_DISQ_DAYS_SINCE, String.valueOf(days));
        }
        return reports;
    }
}
