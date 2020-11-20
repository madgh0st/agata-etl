package com.kpmg.agata.mapreduce.second;

import com.kpmg.agata.mapreduce.first.EventLogKey;

import java.util.Comparator;

/**
 * ReverseEventLogKey inherits full functional of EventLogKey makes sort by date descending, then isPdz ascending.
 * For example, (1970-01-02, false), (1970-01-02, true), (1970-01-01, false)
 */
public class ReverseEventLogKey extends EventLogKey {

    public ReverseEventLogKey() {
    }

    public ReverseEventLogKey(String code, String doName, String date, boolean isPdz) {
        super(code, doName, date, isPdz);
    }

    @Override
    public int compareTo(EventLogKey other) {
        if (!(other instanceof ReverseEventLogKey)) {
            throw new IllegalArgumentException("Argument " + other + " is not instanceof ReverseEventLogKey");
        }
        return Comparator.comparing(EventLogKey::getCode)
                .thenComparing(EventLogKey::getDoName)
                .thenComparing(EventLogKey::getDate)
                .reversed()
                .thenComparing(EventLogKey::getIsPdz)
                .compare(this, other);
    }
}
