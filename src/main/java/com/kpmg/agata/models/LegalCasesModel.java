package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LegalCasesModel extends AbstractClientDataModel {

    public LegalCasesModel() {
    }

    @JsonAlias({"ID контрагента"})
    private String contractorID;

    @JsonAlias({"Наименование контрагента"})
    private String name;

    @JsonAlias({"ИНН контрагента"})
    private String inn;

    @JsonAlias({"Наименование контрагента из СПАРК"})
    private String nameSpark;

    @JsonAlias({"Номер дела"})
    private String caseNumber;

    @JsonAlias({"Категория"})
    private String category;

    @JsonAlias({"Состояние"})
    private String status;

    @JsonAlias({"Исход дела"})
    private String outcome;

    @JsonAlias({"Дата иска"})
    private String claimDate;

    @JsonAlias({"Дата решения"})
    private String outcomeDate;

    @JsonAlias({"Сумма иска, RUB"})
    private String claimAmount;

    @JsonAlias({"Сумма по решению, RUB"})
    private String outcomeAmount;

    @JsonAlias({"Суть иска"})
    private String claimCharge;

    @JsonAlias({"Резолютивная часть"})
    private String dictum;

    @JsonAlias({"Тип участника"})
    private String side;

    public String getContractorID() {
        return contractorID;
    }

    public void setContractorID(String contractorID) {
        this.contractorID = contractorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getNameSpark() {
        return nameSpark;
    }

    public void setNameSpark(String nameSpark) {
        this.nameSpark = nameSpark;
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

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getOutcomeDate() {
        return outcomeDate;
    }

    public void setOutcomeDate(String outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public String getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(String claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getOutcomeAmount() {
        return outcomeAmount;
    }

    public void setOutcomeAmount(String outcomeAmount) {
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

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LegalCasesModel that = (LegalCasesModel) o;
        return Objects.equals(contractorID, that.contractorID) &&
                Objects.equals(name, that.name) &&
                Objects.equals(inn, that.inn) &&
                Objects.equals(nameSpark, that.nameSpark) &&
                Objects.equals(caseNumber, that.caseNumber) &&
                Objects.equals(category, that.category) &&
                Objects.equals(status, that.status) &&
                Objects.equals(outcome, that.outcome) &&
                Objects.equals(claimDate, that.claimDate) &&
                Objects.equals(outcomeDate, that.outcomeDate) &&
                Objects.equals(claimAmount, that.claimAmount) &&
                Objects.equals(outcomeAmount, that.outcomeAmount) &&
                Objects.equals(claimCharge, that.claimCharge) &&
                Objects.equals(dictum, that.dictum) &&
                Objects.equals(side, that.side);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contractorID, name, inn, nameSpark, caseNumber, category, status, outcome,
                claimDate, outcomeDate, claimAmount, outcomeAmount, claimCharge, dictum, side);
    }

    @Override
    public String toString() {
        return "LegalCasesModel{" +
                "contractorID='" + contractorID + '\'' +
                ", name='" + name + '\'' +
                ", inn='" + inn + '\'' +
                ", nameSpark='" + nameSpark + '\'' +
                ", caseNumber='" + caseNumber + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", outcome='" + outcome + '\'' +
                ", claimDate='" + claimDate + '\'' +
                ", outcomeDate='" + outcomeDate + '\'' +
                ", claimAmount='" + claimAmount + '\'' +
                ", outcomeAmount='" + outcomeAmount + '\'' +
                ", claimCharge='" + claimCharge + '\'' +
                ", dictum='" + dictum + '\'' +
                ", side='" + side + '\'' +
                "} " + super.toString();
    }
}
