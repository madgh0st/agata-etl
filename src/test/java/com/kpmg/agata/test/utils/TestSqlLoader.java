package com.kpmg.agata.test.utils;

import com.kpmg.agata.utils.sql.SqlLoader;

import java.util.Map;

public class TestSqlLoader extends SqlLoader {

    /**
     * Since Spark 2.2+ it's not possible to assign a view to any database.
     * Therefore, you should fix the script using this method (if you are using views instead of real tables).
     * It removes a prefix with dot sign.
     */
    public TestSqlLoader tablesToViews(String dbPrefix) {
        sql = sql.replace(dbPrefix + ".", "");
        return this;
    }

    @Override
    public TestSqlLoader fromResources(String path) {
        super.fromResources(path);
        return this;
    }

    @Override
    public TestSqlLoader fromPath(String path) {
        super.fromPath(path);
        return this;
    }

    public TestSqlLoader fromString(String sql) {
        this.sql = sql;
        return this;
    }

    @Override
    public TestSqlLoader inject(String key, String value) {
        super.inject(key, value);
        return this;
    }

    @Override
    public TestSqlLoader inject(Map<String, String> injections) {
        super.inject(injections);
        return this;
    }
}
