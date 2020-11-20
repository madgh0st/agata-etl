package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanCounterpartyContractModel;
import com.kpmg.agata.models.clean.CleanRealizationServicesModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.CONTRACT_DAYS_FROM_EXPECTED_PAYMENT;

public class CounterpartyContractsCounter implements EventLogCounter {

    private Integer numDaysFromRealizationToPayment;
    private LocalDate expectedPaymentDate;

    @Override
    public Map<String, String> process(AbstractCleanDataModel model) {

        // Remember paymentTerm as max number of days from realization to expected payment
        if (model instanceof CleanCounterpartyContractModel) {
            CleanCounterpartyContractModel contract = (CleanCounterpartyContractModel) model;
            this.numDaysFromRealizationToPayment = contract.getPaymentTerm();
            return Collections.emptyMap();
        }

        // Calculate current expected payment date if we already seen contract
        if (model instanceof CleanRealizationServicesModel) {
            if (numDaysFromRealizationToPayment != null) {
                this.expectedPaymentDate =
                        model.getEventDate().toLocalDate().plusDays(numDaysFromRealizationToPayment);
            }
            return Collections.emptyMap();
        }

        if (model instanceof CleanWeeklyStatusPDZFlatModel) {
            CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
            return processPdzReport(pdz);
        }
        return Collections.emptyMap();
    }


    /**
     * Calculate number of days from current date (date of PDZ report) and expected payment date
     */
    private Map<String, String> processPdzReport(CleanWeeklyStatusPDZFlatModel pdz) {
        Long daysFromExpectedPayment = null;
        Map<String, String> reports = new HashMap<>();

        if (expectedPaymentDate != null) {
            daysFromExpectedPayment = ChronoUnit.DAYS.between(pdz.getEventDate().toLocalDate(), expectedPaymentDate);
        }

        if (daysFromExpectedPayment != null) {
            if (Math.abs(daysFromExpectedPayment) <= 90) {
                reports.put(CONTRACT_DAYS_FROM_EXPECTED_PAYMENT, String.valueOf(daysFromExpectedPayment));
            }
        }
        return reports;
    }

}
