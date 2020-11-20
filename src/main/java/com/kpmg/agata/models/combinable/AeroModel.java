package com.kpmg.agata.models.combinable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.kpmg.agata.models.AbstractClientDataModel;

public class AeroModel extends AbstractClientDataModel {

    /**
     * Дата отчёта
     */
    @JsonAlias("1")
    private String date;

    /**
     * Наименование ДО
     */
    @JsonAlias("2")
    private String department;

    /**
     * Наименование контрагента
     */
    @JsonAlias("3")
    private String counterparty;

    /**
     * Резидент / нерезидент
     */
    @JsonAlias("4")
    private String resident;

    /**
     * Признак принадлежности к ГК ГПН: Взаимозависимый / Сторонний
     */
    @JsonAlias("5")
    private String dependency;

    /**
     * Общая задолженность
     */
    @JsonAlias("6")
    private String debt;

    /**
     * ПДЗ
     */
    @JsonAlias("7")
    private String pastDueDebt;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getPastDueDebt() {
        return pastDueDebt;
    }

    public void setPastDueDebt(String pastDueDebt) {
        this.pastDueDebt = pastDueDebt;
    }

    @Override
    public String toString() {
        return "AeroModel{" +
                "date='" + date + '\'' +
                ", department='" + department + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", resident='" + resident + '\'' +
                ", dependency='" + dependency + '\'' +
                ", debt='" + debt + '\'' +
                ", pastDueDebt='" + pastDueDebt + '\'' +
                '}';
    }
}
