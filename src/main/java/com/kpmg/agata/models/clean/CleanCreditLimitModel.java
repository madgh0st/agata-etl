package com.kpmg.agata.models.clean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.Objects;


public class CleanCreditLimitModel extends AbstractCleanDataModel {

    private String counterpartyName;
    private String counterpartyInn;
    private Double limitInCurrency;
    //enum
    private String limitCurrency;
    private Double limitInRub;
    private Integer paymentDefermentDays;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+3")
    private Date approvalDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+3")
    private Date expirationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+3")
    private Date reportDate;

    public CleanCreditLimitModel() {
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

    public Double getLimitInCurrency() {
        return limitInCurrency;
    }

    public void setLimitInCurrency(Double limitInCurrency) {
        this.limitInCurrency = limitInCurrency;
    }

    public String getLimitCurrency() {
        return limitCurrency;
    }

    public void setLimitCurrency(String limitCurrency) {
        this.limitCurrency = limitCurrency;
    }

    public Double getLimitInRub() {
        return limitInRub;
    }

    public void setLimitInRub(Double limitInRub) {
        this.limitInRub = limitInRub;
    }

    public Integer getPaymentDefermentDays() {
        return paymentDefermentDays;
    }

    public void setPaymentDefermentDays(Integer paymentDefermentDays) {
        this.paymentDefermentDays = paymentDefermentDays;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanCreditLimitModel that = (CleanCreditLimitModel) o;
        return Objects.equals(counterpartyName, that.counterpartyName) &&
                Objects.equals(counterpartyInn, that.counterpartyInn) &&
                Objects.equals(limitInCurrency, that.limitInCurrency) &&
                Objects.equals(limitCurrency, that.limitCurrency) &&
                Objects.equals(limitInRub, that.limitInRub) &&
                Objects.equals(paymentDefermentDays, that.paymentDefermentDays) &&
                Objects.equals(approvalDate, that.approvalDate) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(reportDate, that.reportDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counterpartyName, counterpartyInn, limitInCurrency, limitCurrency, limitInRub,
                paymentDefermentDays, approvalDate, expirationDate, reportDate);
    }

    @Override
    public String toString() {
        return "CleanCreditLimitModel{" +
                "counterpartyName='" + counterpartyName + '\'' +
                ", counterpartyInn='" + counterpartyInn + '\'' +
                ", limitInCurrency=" + limitInCurrency +
                ", limitCurrency='" + limitCurrency + '\'' +
                ", limitInRub=" + limitInRub +
                ", paymentDefermentDays=" + paymentDefermentDays +
                ", approvalDate=" + approvalDate +
                ", expirationDate=" + expirationDate +
                ", reportDate=" + reportDate +
                '}';
    }
}
