package com.kpmg.agata.models.clean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.Objects;

public class CleanPaymentFromCustomerModel extends AbstractCleanDataModel {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+3")
    private Date paymentDate;
    //enum
    private String operationType;
    private Double amount;
    //enum
    private String currency;
    //enum
    private String cashFlowLine;
    private Boolean isPayed;
    private String contractCode;
    private String contractNumber;
    private Boolean posted;
    private Boolean deleted;
    private String counterpartyName;

    public CleanPaymentFromCustomerModel() {
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
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

    public String getCashFlowLine() {
        return cashFlowLine;
    }

    public void setCashFlowLine(String cashFlowLine) {
        this.cashFlowLine = cashFlowLine;
    }

    public Boolean getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(Boolean payed) {
        isPayed = payed;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
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

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanPaymentFromCustomerModel that = (CleanPaymentFromCustomerModel) o;
        return Objects.equals(paymentDate, that.paymentDate) &&
                Objects.equals(operationType, that.operationType) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(cashFlowLine, that.cashFlowLine) &&
                Objects.equals(isPayed, that.isPayed) &&
                Objects.equals(contractCode, that.contractCode) &&
                Objects.equals(contractNumber, that.contractNumber) &&
                Objects.equals(posted, that.posted) &&
                Objects.equals(deleted, that.deleted) &&
                Objects.equals(counterpartyName, that.counterpartyName);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(paymentDate, operationType, amount, currency, cashFlowLine, isPayed, contractCode, contractNumber,
                        posted, deleted, counterpartyName);
    }

    @Override
    public String toString() {
        return "CleanPaymentFromCustomerModel{" +
                "paymentDate=" + paymentDate +
                ", operationType='" + operationType + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", cashFlowLine='" + cashFlowLine + '\'' +
                ", isPayed=" + isPayed +
                ", contractCode='" + contractCode + '\'' +
                ", contractNumber='" + contractNumber + '\'' +
                ", posted=" + posted +
                ", deleted=" + deleted +
                ", counterpartyName='" + counterpartyName + '\'' +
                '}';
    }
}
