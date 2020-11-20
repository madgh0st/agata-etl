package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"fileName", "sheetName", "dateModified",
        "num", "blockKBE", "groupDO", "counterpartyName", "counterpartyINN", "contractName", "onDate",
        "totalDebtWithoutReserve", "currentDebt", "overdueDebt", "overdueDebtLess5Days", "overdueDebtBetween5and30Days",
        "overdueDebtMore30Days", "comments"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyStatusPDZModelMB extends AbstractClientDataModel {
    public WeeklyStatusPDZModelMB() {
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
     * на дату
     */
    @JsonAlias({"6"})
    private String onDate;

    /**
     * Общая задолженность за вычетом резерва по сомнительным долгам, руб.
     */
    @JsonAlias({"7"})
    private String totalDebtWithoutReserve;

    /**
     * Просроченная  задолженность до 5 дней,руб.(гр.14 + гр.15)
     */
    @JsonAlias({"8"})
    private String overdueDebtLess5Days;

    /**
     * Просроченная  задолженность свыше 5 дней,руб.(гр.14 + гр.15)
     */
    @JsonAlias({"9"})
    private String overdueDebtBetween5and30Days;

    /**
     * Комментарии относительно просроченной задолженности
     */
    @JsonAlias({"10"})
    private String comments;

    /**
     * Количество дней просрочки
     */
    @JsonAlias({"11"})
    private String daysOverdue;

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDaysOverdue() {
        return daysOverdue;
    }

    public void setDaysOverdue(String daysOverdue) {
        this.daysOverdue = daysOverdue;
    }

}
