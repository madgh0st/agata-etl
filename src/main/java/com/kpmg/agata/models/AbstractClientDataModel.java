package com.kpmg.agata.models;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractClientDataModel implements Serializable {

    /**
     * fileName
     */
    @JsonAlias({"fileName"})
    private String fileName;

    /**
     * sheetName
     */
    @JsonAlias({"sheetName"})
    private String sheetName;

    /**
     * modificationDate
     */
    @JsonAlias({"modificationDate"})
    private String modificationDate;

    /**
     * loadDate
     */
    @JsonAlias({"loadDate"})
    private String loadDate;

    private String code;

    private String eventDate;

    private String name_do;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(String loadDate) {
        this.loadDate = loadDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getName_do() {
        return name_do;
    }

    public void setName_do(String name_do) {
        this.name_do = name_do;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractClientDataModel that = (AbstractClientDataModel) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(sheetName, that.sheetName) &&
                Objects.equals(modificationDate, that.modificationDate) &&
                Objects.equals(loadDate, that.loadDate) &&
                Objects.equals(code, that.code) &&
                Objects.equals(eventDate, that.eventDate) &&
                Objects.equals(name_do, that.name_do);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, sheetName, modificationDate, loadDate, code, eventDate, name_do);
    }
}
