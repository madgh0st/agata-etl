package com.kpmg.agata.mapreduce.first;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

/**
 * Composite key between mapper and reducer.
 * See https://www.oreilly.com/library/view/data-algorithms/9781491906170/ch01.html .
 * Designed for grouping by natural key (code and doName) and sorting within reducer by secondary key (date, isPdz).
 * Sort order is ascending by date, then ascending by isPdz.
 * For example, (1970-01-01, false), (1970-01-02, false), (1970-01-02, true).
 */
public class EventLogKey implements WritableComparable<EventLogKey> {

    // Natural key
    private Text code = new Text();
    private Text doName = new Text();

    // Secondary key
    private Text date = new Text();
    private BooleanWritable isPdz = new BooleanWritable();

    public EventLogKey() {
    }

    public EventLogKey(String code, String doName, String date, boolean isPdz) {
        this.code.set(code);
        this.doName.set(doName);
        this.date.set(date);
        this.isPdz.set(isPdz);
    }

    public Text getCode() {
        return code;
    }

    public void setCode(Text code) {
        this.code = code;
    }

    public Text getDoName() {
        return doName;
    }

    public void setDoName(Text doName) {
        this.doName = doName;
    }

    public Text getDate() {
        return date;
    }

    public void setDate(Text date) {
        this.date = date;
    }

    public BooleanWritable getIsPdz() {
        return isPdz;
    }

    public void setIsPdz(BooleanWritable isPdz) {
        this.isPdz = isPdz;
    }

    @Override
    public int compareTo(EventLogKey other) {
        return Comparator.comparing(EventLogKey::getCode)
                .thenComparing(EventLogKey::getDoName)
                .thenComparing(EventLogKey::getDate)
                .thenComparing(EventLogKey::getIsPdz)
                .compare(this, other);
    }


    @Override
    public void write(DataOutput out) throws IOException {
        code.write(out);
        doName.write(out);
        date.write(out);
        isPdz.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        code.readFields(in);
        doName.readFields(in);
        date.readFields(in);
        isPdz.readFields(in);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventLogKey that = (EventLogKey) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(doName, that.doName) &&
                Objects.equals(date, that.date) &&
                Objects.equals(isPdz, that.isPdz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, doName, date, isPdz);
    }

    @Override
    public String toString() {
        return "EventLogKey{" +
                "code=" + code +
                ", doName=" + doName +
                ", date=" + date +
                ", isPdz=" + isPdz +
                '}';
    }
}
