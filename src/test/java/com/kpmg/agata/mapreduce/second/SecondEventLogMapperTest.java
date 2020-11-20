package com.kpmg.agata.mapreduce.second;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.mapreduce.EventLogLine;
import com.kpmg.agata.models.clean.CleanCreditCorrectionModel;
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
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SecondEventLogMapperTest {
    private static final LongWritable KEY = new LongWritable(1);
    @Mock
    private Mapper<LongWritable, Text, ReverseEventLogKey, Text>.Context mockContext;

    private SecondEventLogMapper mapper;

    @Before
    public void setUp() {
        mapper = new SecondEventLogMapper();
    }

    @Test
    public void mapTest() throws IOException, InterruptedException {
        ObjectMapper jsonMapper = new ObjectMapper();

        EventModel event = new EventModel();
        event.setData(new CleanCreditCorrectionModel());
        event.setEventType("CreditCorrection");
        event.setCode("code");
        event.setName_do("do");
        event.setEventDate(Date.valueOf("1970-01-01"));
        Text inputValue = new Text(EventLogLine.serialize(event, jsonMapper));

        mapper.map(KEY, inputValue, mockContext);

        doAnswer(inv -> {
            ReverseEventLogKey expectedOutputKey = new ReverseEventLogKey("code", "do", "1970-01-01", false);
            ReverseEventLogKey actualOutputKey = (ReverseEventLogKey) inv.getArguments()[0];
            Text outputValue = (Text) inv.getArguments()[1];

            assertEquals(inputValue, outputValue);
            assertEquals(expectedOutputKey, actualOutputKey);

            return null;
        }).when(mockContext).write(any(), any());
    }

    @Test
    public void inputNonJsonValueTest() throws IOException, InterruptedException {
        mapper.map(KEY, new Text("nonJsonString"), mockContext);
        verifyZeroInteractions(mockContext);
    }
}
