package com.kpmg.agata.utils.sql.to.hive;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class DataFrameToHiveTaskModel implements ToHiveTaskModel {

    private final Dataset<Row> dataFrame;
    private final String hiveTableName;

    public DataFrameToHiveTaskModel(Dataset<Row> dataFrame, String hiveTableName) {
        this.dataFrame = dataFrame;
        this.hiveTableName = hiveTableName;
    }

    public Dataset<Row> getDataFrame() {
        return dataFrame;
    }

    @Override
    public String getHiveTableName() {
        return hiveTableName;
    }

    @Override
    public String toString() {
        return "DataFrameToHiveModel{" +
                "dataFrame=" + dataFrame +
                ", hiveTableName='" + hiveTableName + '\'' +
                '}';
    }
}
