package com.kpmg.agata.mapreduce.counter;

import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.DESC;
import static java.time.LocalDate.of;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WindowBufferTest {
    private WindowBuffer<String> buffer;

    @Test
    public void bufferAscendingTest() {
        buffer = new WindowBuffer<>(3, ASC);
        assertNull(buffer.get(of(1970, 1, 1)));

        buffer.put(of(1970, 1, 1), "v1");
        assertEquals("v1", buffer.get(of(1970, 1, 1)));

        buffer.put(of(1970, 1, 2), "v2");
        buffer.put(of(1970, 1, 3), "v3");
        assertEquals("v1", buffer.get(of(1970, 1, 1)));
        assertEquals("v2", buffer.get(of(1970, 1, 2)));
        assertEquals("v3", buffer.get(of(1970, 1, 3)));

        buffer.put(of(1970, 1, 5), "v4");
        assertNull(buffer.get(of(1970, 1, 1)));
        assertNull(buffer.get(of(1970, 1, 2)));
        assertEquals("v3", buffer.get(of(1970, 1, 3)));
        assertEquals("v4", buffer.get(of(1970, 1, 5)));
    }

    @Test
    public void bufferDescendingTest() {
        buffer = new WindowBuffer<>(3, DESC);

        assertNull(buffer.get(of(1970, 1, 5)));

        buffer.put(of(1970, 1, 5), "v1");
        assertEquals("v1", buffer.get(of(1970, 1, 5)));

        buffer.put(of(1970, 1, 4), "v2");
        buffer.put(of(1970, 1, 3), "v3");
        assertEquals("v1", buffer.get(of(1970, 1, 5)));
        assertEquals("v2", buffer.get(of(1970, 1, 4)));
        assertEquals("v3", buffer.get(of(1970, 1, 3)));

        buffer.put(of(1970, 1, 1), "v4");
        assertNull(buffer.get(of(1970, 1, 5)));
        assertNull(buffer.get(of(1970, 1, 4)));
        assertEquals("v3", buffer.get(of(1970, 1, 3)));
        assertEquals("v4", buffer.get(of(1970, 1, 1)));
    }

    @Test
    public void isEmpty() {
        buffer = new WindowBuffer<>(3, DESC);

        assertTrue(buffer.isEmpty());
        buffer.put(of(1970, 1, 5), "v1");
        assertFalse(buffer.isEmpty());
        buffer.put(of(1970, 1, 4), "v2");
        assertFalse(buffer.isEmpty());
        buffer.put(of(1970, 1, 3), "v3");
        assertFalse(buffer.isEmpty());
        buffer.put(of(1970, 1, 1), "v4");
        assertFalse(buffer.isEmpty());
    }

    @Test
    public void putWithReducerTest() {
        BinaryOperator<String> reducer = (oldValue, value) -> oldValue + ", " + value;
        LocalDate date = of(1970, 1, 1);
        buffer = new WindowBuffer<>(3, ASC);

        buffer.put(date, "v1", reducer);
        assertEquals("v1", buffer.get(date));

        buffer.put(date, "v2", reducer);
        assertEquals("v1, v2", buffer.get(date));

        buffer.put(date, "v3", reducer);
        assertEquals("v1, v2, v3", buffer.get(date));

        buffer.put(date.plusDays(1), "v4", reducer);
        assertEquals("v4", buffer.get(date.plusDays(1)));
    }

    @Test
    public void streamWithinRange() {
        buffer = new WindowBuffer<>(2, ASC);
        buffer.put(of(1970, 1, 1), "v1");
        buffer.put(of(1970, 1, 2), "v2");
        buffer.put(of(1970, 1, 3), "v3");

        List<String> actualList = buffer.streamWithinRange(of(1969, 1, 1),
                of(1971, 1, 1))
                .collect(toList());

        assertFalse(actualList.contains("v1"));
        assertTrue(actualList.contains("v2"));
        assertTrue(actualList.contains("v3"));


        List<String> narrowWindowActualList = buffer.streamWithinRange(of(1970, 1, 2),
                of(1970, 1, 2))
                .collect(toList());

        assertFalse(narrowWindowActualList.contains("v1"));
        assertTrue(narrowWindowActualList.contains("v2"));
        assertFalse(narrowWindowActualList.contains("v3"));
    }

    @Test
    public void streamWithinRangeByOrder() {
        buffer = new WindowBuffer<>(3, ASC);
        buffer.put(of(1970, 1, 1), "v1");
        buffer.put(of(1970, 1, 2), "v2");
        buffer.put(of(1970, 1, 3), "v3");

        List<String> ascActualList = buffer.streamWithinRange(of(1969, 1, 1),
                of(1971, 1, 1), ASC)
                .collect(toList());
        assertEquals( asList("v1", "v2", "v3"), ascActualList);

        List<String> descActualList = buffer.streamWithinRange(of(1969, 1, 1),
                of(1971, 1, 1), DESC)
                .collect(toList());
        assertEquals(asList("v3", "v2", "v1"), descActualList);
    }

    @Test
    public void streamEntriesWithinRange() {
        buffer = new WindowBuffer<>(2, ASC);
        buffer.put(of(1970, 1, 1), "v1");
        buffer.put(of(1970, 1, 2), "v2");

        Map<LocalDate, String> actual = buffer.streamEntriesWithinRange(of(1969, 1, 1),
                of(1971, 1, 1), ASC)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<LocalDate, String> expected = new HashMap<>();
        expected.put(of(1970, 1, 1), "v1");
        expected.put(of(1970, 1, 2), "v2");

        assertEquals(expected, actual);
    }
}
