package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;

import java.time.LocalDate;
import java.util.function.Function;

import static java.lang.Boolean.TRUE;

// squid:S4276 - Functional Interfaces should be as specialised as possible (Supplier<Double> -> DoubleSupplier)
@SuppressWarnings("squid:S4276")
public class OverdueDebtHandler {
    private static final double SIGNIFICANT_OVERDUE_DEBT = 1000.0;
    private LocalDate blameBankDate = null;

    public boolean handleOverdueDebt(CleanWeeklyStatusPDZFlatModel pdz) {
        return handleOverdueDebt(pdz, CleanWeeklyStatusPDZFlatModel::getOverdueDebt);
    }

    public boolean handleOverdueDebt(CleanWeeklyStatusPDZFlatModel pdz,
                                     Function<CleanWeeklyStatusPDZFlatModel, Double> debtGetter) {
        if (significantDebt(pdz, debtGetter)) {
            boolean result = !skipBecauseOfBanks(pdz);
            if (blameBankDate == null) {
                blameBankDate = pdz.getEventDate().toLocalDate();
            }
            return result;
        }
        return false;
    }

    private boolean significantDebt(CleanWeeklyStatusPDZFlatModel pdz,
                                    Function<CleanWeeklyStatusPDZFlatModel, Double> debtGetter) {
        return debtGetter.apply(pdz) != null && debtGetter.apply(pdz) >= SIGNIFICANT_OVERDUE_DEBT;
    }

    private boolean skipBecauseOfBanks(CleanWeeklyStatusPDZFlatModel pdz) {
        return TRUE.equals(pdz.getIs_pdz_because_of_banks())
                && (blameBankDate == null || blameBankDate.equals(pdz.getEventDate().toLocalDate()));
    }
}
