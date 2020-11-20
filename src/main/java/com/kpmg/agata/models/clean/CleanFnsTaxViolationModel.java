package com.kpmg.agata.models.clean;

import java.util.Objects;

public class CleanFnsTaxViolationModel extends AbstractCleanDataModel {

    private String counterpartyName;

    private String counterpartyInn;

    private String fine;

    public CleanFnsTaxViolationModel() {
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyInn() {
        return counterpartyInn;
    }

    public void setCounterpartyInn(String counterpartyInn) {
        this.counterpartyInn = counterpartyInn;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanFnsTaxViolationModel that = (CleanFnsTaxViolationModel) o;
        return Objects.equals(counterpartyName, that.counterpartyName) &&
                Objects.equals(counterpartyInn, that.counterpartyInn) &&
                Objects.equals(fine, that.fine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counterpartyName, counterpartyInn, fine);
    }
}
