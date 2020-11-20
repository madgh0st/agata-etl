package com.kpmg.agata.config;

import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.parser.ConfigType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ModelTypeConfiguration implements Serializable {

    private String nameDo;
    private String rawType;
    private ConfigType configType;
    private String glob;
    private String inputDir;

    private AbstractClientDataModel dataModelInstance;
    private Map<String, String> defaultFields = new HashMap<>();

    private String sheetName;
    private String hiveTableName;
    private String pathToParquet;
    private int headerRowNumber = 0;
    private String outputDir;

    public ModelTypeConfiguration() {
    }

    public ModelTypeConfiguration(String nameDo, String rawType, ConfigType configType, String glob, String inputDir,
                                  String sheetName, String hiveTableName, String pathToParquet, int headerRowNumber,
                                  String outputDir) {
        this.nameDo = nameDo;
        this.rawType = rawType;
        this.configType = configType;
        this.glob = glob;
        this.inputDir = inputDir;
        this.sheetName = sheetName;
        this.hiveTableName = hiveTableName;
        this.pathToParquet = pathToParquet;
        this.headerRowNumber = headerRowNumber;
        this.outputDir = outputDir;
    }

    public ModelTypeConfiguration(ModelTypeConfiguration other) {
        this(other.nameDo, other.rawType, other.configType, other.glob, other.inputDir, other.sheetName,
                other.hiveTableName, other.pathToParquet, other.headerRowNumber, other.outputDir);

        this.dataModelInstance = other.dataModelInstance;
        this.defaultFields = other.defaultFields;
    }

    public String getNameDo() {
        return nameDo;
    }

    public void setNameDo(String nameDo) {
        this.nameDo = nameDo;
    }

    public String getRawType() {
        return rawType;
    }

    public void setRawType(String rawType) {
        this.rawType = rawType;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }

    public String getGlob() {
        return glob;
    }

    public void setGlob(String glob) {
        this.glob = glob;
    }

    public String getInputDir() {
        return inputDir;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getHiveTableName() {
        return hiveTableName;
    }

    public void setHiveTableName(String hiveTableName) {
        this.hiveTableName = hiveTableName;
    }

    public String getPathToParquet() {
        return pathToParquet;
    }

    public void setPathToParquet(String pathToParquet) {
        this.pathToParquet = pathToParquet;
    }

    public int getHeaderRowNumber() {
        return headerRowNumber;
    }

    public void setHeaderRowNumber(int headerRowNumber) {
        this.headerRowNumber = headerRowNumber;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public AbstractClientDataModel getDataModelInstance() {
        return dataModelInstance;
    }

    public void setDataModelInstance(AbstractClientDataModel dataModelInstance) {
        this.dataModelInstance = dataModelInstance;
    }

    public Map<String, String> getDefaultFields() {
        return defaultFields;
    }

    public void setDefaultFields(Map<String, String> defaultFields) {
        this.defaultFields = defaultFields;
    }

    @Override
    public String toString() {
        return "ModelTypeConfiguration{" +
                "nameDo='" + nameDo + '\'' +
                ", rawType='" + rawType + '\'' +
                ", configType=" + configType +
                ", glob='" + glob + '\'' +
                ", inputDir='" + inputDir + '\'' +
                ", dataModelInstance=" + dataModelInstance +
                ", defaultFields=" + defaultFields +
                ", sheetName='" + sheetName + '\'' +
                ", hiveTableName='" + hiveTableName + '\'' +
                ", pathToParquet='" + pathToParquet + '\'' +
                ", headerRowNumber=" + headerRowNumber +
                ", outputDir='" + outputDir + '\'' +
                '}';
    }
}
