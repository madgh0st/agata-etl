package com.kpmg.agata.mapreduce;

import com.kpmg.agata.mapreduce.first.EventLogKey;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.util.Comparator;

/**
 * Groups MapReduce pairs EventLogKey-Value only by natural key (code and doName fields) on the same reducer.
 * In other words, secondary key (date) ignored and one reducer can receive pairs with different dates.
 */
public class EventLogGroupingComparator extends WritableComparator {

    public EventLogGroupingComparator() {
        super(EventLogKey.class, true);
    }

    @Override
    public int compare(WritableComparable firstComparable, WritableComparable secondComparable) {
        EventLogKey first = (EventLogKey) firstComparable;
        EventLogKey second = (EventLogKey) secondComparable;

        return Comparator.comparing(EventLogKey::getCode)
                .thenComparing(EventLogKey::getDoName)
                .compare(first, second);
    }
}
