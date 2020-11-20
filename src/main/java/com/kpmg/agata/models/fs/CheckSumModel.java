package com.kpmg.agata.models.fs;

import java.io.Serializable;

public class CheckSumModel  implements Serializable {

    private String filePath;
    private String fileName;
    private String checkSum;
    private String sourceCategory;

    public CheckSumModel() {
    }

    public CheckSumModel(String filePath, String fileName, String checkSum, String sourceCategory) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.checkSum = checkSum;
        this.sourceCategory = sourceCategory;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getSourceCategory() {
        return sourceCategory;
    }

    public void setSourceCategory(String sourceCategory) {
        this.sourceCategory = sourceCategory;
    }
}
