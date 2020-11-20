package com.kpmg.agata.parser;

import com.kpmg.agata.parser.combiner.BmModelRowCombiner;
import com.kpmg.agata.parser.combiner.ExcelRowCombiner;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BmModelRowCombinerTest {
    private static final String COUNTERPARTY_PERIOD_HEADER = "Контрагент/Период";
    private static final String COUNTERPARTY_HEADER = "Контрагент";
    private static final String PERIOD_HEADER = "Период";
    private static final String TOTAL_DEBT_HEADER = "Общая задолженность";
    private static final String DEBT_HEADER = "Задолженность";
    private static final String PAST_DUE_HEADER = "Просроченная задолженность";
    private static final String PENALTY_HEADER = "Сумма штрафа и пени";

    private static final Map<String, String> COUNTERPARTY_ROW = new HashMap<String, String>() {{
        put(COUNTERPARTY_PERIOD_HEADER, "counterparty 1");
    }};

    private static final Map<String, String> DATA_ROW = new HashMap<String, String>() {{
        put(COUNTERPARTY_PERIOD_HEADER, "01.01.1970");
        put(TOTAL_DEBT_HEADER, "1");
        put(DEBT_HEADER, "2");
        put(PAST_DUE_HEADER, "3");
        put(PENALTY_HEADER, "4");
    }};

    private static final Map<String, String> EXPECTED_ROW = new HashMap<String, String>() {{
        put(COUNTERPARTY_HEADER, "counterparty 1");
        put(PERIOD_HEADER, "01.01.1970");
        put(TOTAL_DEBT_HEADER, "1");
        put(DEBT_HEADER, "2");
        put(PAST_DUE_HEADER, "3");
        put(PENALTY_HEADER, "4");
    }};

    private static final Map<String, String> TOTAL_ROW = Collections.singletonMap(COUNTERPARTY_PERIOD_HEADER, "Итого");

    private ExcelRowCombiner combiner;

    @Before
    public void before() {
        combiner = new BmModelRowCombiner();
    }

    @Test
    public void combinerTest() {
        assertFalse(combiner.ready());
        combiner.add(COUNTERPARTY_ROW);
        assertFalse(combiner.ready());
        combiner.add(DATA_ROW);
        assertTrue(combiner.ready());
        assertEquals(EXPECTED_ROW, combiner.combine());
        assertFalse(combiner.ready());
        combiner.add(TOTAL_ROW);
        assertFalse(combiner.ready());
    }

    @Test(expected = IllegalStateException.class)
    public void emptyCombinerCombiningTest() {
        assertFalse(combiner.ready());
        combiner.combine();
    }

    @Test(expected = IllegalStateException.class)
    public void notReadyCombinerCombineTest() {
        combiner.add(COUNTERPARTY_ROW);

        assertFalse(combiner.ready());
        combiner.combine();
    }

    @Test(expected = IllegalStateException.class)
    public void readyCombinerAddDataTest() {
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(DATA_ROW);

        combiner.add(DATA_ROW);
    }

    @Test(expected = IllegalStateException.class)
    public void readyCombinerAddCounterpartyTest() {
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(DATA_ROW);

        combiner.add(COUNTERPARTY_ROW);
    }

    @Test(expected = IllegalStateException.class)
    public void readyCombinerAddTotalTest() {
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(DATA_ROW);

        combiner.add(TOTAL_ROW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmptyRowTest() {
        combiner.add(emptyMap());
    }

    @Test(expected = IllegalStateException.class)
    public void doubleCombiningTest() {
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(DATA_ROW);
        combiner.combine();
        combiner.combine();
    }

    @Test
    public void skipAllAfterTotalTest() {
        combiner.add(TOTAL_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(DATA_ROW);

        assertFalse(combiner.ready());
    }

    @Test
    public void sameCounterpartyCombinerTest() {
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(DATA_ROW);

        assertEquals(EXPECTED_ROW, combiner.combine());

        combiner.add(DATA_ROW);
        assertEquals(EXPECTED_ROW, combiner.combine());
    }

    @Test
    public void newCounterpartyCombinerTest() {
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(DATA_ROW);

        assertEquals(EXPECTED_ROW, combiner.combine());

        combiner.add(COUNTERPARTY_ROW);
        combiner.add(DATA_ROW);
        assertEquals(EXPECTED_ROW, combiner.combine());
    }
}
