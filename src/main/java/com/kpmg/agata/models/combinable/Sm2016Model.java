package com.kpmg.agata.models.combinable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.models.combinable.CustomHeader;

import java.util.HashMap;
import java.util.Map;

public class Sm2016Model extends AbstractClientDataModel implements CustomHeader {
    @JsonAlias("Номер договора")
    private String contract;

    @JsonAlias("Наименование")
    private String counterpartyName;

    @JsonAlias("Код контрагента")
    private String counterpartyCode;

    @JsonAlias("ДЕБИТОРСКАЯ ЗАДОЛЖЕННОСТЬ, РУБЛИ РФ")
    private String debt;

    @JsonAlias("ПРОСРОЧЕННАЯ ДЕБИТОРСКАЯ ЗАДОЛЖЕННОСТЬ, РУБЛИ РФ")
    private String pastDueDebt;

    @JsonAlias("КОЛИЧЕСТВО ДНЕЙ ПРОСРОЧЕННОЙ ДЗ")
    private String pastDueDays;

    @JsonAlias("Доля в проср. ДЗ, %")
    private String pastDueDebtPart;

    @JsonAlias("Изменение за неделю")
    private String weekDelta;

    @JsonAlias("Дата")
    private String date;

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyCode() {
        return counterpartyCode;
    }

    public void setCounterpartyCode(String counterpartyCode) {
        this.counterpartyCode = counterpartyCode;
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

    public String getPastDueDays() {
        return pastDueDays;
    }

    public void setPastDueDays(String pastDueDays) {
        this.pastDueDays = pastDueDays;
    }

    public String getPastDueDebtPart() {
        return pastDueDebtPart;
    }

    public void setPastDueDebtPart(String pastDueDebtPart) {
        this.pastDueDebtPart = pastDueDebtPart;
    }

    public String getWeekDelta() {
        return weekDelta;
    }

    public void setWeekDelta(String weekDelta) {
        this.weekDelta = weekDelta;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    @JsonIgnore
    public Map<Short, String> createCustomHeader() {
        Map<Short, String> header = new HashMap<>();
        header.put((short) 0, "Номер договора");
        header.put((short) 1, "Наименование");
        header.put((short) 2, "Код контрагента");
        header.put((short) 3, "ДЕБИТОРСКАЯ ЗАДОЛЖЕННОСТЬ, РУБЛИ РФ");
        header.put((short) 4, "ПРОСРОЧЕННАЯ ДЕБИТОРСКАЯ ЗАДОЛЖЕННОСТЬ, РУБЛИ РФ");
        header.put((short) 5, "КОЛИЧЕСТВО ДНЕЙ ПРОСРОЧЕННОЙ ДЗ");
        header.put((short) 6, "Доля в проср. ДЗ, %");
        header.put((short) 7, "Изменение за неделю");
        header.put((short) 8, "Дата");
        return header;
    }

    @Override
    public String toString() {
        return "Sm2016Model{" +
                "contract='" + contract + '\'' +
                ", counterpartyName='" + counterpartyName + '\'' +
                ", counterpartyCode='" + counterpartyCode + '\'' +
                ", debt='" + debt + '\'' +
                ", pastDueDebt='" + pastDueDebt + '\'' +
                ", pastDueDays='" + pastDueDays + '\'' +
                ", pastDueDebtPart='" + pastDueDebtPart + '\'' +
                ", weekDelta='" + weekDelta + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
