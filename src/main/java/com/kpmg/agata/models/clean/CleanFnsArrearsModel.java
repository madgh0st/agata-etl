package com.kpmg.agata.models.clean;

import java.util.Objects;

public class CleanFnsArrearsModel extends AbstractCleanDataModel {

    private String counterpartyName;

    private String counterpartyInn;

    private String taxName;

    private String taxArrears;

    private String penalties;

    private String arrearsFine;

    private String totalArrears;

    public CleanFnsArrearsModel() {
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

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxArrears() {
        return taxArrears;
    }

    public void setTaxArrears(String taxArrears) {
        this.taxArrears = taxArrears;
    }

    public String getPenalties() {
        return penalties;
    }

    public void setPenalties(String penalties) {
        this.penalties = penalties;
    }

    public String getArrearsFine() {
        return arrearsFine;
    }

    public void setArrearsFine(String arrearsFine) {
        this.arrearsFine = arrearsFine;
    }

    public String getTotalArrears() {
        return totalArrears;
    }

    public void setTotalArrears(String totalArrears) {
        this.totalArrears = totalArrears;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanFnsArrearsModel that = (CleanFnsArrearsModel) o;
        return Objects.equals(counterpartyName, that.counterpartyName) &&
                Objects.equals(counterpartyInn, that.counterpartyInn) &&
                Objects.equals(taxName, that.taxName) &&
                Objects.equals(taxArrears, that.taxArrears) &&
                Objects.equals(penalties, that.penalties) &&
                Objects.equals(arrearsFine, that.arrearsFine) &&
                Objects.equals(totalArrears, that.totalArrears);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(counterpartyName, counterpartyInn, taxName, taxArrears, penalties, arrearsFine, totalArrears);
    }
}
