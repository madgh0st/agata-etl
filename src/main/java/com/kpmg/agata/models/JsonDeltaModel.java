package com.kpmg.agata.models;

public class JsonDeltaModel {

    private String fullPathToJsonDeltasFile;
    private String originalNameFile;
    private String targetPath;
    private String targetName;
    private String dateMatch;

    public JsonDeltaModel(String fullPathToJsonDeltasFile, String originalNameFile, String targetPath, String targetName, String dateMatch) {
        this.fullPathToJsonDeltasFile = fullPathToJsonDeltasFile;
        this.originalNameFile = originalNameFile;
        this.targetPath = targetPath;
        this.targetName = targetName;
        this.dateMatch = dateMatch;
    }

    public String getFullPathToJsonDeltasFile() {
        return fullPathToJsonDeltasFile;
    }

    public String getOriginalNameFile() {
        return originalNameFile;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getDateMatch() {
        return dateMatch;
    }
}
