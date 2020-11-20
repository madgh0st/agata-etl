package com.kpmg.agata.mapreduce.second;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ReverseEventLogKeyTest {
    private static final ReverseEventLogKey KEY = new ReverseEventLogKey("code", "do", "date", false);
    private static final ReverseEventLogKey A_KEY = new ReverseEventLogKey("code", "do", "a", false);
    private static final ReverseEventLogKey A_PDZ_KEY = new ReverseEventLogKey("code", "do", "a", true);
    private static final ReverseEventLogKey B_KEY = new ReverseEventLogKey("code", "do", "b", false);
    private static final ReverseEventLogKey B_PDZ_KEY = new ReverseEventLogKey("code", "do", "b", true);
    private static final ReverseEventLogKey C_KEY = new ReverseEventLogKey("code", "do", "c", false);
    private static final ReverseEventLogKey C_PDZ_KEY = new ReverseEventLogKey("code", "do", "c", true);

    @Test
    public void compareToTest() {
        //noinspection EqualsWithItself
        assertEquals(0, KEY.compareTo(KEY));
        assertEquals(0, KEY.compareTo(
                new ReverseEventLogKey("code", "do", "date", false)));

        assertNotEquals(0, KEY.compareTo(
                new ReverseEventLogKey("other code", "do", "date", false)));
        assertNotEquals(0, KEY.compareTo(
                new ReverseEventLogKey("code", "other do", "date", false)));
        assertNotEquals(0, KEY.compareTo(
                new ReverseEventLogKey("code", "do", " other date", false)));
        assertNotEquals(0, KEY.compareTo(
                new ReverseEventLogKey("other code", "other do", " other date", false)));
    }

    @Test
    public void compareToAlphabeticalDescendingTest() {
        assertTrue(C_KEY.compareTo(C_PDZ_KEY) < 0);
        assertTrue(C_PDZ_KEY.compareTo(B_KEY) < 0);
        assertTrue(B_KEY.compareTo(B_PDZ_KEY) < 0);
        assertTrue(B_PDZ_KEY.compareTo(A_KEY) < 0);
        assertTrue(A_KEY.compareTo(A_PDZ_KEY) < 0);
    }
}
