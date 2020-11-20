package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RealizationServicesModel extends AbstractClientDataModel {

    public RealizationServicesModel() {
    }

    @JsonAlias({"Дата"})
    private String docDate;

    @JsonAlias({"КонтрагентПредставление", "Контрагент"})
    private String counterparty;

    @JsonAlias({"ДоговорКонтрагентаПредставление", "Договор"})
    private String contract;

    @JsonAlias({"СуммаДокумента", "Сумма"})
    private String amount;

    @JsonAlias({"ВалютаДокумента", "Валюта"})
    private String currency;

    @JsonAlias({"Номер"})
    private String number;

    @JsonAlias({"ВидОперации", "Вид операции", "ХозяйственнаяОперация"})
    private String operationType;

    @JsonAlias({"КонтрагентКод", "Код (контрагента)", "Контрагент1"})
    private String counterpartyCode;

    @JsonAlias({"ДоговорКонтрагентаКод", "Код (договора)", "Договор1"})
    private String contractCode;

    @JsonAlias({"Комментарий"})
    private String comment;

    @JsonAlias({"Сделка", "Документ основание"})
    private String baseDoc;

    @JsonAlias({"ОтключитьКонтрольВзаиморасчетов"})
    private String noSettlementControl;

    @JsonAlias({"Организация"})
    private String organisation;

    @JsonAlias({"Проведен", "Статус проведения"})
    private String posted;

    @JsonAlias({"ПометкаУдаления"})
    private String deleted;

    @JsonAlias({"ОтражатьВБухгалтерскомУчете"})
    private String affectsAccounting;

    @JsonAlias({"ОтражатьВУправленческомУчете"})
    private String affectsManagement;

    @JsonAlias({"ГПН_ВидРеализации"})
    private String exportSelling;

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
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

    public String getNoSettlementControl() {
        return noSettlementControl;
    }

    public void setNoSettlementControl(String noSettlementControl) {
        this.noSettlementControl = noSettlementControl;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
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

    public String getExportSelling() {
        return exportSelling;
    }

    public void setExportSelling(String exportSelling) {
        this.exportSelling = exportSelling;
    }
}
