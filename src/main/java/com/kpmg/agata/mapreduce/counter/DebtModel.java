package com.kpmg.agata.mapreduce.counter;

public class DebtModel {
    private final boolean overdueDebt;
    private final boolean overdue5To30Debt;
    private final boolean overdueFrom30Debt;

    public DebtModel(boolean overdueDebt, boolean overdue5To30Debt, boolean overdueFrom30Debt) {
        this.overdueDebt = overdueDebt;
        this.overdue5To30Debt = overdue5To30Debt;
        this.overdueFrom30Debt = overdueFrom30Debt;
    }

    public boolean getOverdueDebt() {
        return overdueDebt;
    }

    public boolean getOverdue5To30Debt() {
        return overdue5To30Debt;
    }

    public boolean getOverdueFrom30Debt() {
        return overdueFrom30Debt;
    }
}
