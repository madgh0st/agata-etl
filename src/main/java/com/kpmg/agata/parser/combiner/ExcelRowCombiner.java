package com.kpmg.agata.parser.combiner;

import java.util.Map;

public interface ExcelRowCombiner {
    void add(Map<String, String> row);

    boolean ready();

    Map<String, String> combine();
}
