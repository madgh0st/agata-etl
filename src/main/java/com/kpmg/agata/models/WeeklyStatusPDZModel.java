package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"fileName", "sheetName", "dateModified",
        "num", "blockKBE", "groupDO", "counterpartyName", "counterpartyINN", "contractName", "onDate",
        "totalDebtWithoutReserve", "currentDebt", "overdueDebt", "overdueDebtLess5Days", "overdueDebtBetween5and30Days",
        "overdueDebtMore30Days", "comments"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyStatusPDZModel extends AbstractClientDataModel {
    public WeeklyStatusPDZModel() {
    }

    /**
     * №п/п
     */
    @JsonAlias({"1"})
    private String num;

    /**
     * Блок / КБЕ
     */
    @JsonAlias({"2"})
    private String blockKBE;

    /**
     * ДО Группы
     */
    @JsonAlias({"3"})
    private String groupDO;

    /**
     * Наименование контрагента
     */
    @JsonAlias({"4"})
    private String counterpartyName;

    /**
     * ИНН контрагента
     */
    @JsonAlias({"5"})
    private String counterpartyINN;

    /**
     * Номер договора
     */
    @JsonAlias({"6"})
    private String contractName;

    /**
     * на дату
     */
    @JsonAlias({"7"})
    private String onDate;

    /**
     * Общая задолженность за вычетом резерва по сомнительным долгам, руб.
     */
    @JsonAlias({"8"})
    private String totalDebtWithoutReserve;

    /**
     * Текущая задолженность
     * (гр.11 - гр.13),
     * руб.
     */
    @JsonAlias({"9"})
    private String currentDebt;

    /**
     * "Просроченная  задолженность,
     * руб.
     * (гр.14 + гр.15 + гр.16)"
     */
    @JsonAlias({"10"})
    private String overdueDebt;

    /**
     * в том числе, просроченная задолженность менее 5 дней
     * (<=5),
     * руб.
     */
    @JsonAlias({"11"})
    private String overdueDebtLess5Days;

    /**
     * в том числе, просроченная задолженность более 5 (>5) и менее 30 дней
     * (<=30),
     * руб.
     */
    @JsonAlias({"12"})
    private String overdueDebtBetween5and30Days;

    /**
     * в том числе, просроченная задолженность более 30 дней
     * (>30),
     * руб.
     */
    @JsonAlias({"13"})
    private String overdueDebtMore30Days;

    /**
     * Комментарии относительно просроченной задолженности
     */
    @JsonAlias({"14"})
    private String comments;


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getBlockKBE() {
        return blockKBE;
    }

    public void setBlockKBE(String blockKBE) {
        this.blockKBE = blockKBE;
    }

    public String getGroupDO() {
        return groupDO;
    }

    public void setGroupDO(String groupDO) {
        this.groupDO = groupDO;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyINN() {
        return counterpartyINN;
    }

    public void setCounterpartyINN(String counterpartyINN) {
        this.counterpartyINN = counterpartyINN;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getOnDate() {
        return onDate;
    }

    public void setOnDate(String onDate) {
        this.onDate = onDate;
    }

    public String getTotalDebtWithoutReserve() {
        return totalDebtWithoutReserve;
    }

    public void setTotalDebtWithoutReserve(String totalDebtWithoutReserve) {
        this.totalDebtWithoutReserve = totalDebtWithoutReserve;
    }

    public String getCurrentDebt() {
        return currentDebt;
    }

    public void setCurrentDebt(String currentDebt) {
        this.currentDebt = currentDebt;
    }

    public String getOverdueDebt() {
        return overdueDebt;
    }

    public void setOverdueDebt(String overdueDebt) {
        this.overdueDebt = overdueDebt;
    }

    public String getOverdueDebtLess5Days() {
        return overdueDebtLess5Days;
    }

    public void setOverdueDebtLess5Days(String overdueDebtLess5Days) {
        this.overdueDebtLess5Days = overdueDebtLess5Days;
    }

    public String getOverdueDebtBetween5and30Days() {
        return overdueDebtBetween5and30Days;
    }

    public void setOverdueDebtBetween5and30Days(String overdueDebtBetween5and30Days) {
        this.overdueDebtBetween5and30Days = overdueDebtBetween5and30Days;
    }

    public String getOverdueDebtMore30Days() {
        return overdueDebtMore30Days;
    }

    public void setOverdueDebtMore30Days(String overdueDebtMore30Days) {
        this.overdueDebtMore30Days = overdueDebtMore30Days;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
