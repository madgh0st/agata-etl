package com.kpmg.agata.parser;

import com.kpmg.agata.parser.combiner.CellCache;
import org.apache.poi.ss.util.CellReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CellCacheTest {
    private static final CellReference VALUE_POINT = new CellReference(0, 0);
    private static final CellReference NO_VALUE_POINT = new CellReference(1, 1);
    private static final CellReference ANOTHER_VALUE_POINT = new CellReference(2, 2);

    @Test
    public void cellCacheTest() {
        CellCache cache = new CellCache()
                .subscribeToPoint(VALUE_POINT);
        cache.consume(VALUE_POINT, "value");

        assertEquals("value", cache.get(VALUE_POINT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotSubscribedPointTest() {
        CellCache cache = new CellCache();

        cache.get(NO_VALUE_POINT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotConsumedPointTest() {
        CellCache cache = new CellCache()
                .subscribeToPoint(NO_VALUE_POINT);

        cache.get(NO_VALUE_POINT);
    }

    @Test
    public void consumeOnePointWithoutErrorTest() {
        CellCache cache = new CellCache()
                .subscribeToPoint(VALUE_POINT);
        cache.consume(VALUE_POINT, "value 1");
        cache.consume(VALUE_POINT, "value 2");

        assertEquals("value 2", cache.get(VALUE_POINT));
    }

    @Test
    public void consumeSeveralPointsTest() {
        CellCache cache = new CellCache()
                .subscribeToPoint(VALUE_POINT);
        cache.consume(ANOTHER_VALUE_POINT, "value 1");
        cache.consume(VALUE_POINT, "value 2");

        assertEquals("value 2", cache.get(VALUE_POINT));
    }

    @Test
    public void subscribeSeveralPointsTest() {
        CellCache cache = new CellCache()
                .subscribeToPoint(VALUE_POINT)
                .subscribeToPoint(ANOTHER_VALUE_POINT);
        cache.consume(ANOTHER_VALUE_POINT, "value");

        assertEquals("value", cache.get(ANOTHER_VALUE_POINT));
    }

    @Test
    public void containsValueTest() {
        CellCache cache = new CellCache()
                .subscribeToPoint(VALUE_POINT);

        assertFalse(cache.containsValueInPoint(VALUE_POINT));
        assertFalse(cache.containsValueInPoint(ANOTHER_VALUE_POINT));

        cache.consume(VALUE_POINT, "value");

        assertTrue(cache.containsValueInPoint(VALUE_POINT));
        assertFalse(cache.containsValueInPoint(ANOTHER_VALUE_POINT));
    }
}
