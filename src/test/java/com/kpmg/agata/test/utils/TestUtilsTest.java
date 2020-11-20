package com.kpmg.agata.test.utils;

import org.junit.ComparisonFailure;
import org.junit.Test;

import java.util.List;

import static com.kpmg.agata.test.utils.TestUtils.assertEqualsOrNull;
import static com.kpmg.agata.test.utils.TestUtils.assertException;
import static com.kpmg.agata.test.utils.TestUtils.checkAllAfter;
import static com.kpmg.agata.test.utils.TestUtils.checkAllBefore;
import static com.kpmg.agata.test.utils.TestUtils.checkOrder;
import static com.kpmg.agata.utils.Utils.listOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestUtilsTest {

    @Test
    public void checkOrderTest() {
        List<String> source = listOf("1", "2", "3", "4", "5");
        assertTrue(checkOrder(source, listOf()));
        assertTrue(checkOrder(source, listOf("1")));
        assertTrue(checkOrder(source, listOf("3")));
        assertTrue(checkOrder(source, listOf("5")));
        assertTrue(checkOrder(source, listOf("2", "4")));
        assertTrue(checkOrder(source, source));

        assertFalse(checkOrder(source, listOf("6")));
        assertFalse(checkOrder(source, listOf("1", "2", "3", "4", "5", "6")));
        assertFalse(checkOrder(source, listOf("2", "1")));
    }

    @Test
    public void checkAllBeforeTest() {
        List<String> source = listOf("1", "2", "3", "4", "5");

        assertTrue(checkAllBefore(source, listOf(), "3"));
        assertTrue(checkAllBefore(source, listOf("1"), "2"));
        assertTrue(checkAllBefore(source, listOf("2", "3"), "5"));
        assertTrue(checkAllBefore(source, listOf("1", "2", "3", "4"), "5"));
        assertTrue(checkAllBefore(source, listOf("2", "4", "1", "3"), "5"));

        assertFalse(checkAllBefore(source, listOf(), "6"));
        assertFalse(checkAllBefore(source, listOf("2"), "1"));
        assertFalse(checkAllBefore(source, listOf("1", "2", "4"), "3"));
        assertFalse(checkAllBefore(source, listOf("2", "5", "1", "3"), "4"));
    }

    @Test
    public void checkAllAfterTest() {
        List<String> source = listOf("1", "2", "3", "4", "5");

        assertTrue(checkAllAfter(source, "1", listOf()));
        assertTrue(checkAllAfter(source, "3", listOf()));
        assertTrue(checkAllAfter(source, "5", listOf()));
        assertTrue(checkAllAfter(source, "1", listOf("2")));
        assertTrue(checkAllAfter(source, "3", listOf("4", "5")));
        assertTrue(checkAllAfter(source, "1", listOf("2", "5", "4", "3")));

        assertFalse(checkAllAfter(source, "2", listOf("1")));
        assertFalse(checkAllAfter(source, "5", listOf("4")));
        assertFalse(checkAllAfter(source, "3", listOf("1", "2", "4", "5")));
        assertFalse(checkAllAfter(source, "3", listOf("5", "2", "1", "4")));
    }

    @Test
    public void assertExceptionTest() {
        assertException(IllegalStateException.class, () -> {
            throw new IllegalStateException();
        });
    }

    @Test(expected = AssertionError.class)
    public void assertExceptionNoExceptionTest() {
        assertException(IllegalStateException.class, () -> {
            // test method works correct, no exceptions should be thrown
        });
    }

    @Test(expected = AssertionError.class)
    public void assertExceptionOtherExceptionTest() {
        assertException(IllegalStateException.class, () -> {
            throw new ArithmeticException();
        });
    }

    @Test(expected = AssertionError.class)
    public void assertExceptionParentExceptionTest() {
        assertException(IllegalStateException.class, () -> {
            throw new RuntimeException();
        });
    }

    @Test(expected = AssertionError.class)
    public void assertExceptionChildExceptionTest() {
        assertException(RuntimeException.class, () -> {
            throw new IllegalStateException();
        });
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void assertEqualsOrNullTest() {
        assertEqualsOrNull("str", "str");
        assertEqualsOrNull("str", null);
        assertException(ComparisonFailure.class, () -> assertEqualsOrNull("str", "another str"));
        assertException(IllegalArgumentException.class, () -> assertEqualsOrNull(null, "str"));
    }
}
