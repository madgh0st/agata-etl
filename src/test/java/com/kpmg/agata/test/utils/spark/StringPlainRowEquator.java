package com.kpmg.agata.test.utils.spark;

import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.collections4.Equator;
import org.apache.spark.sql.Row;

public class StringPlainRowEquator implements Equator<Row> {

    private static String getStringFromRow(Row row, String fieldName) {
        Object value = row.getAs(fieldName);
        return value == null ? null : value.toString();
    }

    // todo: skip columns on demand
    @Override
    public boolean equate(Row row, Row other) {
        return Arrays.stream(row.schema().fieldNames())
                     .allMatch(fieldName -> Objects
                             .equals(getStringFromRow(row, fieldName), getStringFromRow(row, fieldName)));
    }

    @Override
    public int hash(Row row) {
        return Arrays.stream(row.schema().fieldNames())
                     .sorted()
                     .map(fieldName -> getStringFromRow(row, fieldName))
                     .filter(Objects::nonNull)
                     .map(String::hashCode)
                     .reduce(0, (acc, hash) -> Objects.hash(acc, hash));
    }
}
