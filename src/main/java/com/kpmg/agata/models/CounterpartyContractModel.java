package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"fileName", "sheetName", "dateModified",
        "ownerCode", "erpCode", "number", "name", "date", "inn", "validityPeriod",
        "contractKind", "settlementCurrency", "settlementManagement", "markRemove", "paymentTerm"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterpartyContractModel extends AbstractClientDataModel {
    public CounterpartyContractModel() {
    }

    @JsonAlias({"ВладелецКод"})
    private String ownerCode;

    @JsonAlias({"Код"})
    private String erpCode;

    @JsonAlias({"Номер"})
    private String number;

    @JsonAlias({"Наименование"})
    private String name;

    @JsonAlias({"Дата"})
    private String date;

    @JsonAlias({"ИНН"})
    private String inn;

    @JsonAlias({"СрокДействия"})
    private String validityPeriod;

    @JsonAlias({"ВидДоговора"})
    private String contractKind;

    @JsonAlias({"ВалютаВзаиморасчетов"})
    private String settlementCurrency;

    @JsonAlias({"ВедениеВзаиморасчетов"})
    private String settlementManagement;

    @JsonAlias({"ПометкаУдаления"})
    private String markRemove;

    @JsonAlias({"УсловиеПлатежа"})
    private String paymentTerm;

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getContractKind() {
        return contractKind;
    }

    public void setContractKind(String contractKind) {
        this.contractKind = contractKind;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getSettlementManagement() {
        return settlementManagement;
    }

    public void setSettlementManagement(String settlementManagement) {
        this.settlementManagement = settlementManagement;
    }

    public String getMarkRemove() {
        return markRemove;
    }

    public void setMarkRemove(String markRemove) {
        this.markRemove = markRemove;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }
}
