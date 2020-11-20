package com.kpmg.agata.models.combinable;

import com.kpmg.agata.parser.combiner.ExcelRowCombiner;

public interface Combinable {
    ExcelRowCombiner createCombiner();
}
