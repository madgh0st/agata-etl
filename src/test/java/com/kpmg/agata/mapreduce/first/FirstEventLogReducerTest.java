package com.kpmg.agata.mapreduce.first;

import com.fasterxml.jackson.core.JsonParseException;
import com.kpmg.agata.mapreduce.AbstractEventLogReducerTest;
import com.kpmg.agata.mapreduce.EventLogLine;
import com.kpmg.agata.mapreduce.counter.EventLogCounter;
import com.kpmg.agata.models.clean.CleanCreditCorrectionModel;
import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanExpressAnalysisModel;
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
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Map;

import static com.kpmg.agata.utils.Utils.listOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FirstEventLogReducerTest extends AbstractEventLogReducerTest {
    private static final EventLogKey KEY = new EventLogKey("code", "do", "1970-01-01", false);

    @Mock
    private Reducer<EventLogKey, Text, Text, NullWritable>.Context mockContext;
    private FirstEventLogReducer reducer;

    private static Object assertPdz(InvocationOnMock invocation) throws IOException {
        String line = invocation.getArguments()[0].toString();
        EventModel actualEvent = EventLogLine.deserialize(line, JSON_MAPPER).getEvent();
        CleanWeeklyStatusPDZFlatModel actualPdz = (CleanWeeklyStatusPDZFlatModel) actualEvent.getData();
        Map<String, String> actualReports = actualPdz.getReports();

        assertEquals(2, actualReports.size());
        assertEquals("value 1", actualReports.get("key 1"));
        assertEquals("value 2", actualReports.get("key 2"));
        return null;
    }

    private static Object assertCl(InvocationOnMock invocation) throws IOException {
        String line = invocation.getArguments()[0].toString();
        EventModel actualEvent = EventLogLine.deserialize(line, JSON_MAPPER).getEvent();
        assertTrue(actualEvent.getData() instanceof CleanCreditLimitModel);
        return null;
    }

    private static void doAssertPdzWhenContextWrite(Reducer<EventLogKey, Text, Text, NullWritable>.Context mockContext)
            throws IOException, InterruptedException {
        doAnswer(FirstEventLogReducerTest::assertPdz)
                .when(mockContext)
                .write(any(), any());
    }

    private static void doAssertClWhenContextWrite(Reducer<EventLogKey, Text, Text, NullWritable>.Context mockContext)
            throws IOException, InterruptedException {
        doAnswer(FirstEventLogReducerTest::assertCl)
                .when(mockContext)
                .write(any(), any());
    }

    @Before
    public void setUp() {
        reducer = new FirstEventLogReducer();
    }

    @Test
    public void reduceNonPdzEventTest()
            throws IOException, InterruptedException {
        Text jsonEvent1 = createJsonEvent("CreditCorrection", new CleanCreditCorrectionModel());
        Text jsonEvent2 = createJsonEvent("ExpressAnalysis", new CleanExpressAnalysisModel());
        EventLogCounter counterMock1 = mock(EventLogCounter.class);
        EventLogCounter counterMock2 = mock(EventLogCounter.class);
        mockInitializer(reducer, asList(counterMock1, counterMock2));

        reducer.reduce(KEY, listOf(jsonEvent1, jsonEvent2), mockContext);

        verify(counterMock1, times(2)).process(any());
        verify(counterMock2, times(2)).process(any());
        verifyZeroInteractions(mockContext);
    }

    @Test
    public void reducePdzEventTest()
            throws IOException, InterruptedException {
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

    @Test
    public void reduceCreditLimitEventTest()
            throws IOException, InterruptedException {
        Text jsonClEvent = createJsonEvent("CreditLimit", new CleanCreditLimitModel());
        EventLogCounter counterMock1 = mock(EventLogCounter.class);
        EventLogCounter counterMock2 = mock(EventLogCounter.class);
        mockInitializer(reducer, asList(counterMock1, counterMock2));
        doAssertClWhenContextWrite(mockContext);

        reducer.reduce(KEY, listOf(jsonClEvent), mockContext);

        verify(counterMock1, only()).process(any());
        verify(counterMock2, only()).process(any());
        verify(mockContext, only()).write(any(), any());
    }

    @Test(expected = JsonParseException.class)
    public void inputNonJsonValueTest() throws IOException, InterruptedException {
        reducer.reduce(KEY, listOf(new Text("nonJsonString")), mockContext);
    }
}
