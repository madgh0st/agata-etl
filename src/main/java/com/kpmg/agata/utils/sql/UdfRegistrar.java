package com.kpmg.agata.utils.sql;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;

import java.sql.Date;

public class UdfRegistrar {

    private UdfRegistrar() {
    }

    public static void register(SparkSession sparkSession) {
        Normalizer norm = new Normalizer();

        sparkSession.udf().register("udf_normalized",
                (UDF1<String, Object>) norm::normalize,
                DataTypes.StringType);
        // todo: rename "valuedate" to "ticks" or "timespamp"
        sparkSession.udf().register("udf_convert_valuedate_to_string",
                (UDF1<String, Object>) norm::getValueDateToString,
                DataTypes.StringType);
        // todo: replace with spark internal functions
        sparkSession.udf().register("udf_convert_to_local_date",
                (UDF1<String, Object>) date -> norm.convertToLocalDate(date) == null
                        ? null : Date.valueOf(norm.convertToLocalDate(date)),
                DataTypes.DateType);
    }
}
