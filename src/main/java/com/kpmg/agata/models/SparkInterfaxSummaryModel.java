package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkInterfaxSummaryModel extends AbstractClientDataModel {

    @JsonAlias({"Наименование контрагента"})
    private String name;

    @JsonAlias({"ИНН контрагента"})
    private String inn;

    @JsonAlias({"Статус"})
    private String status;

    @JsonAlias({"Кредитный лимит (по данным spark-interfax)"})
    private String sparkInterfaxCreditLimit;

    @JsonAlias({"Индекс Должной Осмотрительности"})
    private String cautionIndex;

    @JsonAlias({"Индекс Финансового Риска"})
    private String financialRiskIndex;

    @JsonAlias({"Индекс Платежной Дисциплины"})
    private String paymentDisciplineIndex;

    @JsonAlias({"Факторы риска", "Сводный индикатор риска"})
    private String riskFactors;

    @JsonAlias({"Негативные реестры ", "Реестры СПАРК"})
    private String negativeRegistries;

    @JsonAlias({"Залоги"})
    private String pledges;

    @JsonAlias({"Количество судебных дел, по которым контрагент выступает ответчиком за последние 2 года"})
    private String legalCasesCountTwoYears;

    @JsonAlias({"Сумма исков за последние 2 года",
            "Сумма исков, по которым контрагент выступает ответчиком за последние 2 года"})
    private String legalCasesClaimsSumTwoYears;

    @JsonAlias({"Сумма решений по искам за последние 2 года",
            "Сумма решений по искам, по которым контрагент выступает ответчиком за последние 2 года"})
    private String legalCasesDecisionsSumTwoYears;

    @JsonAlias({"Новость 1 из новостного агрегатора"})
    private String news1;

    @JsonAlias({"Новость 2 из новостного агрегатора"})
    private String news2;

    @JsonAlias({"Новость 3 из новостного агрегатора"})
    private String news3;

    @JsonAlias({"Новость 4 из новостного агрегатора"})
    private String news4;

    @JsonAlias(
            {"Сведения из реестра недобросовестных поставщиков (подрядчиков, исполнителей) и реестра недобросовестных подрядных организаций"})
    private String isDishonestSupplier;

    // version 2 columns

    @JsonAlias({"Дата регистрации (возраст компании, лет)"})
    private String registrationDate;

    @JsonAlias({"Регион регистрации"})
    private String registrationRegion;

    @JsonAlias({"Адрес контрагента"})
    private String counterpartyAddress;

    @JsonAlias({"Рабочий сайт"})
    private String site;

    @JsonAlias({"Вид деятельности"})
    private String activityType;

    @JsonAlias({"Код основного вида деятельности"})
    private String activityCode;

    @JsonAlias({"Руководитель (должность)"})
    private String chiefPosition;

    @JsonAlias({"Форма собственности"})
    private String propertyType;


    @JsonAlias({"Размер предприятия"})
    private String companySize;

    @JsonAlias({"Среднесписочная численность работников"})
    private String employeesCount;

    @JsonAlias({"Важная информация "})
    private String info;

    @JsonAlias({"Наличие сведений в Едином Федеральном реестре о банкротстве"})
    private String isInBankruptcyRegister;

    @JsonAlias({"Ссылка на карточку  в Едином Федеральном реестре о банкротстве"})
    private String bankruptcyRegisterLink;

    @JsonAlias({"Дисквалифицированные лица в составе исполнительных органов"})
    private String disqualifiedPersons;

    @JsonAlias({"Руководитель контрагента включен в реестр ФНС как массовый"})
    private String chiefIsInFnsRegistry;

    @JsonAlias({"Сумма исполнительных производств"})
    private String bailiffCasesSum;


    @JsonAlias({"Сумма недоимки по налогам и сборам"})
    private String arrears;

    @JsonAlias({"Сумма штрафов, RUB"})
    private String penaltySum;

    @JsonAlias({"Задолженность по пеням и штрафам"})
    private String penaltyDebtSum;

    @JsonAlias({"Последний доступный отчетный период"})
    private String lastReportPeriod;

    @JsonAlias({"Выручка за последний доступный отчетный период"})
    private String lastReportPeriodRevenue;

    @JsonAlias({"Чистая прибыль (убыток) за последний доступный отчетный период"})
    private String lastReportPeriodNetProfit;

    @JsonAlias({"Чистые активы за последний доступный отчетный период"})
    private String lastReportPeriodNetAssets;

    @JsonAlias({"Основные средства за последний доступный отчетный период"})
    private String lastReportPeriodMainAssets;


    @JsonAlias({"Период, предшествующий последнему доступному"})
    private String preLastReportPeriod;

    @JsonAlias({"Выручка за период, предшествующий последнему доступному"})
    private String preLastReportPeriodRevenue;

    @JsonAlias({" Чистая прибыль (убыток) за период, предшествующий последнему доступному"})
    private String preLastReportPeriodNetProfit;

    @JsonAlias({"Чистые активы за период, предшествующий последнему доступному"})
    private String preLastReportPeriodNetAssets;

    @JsonAlias({"Основные средства за период, предшествующий последнему доступному"})
    private String preLastReportPeriodMainAssets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSparkInterfaxCreditLimit() {
        return sparkInterfaxCreditLimit;
    }

    public void setSparkInterfaxCreditLimit(String sparkInterfaxCreditLimit) {
        this.sparkInterfaxCreditLimit = sparkInterfaxCreditLimit;
    }

    public String getCautionIndex() {
        return cautionIndex;
    }

    public void setCautionIndex(String cautionIndex) {
        this.cautionIndex = cautionIndex;
    }

    public String getFinancialRiskIndex() {
        return financialRiskIndex;
    }

    public void setFinancialRiskIndex(String financialRiskIndex) {
        this.financialRiskIndex = financialRiskIndex;
    }

    public String getPaymentDisciplineIndex() {
        return paymentDisciplineIndex;
    }

    public void setPaymentDisciplineIndex(String paymentDisciplineIndex) {
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

    public String getPledges() {
        return pledges;
    }

    public void setPledges(String pledges) {
        this.pledges = pledges;
    }

    public String getLegalCasesCountTwoYears() {
        return legalCasesCountTwoYears;
    }

    public void setLegalCasesCountTwoYears(String legalCasesCountTwoYears) {
        this.legalCasesCountTwoYears = legalCasesCountTwoYears;
    }

    public String getLegalCasesClaimsSumTwoYears() {
        return legalCasesClaimsSumTwoYears;
    }

    public void setLegalCasesClaimsSumTwoYears(String legalCasesClaimsSumTwoYears) {
        this.legalCasesClaimsSumTwoYears = legalCasesClaimsSumTwoYears;
    }

    public String getLegalCasesDecisionsSumTwoYears() {
        return legalCasesDecisionsSumTwoYears;
    }

    public void setLegalCasesDecisionsSumTwoYears(String legalCasesDecisionsSumTwoYears) {
        this.legalCasesDecisionsSumTwoYears = legalCasesDecisionsSumTwoYears;
    }

    public String getNews1() {
        return news1;
    }

    public void setNews1(String news1) {
        this.news1 = news1;
    }

    public String getNews2() {
        return news2;
    }

    public void setNews2(String news2) {
        this.news2 = news2;
    }

    public String getNews3() {
        return news3;
    }

    public void setNews3(String news3) {
        this.news3 = news3;
    }

    public String getNews4() {
        return news4;
    }

    public void setNews4(String news4) {
        this.news4 = news4;
    }

    public String getIsDishonestSupplier() {
        return isDishonestSupplier;
    }

    public void setIsDishonestSupplier(String isDishonestSupplier) {
        this.isDishonestSupplier = isDishonestSupplier;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationRegion() {
        return registrationRegion;
    }

    public void setRegistrationRegion(String registrationRegion) {
        this.registrationRegion = registrationRegion;
    }

    public String getCounterpartyAddress() {
        return counterpartyAddress;
    }

    public void setCounterpartyAddress(String counterpartyAddress) {
        this.counterpartyAddress = counterpartyAddress;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getChiefPosition() {
        return chiefPosition;
    }

    public void setChiefPosition(String chiefPosition) {
        this.chiefPosition = chiefPosition;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(String employeesCount) {
        this.employeesCount = employeesCount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIsInBankruptcyRegister() {
        return isInBankruptcyRegister;
    }

    public void setIsInBankruptcyRegister(String isInBankruptcyRegister) {
        this.isInBankruptcyRegister = isInBankruptcyRegister;
    }

    public String getBankruptcyRegisterLink() {
        return bankruptcyRegisterLink;
    }

    public void setBankruptcyRegisterLink(String bankruptcyRegisterLink) {
        this.bankruptcyRegisterLink = bankruptcyRegisterLink;
    }

    public String getDisqualifiedPersons() {
        return disqualifiedPersons;
    }

    public void setDisqualifiedPersons(String disqualifiedPersons) {
        this.disqualifiedPersons = disqualifiedPersons;
    }

    public String getChiefIsInFnsRegistry() {
        return chiefIsInFnsRegistry;
    }

    public void setChiefIsInFnsRegistry(String chiefIsInFnsRegistry) {
        this.chiefIsInFnsRegistry = chiefIsInFnsRegistry;
    }

    public String getBailiffCasesSum() {
        return bailiffCasesSum;
    }

    public void setBailiffCasesSum(String bailiffCasesSum) {
        this.bailiffCasesSum = bailiffCasesSum;
    }

    public String getArrears() {
        return arrears;
    }

    public void setArrears(String arrears) {
        this.arrears = arrears;
    }

    public String getPenaltySum() {
        return penaltySum;
    }

    public void setPenaltySum(String penaltySum) {
        this.penaltySum = penaltySum;
    }

    public String getPenaltyDebtSum() {
        return penaltyDebtSum;
    }

    public void setPenaltyDebtSum(String penaltyDebtSum) {
        this.penaltyDebtSum = penaltyDebtSum;
    }

    public String getLastReportPeriod() {
        return lastReportPeriod;
    }

    public void setLastReportPeriod(String lastReportPeriod) {
        this.lastReportPeriod = lastReportPeriod;
    }

    public String getLastReportPeriodRevenue() {
        return lastReportPeriodRevenue;
    }

    public void setLastReportPeriodRevenue(String lastReportPeriodRevenue) {
        this.lastReportPeriodRevenue = lastReportPeriodRevenue;
    }

    public String getLastReportPeriodNetProfit() {
        return lastReportPeriodNetProfit;
    }

    public void setLastReportPeriodNetProfit(String lastReportPeriodNetProfit) {
        this.lastReportPeriodNetProfit = lastReportPeriodNetProfit;
    }

    public String getLastReportPeriodNetAssets() {
        return lastReportPeriodNetAssets;
    }

    public void setLastReportPeriodNetAssets(String lastReportPeriodNetAssets) {
        this.lastReportPeriodNetAssets = lastReportPeriodNetAssets;
    }

    public String getLastReportPeriodMainAssets() {
        return lastReportPeriodMainAssets;
    }

    public void setLastReportPeriodMainAssets(String lastReportPeriodMainAssets) {
        this.lastReportPeriodMainAssets = lastReportPeriodMainAssets;
    }

    public String getPreLastReportPeriod() {
        return preLastReportPeriod;
    }

    public void setPreLastReportPeriod(String preLastReportPeriod) {
        this.preLastReportPeriod = preLastReportPeriod;
    }

    public String getPreLastReportPeriodRevenue() {
        return preLastReportPeriodRevenue;
    }

    public void setPreLastReportPeriodRevenue(String preLastReportPeriodRevenue) {
        this.preLastReportPeriodRevenue = preLastReportPeriodRevenue;
    }

    public String getPreLastReportPeriodNetProfit() {
        return preLastReportPeriodNetProfit;
    }

    public void setPreLastReportPeriodNetProfit(String preLastReportPeriodNetProfit) {
        this.preLastReportPeriodNetProfit = preLastReportPeriodNetProfit;
    }

    public String getPreLastReportPeriodNetAssets() {
        return preLastReportPeriodNetAssets;
    }

    public void setPreLastReportPeriodNetAssets(String preLastReportPeriodNetAssets) {
        this.preLastReportPeriodNetAssets = preLastReportPeriodNetAssets;
    }

    public String getPreLastReportPeriodMainAssets() {
        return preLastReportPeriodMainAssets;
    }

    public void setPreLastReportPeriodMainAssets(String preLastReportPeriodMainAssets) {
        this.preLastReportPeriodMainAssets = preLastReportPeriodMainAssets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SparkInterfaxSummaryModel that = (SparkInterfaxSummaryModel) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(inn, that.inn) &&
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
                Objects.equals(news1, that.news1) &&
                Objects.equals(news2, that.news2) &&
                Objects.equals(news3, that.news3) &&
                Objects.equals(news4, that.news4) &&
                Objects.equals(isDishonestSupplier, that.isDishonestSupplier) &&
                Objects.equals(registrationDate, that.registrationDate) &&
                Objects.equals(registrationRegion, that.registrationRegion) &&
                Objects.equals(counterpartyAddress, that.counterpartyAddress) &&
                Objects.equals(site, that.site) &&
                Objects.equals(activityType, that.activityType) &&
                Objects.equals(activityCode, that.activityCode) &&
                Objects.equals(chiefPosition, that.chiefPosition) &&
                Objects.equals(propertyType, that.propertyType) &&
                Objects.equals(companySize, that.companySize) &&
                Objects.equals(employeesCount, that.employeesCount) &&
                Objects.equals(info, that.info) &&
                Objects.equals(isInBankruptcyRegister, that.isInBankruptcyRegister) &&
                Objects.equals(bankruptcyRegisterLink, that.bankruptcyRegisterLink) &&
                Objects.equals(disqualifiedPersons, that.disqualifiedPersons) &&
                Objects.equals(chiefIsInFnsRegistry, that.chiefIsInFnsRegistry) &&
                Objects.equals(bailiffCasesSum, that.bailiffCasesSum) &&
                Objects.equals(arrears, that.arrears) &&
                Objects.equals(penaltySum, that.penaltySum) &&
                Objects.equals(penaltyDebtSum, that.penaltyDebtSum) &&
                Objects.equals(lastReportPeriod, that.lastReportPeriod) &&
                Objects.equals(lastReportPeriodRevenue, that.lastReportPeriodRevenue) &&
                Objects.equals(lastReportPeriodNetProfit, that.lastReportPeriodNetProfit) &&
                Objects.equals(lastReportPeriodNetAssets, that.lastReportPeriodNetAssets) &&
                Objects.equals(lastReportPeriodMainAssets, that.lastReportPeriodMainAssets) &&
                Objects.equals(preLastReportPeriod, that.preLastReportPeriod) &&
                Objects.equals(preLastReportPeriodRevenue, that.preLastReportPeriodRevenue) &&
                Objects.equals(preLastReportPeriodNetProfit, that.preLastReportPeriodNetProfit) &&
                Objects.equals(preLastReportPeriodNetAssets, that.preLastReportPeriodNetAssets) &&
                Objects.equals(preLastReportPeriodMainAssets, that.preLastReportPeriodMainAssets);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(super.hashCode(), name, inn, status, sparkInterfaxCreditLimit, cautionIndex, financialRiskIndex,
                        paymentDisciplineIndex, riskFactors, negativeRegistries, pledges, legalCasesCountTwoYears,
                        legalCasesClaimsSumTwoYears, legalCasesDecisionsSumTwoYears, news1, news2, news3, news4,
                        isDishonestSupplier, registrationDate, registrationRegion, counterpartyAddress, site,
                        activityType,
                        activityCode, chiefPosition, propertyType, companySize, employeesCount, info,
                        isInBankruptcyRegister,
                        bankruptcyRegisterLink, disqualifiedPersons, chiefIsInFnsRegistry, bailiffCasesSum, arrears,
                        penaltySum,
                        penaltyDebtSum, lastReportPeriod, lastReportPeriodRevenue, lastReportPeriodNetProfit,
                        lastReportPeriodNetAssets, lastReportPeriodMainAssets, preLastReportPeriod,
                        preLastReportPeriodRevenue,
                        preLastReportPeriodNetProfit, preLastReportPeriodNetAssets, preLastReportPeriodMainAssets);
    }

    @Override
    public String toString() {
        return "SparkInterfaxSummaryModel{" +
                "name='" + name + '\'' +
                ", inn='" + inn + '\'' +
                ", status='" + status + '\'' +
                ", sparkInterfaxCreditLimit='" + sparkInterfaxCreditLimit + '\'' +
                ", cautionIndex='" + cautionIndex + '\'' +
                ", financialRiskIndex='" + financialRiskIndex + '\'' +
                ", paymentDisciplineIndex='" + paymentDisciplineIndex + '\'' +
                ", riskFactors='" + riskFactors + '\'' +
                ", negativeRegistries='" + negativeRegistries + '\'' +
                ", pledges='" + pledges + '\'' +
                ", legalCasesCountTwoYears='" + legalCasesCountTwoYears + '\'' +
                ", legalCasesClaimsSumTwoYears='" + legalCasesClaimsSumTwoYears + '\'' +
                ", legalCasesDecisionsSumTwoYears='" + legalCasesDecisionsSumTwoYears + '\'' +
                ", news1='" + news1 + '\'' +
                ", news2='" + news2 + '\'' +
                ", news3='" + news3 + '\'' +
                ", news4='" + news4 + '\'' +
                ", isDishonestSupplier='" + isDishonestSupplier + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", registrationRegion='" + registrationRegion + '\'' +
                ", counterpartyAddress='" + counterpartyAddress + '\'' +
                ", site='" + site + '\'' +
                ", activityType='" + activityType + '\'' +
                ", activityCode='" + activityCode + '\'' +
                ", chiefPosition='" + chiefPosition + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", companySize='" + companySize + '\'' +
                ", employeesCount='" + employeesCount + '\'' +
                ", info='" + info + '\'' +
                ", isInBankruptcyRegister='" + isInBankruptcyRegister + '\'' +
                ", bankruptcyRegisterLink='" + bankruptcyRegisterLink + '\'' +
                ", disqualifiedPersons='" + disqualifiedPersons + '\'' +
                ", chiefIsInFnsRegistry='" + chiefIsInFnsRegistry + '\'' +
                ", bailiffCasesSum='" + bailiffCasesSum + '\'' +
                ", arrears='" + arrears + '\'' +
                ", penaltySum='" + penaltySum + '\'' +
                ", penaltyDebtSum='" + penaltyDebtSum + '\'' +
                ", lastReportPeriod='" + lastReportPeriod + '\'' +
                ", lastReportPeriodRevenue='" + lastReportPeriodRevenue + '\'' +
                ", lastReportPeriodNetProfit='" + lastReportPeriodNetProfit + '\'' +
                ", lastReportPeriodNetAssets='" + lastReportPeriodNetAssets + '\'' +
                ", lastReportPeriodMainAssets='" + lastReportPeriodMainAssets + '\'' +
                ", preLastReportPeriod='" + preLastReportPeriod + '\'' +
                ", preLastReportPeriodRevenue='" + preLastReportPeriodRevenue + '\'' +
                ", preLastReportPeriodNetProfit='" + preLastReportPeriodNetProfit + '\'' +
                ", preLastReportPeriodNetAssets='" + preLastReportPeriodNetAssets + '\'' +
                ", preLastReportPeriodMainAssets='" + preLastReportPeriodMainAssets + '\'' +
                "} " + super.toString();
    }
}
