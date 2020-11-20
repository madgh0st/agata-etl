package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditCorrectionModel  extends AbstractClientDataModel {
    public CreditCorrectionModel() {
    }

    @JsonAlias({"КатегорияДокумента"})
    private String documentType;

    @JsonAlias({"Номер"})
    private String number;

    @JsonAlias({"Дата"})
    private String docDate;

    @JsonAlias({"Проведен"})
    private String posted;

    @JsonAlias({"ВидОперации"})
    private String operationType;

    @JsonAlias({"КонтагентКод", "КонтрагентКредиторКод", "КонтрагентКредитор"})
    private String counterpartyCode;

    @JsonAlias({"ДоговорКод", "ДоговорКонтрагентаКод", "ДоговорКонтрагента"})
    private String contractCode;

    @JsonAlias({"Сумма","СуммыДолга"})
    private String amount;

    @JsonAlias({"Валюта","ВалютаДокумента"})
    private String currency;

    @JsonAlias({"Комментарий"})
    private String comment;

    @JsonAlias({"Организация"})
    private String organisation;

    @JsonAlias({"ПометкаУдаления"})
    private String deleted;

    @JsonAlias({"БухУчет","ОтражатьВБухгалтерскомУчете"})
    private String affectsAccounting;

    @JsonAlias({"УпрУчет","ОтражатьВУправленческомУчете"})
    private String affectsManagement;

    @JsonAlias({"КонтрагентДебиторКод","КонтрагентДебитор"})
    private String debitorContractorCode;

    @JsonAlias({"ДоговорКонтрагентаДебитораКод","ТЧ_ДоговорКонтрагентаКод", "ДоговорКонтрагентаДебитора"})
    private String debitorContractCode;

    @JsonAlias({"ДокументОснованиеНомер", "ДокументОснование"})
    private String baseDoc;

    @JsonAlias({"ВидЗадолженности"})
    private String liabilityType;

    @JsonAlias({"Подразделение"})
    private String department;

    @JsonAlias({"ИдетПроведение"})
    private String isBeingPosted;

    @JsonAlias({"КорректировкаПеней"})
    private String penaltyCorrection;

    @JsonAlias({"СписаниеЗадолженности"})
    private String debtWritOff;


    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getAffectsAccounting() {
        return affectsAccounting;
    }

    public void setAffectsAccounting(String affectsAccounting) {
        this.affectsAccounting = affectsAccounting;
    }

    public String getAffectsManagement() {
        return affectsManagement;
    }

    public void setAffectsManagement(String affectsManagement) {
        this.affectsManagement = affectsManagement;
    }

    public String getDebitorContractorCode() {
        return debitorContractorCode;
    }

    public void setDebitorContractorCode(String debitorContractorCode) {
        this.debitorContractorCode = debitorContractorCode;
    }

    public String getDebitorContractCode() {
        return debitorContractCode;
    }

    public void setDebitorContractCode(String debitorContractCode) {
        this.debitorContractCode = debitorContractCode;
    }

    public String getBaseDoc() {
        return baseDoc;
    }

    public void setBaseDoc(String baseDoc) {
        this.baseDoc = baseDoc;
    }

    public String getLiabilityType() {
        return liabilityType;
    }

    public void setLiabilityType(String liabilityType) {
        this.liabilityType = liabilityType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIsBeingPosted() {
        return isBeingPosted;
    }

    public void setIsBeingPosted(String isBeingPosted) {
        this.isBeingPosted = isBeingPosted;
    }

    public String getPenaltyCorrection() {
        return penaltyCorrection;
    }

    public void setPenaltyCorrection(String penaltyCorrection) {
        this.penaltyCorrection = penaltyCorrection;
    }

    public String getDebtWritOff() {
        return debtWritOff;
    }

    public void setDebtWritOff(String debtWritOff) {
        this.debtWritOff = debtWritOff;
    }
}
