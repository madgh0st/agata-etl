package com.kpmg.agata.processors.logs;

import java.io.Serializable;

public class OutputLogsModel implements Serializable {

    private final String fileName;
    private final String consoleOrContainer;
    private final Long numLine;
    private final Boolean error;
    private final String time;
    private final String logLevel;
    private final String className;
    private final String rawLogs;

    private Long blockNum;

    @SuppressWarnings("squid:S00107")
    OutputLogsModel(String fileName, String consoleOrContainer, Long numLine, Boolean error, String time,
                           String logLevel, String className, String rawLogs, Long blockNum) {
        this.fileName = fileName;
        this.consoleOrContainer = consoleOrContainer;
        this.numLine = numLine;
        this.error = error;
        this.time = time;
        this.logLevel = logLevel;
        this.className = className;
        this.rawLogs = rawLogs;
        this.blockNum = blockNum;
    }

    public String getFileName() {
        return fileName;
    }

    public String getConsoleOrContainer() {
        return consoleOrContainer;
    }

    public Long getNumLine() {
        return numLine;
    }

    public Boolean getError() {
        return error;
    }

    public String getTime() {
        return time;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getClassName() {
        return className;
    }

    public String getRawLogs() {
        return rawLogs;
    }

    public Long getBlockNum() {
        return blockNum;
    }

    @Override
    public String toString() {
        return "OutputLogsModel{" +
                "fileName='" + fileName + '\'' +
                ", consoleOrContainer='" + consoleOrContainer + '\'' +
                ", numLine=" + numLine +
                ", error=" + error +
                ", time='" + time + '\'' +
                ", logLevel='" + logLevel + '\'' +
                ", className='" + className + '\'' +
                ", rawLogs='" + rawLogs + '\'' +
                ", blockNum=" + blockNum + '\'' +
                '}';
    }
}
