package com.kpmg.agata.parser;

import com.kpmg.agata.parser.combiner.ExcelRowCombiner;
import com.kpmg.agata.parser.combiner.MbModelRowCombiner;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MbModelRowCombinerTest {
    private static final String CUR_CP_CONT_DEAL_HEADER = "Валюта/Контрагент, Код/Договор контрагента, Код/Сделка";
    private static final String CURRENCY_ORDER_CP_HEADER = "Валюта/Контрагент заказа";
    private static final String DEAL_DATE_HEADER = "Дата сделки";
    private static final String DEPARTMENT_HEADER = "Подразделение";
    private static final String PAST_DUE_DAYS_HEADER = "Просрочка, дней";
    private static final String DELAY_DAYS_HEADER = "Дней отсрочки";
    private static final String DEBT_WITHIN_DELAY_HEADER = "Сумма ДЗ в валюте договора/Сумма в пределах отсрочки";
    private static final String PAST_DUE_5_HEADER = "Сумма ДЗ в валюте договора/Сумма просрочки <5 дней";
    private static final String PAST_DUE_6_20_HEADER = "Сумма ДЗ в валюте договора/Сумма просрочки 6-20 дней";
    private static final String PAST_DUE_OVER_20_HEADER = "Сумма ДЗ в валюте договора/Сумма просрочки >20 дней";

    private static final Map<String, String> CURRENCY_ROW = singletonMap(CUR_CP_CONT_DEAL_HEADER, "EUR");
    private static final Map<String, String> COUNTERPARTY_ROW = singletonMap(CUR_CP_CONT_DEAL_HEADER, "counterparty 1");
    private static final Map<String, String> CONTRACT_ROW = new HashMap<String, String>() {{
        put(CUR_CP_CONT_DEAL_HEADER, "contract 1");
        put(CURRENCY_ORDER_CP_HEADER, "EUR");
        put(DELAY_DAYS_HEADER, "1");
    }};
    private static final Map<String, String> DATA_ROW = new HashMap<String, String>() {{
        put(CUR_CP_CONT_DEAL_HEADER, "deal 1");
        put(CURRENCY_ORDER_CP_HEADER, "counterparty 2");
        put(DEAL_DATE_HEADER, "01.01.1970");
        put(DEPARTMENT_HEADER, "department 1");
        put(PAST_DUE_DAYS_HEADER, "2");
        put(DEBT_WITHIN_DELAY_HEADER, "3");
        put(PAST_DUE_5_HEADER, "4");
        put(PAST_DUE_6_20_HEADER, "5");
        put(PAST_DUE_OVER_20_HEADER, "6");
    }};
    private static final Map<String, String> TOTAL_ROW = singletonMap(CUR_CP_CONT_DEAL_HEADER, "Итого");
    private static final Map<String, String> EXPECTED_ROW = new HashMap<String, String>() {{
        put("Валюта", "EUR");
        put("Контрагент", "counterparty 1");
        put("Договор контрагента", "contract 1");
        put("Сделка", "deal 1");
        put("Контрагент заказа", "counterparty 2");
        put(DEAL_DATE_HEADER, "01.01.1970");
        put(DEPARTMENT_HEADER, "department 1");
        put(PAST_DUE_DAYS_HEADER, "2");
        put(DELAY_DAYS_HEADER, "1");
        put(DEBT_WITHIN_DELAY_HEADER, "3");
        put(PAST_DUE_5_HEADER, "4");
        put(PAST_DUE_6_20_HEADER, "5");
        put(PAST_DUE_OVER_20_HEADER, "6");
    }};

    private ExcelRowCombiner combiner;

    @Before
    public void before() {
        combiner = new MbModelRowCombiner();
    }

    @Test
    public void combinerTest() {
        assertFalse(combiner.ready());
        combiner.add(CURRENCY_ROW);
        assertFalse(combiner.ready());
        combiner.add(COUNTERPARTY_ROW);
        assertFalse(combiner.ready());
        combiner.add(CONTRACT_ROW);
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
        combiner.add(CURRENCY_ROW);

        assertFalse(combiner.ready());
        combiner.combine();
    }

    @Test(expected = IllegalStateException.class)
    public void readyCombinerAddDataTest() {
        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertTrue(combiner.ready());
        combiner.add(DATA_ROW);
    }

    @Test(expected = IllegalStateException.class)
    public void readyCombinerAddCurrencyTest() {
        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertTrue(combiner.ready());
        combiner.add(CURRENCY_ROW);
    }

    @Test(expected = IllegalStateException.class)
    public void readyCombinerAddTotalTest() {
        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertTrue(combiner.ready());
        combiner.add(TOTAL_ROW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmptyRowTest() {
        combiner.add(emptyMap());
    }

    @Test(expected = IllegalStateException.class)
    public void doubleCombiningTest() {
        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        combiner.combine();
        combiner.combine();
    }

    @Test
    public void skipAllAfterTotalTest() {
        combiner.add(TOTAL_ROW);
        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertFalse(combiner.ready());
    }

    @Test
    public void newDataSameOthersCombinerTest() {
        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertEquals(EXPECTED_ROW, combiner.combine());

        combiner.add(DATA_ROW);
        assertEquals(EXPECTED_ROW, combiner.combine());
    }

    @Test
    public void newContractDataSameOthersCombinerTest() {
        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertEquals(EXPECTED_ROW, combiner.combine());

        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertEquals(EXPECTED_ROW, combiner.combine());
    }

    @Test
    public void newCounterpartyContractDataSameOthersCombinerTest() {
        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertEquals(EXPECTED_ROW, combiner.combine());

        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertEquals(EXPECTED_ROW, combiner.combine());
    }

    @Test
    public void newAllRowsCombinerTest() {
        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertEquals(EXPECTED_ROW, combiner.combine());

        combiner.add(CURRENCY_ROW);
        combiner.add(COUNTERPARTY_ROW);
        combiner.add(CONTRACT_ROW);
        combiner.add(DATA_ROW);

        assertEquals(EXPECTED_ROW, combiner.combine());
    }
}
