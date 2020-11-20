package com.kpmg.agata.mapreduce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.models.eventlog.EventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Pattern;

import static java.lang.Boolean.parseBoolean;

/**
 * has format code|doName|eventDate|isPdzΩeventJson
 */
public class EventLogLine {
    private static final Logger log = LoggerFactory.getLogger(EventLogLine.class);
    private static final String KEY_PARTS_SPLITTER = "|";
    private static final String KEY_VALUE_SPLITTER = "Ω";

    private final String code;
    private final String doName;
    private final String eventDate;
    private final boolean isPdz;

    private String eventJson;
    private EventModel event;

    private EventLogLine(String code, String doName, String eventDate, boolean isPdz) {
        this.code = code;
        this.doName = doName;
        this.eventDate = eventDate;
        this.isPdz = isPdz;
    }

    public static String serialize(EventModel event, ObjectMapper mapper) {
        boolean isPdz = event.getData() instanceof CleanWeeklyStatusPDZFlatModel;
        String key = event.getCode() + KEY_PARTS_SPLITTER
                + event.getName_do() + KEY_PARTS_SPLITTER
                + event.getEventDate() + KEY_PARTS_SPLITTER + isPdz;

        String json = "";
        try {
            json = mapper.writeValueAsString(event);
        } catch (Exception e) {
            log.error("Error occurred during serializing JSON for event {}", event, e);
        }
        return key + KEY_VALUE_SPLITTER + json;
    }

    public static EventLogLine deserialize(String line, ObjectMapper mapper) throws IOException {
        EventLogLine result = deserializeKey(line);
        result.event = mapper.readValue(result.eventJson, EventModel.class);
        return result;
    }

    public static EventLogLine deserializeKey(String line) {
        String[] data = line.split(KEY_VALUE_SPLITTER);

        if (data.length != 2) {
            throw new IllegalArgumentException("Malformed line");
        }

        String key = data[0];
        String[] keyParts = key.split(Pattern.quote(KEY_PARTS_SPLITTER));

        if (keyParts.length != 4) {
            throw new IllegalArgumentException("Malformed key");
        }
        EventLogLine result = new EventLogLine(keyParts[0], keyParts[1], keyParts[2], parseBoolean(keyParts[3]));
        result.eventJson = data[1];
        return result;
    }

    public String getCode() {
        return code;
    }

    public String getDoName() {
        return doName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public boolean isPdz() {
        return isPdz;
    }

    public String getEventJson() {
        return eventJson;
    }

    public EventModel getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "EventLogLine{" +
                "code='" + code + '\'' +
                ", doName='" + doName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", isPdz=" + isPdz +
                ", eventJson='" + eventJson + '\'' +
                ", event=" + event +
                '}';
    }
}
