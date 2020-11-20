package com.kpmg.agata.mapreduce.first;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.mapreduce.EventLogLine;
import com.kpmg.agata.mapreduce.counter.CounterpartyContractsCounter;
import com.kpmg.agata.mapreduce.counter.CreditLimitCounter;
import com.kpmg.agata.mapreduce.counter.EventLogCounter;
import com.kpmg.agata.mapreduce.counter.ExpressAnalysisCounter;
import com.kpmg.agata.mapreduce.counter.FnsArrearsCounter;
import com.kpmg.agata.mapreduce.counter.FnsDisqCounter;
import com.kpmg.agata.mapreduce.counter.FnsTaxViolationCounter;
import com.kpmg.agata.mapreduce.counter.FsspCounter;
import com.kpmg.agata.mapreduce.counter.GenprocCounter;
import com.kpmg.agata.mapreduce.counter.LegalCasesCounter;
import com.kpmg.agata.mapreduce.counter.Overdue21DebtCounter;
import com.kpmg.agata.mapreduce.counter.OverdueDebtCounter;
import com.kpmg.agata.mapreduce.counter.PaymentsRealizationsCounter;
import com.kpmg.agata.mapreduce.counter.PdzCounter;
import com.kpmg.agata.mapreduce.counter.SectoralIndicesCounter;
import com.kpmg.agata.mapreduce.counter.SparkInterfaxSummaryCounter;
import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import com.kpmg.agata.models.eventlog.EventModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstEventLogReducer extends Reducer<EventLogKey, Text, Text, NullWritable> {

    private static final NullWritable NULL_WRITABLE = NullWritable.get();
    private static Logger log = LoggerFactory.getLogger(FirstEventLogReducer.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<EventLogCounter> counters = new ArrayList<>();
    private final List<Supplier<EventLogCounter>> countersInitializer = new ArrayList<>();

    public FirstEventLogReducer() {
        countersInitializer.add(OverdueDebtCounter::new);
        countersInitializer.add(Overdue21DebtCounter::new);
        countersInitializer.add(PdzCounter::new);
        countersInitializer.add(CreditLimitCounter::new);
        countersInitializer.add(PaymentsRealizationsCounter::new);
        countersInitializer.add(ExpressAnalysisCounter::new);
        countersInitializer.add(LegalCasesCounter::new);
        countersInitializer.add(FnsArrearsCounter::new);
        countersInitializer.add(FnsTaxViolationCounter::new);
        countersInitializer.add(GenprocCounter::new);
        countersInitializer.add(FnsDisqCounter::new);
        countersInitializer.add(SparkInterfaxSummaryCounter::new);
        countersInitializer.add(FsspCounter::new);
        countersInitializer.add(SectoralIndicesCounter::new);
        countersInitializer.add(CounterpartyContractsCounter::new);
    }

    @Override
    public void reduce(EventLogKey key, Iterable<Text> values, Context context)
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
                pdz.setReports(reports);
                reports = new HashMap<>();
                String line = EventLogLine.serialize(event, mapper);
                context.write(new Text(line), NULL_WRITABLE);
            }

            if (model instanceof CleanCreditLimitModel) {
                String eventJson = EventLogLine.serialize(event, mapper);
                context.write(new Text(eventJson), NULL_WRITABLE);
            }
        }
    }
}
