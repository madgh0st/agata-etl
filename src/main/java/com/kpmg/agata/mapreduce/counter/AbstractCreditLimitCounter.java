package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanExpressAnalysisModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.utils.Utils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;

/**
 * When CreditLimitCounter consumes pdz, it takes data from last credit limit and counts 3 reports.
 * cl_limit_utilization - pdz total debt / last credit limit in rub.
 * cl_overdue_limit_utilization - pdz overdue debt / last credit limit in rub.
 * cl_has_effective_limit - is pdz inside one of all last credit limit's (approvalDate, expirationDate].
 * For cl_has_effective_limit this class handles only intervals when pdz comes before cl.
 * For "after" cases use ReverseCreditLimitCounter.
 */
public abstract class AbstractCreditLimitCounter implements EventLogCounter {

    private static final Logger log = LoggerFactory.getLogger(AbstractCreditLimitCounter.class);

    protected final List<Interval> intervals = new ArrayList<>();
    private final OverdueDebtHandler handler = new OverdueDebtHandler();

    private Double actualClRub = null;
    private CleanExpressAnalysisModel actualEa = null;

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (model instanceof CleanCreditLimitModel) {
            CleanCreditLimitModel cl = (CleanCreditLimitModel) model;
            return reportByCreditLimit(cl);
        }

        if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
            return reportByPdz(pdz);
        }

        if (model instanceof CleanExpressAnalysisModel) {
            return reportByExpressAnalysis((CleanExpressAnalysisModel) model);
        }

        return emptyMap();
    }

    protected Map<String, String> reportByCreditLimit(CleanCreditLimitModel creditLimit) {
        if (creditLimit.getApprovalDate() != null && creditLimit.getExpirationDate() != null) {
            LocalDate from = creditLimit.getApprovalDate().toLocalDate();
            LocalDate to = creditLimit.getExpirationDate().toLocalDate();
            if (from.isAfter(to)) {
                log.warn("\"From\" can't be after \"to\". Skipping: begin {}, end {} creditLimit {}",
                        from, to, creditLimit);
            } else {
                intervals.add(new Interval(from, to));
            }
        }

        actualClRub = creditLimit.getLimitInRub();

        return emptyMap();
    }

    protected Map<String, String> reportByPdz(CleanWeeklyStatusPDZFlatModel pdz) {
        Map<String, String> reports = new HashMap<>();

        LocalDate currentDate = pdz.getEventDate().toLocalDate();
        boolean hasLimit = intervals.stream().anyMatch(interval -> interval.isAround(currentDate));
        reports.put(CounterConstants.CL_HAS_EFF_LIMIT, hasLimit ? "1" : "0");
        reports.put(CounterConstants.CL_LIMIT_IN_RUB, Utils.toStringOrNull(actualClRub));

        if (actualClRub == null || actualClRub == 0) {
            return reports;
        }

        if (pdz.getTotalDebtWithoutReserve() != null) {
            double totalDebt = pdz.getTotalDebtWithoutReserve();
            double debtUtil = totalDebt / actualClRub;
            reports.put(CounterConstants.CL_LIMIT_UTIL, Double.toString(debtUtil));
        }

        if (handler.handleOverdueDebt(pdz)) {
            double overdueDebt = pdz.getOverdueDebt();
            double overdueUtil = overdueDebt / actualClRub;
            reports.put(CounterConstants.CL_OVERDUE_LIMIT_UTIL, Double.toString(overdueUtil));
        }

        if (actualEa != null && actualEa.getCredit_limit() != null) {
            double eaRatio = Double.parseDouble(actualEa.getCredit_limit()) / actualClRub;
            reports.put(CounterConstants.CL_EA_RATIO, Double.toString(eaRatio));
        }

        return reports;
    }

    private Map<String, String> reportByExpressAnalysis(CleanExpressAnalysisModel ea) {
        actualEa = ExpressAnalysisCounter.selectActualExpressAnalysis(actualEa, ea);
        return emptyMap();
    }

    protected static class Interval {

        private final LocalDate begin;
        private final LocalDate end;

        /**
         * Become arguments as [begin; end) interval
         * Store as (begin-1; end)
         */
        private Interval(LocalDate begin, LocalDate end) {
            if (begin.isAfter(end)) {
                String msg = format("\"Begin\" can't be after \"end\": begin %s, end %s", begin, end);
                throw new IllegalArgumentException(msg);
            }

            this.end = end;
            this.begin = begin.minusDays(1);
        }

        protected boolean isAround(LocalDate date) {
            return begin.isBefore(date) && date.isBefore(end);
        }
    }
}
