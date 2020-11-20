package com.kpmg.agata;

import com.kpmg.agata.config.Environment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SqlResourceTest {

    @Test
    public void isValidSqlResourceWithoutParamsTest() {
        assertEquals(
                "SELECT '%1$s' AS tableName FROM {dbSchema}.%1$s",
                Environment.getSqlResource("sql/some_sql_resource.sql")
        );
    }

    @Test
    public void isValidSqlResourceWithParamsTest() {
        assertEquals(
                "SELECT 'table1' AS tableName FROM {dbSchema}.table1",
                Environment.getSqlResource("sql/some_sql_resource.sql", "table1", "someArg")
        );
    }
}
