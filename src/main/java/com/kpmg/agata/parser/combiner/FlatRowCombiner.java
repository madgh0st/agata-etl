package com.kpmg.agata.parser.combiner;

import java.util.Map;

public class FlatRowCombiner implements ExcelRowCombiner {
    private Map<String, String> row;

    @Override
    public void add(Map<String, String> newRow) {
        if (row != null) throw new IllegalStateException("Combiner is already full");
        row = newRow;
    }

    @Override
    public boolean ready() {
        return row != null;
    }

    @Override
    public Map<String, String> combine() {
        if (!ready()) throw new IllegalStateException("Combiner is not ready");

        Map<String, String> result = row;
        row = null;
        return result;
    }

    @Override
    public String toString() {
        return "FlatRowCombiner{" +
                "row=" + row +
                '}';
    }
}
