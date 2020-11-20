package com.kpmg.agata.mapreduce.third;

import com.kpmg.agata.mapreduce.second.ReverseEventLogKey;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.min;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ThirdEventLogReducerTest {

    private static int limitMonth;
    @Mock
    private Reducer<ReverseEventLogKey, Text, Text, NullWritable>.Context mockContext;
    private ThirdEventLogReducer reducer;

    @BeforeClass
    public static void beforeClass() throws IllegalAccessException {
        limitMonth = (int) Whitebox.getField(ThirdEventLogReducer.class, "LIMIT_MONTH").get(null);
    }

    private static List<Text> generateValues(int count) {
        return IntStream.iterate(0, i -> i + 1)
                .limit(count)
                .mapToObj(i -> "item-" + i)
                .map(Text::new)
                .collect(toList());
    }

    private static List<Text> generateKeys(int count) {
        return IntStream.iterate(0, i -> i + 1)
                .limit(count)
                .mapToObj(i -> LocalDate.of(1970, 1, 1).minusMonths(i))
                .map(date -> date.format(ofPattern("yyyy-MM-dd")))
                .map(Text::new)
                .collect(toList());
    }

    @Before
    public void setUp() {
        reducer = new ThirdEventLogReducer();
    }

    @Test
    public void reduceMoreThanLimitTest() throws IOException, InterruptedException {
        List<Text> values = generateValues(limitMonth + 1);
        List<Text> keys = generateKeys(limitMonth + 1);
        verifyReduce(values, keys, limitMonth);
    }

    @Test
    public void reduceLessThanLimitTest() throws IOException, InterruptedException {
        List<Text> values = generateValues(limitMonth - 1);
        List<Text> keys = generateKeys(limitMonth - 1);
        verifyReduce(values, keys, limitMonth);
    }

    private void verifyReduce(List<Text> values, List<Text> keys, int limitMonth)
            throws IOException, InterruptedException {
        ReverseEventLogKey keyMock = mockKeys(keys);
        reducer.reduce(keyMock, values, mockContext);

        int expectedCount = min(values.size(), limitMonth);
        for (int i = 0; i < expectedCount; i++) {
            Text value = values.get(i);
            verify(mockContext, times(1))
                    .write(eq(value), eq(NullWritable.get()));
        }
        verify(mockContext, times(expectedCount)).write(any(), any());
    }

    private ReverseEventLogKey mockKeys(List<Text> keys) {
        List<Text> keyRequests = new ArrayList<>();
        keyRequests.add(keys.get(0));
        keyRequests.addAll(keys);

        ReverseEventLogKey keyMock = mock(ReverseEventLogKey.class);
        Iterator<Text> keysIterator = keyRequests.iterator();
        when(keyMock.getDate())
                .then(invocation -> keysIterator.next());
        return keyMock;
    }
}
