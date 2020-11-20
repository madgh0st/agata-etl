package com.kpmg.agata.parser;

import com.kpmg.agata.parser.combiner.ExcelRowCombiner;
import com.kpmg.agata.parser.combiner.KpModelRowCombiner;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KpModelRowCombinerTest {
    private final Map<String, String> departmentRow = Collections.singletonMap("1", "department 1");
    private final Map<String, String> anotherDepartmentRow = Collections.singletonMap("1", "department 2");
    private final Map<String, String> managerRow = Collections.singletonMap("2", "manager 1");
    private final Map<String, String> anotherManagerRow = Collections.singletonMap("2", "manager 2");
    private final Map<String, String> counterpartyRow = new HashMap<String, String>() {{
        put("3", "counterparty 1");
        put("4", "no");
        put("5", "contract 1");
    }};
    private final Map<String, String> anotherCounterpartyRow = new HashMap<String, String>() {{
        put("3", "counterparty 2");
        put("4", "yes");
        put("5", "contract 2");
    }};
    private final Map<String, String> detailsRow = new HashMap<String, String>() {{
        put("6", "123");
        put("7", "456");
        put("8", "789");
    }};
    private final Map<String, String> anotherDetailsRow = new HashMap<String, String>() {{
        put("6", "987");
        put("7", "654");
        put("8", "321");
    }};

    private final Map<String, String> expectedRow = new HashMap<String, String>() {{
        put("1", "department 1");
        put("2", "manager 1");
        put("3", "counterparty 1");
        put("4", "no");
        put("5", "contract 1");
        put("6", "123");
        put("7", "456");
        put("8", "789");
    }};
    private final Map<String, String> multipleTypesRow = new HashMap<String, String>() {{
        put("1", "department 1");
        put("2", "manager 1");
    }};
    private final Map<String, String> noTypeRow = Collections.singletonMap("key", "value");

    @Test
    public void combinerTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        assertFalse(combiner.ready());
        combiner.add(departmentRow);
        assertFalse(combiner.ready());
        combiner.add(managerRow);
        assertFalse(combiner.ready());
        combiner.add(counterpartyRow);
        assertFalse(combiner.ready());
        combiner.add(detailsRow);

        assertTrue(combiner.ready());
        assertEquals(expectedRow, combiner.combine());
        assertFalse(combiner.ready());
    }

    @Test(expected = IllegalStateException.class)
    public void emptyCombinerCombiningTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();

        assertFalse(combiner.ready());
        combiner.combine();
    }

    @Test(expected = IllegalStateException.class)
    public void notReadyCombinerCombineTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        combiner.add(departmentRow);
        combiner.add(managerRow);

        assertFalse(combiner.ready());
        combiner.combine();
    }

    @Test(expected = IllegalStateException.class)
    public void readyCombinerAddTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        combiner.add(departmentRow);
        combiner.add(managerRow);
        combiner.add(counterpartyRow);
        combiner.add(detailsRow);

        combiner.add(anotherManagerRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmptyRowTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        combiner.add(emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMultipleTypesRowTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        combiner.add(multipleTypesRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNoTypeRowTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        combiner.add(noTypeRow);
    }

    @Test(expected = IllegalStateException.class)
    public void doubleCombiningTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        combiner.add(departmentRow);
        combiner.add(managerRow);
        combiner.add(counterpartyRow);
        combiner.add(detailsRow);
        combiner.combine();
        combiner.combine();
    }

    @Test
    public void newDepartmentTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        combiner.add(anotherDepartmentRow);
        combiner.add(anotherManagerRow);
        combiner.add(anotherCounterpartyRow);
        combiner.add(anotherDetailsRow);

        assertTrue(combiner.ready());
        combiner.combine();
        combiner.add(departmentRow);
        assertFalse(combiner.ready());
        combiner.add(detailsRow);
        assertFalse(combiner.ready());      // providing only details is not enough, because "manager row" and "counterparty row" were also removed
        combiner.add(managerRow);
        assertFalse(combiner.ready());
        combiner.add(counterpartyRow);
        assertFalse(combiner.ready());
        combiner.add(detailsRow);
        assertTrue(combiner.ready());
        assertEquals(expectedRow, combiner.combine());
    }

    @Test
    public void newManagerTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        combiner.add(departmentRow);
        combiner.add(anotherManagerRow);
        combiner.add(anotherCounterpartyRow);
        combiner.add(anotherDetailsRow);

        assertTrue(combiner.ready());
        combiner.combine();
        combiner.add(managerRow);
        assertFalse(combiner.ready());
        combiner.add(detailsRow);
        assertFalse(combiner.ready());      // providing only details is not enough, because "counterparty row" was also removed
        combiner.add(counterpartyRow);
        assertFalse(combiner.ready());
        combiner.add(detailsRow);
        assertTrue(combiner.ready());
        assertEquals(expectedRow, combiner.combine());
    }

    @Test
    public void newCounterpartyTest() {
        ExcelRowCombiner combiner = new KpModelRowCombiner();
        combiner.add(departmentRow);
        combiner.add(managerRow);
        combiner.add(anotherCounterpartyRow);
        combiner.add(anotherDetailsRow);

        assertTrue(combiner.ready());
        combiner.combine();
        combiner.add(counterpartyRow);
        assertFalse(combiner.ready());          // should also provide new "details row"
        combiner.add(detailsRow);
        assertTrue(combiner.ready());
        assertEquals(expectedRow, combiner.combine());
    }
}
