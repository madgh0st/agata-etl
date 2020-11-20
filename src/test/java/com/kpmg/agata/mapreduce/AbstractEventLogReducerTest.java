package com.kpmg.agata.mapreduce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.mapreduce.counter.EventLogCounter;
import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.eventlog.EventModel;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("squid:S2187")    // squid:S2187 - TestCases should contain tests
public class AbstractEventLogReducerTest {
    protected static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    protected static Text createJsonEvent(String type, AbstractCleanDataModel data) throws JsonProcessingException {
        EventModel event1 = new EventModel();
        event1.setEventType(type);
        event1.setData(data);
        return new Text(JSON_MAPPER.writeValueAsString(event1));
    }

    protected static void mockInitializer(Reducer reducer, List<EventLogCounter> counters) {
        List<Supplier<EventLogCounter>> initializer = new ArrayList<>();
        counters.forEach(counter -> initializer.add(() -> counter));
        Whitebox.setInternalState(reducer, "countersInitializer", initializer);
    }

    protected static void mockCounters(EventLogCounter mockCounter1,
                                       EventLogCounter mockCounter2) {
        when(mockCounter1.process(any())).thenReturn(singletonMap("key 1", "value 1"));
        when(mockCounter2.process(any())).thenReturn(singletonMap("key 2", "value 2"));
    }
}
