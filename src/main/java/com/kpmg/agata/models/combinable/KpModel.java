package com.kpmg.agata.models.combinable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.parser.combiner.ExcelRowCombiner;
import com.kpmg.agata.parser.combiner.KpModelRowCombiner;

import java.util.Objects;

public class KpModel extends AbstractClientDataModel implements Combinable {
    /**
     * Отделение
     */
    @JsonAlias("1")
    private String department;

    /**
     * Ответственный менеджер по договору контрагента
     */
    @JsonAlias("2")
    private String manager;

    /**
     * Контрагент
     */
    @JsonAlias("3")
    private String counterparty;

    /**
     * Ведутся судебные разбирательства
     */
    @JsonAlias("4")
    private String litigation;

    /**
     * Договор контрагента
     */
    @JsonAlias("5")
    private String contractName;

    /**
     * Дата возникновения задолженности/ Конец периода
     */
    @JsonAlias("6")
    private String date;

    /**
     * Контроль лимита
     */
    @JsonAlias("7")
    private String limit;

    /**
     * Сумма дебиторской задолженности на конец дня 31.12.2017
     */
    @JsonAlias("8")
    private String debtOnDate;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Всего
     */
    @JsonAlias("9")
    private String totalDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * В т.ч. по гос.контрактам
     */
    @JsonAlias("10")
    private String totalGowDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Сумма разрешенной дебиторской задолженности
     */
    @JsonAlias("11")
    private String allowedDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Сумма просроченной дебиторской задолженности
     * Всего
     */
    @JsonAlias("12")
    private String totalPastDueDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Сумма просроченной дебиторской задолженности
     * В т.ч. Просроч. долг (от 1 до 10 дней)
     */
    @JsonAlias("13")
    private String pastDueTenDaysDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Сумма просроченной дебиторской задолженности
     * В т.ч. Просроч. долг от 11 до 30 дней
     */
    @JsonAlias("14")
    private String pastDueTenToThirtyDaysDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Сумма просроченной дебиторской задолженности
     * В т.ч. Просроч. долг от 31 до 90 дней
     */
    @JsonAlias("15")
    private String pastDueThirtyToNinetyDaysDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Сумма просроченной дебиторской задолженности
     * В т.ч. Просроч. Долг от 91 дней и более
     */
    @JsonAlias("16")
    private String pastDueOverNinetyDaysDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Сумма просроченной дебиторской задолженности
     * В т.ч. Просроч. по гос. контрактам
     */
    @JsonAlias("17")
    private String pastDueDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Сумма просроченной дебиторской задолженности
     * В т.ч. Просроч. с обеспечением
     */
    @JsonAlias("18")
    private String hardPastDueDebt;

    /**
     * Сумма дебиторской задолженности на дату отчета
     * Сумма просроченной дебиторской задолженности
     * В т.ч. Просроч. судебные разбирательства
     */
    @JsonAlias("19")
    private String litigationPastDueDebt;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getLitigation() {
        return litigation;
    }

