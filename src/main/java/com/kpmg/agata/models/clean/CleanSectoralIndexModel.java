package com.kpmg.agata.models.clean;

import java.util.Objects;

public class CleanSectoralIndexModel extends AbstractCleanDataModel {

    private String id;
    private Double close;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanSectoralIndexModel that = (CleanSectoralIndexModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(close, that.close);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, close);
    }

    @Override
    public String toString() {
        return "CleanSectoralIndexModel{" +
                "id='" + id + '\'' +
                ", close=" + close +
                "} " + super.toString();
    }
}
