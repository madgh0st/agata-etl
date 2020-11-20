package com.kpmg.agata.mapreduce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.models.eventlog.EventModel;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class EventLogLineTest {
    private static final CleanWeeklyStatusPDZFlatModel PDZ = new CleanWeeklyStatusPDZFlatModel();
    private static final EventModel EVENT = new EventModel("code", "do", Date.valueOf("1970-01-01"), PDZ);
    private static final String LINE = "code|do|1970-01-01|trueΩ{json}";
    private ObjectMapper mapperMock;

    @Before
    public void setUp() {
        mapperMock = mock(ObjectMapper.class);
    }

    @Test
    public void serializeTest() throws JsonProcessingException {
        when(mapperMock.writeValueAsString(any()))
                .thenReturn("{json}");

        assertEquals(LINE, EventLogLine.serialize(EVENT, mapperMock));
    }

    @Test
    public void deserializeTest() throws IOException {
        when(mapperMock.readValue(anyString(), eq(EventModel.class)))
                .thenReturn(EVENT);
        EventLogLine actual = EventLogLine.deserialize(LINE, mapperMock);

        assertEquals("code", actual.getCode());
        assertEquals("do", actual.getDoName());
        assertEquals("1970-01-01", actual.getEventDate());
        assertEquals("{json}", actual.getEventJson());

        assertTrue(actual.isPdz());
        assertEquals(EVENT, actual.getEvent());

        verify(mapperMock, only()).readValue(anyString(), eq(EventModel.class));
    }

    @Test
    public void deserializeKeyTest() {
        EventLogLine actual = EventLogLine.deserializeKey(LINE);

        assertEquals("code", actual.getCode());
        assertEquals("do", actual.getDoName());
        assertEquals("1970-01-01", actual.getEventDate());
        assertEquals("{json}", actual.getEventJson());

        assertTrue(actual.isPdz());
        assertNull(actual.getEvent());

        verifyZeroInteractions(mapperMock);
    }
}
