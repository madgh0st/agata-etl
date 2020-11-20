package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"fileName", "sheetName", "dateModified",
        "block", "doName", "segment", "counterpartyName", "counterpartyInn", "limitInCurrency",
        "limitCurrency", "limitInRub", "paymentDefermentDays", "approvalAuthority",
        "approvalDocumentDetails", "approvalDate", "expirationDate", "reportDate",
        "collateral", "collateralComment"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditLimitModel extends AbstractClientDataModel {
    public CreditLimitModel() {
    }

    @JsonAlias({"Блок"})
    private String block;

    @JsonAlias({"ДО"})
    private String doName;

    @JsonAlias({"Сегмент (только для КД ГПН)", "Сегмент"})
    private String segment;

    @JsonAlias({"Контрагент"})
    private String counterpartyName;

    @JsonAlias({"ИНН контрагента"})
    private String counterpartyInn;

    @JsonAlias({" Утвержденный кредитный лимит, валюта ", "Утвержденный кредитный лимит, валюта"})
    private String limitInCurrency;

    @JsonAlias({"Валюта лимита"})
    private String limitCurrency;

    @JsonAlias({" Утвержденный кредитный лимит, руб. ", "Утвержденный кредитный лимит, руб."})
    private String limitInRub;

    @JsonAlias({"Утвержденная отсрочка платежа, календарные дни"})
    private String paymentDefermentDays;

    @JsonAlias({"Коллегиальный орган, утвердивший кредитный лимит"})
    private String approvalAuthority;

    @JsonAlias({"Реквизиты документа, согласно которому утвержден кредитный лимит"})
    private String approvalDocumentDetails;

    @JsonAlias({"Дата утверждения кредитного лимита"})
    private String approvalDate;

    @JsonAlias({"Дата окончания действия кредитного лимита"})
    private String expirationDate;

    @JsonAlias({"Дата отчета"})
    private String reportDate;

    @JsonAlias({"Описание обеспечения"})
    private String collateral;

    @JsonAlias({"Комментарии по обеспечению"})
    private String collateralComment;

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getDoName() {
        return doName;
    }

    public void setDoName(String doName) {
        this.doName = doName;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
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

    public String getLimitInCurrency() {
        return limitInCurrency;
    }

    public void setLimitInCurrency(String limitInCurrency) {
        this.limitInCurrency = limitInCurrency;
    }

    public String getLimitCurrency() {
        return limitCurrency;
    }

    public void setLimitCurrency(String limitCurrency) {
        this.limitCurrency = limitCurrency;
    }

    public String getLimitInRub() {
        return limitInRub;
    }

    public void setLimitInRub(String limitInRub) {
        this.limitInRub = limitInRub;
    }

    public String getPaymentDefermentDays() {
        return paymentDefermentDays;
    }

    public void setPaymentDefermentDays(String paymentDefermentDays) {
        this.paymentDefermentDays = paymentDefermentDays;
    }

    public String getApprovalAuthority() {
        return approvalAuthority;
    }

    public void setApprovalAuthority(String approvalAuthority) {
        this.approvalAuthority = approvalAuthority;
    }

    public String getApprovalDocumentDetails() {
        return approvalDocumentDetails;
    }

    public void setApprovalDocumentDetails(String approvalDocumentDetails) {
        this.approvalDocumentDetails = approvalDocumentDetails;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getCollateral() {
        return collateral;
    }

    public void setCollateral(String collateral) {
        this.collateral = collateral;
    }

    public String getCollateralComment() {
        return collateralComment;
    }

    public void setCollateralComment(String collateralComment) {
        this.collateralComment = collateralComment;
    }
}
