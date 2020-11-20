package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Objects;

public class FnsDisqModel extends AbstractClientDataModel {

    /**
     * Полное наименование юридического лица
     */
    @JsonAlias("G1")
    private String organizationFullName;

    /**
     * ОГРН - Основной государственный регистрационный номер
     */
    @JsonAlias("G2")
    private String ogrn;

    /**
     * ИНН - Идентификационный номер налогоплательщика
     */
    @JsonAlias("G3")
    private String inn;

    /**
     * КПП - Код причины постановки на учет
     */
    @JsonAlias("G4")
    private String kpp;

    /**
     * Адрес расположения
     */
    @JsonAlias("G5")
    private String address;

    public String getOrganizationFullName() {
        return organizationFullName;
    }

    public void setOrganizationFullName(String organizationFullName) {
        this.organizationFullName = organizationFullName;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FnsDisqModel that = (FnsDisqModel) o;
        return Objects.equals(organizationFullName, that.organizationFullName) &&
                Objects.equals(ogrn, that.ogrn) &&
                Objects.equals(inn, that.inn) &&
                Objects.equals(kpp, that.kpp) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), organizationFullName, ogrn, inn, kpp, address);
    }

    @Override
    public String toString() {
        return "FnsDisqModel{" +
                "organizationFullName='" + organizationFullName + '\'' +
                ", ogrn='" + ogrn + '\'' +
                ", inn='" + inn + '\'' +
                ", kpp='" + kpp + '\'' +
                ", address='" + address + '\'' +
                "} " + super.toString();
    }
}
