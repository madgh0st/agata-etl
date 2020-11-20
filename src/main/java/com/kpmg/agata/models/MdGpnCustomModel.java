package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public class MdGpnCustomModel extends AbstractClientDataModel{

    public MdGpnCustomModel() {
    }

    @JsonAlias({"Контрагент Наименование"})
    private String nameRaw;

    @JsonAlias({"Контрагент ИНН"})
    private String innRaw;

    @JsonAlias({"Предлагаемое наименование"})
    private String nameClean;

    @JsonAlias({"Предлагаемый ИНН"})
    private String innClean;

    public String getNameRaw() {
        return nameRaw;
    }

    public void setNameRaw(String nameRaw) {
        this.nameRaw = nameRaw;
    }

    public String getInnRaw() {
        return innRaw;
    }

    public void setInnRaw(String innRaw) {
        this.innRaw = innRaw;
    }

    public String getNameClean() {
        return nameClean;
    }

    public void setNameClean(String nameClean) {
        this.nameClean = nameClean;
    }

    public String getInnClean() {
        return innClean;
    }

    public void setInnClean(String innClean) {
        this.innClean = innClean;
    }
}
