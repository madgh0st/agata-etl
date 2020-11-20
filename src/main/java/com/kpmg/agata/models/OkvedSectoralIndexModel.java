package com.kpmg.agata.models;

import java.io.Serializable;
import java.util.Objects;

public class OkvedSectoralIndexModel implements Serializable {

    /**
     * ОКВЭД - Общероссийский классификатор видов экономической деятельности
     */
    private String okved;
    private String sectoralIndexId;

    public OkvedSectoralIndexModel() {
    }

    public OkvedSectoralIndexModel(String okved, String sectoralIndexId) {
        this.okved = okved;
        this.sectoralIndexId = sectoralIndexId;
    }

    public String getOkved() {
        return okved;
    }

    public void setOkved(String okved) {
        this.okved = okved;
    }

    public String getSectoralIndexId() {
        return sectoralIndexId;
    }

    public void setSectoralIndexId(String sectoralIndexId) {
        this.sectoralIndexId = sectoralIndexId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OkvedSectoralIndexModel that = (OkvedSectoralIndexModel) o;
        return Objects.equals(okved, that.okved) &&
                Objects.equals(sectoralIndexId, that.sectoralIndexId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(okved, sectoralIndexId);
    }

    @Override
    public String toString() {
        return "OkvedSectoralIndex{" +
                "okved='" + okved + '\'' +
                ", sectoralIndexId='" + sectoralIndexId + '\'' +
                '}';
    }
}
