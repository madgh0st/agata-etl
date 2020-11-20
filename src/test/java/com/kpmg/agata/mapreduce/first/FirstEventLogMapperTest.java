package com.kpmg.agata.mapreduce.first;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.models.eventlog.EventModel;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FirstEventLogMapperTest {
    @Mock
    private Mapper<LongWritable, Text, EventLogKey, Text>.Context mockContext;

    private FirstEventLogMapper mapper;

    @Before
    public void setUp() {
        mapper = new FirstEventLogMapper();
    }

    @Test
    public void testGoodExample() throws IOException, InterruptedException {
        ObjectMapper jsonMapper = new ObjectMapper();
        EventModel event = new EventModel();
        event.setData(new CleanWeeklyStatusPDZFlatModel());
        String json = jsonMapper.writeValueAsString(event);
        Text inputValue = new Text("code|do|date|trueΩ" + json);
        mapper.map(new LongWritable(1), inputValue, mockContext);

        EventLogKey expectedKey = new EventLogKey("code", "do", "date", true);
        Text expectedValue = new Text(json);

        verify(mockContext, only()).write(expectedKey, expectedValue);
    }

    @Test
    public void testMalformedComplexKey() throws IOException, InterruptedException {
        Text inputValue = new Text("code|doΩvalue");

        mapper.map(new LongWritable(1), inputValue, mockContext);
        verifyZeroInteractions(mockContext);
    }

    @Test
    public void testMalformedKeyValuePair() throws IOException, InterruptedException {
        Text inputValue = new Text("code|do|date|value");

        mapper.map(new LongWritable(1), inputValue, mockContext);
        verifyZeroInteractions(mockContext);
    }
}
