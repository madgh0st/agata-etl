package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.DAYS_SINCE_LAST_OVERDUE_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.DIST_21D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.IS_OVERDUE_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.OVERDUE_NUM_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PCT_21D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PDZ_COMMENTS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PDZ_SEASONALITY;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_21_DAYS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_90_DAYS;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.DESC;
import static java.lang.Boolean.TRUE;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyMap;

public class PdzCounter implements EventLogCounter {
    private final WindowBuffer<Boolean> pdzBuffer = new WindowBuffer<>(RANGE_90_DAYS, ASC);
    private final OverdueDebtHandler handler = new OverdueDebtHandler();

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (!(model instanceof CleanWeeklyStatusPDZFlatModel)) return emptyMap();
        CleanWeeklyStatusPDZFlatModel pdzModel = (CleanWeeklyStatusPDZFlatModel) model;
        LocalDate currentDate = pdzModel.getEventDate().toLocalDate();
        Map<String, String> result = new HashMap<>();

        boolean overdueDebt = handler.handleOverdueDebt(pdzModel);
        pdzBuffer.put(currentDate, overdueDebt, (last, cur) -> last || cur);

        result.put(PCT_21D, String.valueOf(shareIn21Days(currentDate)));

        String pct21d = countPercentFor21Days(overdueDebt, currentDate);
        result.put(DIST_21D, pct21d);

        String anyPdzOverdue90Days = isAnyPdzOverdue90Days(currentDate);
        result.put(IS_OVERDUE_90D, anyPdzOverdue90Days);

        String pdzAmountIn90Days = countPdzAmountIn90Days(currentDate);
        result.put(OVERDUE_NUM_90D, pdzAmountIn90Days);

        String daysSinceLastOverdue = countDaysSinceLastOverdue(currentDate);
        result.put(DAYS_SINCE_LAST_OVERDUE_90D, daysSinceLastOverdue);

        if (StringUtils.isNotBlank(pdzModel.getComments())) {
            result.put(PDZ_COMMENTS, pdzModel.getComments());
        }

        if ("0".equals(pdzModel.getSeasonality()) || "1".equals(pdzModel.getSeasonality())) {
            result.put(PDZ_SEASONALITY, pdzModel.getSeasonality());
        }

        return result;
    }

    private String countDaysSinceLastOverdue(LocalDate currentDate) {
        LocalDate from = currentDate.minusDays(RANGE_21_DAYS).plusDays(1);
        long daysSinceLastOverdue = pdzBuffer.streamEntriesWithinRange(from, currentDate, DESC)
                .filter(entry -> TRUE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .map(lastDate -> DAYS.between(lastDate, currentDate))
                .orElse(90L);
        return String.valueOf(daysSinceLastOverdue);
    }

    private String countPdzAmountIn90Days(LocalDate currentDate) {
        LocalDate from = currentDate.minusDays(RANGE_90_DAYS).plusDays(1);
        long pdzAmountIn90Days = pdzBuffer.streamWithinRange(from, currentDate)
                .filter(TRUE::equals)
                .count();
        return String.valueOf(pdzAmountIn90Days);
    }

    private String isAnyPdzOverdue90Days(LocalDate currentDate) {
        LocalDate from = currentDate.minusDays(RANGE_21_DAYS).plusDays(1);
        boolean anyPdzOverdue90Days = pdzBuffer.streamWithinRange(from, currentDate)
                .anyMatch(TRUE::equals);
        return anyPdzOverdue90Days ? "1" : "0";
    }

    private String countPercentFor21Days(boolean hasOverdueDebt, LocalDate currentDate) {
        double percent = hasOverdueDebt ? shareIn21Days(currentDate) : 0;
        return String.valueOf(percent);
    }

    private Double shareIn21Days(LocalDate date) {
        LocalDate from = date.minusDays(RANGE_21_DAYS).plusDays(1);
        double overdueAmount = pdzBuffer.streamWithinRange(from, date)
                .filter(TRUE::equals)
                .count();
        long eventsAmount = pdzBuffer.streamWithinRange(from, date)
                .count();
        return eventsAmount == 0 ? 0 : overdueAmount / eventsAmount;
    }
}
