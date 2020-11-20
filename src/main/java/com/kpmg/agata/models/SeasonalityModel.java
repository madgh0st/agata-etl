package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeasonalityModel extends AbstractClientDataModel {

    @JsonAlias("ДО")
    private String seasonalityOfDo;

    @JsonAlias("Месяц")
    private String month;

    @JsonAlias("Сезонность")
    private String seasonality;

    public String getSeasonalityOfDo() {
        return seasonalityOfDo;
    }

    public void setSeasonalityOfDo(String seasonalityOfDo) {
        this.seasonalityOfDo = seasonalityOfDo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSeasonality() {
        return seasonality;
    }

    public void setSeasonality(String seasonality) {
        this.seasonality = seasonality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SeasonalityModel that = (SeasonalityModel) o;
        return Objects.equals(seasonalityOfDo, that.seasonalityOfDo) &&
                Objects.equals(month, that.month) &&
                Objects.equals(seasonality, that.seasonality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), seasonalityOfDo, month, seasonality);
    }

    @Override
    public String toString() {
        return "SeasonalityModel{" +
                "seasonalityOfDo='" + seasonalityOfDo + '\'' +
                ", month='" + month + '\'' +
                ", seasonality='" + seasonality + '\'' +
                "} " + super.toString();
    }
}
