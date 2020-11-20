package com.kpmg.agata.models.clean;

import java.util.Objects;

public class CleanCreditCorrectionModel extends AbstractCleanDataModel {

    //String
    private String documentType;
    //String
    private String number;
    //Date 31.12.1970 23:59:59
    private String docDate;
    //String
    private String posted;
    //String
    private String operationType;
    //String
    private String counterpartyCode;
    //String
    private String contractCode;
    //Double
    private String amount;
    //String
    private String currency;
    //String
    private String comment;
    //String
    private String organisation;
    //String
    private String deleted;
    //String
    private String affectsAccounting;
    //String
    private String affectsManagement;
    //String
    private String debitorContractorCode;
    //String
    private String debitorContractCode;
    //String? always null in provided example
    private String baseDoc;
    //String? always null in provided example
    private String liabilityType;
    //String? always null in provided example
    private String department;
    //String? always null in provided example
    private String isBeingPosted;
    //String? always null in provided example
    private String penaltyCorrection;
    //String? always null in provided example
    private String debtWritOff;
    public CleanCreditCorrectionModel() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanCreditCorrectionModel that = (CleanCreditCorrectionModel) o;
        return Objects.equals(documentType, that.documentType) &&
                Objects.equals(number, that.number) &&
                Objects.equals(docDate, that.docDate) &&
                Objects.equals(posted, that.posted) &&
                Objects.equals(operationType, that.operationType) &&
                Objects.equals(counterpartyCode, that.counterpartyCode) &&
                Objects.equals(contractCode, that.contractCode) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(organisation, that.organisation) &&
                Objects.equals(deleted, that.deleted) &&
                Objects.equals(affectsAccounting, that.affectsAccounting) &&
                Objects.equals(affectsManagement, that.affectsManagement) &&
                Objects.equals(debitorContractorCode, that.debitorContractorCode) &&
                Objects.equals(debitorContractCode, that.debitorContractCode) &&
                Objects.equals(baseDoc, that.baseDoc) &&
                Objects.equals(liabilityType, that.liabilityType) &&
                Objects.equals(department, that.department) &&
                Objects.equals(isBeingPosted, that.isBeingPosted) &&
                Objects.equals(penaltyCorrection, that.penaltyCorrection) &&
                Objects.equals(debtWritOff, that.debtWritOff);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(documentType, number, docDate, posted, operationType, counterpartyCode, contractCode, amount,
                        currency, comment, organisation, deleted, affectsAccounting, affectsManagement,
                        debitorContractorCode, debitorContractCode, baseDoc, liabilityType, department, isBeingPosted,
                        penaltyCorrection, debtWritOff);
    }

    @Override
    public String toString() {
        return "CleanCreditCorrectionModel{" +
                "documentType='" + documentType + '\'' +
                ", number='" + number + '\'' +
                ", docDate='" + docDate + '\'' +
                ", posted='" + posted + '\'' +
                ", operationType='" + operationType + '\'' +
                ", counterpartyCode='" + counterpartyCode + '\'' +
                ", contractCode='" + contractCode + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", comment='" + comment + '\'' +
                ", organisation='" + organisation + '\'' +
                ", deleted='" + deleted + '\'' +
                ", affectsAccounting='" + affectsAccounting + '\'' +
                ", affectsManagement='" + affectsManagement + '\'' +
                ", debitorContractorCode='" + debitorContractorCode + '\'' +
                ", debitorContractCode='" + debitorContractCode + '\'' +
                ", baseDoc='" + baseDoc + '\'' +
                ", liabilityType='" + liabilityType + '\'' +
                ", department='" + department + '\'' +
                ", isBeingPosted='" + isBeingPosted + '\'' +
                ", penaltyCorrection='" + penaltyCorrection + '\'' +
                ", debtWritOff='" + debtWritOff + '\'' +
                '}';
    }
}
