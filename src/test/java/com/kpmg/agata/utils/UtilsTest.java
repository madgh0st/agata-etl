package com.kpmg.agata.utils;

import com.kpmg.agata.test.utils.TestUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void roundToTwoDecimalPoints() {
        assertEquals("1.2345", Double.toString(1.2345));
        assertEquals("1.23", Double.toString(Utils.round(1.2345, 2)));
        assertEquals("1.0", Double.toString(Utils.round(1, 2)));
    }

    @Test
    public void roundToNonPositiveDecimalPoints() {
        TestUtils.assertException(IllegalArgumentException.class,
                () -> Utils.round(1, 0));
        TestUtils.assertException(IllegalArgumentException.class,
                () -> Utils.round(1, -1));
        TestUtils.assertException(IllegalArgumentException.class,
                () -> Utils.round(1, -123));
    }
}
