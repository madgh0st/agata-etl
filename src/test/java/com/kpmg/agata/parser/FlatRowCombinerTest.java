package com.kpmg.agata.parser;

import com.kpmg.agata.parser.combiner.ExcelRowCombiner;
import com.kpmg.agata.parser.combiner.FlatRowCombiner;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.test.utils.TestUtils.assertException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FlatRowCombinerTest {

    @Test
    public void combinerTest() {
        ExcelRowCombiner combiner = new FlatRowCombiner();

        assertFalse(combiner.ready());

        Map<String, String> row = new HashMap<>();
        row.put("123", "456");
        combiner.add(row);
        assertTrue(combiner.ready());

        assertEquals(row, combiner.combine());
        assertFalse(combiner.ready());
    }

    @Test
    public void emptyCombinerTest() {
        ExcelRowCombiner combiner = new FlatRowCombiner();
        assertFalse(combiner.ready());

        assertException(IllegalStateException.class, combiner::combine);
    }

    @Test
    public void fullCombinerTest() {
        ExcelRowCombiner combiner = new FlatRowCombiner();
        Map<String, String> row = new HashMap<>();
        row.put("123", "456");
        combiner.add(row);

        assertException(IllegalStateException.class, () -> combiner.add(row));
    }
}
