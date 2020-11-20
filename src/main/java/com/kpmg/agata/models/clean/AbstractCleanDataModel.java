package com.kpmg.agata.models.clean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Date;

public abstract class AbstractCleanDataModel implements Serializable {

    private String code;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="GMT+3")
    private Date eventDate;

    private String name_do;

    public AbstractCleanDataModel() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getName_do() {
        return name_do;
    }

    public void setName_do(String name_do) {
        this.name_do = name_do;
    }


}
