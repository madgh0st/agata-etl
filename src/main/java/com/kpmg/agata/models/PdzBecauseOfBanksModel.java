package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PdzBecauseOfBanksModel extends AbstractClientDataModel {

    public PdzBecauseOfBanksModel() {
    }

    @JsonAlias({"Контрагенты"})
    private String counterpartyName;

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    @Override
    public String toString() {
        return "PdzBecauseOfBanksModel{" +
                "counterpartyName='" + counterpartyName + '\'' +
                '}';
    }
}
