package com.kpmg.agata.utils.sql.to.hive;

import com.kpmg.agata.utils.sql.ReplaceModel;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class SqlToHiveTaskModel implements ToHiveTaskModel {

    private final String pathToSqlFile;
    private final String hiveTableName;
    private final List<ReplaceModel> replacers;

    public SqlToHiveTaskModel(String pathToSqlFile, String hiveTableName, List<ReplaceModel> replacers) {
        this.pathToSqlFile = pathToSqlFile;
        this.hiveTableName = hiveTableName;
        this.replacers = replacers;
    }

    public SqlToHiveTaskModel(String pathToSqlFile, String hiveTableName) {
        this(pathToSqlFile, hiveTableName, emptyList());
    }

    public SqlToHiveTaskModel(String pathToSqlFile, String hiveTableName, ReplaceModel replacer) {
        this(pathToSqlFile, hiveTableName, singletonList(replacer));
    }

    public String getPathToSqlFile() {
        return pathToSqlFile;
    }

    @Override
    public String getHiveTableName() {
        return hiveTableName;
    }

    public List<ReplaceModel> getReplacers() {
        return replacers;
    }

    @Override
    public String toString() {
        return "SqlToHiveModel{" +
                "pathToSqlFile='" + pathToSqlFile + '\'' +
                ", hiveTableName='" + hiveTableName + '\'' +
                ", replacers=" + replacers +
                '}';
    }
}
