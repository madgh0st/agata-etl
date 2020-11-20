package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.utils.Utils;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_PAY_DEFER_AVG_IN_90_DAYS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_90_DAYS;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;

public class CreditLimitCounter extends AbstractCreditLimitCounter {

    private final WindowBuffer<List<Integer>> defermentPayBuffer = new WindowBuffer<>(RANGE_90_DAYS, ASC);

    @Override
    protected Map<String, String> reportByCreditLimit(CleanCreditLimitModel creditLimit) {
        super.reportByCreditLimit(creditLimit);

        if (creditLimit.getPaymentDefermentDays() != null) {
            LocalDate currentDate = creditLimit.getEventDate().toLocalDate();
            List<Integer> value = singletonList(creditLimit.getPaymentDefermentDays());
            defermentPayBuffer.put(currentDate, value, Utils::concatLists);
        }

        return emptyMap();
    }

    @Override
    protected Map<String, String> reportByPdz(CleanWeeklyStatusPDZFlatModel pdz) {
        Map<String, String> reports = super.reportByPdz(pdz);

        LocalDate currentDate = pdz.getEventDate().toLocalDate();
        addDefermentPayAverageIn90Days(reports, currentDate);

        return reports;
    }

    private void addDefermentPayAverageIn90Days(Map<String, String> reports, LocalDate currentDate) {
        LocalDate from = currentDate.minusDays(RANGE_90_DAYS).plusDays(1);
        OptionalDouble average = defermentPayBuffer.streamWithinRange(from, currentDate)
                                                   .flatMap(List::stream)
                                                   .mapToInt(i -> i)
                                                   .average();
        if (average.isPresent()) {
            String stringAverage = Double.toString(Utils.round(average.getAsDouble(), 3));
            reports.put(CL_PAY_DEFER_AVG_IN_90_DAYS, stringAverage);
        }
    }
}
