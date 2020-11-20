package com.kpmg.agata.models.clean;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Date;
import java.util.Objects;

public class CleanLegalCasesModel extends AbstractCleanDataModel {

    private String counterpartyName;
    //Long
    private String counterpartyInn;
    private String caseNumber;
    //enum
    private String category;
    // enum
    private String status;
    //Long nullable
    private String outcome;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+3")
    private Date claimDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+3")
    private Date outcomeDate;
    private Double claimAmount;
    private Double outcomeAmount;
    private String claimCharge;
    private String dictum;

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

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public Date getOutcomeDate() {
        return outcomeDate;
    }

    public void setOutcomeDate(Date outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public Double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(Double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public Double getOutcomeAmount() {
        return outcomeAmount;
    }

    public void setOutcomeAmount(Double outcomeAmount) {
        this.outcomeAmount = outcomeAmount;
    }

    public String getClaimCharge() {
        return claimCharge;
    }

    public void setClaimCharge(String claimCharge) {
        this.claimCharge = claimCharge;
    }

    public String getDictum() {
        return dictum;
    }

    public void setDictum(String dictum) {
        this.dictum = dictum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanLegalCasesModel that = (CleanLegalCasesModel) o;
        return Objects.equals(counterpartyName, that.counterpartyName) &&
                Objects.equals(counterpartyInn, that.counterpartyInn) &&
                Objects.equals(caseNumber, that.caseNumber) &&
                Objects.equals(category, that.category) &&
                Objects.equals(status, that.status) &&
                Objects.equals(outcome, that.outcome) &&
                Objects.equals(claimDate, that.claimDate) &&
                Objects.equals(outcomeDate, that.outcomeDate) &&
                Objects.equals(claimAmount, that.claimAmount) &&
                Objects.equals(outcomeAmount, that.outcomeAmount) &&
                Objects.equals(claimCharge, that.claimCharge) &&
                Objects.equals(dictum, that.dictum);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(counterpartyName, counterpartyInn, caseNumber, category, status, outcome, claimDate, outcomeDate,
                        claimAmount, outcomeAmount, claimCharge, dictum);
    }

    @Override
    public String toString() {
        return "CleanLegalCasesModel{" +
                "counterpartyName='" + counterpartyName + '\'' +
                ", counterpartyInn='" + counterpartyInn + '\'' +
                ", caseNumber='" + caseNumber + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", outcome='" + outcome + '\'' +
                ", claimDate=" + claimDate +
                ", outcomeDate=" + outcomeDate +
                ", claimAmount=" + claimAmount +
                ", outcomeAmount=" + outcomeAmount +
                ", claimCharge='" + claimCharge + '\'' +
                ", dictum='" + dictum + '\'' +
                "} " + super.toString();
    }
}
