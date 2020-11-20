package com.kpmg.agata.parser;

import com.kpmg.agata.parser.combiner.CellCache;
import com.kpmg.agata.parser.combiner.CellCacheConsumer;
import com.kpmg.agata.parser.combiner.ExcelRowCombiner;
import com.kpmg.agata.parser.combiner.RpModelRowCombiner;
import org.apache.poi.ss.util.CellReference;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.test.utils.TestUtils.assertException;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RpModelRowCombinerTest {

    private static final CellReference DATE_3_POINT = new CellReference(3, 4);
    private static final CellReference DATE_2_POINT = new CellReference(4, 4);
    private static final CellReference DATE_1_POINT = new CellReference(5, 4);
    private static final CellReference MEASURE_POINT = new CellReference(6, 4);

    private static final String BUSINESS_UNIT_HEADER = "БЕ";
    private static final String LEGAL_ENTITY_HEADER = "ЮЛ";
    private static final String REGION_HEADER = "Регион";
    private static final String COUNTERPARTY_HEADER = "Контрагент";
    private static final String PDZ_HEADER = "ПДЗ";
    private static final String DATE_HEADER = "Дата";
    private static final String MEASURE_HEADER = "Единица измерения";
    private static final String REPORT_DATE_HEADER = "Отчетная дата";

    private static final String MEASURE_VALUE = "млн руб";
    private static final String DATE_1_VALUE = "01/01/01";
    private static final String DATE_2_VALUE = "02/01/01";
    private static final String DATE_3_VALUE = "03/01/01";
    private static final String REPORT_DATE_VALUE = DATE_3_VALUE;

    private static final Map<String, String> TOTAL_ROW = new HashMap<String, String>() {{
        put(BUSINESS_UNIT_HEADER, "unit 1");
        put(LEGAL_ENTITY_HEADER, "ИТОГО");
        put(REGION_HEADER, "department 1");
        put(COUNTERPARTY_HEADER, "ИТОГО");
        put("ДЗ всего на 1.01", "123");
        put("ДЗ всего на 2.01", "123");
        put("ДЗ всего на 3.01", "123");
        put("ПДЗ на 1.01", "-");
        put("ПДЗ на 2.01", "-");
        put("ПДЗ на 3.01", "-");
    }};
    private static final Map<String, String> DATA_ROW = new HashMap<String, String>() {{
        put(BUSINESS_UNIT_HEADER, "unit 1");
        put(LEGAL_ENTITY_HEADER, "entiry 1");
        put(REGION_HEADER, "department 1");
        put(COUNTERPARTY_HEADER, "counterparty 1");
        put("ДЗ всего на 1.01", "");
        put("ДЗ всего на 2.01", "");
        put("ДЗ всего на 3.01", "");
        put("ПДЗ на 1.01", "123");
        put("ПДЗ на 2.01", "123");
        put("ПДЗ на 3.01", "123");
    }};

    private static final Map<String, String> DESCRIPTION_ROW = singletonMap("desc key", "desk value");

    private static final Map<String, String> EXPECTED_ROW_1 = new HashMap<String, String>() {{
        put(BUSINESS_UNIT_HEADER, "unit 1");
        put(LEGAL_ENTITY_HEADER, "entiry 1");
        put(REGION_HEADER, "department 1");
        put(COUNTERPARTY_HEADER, "counterparty 1");
        put(PDZ_HEADER, "123");
        put(DATE_HEADER, DATE_1_VALUE);
        put(MEASURE_HEADER, MEASURE_VALUE);
        put(REPORT_DATE_HEADER, REPORT_DATE_VALUE);
    }};

    private static final Map<String, String> EXPECTED_ROW_2 = new HashMap<String, String>() {{
        putAll(EXPECTED_ROW_1);
        put(DATE_HEADER, DATE_2_VALUE);
    }};

    private static final Map<String, String> EXPECTED_ROW_3 = new HashMap<String, String>() {{
        putAll(EXPECTED_ROW_1);
        put(DATE_HEADER, DATE_3_VALUE);
    }};

    private ExcelRowCombiner combiner;

    @Before
    public void before() {
        combiner = new RpModelRowCombiner();
        CellCache cache = new CellCache()
                .subscribeToPoint(DATE_1_POINT)
                .subscribeToPoint(DATE_2_POINT)
                .subscribeToPoint(DATE_3_POINT)
                .subscribeToPoint(MEASURE_POINT);

        cache.consume(DATE_1_POINT, DATE_1_VALUE);
        cache.consume(DATE_2_POINT, DATE_2_VALUE);
        cache.consume(DATE_3_POINT, DATE_3_VALUE);
        cache.consume(MEASURE_POINT, MEASURE_VALUE);

        ((CellCacheConsumer) combiner).setCellCache(cache);

        combiner.add(DESCRIPTION_ROW);
    }

    @Test
    public void combinerTest() {
        assertFalse(combiner.ready());
        combiner.add(TOTAL_ROW);
        assertFalse(combiner.ready());
        combiner.add(DATA_ROW);

        assertTrue(combiner.ready());
        assertEquals(EXPECTED_ROW_1, combiner.combine());
        assertEquals(EXPECTED_ROW_2, combiner.combine());
        assertEquals(EXPECTED_ROW_3, combiner.combine());
        assertFalse(combiner.ready());
    }

    @Test(expected = IllegalStateException.class)
    public void emptyCombinerTest() {
        assertFalse(combiner.ready());
        combiner.combine();
    }

    @Test(expected = IllegalStateException.class)
    public void notReadyCombinerCombiningTest() {
        combiner.add(TOTAL_ROW);

        assertFalse(combiner.ready());
        combiner.combine();
    }

    @Test(expected = IllegalStateException.class)
    public void notSubscribedCombinerCombiningTest() {
        ((CellCacheConsumer) combiner).setCellCache(new CellCache());

        assertFalse(combiner.ready());
        combiner.combine();
    }

    @Test(expected = IllegalStateException.class)
    public void readyCombinerAddTest() {
        combiner.add(DATA_ROW);

        assertTrue(combiner.ready());
        combiner.add(DATA_ROW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmptyRowTest() {
        combiner.add(emptyMap());
    }

    @Test
    public void doubleCombiningTest() {
        combiner.add(DATA_ROW);
        combiner.combine();
        combiner.combine();
        combiner.combine();

        assertException(IllegalStateException.class, () -> combiner.combine());
    }
}
