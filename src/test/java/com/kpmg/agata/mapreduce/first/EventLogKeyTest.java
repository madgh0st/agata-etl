package com.kpmg.agata.mapreduce.first;

import com.kpmg.agata.mapreduce.EventLogGroupingComparator;
import com.kpmg.agata.mapreduce.EventLogPartitioner;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class EventLogKeyTest {
    private static final EventLogKey KEY = new EventLogKey("code", "do", "date", false);
    private static final EventLogKey A_KEY = new EventLogKey("code", "do", "a", false);
    private static final EventLogKey A_PDZ_KEY = new EventLogKey("code", "do", "a", true);
    private static final EventLogKey B_KEY = new EventLogKey("code", "do", "b", false);
    private static final EventLogKey B_PDZ_KEY = new EventLogKey("code", "do", "b", true);
    private static final EventLogKey C_KEY = new EventLogKey("code", "do", "c", false);
    private static final EventLogKey C_PDZ_KEY = new EventLogKey("code", "do", "c", true);
    private static final int PARTITIONS = 200;

    private static Text randomValue() {
        return new Text("value-" + new Random().nextInt());
    }

    private static void assertPartitionRange(int number) {
        assertTrue(number < PARTITIONS && number >= 0);
    }

    @Test
    public void compareToTest() {
        //noinspection EqualsWithItself
        assertEquals(0, KEY.compareTo(KEY));
        assertEquals(0, KEY.compareTo(
                new EventLogKey("code", "do", "date", false)));

        assertNotEquals(0, KEY.compareTo(
                new EventLogKey("other code", "do", "date", false)));
        assertNotEquals(0, KEY.compareTo(
                new EventLogKey("code", "other do", "date", false)));
        assertNotEquals(0, KEY.compareTo(
                new EventLogKey("code", "do", " other date", false)));
        assertNotEquals(0, KEY.compareTo(
                new EventLogKey("other code", "other do", " other date", false)));
    }

    @Test
    public void compareToAlphabeticalAscendingTest() {
        assertTrue(A_KEY.compareTo(A_PDZ_KEY) < 0);
        assertTrue(A_PDZ_KEY.compareTo(B_KEY) < 0);
        assertTrue(B_KEY.compareTo(B_PDZ_KEY) < 0);
        assertTrue(B_PDZ_KEY.compareTo(C_KEY) < 0);
        assertTrue(C_KEY.compareTo(C_PDZ_KEY) < 0);
    }

    @Test
    public void groupingComparatorTest() {
        EventLogGroupingComparator groupComparator = new EventLogGroupingComparator();

        assertEquals(0, groupComparator.compare(KEY, KEY));
        assertEquals(0, groupComparator.compare(KEY,
                new EventLogKey("code", "do", "date", false)));
        assertEquals(0, groupComparator.compare(KEY,
                new EventLogKey("code", "do", "other date", false)));

        assertNotEquals(0, groupComparator.compare(KEY,
                new EventLogKey("other code", "do", "date", false)));
        assertNotEquals(0, groupComparator.compare(KEY,
                new EventLogKey("code", "other do", "date", false)));
        assertNotEquals(0, groupComparator.compare(KEY,
                new EventLogKey("other code", "other do", "other date", false)));
    }

    @Test
    public void partitionerTest() {
        EventLogPartitioner partitioner = new EventLogPartitioner();
        int expected = partitioner.getPartition(KEY, randomValue(), PARTITIONS);
        assertPartitionRange(expected);
        int actual;

        actual = partitioner.getPartition(KEY, randomValue(), PARTITIONS);
        assertEquals(expected, actual);
        assertPartitionRange(actual);

        actual = partitioner.getPartition(
                new EventLogKey("code", "do", "date", false), randomValue(), PARTITIONS);
        assertEquals(expected, actual);
        assertPartitionRange(actual);

        actual = partitioner.getPartition(
                new EventLogKey("code", "do", "other date", false), randomValue(), PARTITIONS);
        assertEquals(expected, actual);
        assertPartitionRange(actual);


        actual = partitioner.getPartition(
                new EventLogKey("other code", "do", "date", false), randomValue(), PARTITIONS);
        assertNotEquals(expected, actual);
        assertPartitionRange(actual);

        actual = partitioner.getPartition(
                new EventLogKey("code", "other do", "date", false), randomValue(), PARTITIONS);
        assertNotEquals(expected, actual);
        assertPartitionRange(actual);

        actual = partitioner.getPartition(
                new EventLogKey("other code", "other do", "other date", false),
                randomValue(),
                PARTITIONS);
        assertNotEquals(expected, actual);
        assertPartitionRange(actual);
    }
}
