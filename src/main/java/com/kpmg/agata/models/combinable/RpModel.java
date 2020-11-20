package com.kpmg.agata.models.combinable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.parser.combiner.ExcelRowCombiner;
import com.kpmg.agata.parser.combiner.RpModelRowCombiner;

import java.util.Objects;

public class RpModel extends AbstractClientDataModel implements Combinable {

    @JsonAlias("БЕ")
    private String businessUnit;

    @JsonAlias("ЮЛ")
    private String legalEntity;

    @JsonAlias("Регион")
    private String region;

    @JsonAlias("Контрагент")
    private String counterparty;

    @JsonAlias("ПДЗ")
    private String pdz;

    @JsonAlias("Дата")
    private String date;

    @JsonAlias("Единица измерения")
    private String measure;

    @JsonAlias("Отчетная дата")
    private String reportDate;

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getPdz() {
        return pdz;
    }

    public void setPdz(String pdz) {
        this.pdz = pdz;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    @Override
    @JsonIgnore
    public ExcelRowCombiner createCombiner() {
        return new RpModelRowCombiner();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpModel rpModel = (RpModel) o;
        return Objects.equals(businessUnit, rpModel.businessUnit) &&
                Objects.equals(legalEntity, rpModel.legalEntity) &&
                Objects.equals(region, rpModel.region) &&
                Objects.equals(counterparty, rpModel.counterparty) &&
                Objects.equals(pdz, rpModel.pdz) &&
                Objects.equals(date, rpModel.date) &&
                Objects.equals(measure, rpModel.measure) &&
                Objects.equals(reportDate, rpModel.reportDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessUnit, legalEntity, region, counterparty, pdz, date, measure, reportDate);
    }

    @Override
    public String toString() {
        return "RpModel{" +
                "businessUnit='" + businessUnit + '\'' +
                ", legalEntity='" + legalEntity + '\'' +
                ", region='" + region + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", pdz='" + pdz + '\'' +
                ", date='" + date + '\'' +
                ", measure='" + measure + '\'' +
                ", reportDate='" + reportDate + '\'' +
                '}';
    }
}
