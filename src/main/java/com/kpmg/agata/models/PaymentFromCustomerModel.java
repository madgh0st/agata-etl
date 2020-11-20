package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentFromCustomerModel extends AbstractClientDataModel {
    public PaymentFromCustomerModel() {
    }

    @JsonAlias({"ДатаОплаты"})
    private String paymentDate;

    @JsonAlias({"Номер"})
    private String number;

    @JsonAlias({"ВидОперации", "ХозяйственнаяОперация"})
    private String operationType;

    @JsonAlias({"СуммаДокумента"})
    private String amount;

    @JsonAlias({"ВалютаДокумента", "Валюта"})
    private String currency;

    @JsonAlias({"СчетОрганизации", "БанковскийСчет"})
    private String organisationBankingAccount;

    @JsonAlias({"Организация"})
    private String organisation;

    @JsonAlias({"НомерВходящегоДокумента"})
    private String incomingDocNumber;

    @JsonAlias({"ДатаВходящегоДокумента"})
    private String incomingDocDate;

    @JsonAlias({"СтатьяДвиженияДенежныхСредств", "бит_СтатьяДвиженияДенежныхСредств"})
    private String cashFlowLine;

    @JsonAlias({"Оплачено"})
    private String isPayed;

    @JsonAlias({"КонтрагентКод","Контрагент1"})
    private String counterpartyCode;

    @JsonAlias({"ДоговорКонтрагентаКод"})
    private String contractCode;

    @JsonAlias({"Комментарий"})
    private String comment;

    @JsonAlias({"ДокументОснование"})
    private String baseDoc;

    @JsonAlias({"НазначениеПлатежа"})
    private String paymentReference;

    @JsonAlias({"ДокументОснование1"})
    private String baseDoc2;

    @JsonAlias({"Организация1"})
    private String organisation1;

    @JsonAlias({"Проведен"})
    private String posted;

    @JsonAlias({"ПометкаУдаления"})
    private String deleted;

    @JsonAlias({"ЛокальнаяСДДС"})
    private String cashFlowLineLocal;

    @JsonAlias({"Контрагент"})
    private String counterparty;

    @JsonAlias({"ИдентификаторПлатежа"})
    private String paymentID;

    @JsonAlias({"бит_НомерДоговора"})
    private String contractNumber;

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrganisationBankingAccount() {
        return organisationBankingAccount;
    }

    public void setOrganisationBankingAccount(String organisationBankingAccount) {
        this.organisationBankingAccount = organisationBankingAccount;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getIncomingDocNumber() {
        return incomingDocNumber;
    }

    public void setIncomingDocNumber(String incomingDocNumber) {
        this.incomingDocNumber = incomingDocNumber;
    }

    public String getIncomingDocDate() {
        return incomingDocDate;
    }

    public void setIncomingDocDate(String incomingDocDate) {
        this.incomingDocDate = incomingDocDate;
    }

    public String getCashFlowLine() {
        return cashFlowLine;
    }

    public void setCashFlowLine(String cashFlowLine) {
        this.cashFlowLine = cashFlowLine;
    }

    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    public String getCounterpartyCode() {
        return counterpartyCode;
    }

    public void setCounterpartyCode(String counterpartyCode) {
        this.counterpartyCode = counterpartyCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBaseDoc() {
        return baseDoc;
    }

    public void setBaseDoc(String baseDoc) {
        this.baseDoc = baseDoc;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getBaseDoc2() {
        return baseDoc2;
    }

    public void setBaseDoc2(String baseDoc2) {
        this.baseDoc2 = baseDoc2;
    }

    public String getOrganisation1() {
        return organisation1;
    }

    public void setOrganisation1(String organisation1) {
        this.organisation1 = organisation1;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getCashFlowLineLocal() {
        return cashFlowLineLocal;
    }

    public void setCashFlowLineLocal(String cashFlowLineLocal) {
        this.cashFlowLineLocal = cashFlowLineLocal;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
}
