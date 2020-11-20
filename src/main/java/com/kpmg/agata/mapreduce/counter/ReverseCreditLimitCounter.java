package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;

import java.time.LocalDate;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.CL_HAS_EFF_LIMIT;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

/**
 * Works in addition for CreditLimitCounter,
 * but for cases, when pdz comes after credit limit (reverse order)
 */
public class ReverseCreditLimitCounter extends AbstractCreditLimitCounter {

    @Override
    protected Map<String, String> reportByPdz(CleanWeeklyStatusPDZFlatModel pdz) {
        if ("1".equals(pdz.getReports().get(CL_HAS_EFF_LIMIT))) return emptyMap();

        LocalDate currentDate = pdz.getEventDate().toLocalDate();
        boolean hasLimit = intervals.stream().anyMatch(interval -> interval.isAround(currentDate));

        return singletonMap(CL_HAS_EFF_LIMIT, hasLimit ? "1" : "0");
    }
}
