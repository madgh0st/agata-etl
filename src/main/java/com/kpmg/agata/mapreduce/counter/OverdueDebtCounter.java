package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;

import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.HAS_PDZ;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

/**
 * If current CleanWeeklyStatusPDZFlatModel has overdue debt, then "has_pdz" report is 1.
 * If it hasn't overdue debt, then "has_pdz" report is 0.
 */
public class OverdueDebtCounter implements EventLogCounter {
    private final OverdueDebtHandler handler = new OverdueDebtHandler();

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (!(model instanceof CleanWeeklyStatusPDZFlatModel)) return emptyMap();

        CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
        boolean hasPdz = handler.handleOverdueDebt(pdz);
        return singletonMap(HAS_PDZ, hasPdz ? "1" : "0");
    }
}
