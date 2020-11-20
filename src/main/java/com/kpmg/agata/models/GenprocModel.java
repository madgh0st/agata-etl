package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Objects;

/**
 * Genproc - head of prosecutors office.
 * Contains appearance of legal entities in illegal award registry.
 */
public class GenprocModel extends AbstractClientDataModel {

    /**
     * Полное наименования юридического лица
     */
    @JsonAlias({"company_name"})
    private String companyName;

    /**
     * ИНН - Идентификационный номер налогоплательщика
     */
    private String inn;

    /**
     * ОГРН - Основной государственный регистрационный номер
     */
    private String ogrn;

    /**
     * КПП - Код причины постановки на учет
     */
    private String kpp;

    /**
     * Дата вынесения постановления
     */
    @JsonAlias({"creation_date"})
    private String creationDate;

    /**
     * Дата вступления постановления суда в законную силу
     */
    @JsonAlias({"decision_date"})
    private String decisionDate;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(String decisionDate) {
        this.decisionDate = decisionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenprocModel that = (GenprocModel) o;
        return Objects.equals(companyName, that.companyName) &&
                Objects.equals(inn, that.inn) &&
                Objects.equals(ogrn, that.ogrn) &&
                Objects.equals(kpp, that.kpp) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(decisionDate, that.decisionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), companyName, inn, ogrn, kpp, creationDate, decisionDate);
    }

    @Override
    public String toString() {
        return "GenprocModel{" +
                "companyName='" + companyName + '\'' +
                ", inn='" + inn + '\'' +
                ", ogrn='" + ogrn + '\'' +
                ", kpp='" + kpp + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", decisionDate='" + decisionDate + '\'' +
                "} " + super.toString();
    }
}
