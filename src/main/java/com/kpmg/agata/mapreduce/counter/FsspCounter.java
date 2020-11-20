package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanFsspModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.FSSP_NUM_IN_PAST;

public class FsspCounter implements EventLogCounter {

    private Integer fsspExecutionsInPast = 0;

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (model instanceof CleanFsspModel) {
            fsspExecutionsInPast++;
            return Collections.emptyMap();
        } else if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            Map<String, String> reports = new HashMap<>();
            reports.put(FSSP_NUM_IN_PAST, String.valueOf(fsspExecutionsInPast));
            return reports;
        }
        return Collections.emptyMap();
    }
}
