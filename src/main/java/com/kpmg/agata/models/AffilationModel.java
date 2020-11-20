package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AffilationModel extends AbstractClientDataModel {

    @JsonAlias("Наименование")
    private String fullName;

    @JsonAlias("Регистрационный номер")
    private String ogrn;

    @JsonAlias("Краткое наименование")
    private String shortName;

    @JsonAlias("Наименование полное")
    private String name;

    @JsonAlias("Руководитель - ФИО")
    private String chief;

    @JsonAlias("Руководитель - должность")
    private String chiefPosition;

    @JsonAlias("Руководитель - ИНН")
    private String chiefInn;

    @JsonAlias("Возраст компании, лет")
    private String age;

    @JsonAlias("Статус")
    private String status;

    @JsonAlias("Код налогоплательщика")
    private String inn;

    @JsonAlias("Вид деятельности/отрасль")
    private String activitySector;

    @JsonAlias("Код основного вида деятельности")
    private String okved;

    @JsonAlias("Ранее использованные ИНН")
    private String previousInns;

    @JsonAlias("Важная информация")
    private String info;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChief() {
        return chief;
    }

    public void setChief(String chief) {
        this.chief = chief;
    }

    public String getChiefPosition() {
        return chiefPosition;
    }

    public void setChiefPosition(String chiefPosition) {
        this.chiefPosition = chiefPosition;
    }

    public String getChiefInn() {
        return chiefInn;
    }

    public void setChiefInn(String chiefInn) {
        this.chiefInn = chiefInn;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getActivitySector() {
        return activitySector;
    }

    public void setActivitySector(String activitySector) {
        this.activitySector = activitySector;
    }

    public String getOkved() {
        return okved;
    }

    public void setOkved(String okved) {
        this.okved = okved;
    }

    public String getPreviousInns() {
        return previousInns;
    }

    public void setPreviousInns(String previousInns) {
        this.previousInns = previousInns;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AffilationModel that = (AffilationModel) o;
        return Objects.equals(fullName, that.fullName) &&
                Objects.equals(ogrn, that.ogrn) &&
                Objects.equals(shortName, that.shortName) &&
                Objects.equals(name, that.name) &&
                Objects.equals(chief, that.chief) &&
                Objects.equals(chiefPosition, that.chiefPosition) &&
                Objects.equals(chiefInn, that.chiefInn) &&
                Objects.equals(age, that.age) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inn, that.inn) &&
                Objects.equals(activitySector, that.activitySector) &&
                Objects.equals(okved, that.okved) &&
                Objects.equals(previousInns, that.previousInns) &&
                Objects.equals(info, that.info);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(super.hashCode(), fullName, ogrn, shortName, name, chief, chiefPosition, chiefInn, age, status,
                        inn, activitySector, okved, previousInns, info);
    }

    @Override
    public String toString() {
        return "AffilationModel{" +
                "fullName='" + fullName + '\'' +
                ", ogrn='" + ogrn + '\'' +
                ", shortName='" + shortName + '\'' +
                ", name='" + name + '\'' +
                ", chief='" + chief + '\'' +
                ", chiefPosition='" + chiefPosition + '\'' +
                ", chiefInn='" + chiefInn + '\'' +
                ", age='" + age + '\'' +
                ", status='" + status + '\'' +
                ", inn='" + inn + '\'' +
                ", activitySector='" + activitySector + '\'' +
                ", okved='" + okved + '\'' +
                ", previousInns='" + previousInns + '\'' +
                ", info='" + info + '\'' +
                "} " + super.toString();
    }
}
