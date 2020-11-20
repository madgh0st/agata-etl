package com.kpmg.agata.models.clean;

import java.util.Map;
import java.util.Objects;

public class CleanWeeklyStatusPDZFlatModel extends AbstractCleanDataModel {

    //Boolean
    private String fromWeeklyStatus;
    private String counterpartyName;
    private String counterpartyINN;
    //Date
    private String onDate;
    private Double totalDebtWithoutReserve;
    private Double currentDebt;
    private Double overdueDebt;
    private Double overdueDebtLess5Days;
    private Double overdueDebtBetween5and30Days;
    private Double overdueDebtMore30Days;
    private Boolean is_pdz_because_of_banks;
    private Boolean is_valid;
    private String comments;
    private String seasonality;

    private Map<String, String> reports;

    public String getFromWeeklyStatus() {
        return fromWeeklyStatus;
    }

    public void setFromWeeklyStatus(String fromWeeklyStatus) {
        this.fromWeeklyStatus = fromWeeklyStatus;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyINN() {
        return counterpartyINN;
    }

    public void setCounterpartyINN(String counterpartyINN) {
        this.counterpartyINN = counterpartyINN;
    }

    public String getOnDate() {
        return onDate;
    }

    public void setOnDate(String onDate) {
        this.onDate = onDate;
    }

    public Double getTotalDebtWithoutReserve() {
        return totalDebtWithoutReserve;
    }

    public void setTotalDebtWithoutReserve(Double totalDebtWithoutReserve) {
        this.totalDebtWithoutReserve = totalDebtWithoutReserve;
    }

    public Double getCurrentDebt() {
        return currentDebt;
    }

    public void setCurrentDebt(Double currentDebt) {
        this.currentDebt = currentDebt;
    }

    public Double getOverdueDebt() {
        return overdueDebt;
    }

    public void setOverdueDebt(Double overdueDebt) {
        this.overdueDebt = overdueDebt;
    }

    public Double getOverdueDebtLess5Days() {
        return overdueDebtLess5Days;
    }

    public void setOverdueDebtLess5Days(Double overdueDebtLess5Days) {
        this.overdueDebtLess5Days = overdueDebtLess5Days;
    }

    public Double getOverdueDebtBetween5and30Days() {
        return overdueDebtBetween5and30Days;
    }

    public void setOverdueDebtBetween5and30Days(Double overdueDebtBetween5and30Days) {
        this.overdueDebtBetween5and30Days = overdueDebtBetween5and30Days;
    }

    public Double getOverdueDebtMore30Days() {
        return overdueDebtMore30Days;
    }

    public void setOverdueDebtMore30Days(Double overdueDebtMore30Days) {
        this.overdueDebtMore30Days = overdueDebtMore30Days;
    }

    public Boolean getIs_pdz_because_of_banks() {
        return is_pdz_because_of_banks;
    }

    public void setIs_pdz_because_of_banks(Boolean is_pdz_because_of_banks) {
        this.is_pdz_because_of_banks = is_pdz_because_of_banks;
    }

    public Boolean getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(Boolean is_valid) {
        this.is_valid = is_valid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSeasonality() {
        return seasonality;
    }

    public void setSeasonality(String seasonality) {
        this.seasonality = seasonality;
    }

    public Map<String, String> getReports() {
        return reports;
    }

    public void setReports(Map<String, String> reports) {
        this.reports = reports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanWeeklyStatusPDZFlatModel that = (CleanWeeklyStatusPDZFlatModel) o;
        return Objects.equals(fromWeeklyStatus, that.fromWeeklyStatus) &&
                Objects.equals(counterpartyName, that.counterpartyName) &&
                Objects.equals(counterpartyINN, that.counterpartyINN) &&
                Objects.equals(onDate, that.onDate) &&
                Objects.equals(totalDebtWithoutReserve, that.totalDebtWithoutReserve) &&
                Objects.equals(currentDebt, that.currentDebt) &&
                Objects.equals(overdueDebt, that.overdueDebt) &&
                Objects.equals(overdueDebtLess5Days, that.overdueDebtLess5Days) &&
                Objects.equals(overdueDebtBetween5and30Days, that.overdueDebtBetween5and30Days) &&
                Objects.equals(overdueDebtMore30Days, that.overdueDebtMore30Days) &&
                Objects.equals(is_pdz_because_of_banks, that.is_pdz_because_of_banks) &&
                Objects.equals(is_valid, that.is_valid) &&
                Objects.equals(comments, that.comments) &&
                Objects.equals(seasonality, that.seasonality) &&
                Objects.equals(reports, that.reports);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(fromWeeklyStatus, counterpartyName, counterpartyINN, onDate, totalDebtWithoutReserve, currentDebt,
                        overdueDebt, overdueDebtLess5Days, overdueDebtBetween5and30Days, overdueDebtMore30Days,
                        is_pdz_because_of_banks, is_valid, comments, seasonality, reports);
    }

    @Override
    public String toString() {
        return "CleanWeeklyStatusPDZFlatModel{" +
                "fromWeeklyStatus='" + fromWeeklyStatus + '\'' +
                ", counterpartyName='" + counterpartyName + '\'' +
                ", counterpartyINN='" + counterpartyINN + '\'' +
                ", onDate='" + onDate + '\'' +
                ", totalDebtWithoutReserve=" + totalDebtWithoutReserve +
                ", currentDebt=" + currentDebt +
                ", overdueDebt=" + overdueDebt +
                ", overdueDebtLess5Days=" + overdueDebtLess5Days +
                ", overdueDebtBetween5and30Days=" + overdueDebtBetween5and30Days +
                ", overdueDebtMore30Days=" + overdueDebtMore30Days +
                ", is_pdz_because_of_banks=" + is_pdz_because_of_banks +
                ", is_valid=" + is_valid +
                ", comments='" + comments + '\'' +
                ", seasonality='" + seasonality + '\'' +
                ", reports=" + reports +
                "} " + super.toString();
    }
}
