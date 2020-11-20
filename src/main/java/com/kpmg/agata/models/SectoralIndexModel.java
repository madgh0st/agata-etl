package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Objects;

public class SectoralIndexModel extends AbstractClientDataModel {

    @JsonAlias("ID")
    private String id;
    @JsonAlias("NAME")
    private String name;
    @JsonAlias("TRADEDATE")
    private String tradeDate;
    @JsonAlias("HIGH")
    private String high;
    @JsonAlias("LOW")
    private String low;
    @JsonAlias("OPEN")
    private String open;
    @JsonAlias("CLOSE")
    private String close;
    @JsonAlias("VALUE")
    private String value;
    @JsonAlias("DURATION")
    private String duration;
    @JsonAlias("YIELD")
    private String yield;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SectoralIndexModel that = (SectoralIndexModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(tradeDate, that.tradeDate) &&
                Objects.equals(high, that.high) &&
                Objects.equals(low, that.low) &&
                Objects.equals(open, that.open) &&
                Objects.equals(close, that.close) &&
                Objects.equals(value, that.value) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(yield, that.yield);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, tradeDate, high, low, open, close, value, duration, yield);
    }

    @Override
    public String toString() {
        return "SectoralIndexModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tradeDate='" + tradeDate + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", open='" + open + '\'' +
                ", close='" + close + '\'' +
                ", value='" + value + '\'' +
                ", duration='" + duration + '\'' +
                ", yield='" + yield + '\'' +
                "} " + super.toString();
    }
}
