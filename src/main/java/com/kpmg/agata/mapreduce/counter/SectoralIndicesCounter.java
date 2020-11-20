package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanSectoralIndexModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.utils.Utils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.joining;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_90_DAYS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.SECTORAL_INDICES_AVERAGES;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;

public class SectoralIndicesCounter implements EventLogCounter {

    private static final int PERIOD = 30;
    private static final int PERIODS_COUNT = 3;

    private final WindowBuffer<Double> indicesBuffer = new WindowBuffer<>(RANGE_90_DAYS, ASC);
    private String indexId = null;

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (model instanceof CleanSectoralIndexModel) {
            return processSectoralIndex((CleanSectoralIndexModel) model);
        }

        if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            return processPdz((CleanWeeklyStatusPDZFlatModel) model);
        }

        return emptyMap();
    }

    private Map<String, String> processSectoralIndex(CleanSectoralIndexModel index) {
        if (indexId == null) {
            indexId = index.getId();
        }
        if (!indexId.equals(index.getId())) {
            return emptyMap();
        }

        LocalDate currentDate = index.getEventDate().toLocalDate();
        indicesBuffer.put(currentDate, index.getClose());

        return emptyMap();
    }

    private Map<String, String> processPdz(CleanWeeklyStatusPDZFlatModel pdz) {
        LocalDate to = pdz.getEventDate().toLocalDate();
        LocalDate from = to.minusDays(30).plusDays(1);

        List<Double> values = new ArrayList<>();
        for (int i = 0; i < PERIODS_COUNT; i++) {
            indicesBuffer.streamEntriesWithinRange(from, to, ASC)
                         .mapToDouble(Map.Entry::getValue)
                         .average()
                         .ifPresent(values::add);

            from = from.minusDays(PERIOD);
            to = to.minusDays(PERIOD);
        }

        if(values.isEmpty()) return emptyMap();

        String concatValues = values.stream()
                                    .map(value -> Utils.round(value, 2))
                                    .map(value -> Double.toString(value))
                                    .collect(joining(";"));

        return singletonMap(SECTORAL_INDICES_AVERAGES, concatValues);
    }
}
