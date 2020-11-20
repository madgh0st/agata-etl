package com.kpmg.agata.mapreduce.second;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.mapreduce.counter.EventLogCounter;
import com.kpmg.agata.mapreduce.counter.ReverseCreditLimitCounter;
import com.kpmg.agata.mapreduce.counter.TargetCounter;
import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.models.eventlog.EventModel;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SecondEventLogReducer extends Reducer<ReverseEventLogKey, Text, Text, NullWritable> {
    private static final Logger log = LoggerFactory.getLogger(SecondEventLogReducer.class);
    private static final NullWritable NULL_WRITABLE = NullWritable.get();

    private final ObjectMapper mapper = new ObjectMapper();
    private final List<EventLogCounter> counters = new ArrayList<>();
    private final List<Supplier<EventLogCounter>> countersInitializer = new ArrayList<>();

    public SecondEventLogReducer() {
        countersInitializer.add(TargetCounter::new);
        countersInitializer.add(ReverseCreditLimitCounter::new);
    }

    @Override
    public void reduce(ReverseEventLogKey key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        log.debug("Call reducer");

        counters.clear();
        countersInitializer.stream()
                .map(Supplier::get)
                .forEach(counters::add);
        Map<String, String> reports = new HashMap<>();

        for (Text value : values) {
            log.debug("key: {} value: {}", key, value);

            EventModel event = mapper.readValue(value.toString(), EventModel.class);
            AbstractCleanDataModel model = event.getData();
            boolean isPdz = model instanceof CleanWeeklyStatusPDZFlatModel;

            for (EventLogCounter counter : counters) {
                Map<String, String> counterReports = counter.process(model);
                if (isPdz) {
                    reports.putAll(counterReports);
                }
            }

            if (isPdz) {
                CleanWeeklyStatusPDZFlatModel pdz = (CleanWeeklyStatusPDZFlatModel) model;
                if (pdz.getReports() == null) {
                    pdz.setReports(reports);
                    reports = new HashMap<>();
                } else {
                    pdz.getReports().putAll(reports);
                    reports = new HashMap<>();
                }
                String eventJson = mapper.writeValueAsString(event);
                context.write(new Text(eventJson), NULL_WRITABLE);
            }
        }
    }
}
