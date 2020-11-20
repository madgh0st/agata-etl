package com.kpmg.agata.models.combinable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.parser.combiner.ExcelRowCombiner;
import com.kpmg.agata.parser.combiner.MbModelRowCombiner;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MbModel extends AbstractClientDataModel implements Combinable, CustomHeader {

    @JsonAlias("Валюта")
    private String currency;

    @JsonAlias("Контрагент")
    private String counterparty;

    @JsonAlias("Договор контрагента")
    private String contract;

    @JsonAlias("Сделка")
    private String deal;

    @JsonAlias("Контрагент заказа")
    private String orderCounterparty;

    @JsonAlias("Дата сделки")
    private String dealDate;

    @JsonAlias("Подразделение")
    private String department;

    @JsonAlias("Просрочка, дней")
    private String pastDueDays;

    @JsonAlias("Дней отсрочки")
    private String delayDays;

    @JsonAlias("Сумма ДЗ в валюте договора/Сумма в пределах отсрочки")
    private String debtWithinDelay;

    @JsonAlias("Сумма ДЗ в валюте договора/Сумма просрочки <5 дней")
    private String pastDueFiveDays;

    @JsonAlias("Сумма ДЗ в валюте договора/Сумма просрочки 6-20 дней")
    private String pastDueFromSixToTwentyDays;

    @JsonAlias("Сумма ДЗ в валюте договора/Сумма просрочки >20 дней")
    private String pastDueOverTwentyDays;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getOrderCounterparty() {
        return orderCounterparty;
    }

    public void setOrderCounterparty(String orderCounterparty) {
        this.orderCounterparty = orderCounterparty;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPastDueDays() {
        return pastDueDays;
    }

    public void setPastDueDays(String pastDueDays) {
        this.pastDueDays = pastDueDays;
    }

    public String getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(String delayDays) {
        this.delayDays = delayDays;
    }

    public String getDebtWithinDelay() {
        return debtWithinDelay;
    }

    public void setDebtWithinDelay(String debtWithinDelay) {
        this.debtWithinDelay = debtWithinDelay;
    }

    public String getPastDueFiveDays() {
        return pastDueFiveDays;
    }

    public void setPastDueFiveDays(String pastDueFiveDays) {
        this.pastDueFiveDays = pastDueFiveDays;
    }

    public String getPastDueFromSixToTwentyDays() {
        return pastDueFromSixToTwentyDays;
    }

    public void setPastDueFromSixToTwentyDays(String pastDueFromSixToTwentyDays) {
        this.pastDueFromSixToTwentyDays = pastDueFromSixToTwentyDays;
    }

    public String getPastDueOverTwentyDays() {
        return pastDueOverTwentyDays;
    }

    public void setPastDueOverTwentyDays(String pastDueOverTwentyDays) {
        this.pastDueOverTwentyDays = pastDueOverTwentyDays;
    }

    @Override
    @JsonIgnore
    public ExcelRowCombiner createCombiner() {
        return new MbModelRowCombiner();
    }

    @Override
    @JsonIgnore
    public Map<Short, String> createCustomHeader() {
        Map<Short, String> header = new HashMap<>();
        header.put((short) 0, "Валюта/Контрагент, Код/Договор контрагента, Код/Сделка");
        header.put((short) 3, "Валюта/Контрагент заказа");
        header.put((short) 6, "Дата сделки");
        header.put((short) 7, "Подразделение");
        header.put((short) 8, "Просрочка, дней");
        header.put((short) 10, "Дней отсрочки");
        header.put((short) 11, "Сумма ДЗ в валюте договора/Сумма в пределах отсрочки");
        header.put((short) 12, "Сумма ДЗ в валюте договора/Сумма просрочки <5 дней");
        header.put((short) 13, "Сумма ДЗ в валюте договора/Сумма просрочки 6-20 дней");
        header.put((short) 14, "Сумма ДЗ в валюте договора/Сумма просрочки >20 дней");
        return header;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MbModel mbModel = (MbModel) o;
        return Objects.equals(currency, mbModel.currency) &&
                Objects.equals(counterparty, mbModel.counterparty) &&
                Objects.equals(contract, mbModel.contract) &&
                Objects.equals(deal, mbModel.deal) &&
                Objects.equals(orderCounterparty, mbModel.orderCounterparty) &&
                Objects.equals(dealDate, mbModel.dealDate) &&
                Objects.equals(department, mbModel.department) &&
                Objects.equals(pastDueDays, mbModel.pastDueDays) &&
                Objects.equals(delayDays, mbModel.delayDays) &&
                Objects.equals(debtWithinDelay, mbModel.debtWithinDelay) &&
                Objects.equals(pastDueFiveDays, mbModel.pastDueFiveDays) &&
                Objects.equals(pastDueFromSixToTwentyDays, mbModel.pastDueFromSixToTwentyDays) &&
                Objects.equals(pastDueOverTwentyDays, mbModel.pastDueOverTwentyDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, counterparty, contract, deal, orderCounterparty, dealDate, department, pastDueDays, delayDays, debtWithinDelay, pastDueFiveDays, pastDueFromSixToTwentyDays, pastDueOverTwentyDays);
    }

    @Override
    public String toString() {
        return "MbModel{" +
                "currency='" + currency + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", contract='" + contract + '\'' +
                ", deal='" + deal + '\'' +
                ", orderCounterparty='" + orderCounterparty + '\'' +
                ", dealDate='" + dealDate + '\'' +
                ", department='" + department + '\'' +
                ", pastDueDays='" + pastDueDays + '\'' +
                ", delayDays='" + delayDays + '\'' +
                ", debtWithinDelay='" + debtWithinDelay + '\'' +
                ", pastDueFiveDays='" + pastDueFiveDays + '\'' +
                ", pastDueFromSixToTwentyDays='" + pastDueFromSixToTwentyDays + '\'' +
                ", pastDueOverTwentyDays='" + pastDueOverTwentyDays + '\'' +
                '}';
    }
}
