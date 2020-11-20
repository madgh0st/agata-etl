package com.kpmg.agata.utils.sql;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SqlLoader {

    protected String sql;

    public SqlLoader fromResources(String path) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) throw new IllegalArgumentException("Resource not found: " + path);
            sql = IOUtils.toString(inputStream, UTF_8);
            return this;
        } catch (IOException e) {
            throw new IllegalArgumentException("Resource not found: " + path, e);
        }
    }

    public SqlLoader fromPath(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            sql = IOUtils.toString(inputStream, UTF_8);
            return this;
        } catch (IOException e) {
            throw new IllegalArgumentException("Resource not found: " + path, e);
        }
    }

    public SqlLoader inject(String key, String value) {
        sql = sql.replace("{" + key + "}", value);
        return this;
    }

    public SqlLoader inject(Map<String, String> injections) {
        injections.forEach(this::inject);
        return this;
    }

    public String get() {
        return sql;
    }
}
