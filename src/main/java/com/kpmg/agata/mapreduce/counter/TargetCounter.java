package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.PDZ_21;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.TARGET_1M;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.TARGET_3M;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.TARGET_6M;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.DESC;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyMap;

/**
 * Consumes models in date descending order.
 * Has three horizons - 1, 3, 6 months in the future from current date.
 * Provides one independent result per horizon.
 * It's important to have pdz_21 report in all provided AbstractCleanModels
 * TargetCounter searches pdz_21 from current date (excluding it) within horizon.
 * If anyone is "1", then report "1".
 * If none of them is "1", then report "0".
 */
public class TargetCounter implements EventLogCounter {
    // max days in month * biggest horizon
    private static final int BUFFER_SIZE = 31 * 6;

    private WindowBuffer<Boolean> buffer = new WindowBuffer<>(BUFFER_SIZE, DESC);

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (!(model instanceof CleanWeeklyStatusPDZFlatModel)) return emptyMap();

        CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
        LocalDate currentDate = pdz.getEventDate().toLocalDate();

        String pdz21 = pdz.getReports().get(PDZ_21);
        buffer.put(currentDate, "1".equals(pdz21));

        Map<String, String> result = new HashMap<>();
        result.put(TARGET_1M, calculateTarget(1, currentDate));
        result.put(TARGET_3M, calculateTarget(3, currentDate));
        result.put(TARGET_6M, calculateTarget(6, currentDate));

        return result;
    }

    private String calculateTarget(int months, LocalDate currentDate) {
        LocalDate from = currentDate.plusDays(1);
        LocalDate to = currentDate.plusMonths(months);
        boolean target = buffer.streamWithinRange(from, to)
                .anyMatch(TRUE::equals);
        return target ? "1" : "0";
    }
}
