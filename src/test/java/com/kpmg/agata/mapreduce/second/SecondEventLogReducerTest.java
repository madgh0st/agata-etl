package com.kpmg.agata.mapreduce.second;

import com.fasterxml.jackson.core.JsonParseException;
import com.kpmg.agata.mapreduce.AbstractEventLogReducerTest;
import com.kpmg.agata.mapreduce.counter.EventLogCounter;
import com.kpmg.agata.models.clean.CleanCreditCorrectionModel;
import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.models.eventlog.EventModel;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Map;

import static com.kpmg.agata.utils.Utils.listOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SecondEventLogReducer.class)
public class SecondEventLogReducerTest extends AbstractEventLogReducerTest {
    private static final ReverseEventLogKey KEY = new ReverseEventLogKey("code", "do", "1970-01-01", false);

    @Mock
    private Reducer<ReverseEventLogKey, Text, Text, NullWritable>.Context mockContext;
    private SecondEventLogReducer reducer;

    private static void doAssertPdzWhenContextWrite(
            Reducer<ReverseEventLogKey, Text, Text, NullWritable>.Context mockContext)
            throws IOException, InterruptedException {
        doAnswer(SecondEventLogReducerTest::assertPdz)
                .when(mockContext)
                .write(any(), any());
    }

    private static Object assertPdz(InvocationOnMock invocation) throws IOException {
        String json = invocation.getArguments()[0].toString();
        EventModel actualEvent = JSON_MAPPER.readValue(json, EventModel.class);
        CleanWeeklyStatusPDZFlatModel actualPdz = (CleanWeeklyStatusPDZFlatModel) actualEvent.getData();
        Map<String, String> actualReports = actualPdz.getReports();

        assertEquals(2, actualReports.size());
        assertEquals("value 1", actualReports.get("key 1"));
        assertEquals("value 2", actualReports.get("key 2"));
        return null;
    }

    @Before
    public void setUp() {
        reducer = new SecondEventLogReducer();
    }

    @Test
    public void reduceNonPdzEventTest() throws IOException, InterruptedException {
        Text jsonEvent1 = createJsonEvent("CreditCorrection", new CleanCreditCorrectionModel());
        Text jsonEvent2 = createJsonEvent("CreditLimit", new CleanCreditLimitModel());
        EventLogCounter counterMock1 = mock(EventLogCounter.class);
        EventLogCounter counterMock2 = mock(EventLogCounter.class);
        mockInitializer(reducer, asList(counterMock1, counterMock2));

        reducer.reduce(KEY, listOf(jsonEvent1, jsonEvent2), mockContext);

        verify(counterMock1, times(2)).process(any());
        verify(counterMock2, times(2)).process(any());
        verifyZeroInteractions(mockContext);
    }

    @Test
    public void reducePdzEventTest() throws IOException, InterruptedException {
        Text jsonPdzEvent = createJsonEvent("WeeklyStatusPDZFlat", new CleanWeeklyStatusPDZFlatModel());
        EventLogCounter counterMock1 = mock(EventLogCounter.class);
        EventLogCounter counterMock2 = mock(EventLogCounter.class);
        mockInitializer(reducer, asList(counterMock1, counterMock2));
        mockCounters(counterMock1, counterMock2);
        doAssertPdzWhenContextWrite(mockContext);

        reducer.reduce(KEY, listOf(jsonPdzEvent), mockContext);

        verify(counterMock1, only()).process(any());
        verify(counterMock2, only()).process(any());
        verify(mockContext, only()).write(any(), any());
    }

    @Test(expected = JsonParseException.class)
    public void inputNonJsonValueTest() throws IOException, InterruptedException {
        reducer.reduce(KEY, listOf(new Text("nonJsonString")), mockContext);
    }
}
