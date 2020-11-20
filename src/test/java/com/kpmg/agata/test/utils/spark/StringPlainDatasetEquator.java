package com.kpmg.agata.test.utils.spark;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Equator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static java.util.Arrays.asList;

public class StringPlainDatasetEquator {

    private static final Equator<Row> ROW_EQUATOR = new StringPlainRowEquator();

    /**
     * Does not check column type equality in schemas (only column names)
     * It assumes, that one dataFrame may be full String type,
     * and values of another will be casted to String.
     */
    public boolean equals(Dataset<Row> dataFrame, Dataset<Row> other) {
        List<String> dfHeader = asList(dataFrame.schema().fieldNames());
        List<String> otherHeader = asList(other.schema().fieldNames());

        return CollectionUtils.isEqualCollection(dfHeader, otherHeader)
                && CollectionUtils.isEqualCollection(dataFrame.collectAsList(), other.collectAsList(), ROW_EQUATOR);
    }
}
