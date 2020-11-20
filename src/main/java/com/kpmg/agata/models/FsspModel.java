package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FsspModel extends AbstractClientDataModel {

    /**
     * Наименование юридического лица
     */
    @JsonAlias({"The name of the debtor"})
    private String debtorName;

    /**
     * Дата возбуждения исполнительного производства
     */
    @JsonAlias({"Date of the institution of proceeding"})
    private String proceedingInstitutionDate;

    /**
     * Тип исполнительного документа
     */
    @JsonAlias({"The executive document type"})
    private String executiveDocumentType;

    /**
     * Дата исполнительного документа
     */
    @JsonAlias({"Date of the executive document"})
    private String executiveDocumentDate;

    /**
     * Требования исполнительного документа
     */
    @JsonAlias({"The object of executive documents"})
    private String executiveDocumentObject;

    /**
     * Предмет исполнения
     */
    @JsonAlias({"The object of the execution"})
    private String executionObject;

    /**
     * Сумма долга
     */
    @JsonAlias({"Amount due"})
    private String debt;

    /**
     * Остаток непогашенной задолженности
     */
    @JsonAlias({"Debt remaining balance"})
    private String remainingDebt;

    /**
     * Дата, причина окончания или прекращения ИП (статья, часть, пункт основания)
     */
    @JsonAlias({"Date and complete IP reason"})
    private String dateAndCompletionReason;

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getProceedingInstitutionDate() {
        return proceedingInstitutionDate;
    }

    public void setProceedingInstitutionDate(String proceedingInstitutionDate) {
        this.proceedingInstitutionDate = proceedingInstitutionDate;
    }

    public String getExecutiveDocumentType() {
        return executiveDocumentType;
    }

    public void setExecutiveDocumentType(String executiveDocumentType) {
        this.executiveDocumentType = executiveDocumentType;
    }

    public String getExecutiveDocumentDate() {
        return executiveDocumentDate;
    }

    public void setExecutiveDocumentDate(String executiveDocumentDate) {
        this.executiveDocumentDate = executiveDocumentDate;
    }

    public String getExecutiveDocumentObject() {
        return executiveDocumentObject;
    }

    public void setExecutiveDocumentObject(String executiveDocumentObject) {
        this.executiveDocumentObject = executiveDocumentObject;
    }

    public String getExecutionObject() {
        return executionObject;
    }

    public void setExecutionObject(String executionObject) {
        this.executionObject = executionObject;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getRemainingDebt() {
        return remainingDebt;
    }

    public void setRemainingDebt(String remainingDebt) {
        this.remainingDebt = remainingDebt;
    }

    public String getDateAndCompletionReason() {
        return dateAndCompletionReason;
    }

    public void setDateAndCompletionReason(String dateAndCompletionReason) {
        this.dateAndCompletionReason = dateAndCompletionReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FsspModel fsspModel = (FsspModel) o;
        return Objects.equals(debtorName, fsspModel.debtorName) &&
                Objects.equals(proceedingInstitutionDate, fsspModel.proceedingInstitutionDate) &&
                Objects.equals(executiveDocumentType, fsspModel.executiveDocumentType) &&
                Objects.equals(executiveDocumentDate, fsspModel.executiveDocumentDate) &&
                Objects.equals(executiveDocumentObject, fsspModel.executiveDocumentObject) &&
                Objects.equals(executionObject, fsspModel.executionObject) &&
                Objects.equals(debt, fsspModel.debt) &&
                Objects.equals(remainingDebt, fsspModel.remainingDebt) &&
                Objects.equals(dateAndCompletionReason, fsspModel.dateAndCompletionReason);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(super.hashCode(), debtorName, proceedingInstitutionDate, executiveDocumentType,
                        executiveDocumentDate,
                        executiveDocumentObject, executionObject, debt, remainingDebt, dateAndCompletionReason);
    }

    @Override
    public String toString() {
        return "FsspModel{" +
                "debtorName='" + debtorName + '\'' +
                ", proceedingInstitutionDate='" + proceedingInstitutionDate + '\'' +
                ", executiveDocumentType='" + executiveDocumentType + '\'' +
                ", executiveDocumentDate='" + executiveDocumentDate + '\'' +
                ", executiveDocumentObject='" + executiveDocumentObject + '\'' +
                ", executionObject='" + executionObject + '\'' +
                ", debt='" + debt + '\'' +
                ", remainingDebt='" + remainingDebt + '\'' +
                ", dateAndCompletionReason='" + dateAndCompletionReason + '\'' +
                "} " + super.toString();
    }
}