    public void setLitigation(String litigation) {
        this.litigation = litigation;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getDebtOnDate() {
        return debtOnDate;
    }

    public void setDebtOnDate(String debtOnDate) {
        this.debtOnDate = debtOnDate;
    }

    public String getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(String totalDebt) {
        this.totalDebt = totalDebt;
    }

    public String getTotalGowDebt() {
        return totalGowDebt;
    }

    public void setTotalGowDebt(String totalGowDebt) {
        this.totalGowDebt = totalGowDebt;
    }

    public String getAllowedDebt() {
        return allowedDebt;
    }

    public void setAllowedDebt(String allowedDebt) {
        this.allowedDebt = allowedDebt;
    }

    public String getTotalPastDueDebt() {
        return totalPastDueDebt;
    }

    public void setTotalPastDueDebt(String totalPastDueDebt) {
        this.totalPastDueDebt = totalPastDueDebt;
    }

    public String getPastDueTenDaysDebt() {
        return pastDueTenDaysDebt;
    }

    public void setPastDueTenDaysDebt(String pastDueTenDaysDebt) {
        this.pastDueTenDaysDebt = pastDueTenDaysDebt;
    }

    public String getPastDueTenToThirtyDaysDebt() {
        return pastDueTenToThirtyDaysDebt;
    }

    public void setPastDueTenToThirtyDaysDebt(String pastDueTenToThirtyDaysDebt) {
        this.pastDueTenToThirtyDaysDebt = pastDueTenToThirtyDaysDebt;
    }

    public String getPastDueThirtyToNinetyDaysDebt() {
        return pastDueThirtyToNinetyDaysDebt;
    }

    public void setPastDueThirtyToNinetyDaysDebt(String pastDueThirtyToNinetyDaysDebt) {
        this.pastDueThirtyToNinetyDaysDebt = pastDueThirtyToNinetyDaysDebt;
    }

    public String getPastDueOverNinetyDaysDebt() {
        return pastDueOverNinetyDaysDebt;
    }

    public void setPastDueOverNinetyDaysDebt(String pastDueOverNinetyDaysDebt) {
        this.pastDueOverNinetyDaysDebt = pastDueOverNinetyDaysDebt;
    }

    public String getPastDueDebt() {
        return pastDueDebt;
    }

    public void setPastDueDebt(String pastDueDebt) {
        this.pastDueDebt = pastDueDebt;
    }

    public String getHardPastDueDebt() {
        return hardPastDueDebt;
    }

    public void setHardPastDueDebt(String hardPastDueDebt) {
        this.hardPastDueDebt = hardPastDueDebt;
    }

    public String getLitigationPastDueDebt() {
        return litigationPastDueDebt;
    }

    public void setLitigationPastDueDebt(String litigationPastDueDebt) {
        this.litigationPastDueDebt = litigationPastDueDebt;
    }


    @Override
    @JsonIgnore
    public ExcelRowCombiner createCombiner() {
        return new KpModelRowCombiner();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KpModel model = (KpModel) o;
        return Objects.equals(department, model.department) &&
                Objects.equals(manager, model.manager) &&
                Objects.equals(counterparty, model.counterparty) &&
                Objects.equals(litigation, model.litigation) &&
                Objects.equals(contractName, model.contractName) &&
                Objects.equals(date, model.date) &&
                Objects.equals(limit, model.limit) &&
                Objects.equals(debtOnDate, model.debtOnDate) &&
                Objects.equals(totalDebt, model.totalDebt) &&
                Objects.equals(totalGowDebt, model.totalGowDebt) &&
                Objects.equals(allowedDebt, model.allowedDebt) &&
                Objects.equals(totalPastDueDebt, model.totalPastDueDebt) &&
                Objects.equals(pastDueTenDaysDebt, model.pastDueTenDaysDebt) &&
                Objects.equals(pastDueTenToThirtyDaysDebt, model.pastDueTenToThirtyDaysDebt) &&
                Objects.equals(pastDueThirtyToNinetyDaysDebt, model.pastDueThirtyToNinetyDaysDebt) &&
                Objects.equals(pastDueOverNinetyDaysDebt, model.pastDueOverNinetyDaysDebt) &&
                Objects.equals(pastDueDebt, model.pastDueDebt) &&
                Objects.equals(hardPastDueDebt, model.hardPastDueDebt) &&
                Objects.equals(litigationPastDueDebt, model.litigationPastDueDebt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(department, manager, counterparty, litigation, contractName, date, limit, debtOnDate, totalDebt, totalGowDebt, allowedDebt, totalPastDueDebt, pastDueTenDaysDebt, pastDueTenToThirtyDaysDebt, pastDueThirtyToNinetyDaysDebt, pastDueOverNinetyDaysDebt, pastDueDebt, hardPastDueDebt, litigationPastDueDebt);
    }

    @Override
    public String toString() {
        return "KpModel{" +
                "department='" + department + '\'' +
                ", manager='" + manager + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", litigation='" + litigation + '\'' +
                ", contractName='" + contractName + '\'' +
                ", date='" + date + '\'' +
                ", limit='" + limit + '\'' +
                ", debtOnDate='" + debtOnDate + '\'' +
                ", totalDebt='" + totalDebt + '\'' +
                ", totalGowDebt='" + totalGowDebt + '\'' +
                ", allowedDebt='" + allowedDebt + '\'' +
                ", totalPastDueDebt='" + totalPastDueDebt + '\'' +
                ", pastDueTenDaysDebt='" + pastDueTenDaysDebt + '\'' +
                ", pastDueTenToThirtyDaysDebt='" + pastDueTenToThirtyDaysDebt + '\'' +
                ", pastDueThirtyToNinetyDaysDebt='" + pastDueThirtyToNinetyDaysDebt + '\'' +
                ", pastDueOverToNinetyDaysDebt='" + pastDueOverNinetyDaysDebt + '\'' +
                ", pastDueDebt='" + pastDueDebt + '\'' +
                ", hardPastDueDebt='" + hardPastDueDebt + '\'' +
                ", litigationPastDueDebt='" + litigationPastDueDebt + '\'' +
                '}';
    }
}
