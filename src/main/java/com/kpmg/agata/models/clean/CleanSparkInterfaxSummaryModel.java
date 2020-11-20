package com.kpmg.agata.models.clean;

import java.util.Objects;

public class CleanSparkInterfaxSummaryModel extends AbstractCleanDataModel {

    private String counterpartyName;
    private String counterpartyInn;
    /**
     * Enum: ACTIVE, NOT_ACTIVE, REORGANIZATION, LIQUIDATION, LIQUIDATED, BANKRUPT
     */
    private String status;
    private Double sparkInterfaxCreditLimit;
    private Integer cautionIndex;
    private Integer financialRiskIndex;
    private Integer paymentDisciplineIndex;
    /**
     * Enum: LOW, MEDIUM, HIGH
     */
    private String riskFactors;
    private String negativeRegistries;
    private Boolean pledges;
    private Integer legalCasesCountTwoYears;
    private Double legalCasesClaimsSumTwoYears;
    private Double legalCasesDecisionsSumTwoYears;
    private String news;
    private Boolean isDishonestSupplier;

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyInn() {
        return counterpartyInn;
    }

    public void setCounterpartyInn(String counterpartyInn) {
        this.counterpartyInn = counterpartyInn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getSparkInterfaxCreditLimit() {
        return sparkInterfaxCreditLimit;
    }

    public void setSparkInterfaxCreditLimit(Double sparkInterfaxCreditLimit) {
        this.sparkInterfaxCreditLimit = sparkInterfaxCreditLimit;
    }

    public Integer getCautionIndex() {
        return cautionIndex;
    }

    public void setCautionIndex(Integer cautionIndex) {
        this.cautionIndex = cautionIndex;
    }

    public Integer getFinancialRiskIndex() {
        return financialRiskIndex;
    }

    public void setFinancialRiskIndex(Integer financialRiskIndex) {
        this.financialRiskIndex = financialRiskIndex;
    }

    public Integer getPaymentDisciplineIndex() {
        return paymentDisciplineIndex;
    }

    public void setPaymentDisciplineIndex(Integer paymentDisciplineIndex) {
        this.paymentDisciplineIndex = paymentDisciplineIndex;
    }

    public String getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(String riskFactors) {
        this.riskFactors = riskFactors;
    }

    public String getNegativeRegistries() {
        return negativeRegistries;
    }

    public void setNegativeRegistries(String negativeRegistries) {
        this.negativeRegistries = negativeRegistries;
    }

    public Boolean getPledges() {
        return pledges;
    }

    public void setPledges(Boolean pledges) {
        this.pledges = pledges;
    }

    public Integer getLegalCasesCountTwoYears() {
        return legalCasesCountTwoYears;
    }

    public void setLegalCasesCountTwoYears(Integer legalCasesCountTwoYears) {
        this.legalCasesCountTwoYears = legalCasesCountTwoYears;
    }

    public Double getLegalCasesClaimsSumTwoYears() {
        return legalCasesClaimsSumTwoYears;
    }

    public void setLegalCasesClaimsSumTwoYears(Double legalCasesClaimsSumTwoYears) {
        this.legalCasesClaimsSumTwoYears = legalCasesClaimsSumTwoYears;
    }

    public Double getLegalCasesDecisionsSumTwoYears() {
        return legalCasesDecisionsSumTwoYears;
    }

    public void setLegalCasesDecisionsSumTwoYears(Double legalCasesDecisionsSumTwoYears) {
        this.legalCasesDecisionsSumTwoYears = legalCasesDecisionsSumTwoYears;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public Boolean getIsDishonestSupplier() {
        return isDishonestSupplier;
    }

    public void setIsDishonestSupplier(Boolean isDishonestSupplier) {
        this.isDishonestSupplier = isDishonestSupplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanSparkInterfaxSummaryModel that = (CleanSparkInterfaxSummaryModel) o;
        return Objects.equals(counterpartyName, that.counterpartyName) &&
                Objects.equals(counterpartyInn, that.counterpartyInn) &&
                Objects.equals(status, that.status) &&
                Objects.equals(sparkInterfaxCreditLimit, that.sparkInterfaxCreditLimit) &&
                Objects.equals(cautionIndex, that.cautionIndex) &&
                Objects.equals(financialRiskIndex, that.financialRiskIndex) &&
                Objects.equals(paymentDisciplineIndex, that.paymentDisciplineIndex) &&
                Objects.equals(riskFactors, that.riskFactors) &&
                Objects.equals(negativeRegistries, that.negativeRegistries) &&
                Objects.equals(pledges, that.pledges) &&
                Objects.equals(legalCasesCountTwoYears, that.legalCasesCountTwoYears) &&
                Objects.equals(legalCasesClaimsSumTwoYears, that.legalCasesClaimsSumTwoYears) &&
                Objects.equals(legalCasesDecisionsSumTwoYears, that.legalCasesDecisionsSumTwoYears) &&
                Objects.equals(news, that.news) &&
                Objects.equals(isDishonestSupplier, that.isDishonestSupplier);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(counterpartyName, counterpartyInn, status, sparkInterfaxCreditLimit, cautionIndex,
                        financialRiskIndex,
                        paymentDisciplineIndex, riskFactors, negativeRegistries, pledges, legalCasesCountTwoYears,
                        legalCasesClaimsSumTwoYears, legalCasesDecisionsSumTwoYears, news, isDishonestSupplier);
    }

    @Override
    public String toString() {
        return "CleanSparkInterfaxSummaryModel{" +
                "counterpartyName='" + counterpartyName + '\'' +
                ", counterpartyInn='" + counterpartyInn + '\'' +
                ", status='" + status + '\'' +
                ", sparkInterfaxCreditLimit=" + sparkInterfaxCreditLimit +
                ", cautionIndex=" + cautionIndex +
                ", financialRiskIndex=" + financialRiskIndex +
                ", paymentDisciplineIndex=" + paymentDisciplineIndex +
                ", riskFactors='" + riskFactors + '\'' +
                ", negativeRegistries='" + negativeRegistries + '\'' +
                ", pledges=" + pledges +
                ", legalCasesCountTwoYears=" + legalCasesCountTwoYears +
                ", legalCasesClaimsSumTwoYears=" + legalCasesClaimsSumTwoYears +
                ", legalCasesDecisionsSumTwoYears=" + legalCasesDecisionsSumTwoYears +
                ", news='" + news + '\'' +
                ", isDishonestSupplier=" + isDishonestSupplier +
                "} " + super.toString();
    }
}
