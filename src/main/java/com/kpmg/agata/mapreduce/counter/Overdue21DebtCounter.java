package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;

import java.time.LocalDate;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.PDZ_21;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_21_DAYS;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.DESC;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

/**
 * If all CleanWeeklyStatusPDZFlatModel by past 21 days has sustainable overdue debt, then "pdz_21" report is 1.
 * Sustainable overdue debt means significant at least 5 days overdue debt.
 * If any of them is hasn't overdue debt, then "pdz_21" report is 0.
 */
public class Overdue21DebtCounter implements EventLogCounter {
    private final WindowBuffer<DebtModel> buffer = new WindowBuffer<>(RANGE_21_DAYS, ASC);
    private final OverdueDebtHandler handler = new OverdueDebtHandler();

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (!(model instanceof CleanWeeklyStatusPDZFlatModel)) return emptyMap();

        CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
        LocalDate currentDate = pdz.getEventDate().toLocalDate();

        boolean hasOverdueDebt = handler.handleOverdueDebt(pdz);
        boolean hasOverdue5To30Debt = handler.handleOverdueDebt(pdz,
                CleanWeeklyStatusPDZFlatModel::getOverdueDebtBetween5and30Days);
        boolean hasOverdue30Debt = handler.handleOverdueDebt(pdz,
                CleanWeeklyStatusPDZFlatModel::getOverdueDebtMore30Days);
        buffer.put(currentDate, new DebtModel(hasOverdueDebt, hasOverdue5To30Debt, hasOverdue30Debt));

        LocalDate from = currentDate.minusDays(RANGE_21_DAYS - 1L);

        boolean hasPdz21 = lastPdzMore30DaysDebt(from, currentDate)
                || sustainableDebt(from, currentDate);
        return singletonMap(PDZ_21, hasPdz21 ? "1" : "0");
    }

    private boolean lastPdzMore30DaysDebt(LocalDate from, LocalDate to) {
        return buffer.streamWithinRange(from, to, DESC)
                .map(DebtModel::getOverdueFrom30Debt)
                .findFirst()
                .orElse(false);
    }

    private boolean sustainableDebt(LocalDate from, LocalDate to) {
        return threeLastPdzOverdueDebt(from, to)
                && preLastPdzFrom5DaysToEndlessDebt(from, to)
                && lastPdzFrom5DaysToEndlessDebt(from, to);
    }

    private boolean threeLastPdzOverdueDebt(LocalDate from, LocalDate to) {
        return buffer.streamWithinRange(from, to, DESC)
                .map(DebtModel::getOverdueDebt)
                .limit(3)
                .allMatch(TRUE::equals);
    }

    private boolean preLastPdzFrom5DaysToEndlessDebt(LocalDate from, LocalDate to) {
        boolean first = buffer.streamWithinRange(from, to, DESC)
                .map(DebtModel::getOverdue5To30Debt)
                .skip(1)
                .findFirst()
                .orElse(false);

        boolean second = buffer.streamWithinRange(from, to, DESC)
                .map(DebtModel::getOverdueFrom30Debt)
                .skip(1)
                .findFirst()
                .orElse(false);
        return first || second;
    }

    private boolean lastPdzFrom5DaysToEndlessDebt(LocalDate from, LocalDate to) {
        boolean first = buffer.streamWithinRange(from, to, DESC)
                .map(DebtModel::getOverdue5To30Debt)
                .findFirst()
                .orElse(false);

        boolean second = buffer.streamWithinRange(from, to, DESC)
                .map(DebtModel::getOverdueFrom30Debt)
                .findFirst()
                .orElse(false);
        return first || second;
    }
}
