package com.kpmg.agata.models;

import java.util.Objects;

public class FnsArrearsModel extends FnsModel {

    private String taxName;
    private String taxArrears;
    private String penalties;
    private String arrearsFine;
    private String totalArrears;

    public String getTaxName() {
        return taxName;
    }

    public String getTaxArrears() {
        return taxArrears;
    }

    public String getPenalties() {
        return penalties;
    }

    public String getArrearsFine() {
        return arrearsFine;
    }

    public String getTotalArrears() {
        return totalArrears;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public void setTaxArrears(String taxArrears) {
        this.taxArrears = taxArrears;
    }

    public void setPenalties(String penalties) {
        this.penalties = penalties;
    }

    public void setArrearsFine(String arrearsFine) {
        this.arrearsFine = arrearsFine;
    }

    public void setTotalArrears(String totalArrears) {
        this.totalArrears = totalArrears;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FnsArrearsModel that = (FnsArrearsModel) o;
        return Objects.equals(taxName, that.taxName) &&
                Objects.equals(taxArrears, that.taxArrears) &&
                Objects.equals(penalties, that.penalties) &&
                Objects.equals(arrearsFine, that.arrearsFine) &&
                Objects.equals(totalArrears, that.totalArrears);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), taxName, taxArrears, penalties, arrearsFine, totalArrears);
    }
}
