package com.kpmg.agata.models.combinable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.parser.combiner.BmModelRowCombiner;
import com.kpmg.agata.parser.combiner.ExcelRowCombiner;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BmModel extends AbstractClientDataModel implements Combinable, CustomHeader {

    @JsonAlias("Контрагент")
    private String counterparty;

    @JsonAlias("Период")
    private String period;

    @JsonAlias("Общая задолженность")
    private String totalDept;

    @JsonAlias("Задолженность")
    private String dept;

    @JsonAlias("Просроченная задолженность")
    private String pastDueDebt;

    @JsonAlias("Сумма штрафа и пени")
    private String penalty;

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTotalDept() {
        return totalDept;
    }

    public void setTotalDept(String totalDept) {
        this.totalDept = totalDept;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPastDueDebt() {
        return pastDueDebt;
    }

    public void setPastDueDebt(String pastDueDebt) {
        this.pastDueDebt = pastDueDebt;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    @Override
    @JsonIgnore
    public ExcelRowCombiner createCombiner() {
        return new BmModelRowCombiner();
    }

    @Override
    @JsonIgnore
    public Map<Short, String> createCustomHeader() {
        Map<Short, String> header = new HashMap<>();
        header.put((short) 0, "Контрагент/Период");
        header.put((short) 3, "Общая задолженность");
        header.put((short) 4, "Задолженность");
        header.put((short) 5, "Просроченная задолженность");
        header.put((short) 8, "Сумма штрафа и пени");
        return header;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BmModel bmModel = (BmModel) o;
        return Objects.equals(counterparty, bmModel.counterparty) &&
                Objects.equals(period, bmModel.period) &&
                Objects.equals(totalDept, bmModel.totalDept) &&
                Objects.equals(dept, bmModel.dept) &&
                Objects.equals(pastDueDebt, bmModel.pastDueDebt) &&
                Objects.equals(penalty, bmModel.penalty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counterparty, period, totalDept, dept, pastDueDebt, penalty);
    }

    @Override
    public String toString() {
        return "BmModel{" +
                "counterparty='" + counterparty + '\'' +
                ", period='" + period + '\'' +
                ", totalDept='" + totalDept + '\'' +
                ", dept='" + dept + '\'' +
                ", pastDueDebt='" + pastDueDebt + '\'' +
                ", penalty='" + penalty + '\'' +
                '}';
    }
}
