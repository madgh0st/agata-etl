package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"fileName", "sheetName", "dateModified",
        "num", "blockKBE", "counterpartyName", "counterpartyINN", "groupCounterparty"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterpartyKbebModel extends AbstractClientDataModel {
    public CounterpartyKbebModel() {
    }

    /**
     * №п/п
     */
    @JsonAlias({"1"})
    private String num;

    /**
     * Блок / КБЕ
     */
    @JsonAlias({"2"})
    private String blockKBE;

    /**
     * Наименование контрагента
     */
    @JsonAlias({"3"})
    private String counterpartyName;

    /**
     * ИНН контрагента
     */
    @JsonAlias({"4"})
    private String counterpartyINN;

    /**
     * Группа контрагентов
     */
    @JsonAlias({"5"})
    private String groupCounterparty;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getBlockKBE() {
        return blockKBE;
    }

    public void setBlockKBE(String blockKBE) {
        this.blockKBE = blockKBE;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyINN() {
        return counterpartyINN;
    }

    public void setCounterpartyINN(String counterpartyINN) {
        this.counterpartyINN = counterpartyINN;
    }

    public String getGroupCounterparty() {
        return groupCounterparty;
    }

    public void setGroupCounterparty(String groupCounterparty) {
        this.groupCounterparty = groupCounterparty;
    }
}
