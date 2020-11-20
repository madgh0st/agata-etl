package com.kpmg.agata.mapreduce.first;

import com.kpmg.agata.mapreduce.EventLogLine;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class FirstEventLogMapper extends Mapper<LongWritable, Text, EventLogKey, Text> {
    private static final Logger log = getLogger(FirstEventLogMapper.class);

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        log.debug("key: {} value: {}", key, value);

        EventLogLine line;
        try {
            line = EventLogLine.deserializeKey(value.toString());
        } catch (IllegalArgumentException e) {
            log.warn("EventLogLine deserialization failed: {}", e.getMessage());
            return;
        }

        context.write(new EventLogKey(line.getCode(), line.getDoName(), line.getEventDate(), line.isPdz()),
                new Text(line.getEventJson()));
    }
}
