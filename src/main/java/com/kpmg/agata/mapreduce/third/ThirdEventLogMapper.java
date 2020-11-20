package com.kpmg.agata.mapreduce.third;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.mapreduce.second.ReverseEventLogKey;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.models.eventlog.EventModel;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class ThirdEventLogMapper extends Mapper<LongWritable, Text, ReverseEventLogKey, Text> {
    private static final Logger log = getLogger(ThirdEventLogMapper.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        log.debug("key: {} value: {}", key, value);

        String json = value.toString();
        EventModel event = mapper.readValue(json, EventModel.class);
        ReverseEventLogKey outputKey = new ReverseEventLogKey(event.getCode(),
                event.getName_do(),
                event.getEventDate().toString(),
                event.getData() instanceof CleanWeeklyStatusPDZFlatModel);
        context.write(outputKey, new Text(json));
    }
}
