package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanPaymentFromCustomerModel;
import com.kpmg.agata.models.clean.CleanRealizationServicesModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;

import com.kpmg.agata.utils.Utils;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_DIFF_90;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_CUR_TO_LAST_MONTH_REAL_RATIO;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_PAY_SUM_30;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_PAY_SUM_60_30;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_PAY_SUM_90;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_PAY_SUM_90_60;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REAL_SUM_30;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REAL_SUM_60_30;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REAL_SUM_90;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REAL_SUM_90_60;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.PR_REL_DIFF_90;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_30_DAYS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_60_DAYS;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.RANGE_90_DAYS;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;
import static java.util.Collections.emptyMap;

public class PaymentsRealizationsCounter implements EventLogCounter {
    private static final BinaryOperator<Double> SUM_REDUCER = Double::sum;

    private Double actualClRub = null;
    private WindowBuffer<Double> paymentsBuffer = new WindowBuffer<>(RANGE_90_DAYS, ASC);
    private WindowBuffer<Double> realizationsBuffer = new WindowBuffer<>(RANGE_90_DAYS, ASC);

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {
        if (model instanceof CleanCreditLimitModel) {
            CleanCreditLimitModel cl = (CleanCreditLimitModel) model;
            return reportByCreditLimit(cl);
        }

        if (model instanceof CleanPaymentFromCustomerModel) {
            CleanPaymentFromCustomerModel payment = (CleanPaymentFromCustomerModel) model;
            return reportByPayment(payment);
        }

        if (model instanceof CleanRealizationServicesModel) {
            CleanRealizationServicesModel realization = (CleanRealizationServicesModel) model;
            return reportByRealizations(realization);
        }

        if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
            return reportByPdz(pdz);
        }

        return emptyMap();
    }

    private Map<String, String> reportByCreditLimit(CleanCreditLimitModel cl) {
        actualClRub = cl.getLimitInRub();
        return emptyMap();
    }

    private Map<String, String> reportByPayment(CleanPaymentFromCustomerModel payment) {
        LocalDate currentDate = payment.getEventDate().toLocalDate();
        if (payment.getAmount() != null) {
            paymentsBuffer.put(currentDate, payment.getAmount(), SUM_REDUCER);
        }
        return emptyMap();
    }

    private Map<String, String> reportByRealizations(CleanRealizationServicesModel realization) {
        LocalDate currentDate = realization.getEventDate().toLocalDate();
        if (realization.getAmount() != null) {
            realizationsBuffer.put(currentDate, realization.getAmount(), SUM_REDUCER);
        }
        return emptyMap();
    }

    private Map<String, String> reportByPdz(CleanWeeklyStatusPDZFlatModel pdz) {
        LocalDate currentDate = pdz.getEventDate().toLocalDate();
        Map<String, String> reports = new HashMap<>();

        double paysIn90 = createRangeReports(currentDate, reports, paymentsBuffer,
                PR_PAY_SUM_90, PR_PAY_SUM_90_60, PR_PAY_SUM_60_30, PR_PAY_SUM_30);

        double realIn90 = createRangeReports(currentDate, reports, realizationsBuffer,
                PR_REAL_SUM_90, PR_REAL_SUM_90_60, PR_REAL_SUM_60_30, PR_REAL_SUM_30);

        double diffIn90 = realIn90 - paysIn90;
        reports.put(PR_DIFF_90, Double.toString(diffIn90));

        if (actualClRub != null && actualClRub != 0) {
            double relIn90 = diffIn90 / actualClRub;
            reports.put(PR_REL_DIFF_90, Double.toString(relIn90));
        }

        addCurrentToLastMonthRealizationRatio(reports, currentDate);

        return reports;
    }

    private double createRangeReports(LocalDate currentDate, Map<String, String> reports, WindowBuffer<Double> buffer,
                                      String sum90Key, String sum9060Key, String sum6030Key, String sum30Key) {
        double realIn90 = sumInRange(buffer, currentDate.minusDays(RANGE_90_DAYS), currentDate);
        reports.put(sum90Key, Double.toString(realIn90));

        double real90To60 = sumInRange(buffer, currentDate.minusDays(RANGE_90_DAYS),
                currentDate.minusDays(RANGE_60_DAYS));
        reports.put(sum9060Key, Double.toString(real90To60));

        double real60To30 = sumInRange(buffer, currentDate.minusDays(RANGE_60_DAYS),
                currentDate.minusDays(RANGE_30_DAYS));
        reports.put(sum6030Key, Double.toString(real60To30));

        double realIn30 = sumInRange(buffer, currentDate.minusDays(RANGE_30_DAYS), currentDate);
        reports.put(sum30Key, Double.toString(realIn30));

        return realIn90;
    }

    // in (from;to] range
    private double sumInRange(WindowBuffer<Double> buffer, LocalDate from, LocalDate to) {
        return buffer.streamWithinRange(from.plusDays(1), to)
                .mapToDouble(i -> i)
                .sum();
    }

    private void addCurrentToLastMonthRealizationRatio(Map<String, String> reports, LocalDate currentDate) {
        LocalDate lastMonthFrom = currentDate.minusDays(RANGE_30_DAYS).minusDays(RANGE_30_DAYS);
        LocalDate lastMonthTo = currentDate.minusDays(RANGE_30_DAYS);
        double lastMonthRealizations = sumInRange(realizationsBuffer, lastMonthFrom, lastMonthTo);

        if(lastMonthRealizations == 0) return;

        LocalDate currentMonthFrom = currentDate.minusDays(RANGE_30_DAYS);
        double currentMonthRealizations = sumInRange(realizationsBuffer, currentMonthFrom, currentDate);

        double ratio = currentMonthRealizations / lastMonthRealizations;
        String stringRatio = Double.toString(Utils.round(ratio, 3));
        reports.put(PR_CUR_TO_LAST_MONTH_REAL_RATIO, stringRatio);
    }
}
