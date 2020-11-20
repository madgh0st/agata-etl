package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public class WhiteListDoModel extends AbstractClientDataModel{

    public WhiteListDoModel() {
    }

    @JsonAlias({"string_name_do"})
    private String string_name_do;

    @JsonAlias({"name_do"})
    private String name_do;

    public String getString_name_do() {
        return string_name_do;
    }

    public void setString_name_do(String string_name_do) {
        this.string_name_do = string_name_do;
    }

    @Override
    public String getName_do() {
        return name_do;
    }

    @Override
    public void setName_do(String name_do) {
        this.name_do = name_do;
    }
}
