package com.kpmg.agata.models;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"fileName", "sheetName", "dateModified",
        "erpCode", "name", "fullName", "inn", "isBuyer", "isSupplier", "ogrn", "kpp",
        "personType", "isForeignResident", "region", "gpnClass", "deleteMark", "status"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterpartyDictionaryModel extends AbstractClientDataModel {
    public CounterpartyDictionaryModel() {
    }

    @JsonAlias({"Код"})
    private String erpCode;

    @JsonAlias({"Наименование"})
    private String name;

    @JsonAlias({"НаименованиеПолное"})
    private String fullName;

    @JsonAlias({"ИНН"})
    private String inn;

    @JsonAlias({"Покупатель", "бит_Грузополучатель"})
    private String isBuyer;

    @JsonAlias({"Поставщик", "бит_Грузоотправитель"})
    private String isSupplier;

    @JsonAlias({"ОГРН", "АЛП_ОГРН"})
    private String ogrn;

    @JsonAlias({"КПП"})
    private String kpp;

    @JsonAlias({"ЮрФизЛицо"})
    private String personType;

    @JsonAlias({"НеЯвляетсяРезидентом", "бит_Нерезидент"})
    private String isForeignResident;

    @JsonAlias({"Регион"})
    private String region;

    @JsonAlias({"ГПН_Класс"})
    private String gpnClass;

    @JsonAlias({"ПометкаУдаления"})
    private String deleteMark;

    @JsonAlias({"Статус"})
    private String status;

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getIsBuyer() {
        return isBuyer;
    }

    public void setIsBuyer(String isBuyer) {
        this.isBuyer = isBuyer;
    }

    public String getIsSupplier() {
        return isSupplier;
    }

    public void setIsSupplier(String isSupplier) {
        this.isSupplier = isSupplier;
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

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getIsForeignResident() {
        return isForeignResident;
    }

    public void setIsForeignResident(String isForeignResident) {
        this.isForeignResident = isForeignResident;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getGpnClass() {
        return gpnClass;
    }

    public void setGpnClass(String gpnClass) {
        this.gpnClass = gpnClass;
    }

    public String getDeleteMark() {
        return deleteMark;
    }

    public void setDeleteMark(String deleteMark) {
        this.deleteMark = deleteMark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MdModel{" +
                "erpCode='" + erpCode + '\'' +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", inn='" + inn + '\'' +
                ", isBuyer='" + isBuyer + '\'' +
                ", isSupplier='" + isSupplier + '\'' +
                ", ogrn='" + ogrn + '\'' +
                ", kpp='" + kpp + '\'' +
                ", personType='" + personType + '\'' +
                ", isForeignResident='" + isForeignResident + '\'' +
                ", region='" + region + '\'' +
                ", gpnClass='" + gpnClass + '\'' +
                ", deleteMark='" + deleteMark + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
