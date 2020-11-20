package com.kpmg.agata.models.clean;

import java.util.Objects;

public class CleanRealizationServicesModel extends AbstractCleanDataModel {

    //Date
    private String docDate;
    private String counterpartyName;
    private String contractCode;
    private String contract;
    private Double amount;
    //enum
    private String currency;
    private String operationType;
    private Boolean posted;
    private Boolean deleted;

    public CleanRealizationServicesModel() {
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Boolean getPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanRealizationServicesModel that = (CleanRealizationServicesModel) o;
        return Objects.equals(docDate, that.docDate) &&
                Objects.equals(counterpartyName, that.counterpartyName) &&
                Objects.equals(contractCode, that.contractCode) &&
                Objects.equals(contract, that.contract) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(operationType, that.operationType) &&
                Objects.equals(posted, that.posted) &&
                Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docDate, counterpartyName, contractCode, contract, amount, currency, operationType, posted,
                deleted);
    }

    @Override
    public String toString() {
        return "CleanRealizationServicesModel{" +
                "docDate='" + docDate + '\'' +
                ", counterpartyName='" + counterpartyName + '\'' +
                ", contractCode='" + contractCode + '\'' +
                ", contract='" + contract + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", operationType='" + operationType + '\'' +
                ", posted=" + posted +
                ", deleted=" + deleted +
                '}';
    }
}
