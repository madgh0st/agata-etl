package com.kpmg.agata.models.combinable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpmg.agata.models.AbstractClientDataModel;

import java.util.HashMap;
import java.util.Map;

public class Sm2017Model extends AbstractClientDataModel implements CustomHeader {
    @JsonAlias("Номер договора")
    private String contract;

    @JsonAlias("Наименование")
    private String counterpartyName;

    @JsonAlias("Продукция")
    private String product;

    @JsonAlias("Сумма кредитного лимита")
    private String value;

    @JsonAlias("Превышение КЛ")
    private String creditLimitOverpass;

    @JsonAlias("Отсрочка, дни")
    private String delayDays;

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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreditLimitOverpass() {
        return creditLimitOverpass;
    }

    public void setCreditLimitOverpass(String creditLimitOverpass) {
        this.creditLimitOverpass = creditLimitOverpass;
    }

    public String getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(String delayDays) {
        this.delayDays = delayDays;
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
        header.put((short) 2, "Продукция");
        header.put((short) 3, "Сумма кредитного лимита");
        header.put((short) 4, "Превышение КЛ");
        header.put((short) 5, "Отсрочка, дни");
        header.put((short) 6, "Код контрагента");
        header.put((short) 7, "ДЕБИТОРСКАЯ ЗАДОЛЖЕННОСТЬ, РУБЛИ РФ");
        header.put((short) 8, "ПРОСРОЧЕННАЯ ДЕБИТОРСКАЯ ЗАДОЛЖЕННОСТЬ, РУБЛИ РФ");
        header.put((short) 9, "КОЛИЧЕСТВО ДНЕЙ ПРОСРОЧЕННОЙ ДЗ");
        header.put((short) 10, "Доля в проср. ДЗ, %");
        header.put((short) 11, "Изменение за неделю");
        header.put((short) 12, "Дата");
        return header;
    }

    @Override
    public String toString() {
        return "SmModel{" +
                "contract='" + contract + '\'' +
                ", counterpartyName='" + counterpartyName + '\'' +
                ", product='" + product + '\'' +
                ", value='" + value + '\'' +
                ", creditLimitOverpass='" + creditLimitOverpass + '\'' +
                ", delayDays='" + delayDays + '\'' +
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
