package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanGenprocModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyMap;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.GENPROC_DAYS_IN_REGISTRY;

/**
 * Counts days since last appearance in head of prosecutor office registry of illegal award
 * If it was today, then returns 0 days
 */
public class GenprocCounter implements EventLogCounter {

    private LocalDate lastSeenInRegistry = null;

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (model instanceof CleanGenprocModel) {
            return processByGenproc(model);
        }

        if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
            return processByPdz(pdz);
        }

        return emptyMap();
    }

    private Map<String, String> processByGenproc(AbstractCleanDataModel genproc) {
        lastSeenInRegistry = genproc.getEventDate().toLocalDate();
        return emptyMap();
    }

    protected Map<String, String> processByPdz(AbstractCleanDataModel pdz) {
        if (lastSeenInRegistry == null) return emptyMap();

        Map<String, String> reports = new HashMap<>();
        long days = DAYS.between(lastSeenInRegistry, pdz.getEventDate().toLocalDate());
        reports.put(GENPROC_DAYS_IN_REGISTRY, String.valueOf(days));
        return reports;
    }
}
