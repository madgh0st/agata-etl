package com.kpmg.agata.mapreduce.counter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import static java.lang.String.format;

import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.ASC;
import static com.kpmg.agata.mapreduce.counter.WindowBuffer.Order.DESC;
import static com.kpmg.agata.utils.Utils.isWithinRange;

/**
 * Contains items in range of some days (set by size field).
 * One item per day.
 * New item must be younger then others or replace the youngest item.
 * Old items out of range immediately removes out of buffer.
 */
public class WindowBuffer<T> {

    private final int size;
    private final Order putOrder;
    private final Map<LocalDate, T> values = new HashMap<>();

    private LocalDate currentDate;
    private LocalDate lastDate;

    /**
     * @param size in days
     */
    public WindowBuffer(int size, Order putOrder) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        this.size = size;
        this.putOrder = putOrder;
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void put(LocalDate date, T value) {
        if (putOrder == ASC) {
            ascendingPut(date, value);
        } else {
            descendingPut(date, value);
        }
    }

    public void put(LocalDate date, T value, BinaryOperator<T> reducer) {
        T oldValue = get(date);
        if (oldValue != null) {
            put(date, reducer.apply(oldValue, value));
        } else {
            put(date, value);
        }
    }

    private void ascendingPut(LocalDate date, T value) {
        if (currentDate != null && date.isBefore(currentDate)) {
            throw new IllegalArgumentException(
                    format("Date must be current or in the future: date:%s, current: %s", date, currentDate)
            );
        }

        values.put(date, value);
        currentDate = date;

        if (lastDate == null) {
            lastDate = currentDate;
            return;
        }

        LocalDate newLastDate = currentDate.minusDays(size - 1L);
        while (lastDate.isBefore(newLastDate)) {
            values.remove(lastDate);
            lastDate = lastDate.plusDays(1);
        }
        lastDate = newLastDate;
    }

    private void descendingPut(LocalDate date, T value) {
        if (currentDate != null && date.isAfter(currentDate)) {
            throw new IllegalArgumentException(
                    format("Date must be current or in the past: date:%s, current: %s", date, currentDate)
            );
        }

        values.put(date, value);
        currentDate = date;

        if (lastDate == null) {
            lastDate = currentDate;
            return;
        }

        LocalDate newLastDate = currentDate.plusDays(size);
        while (lastDate.isAfter(newLastDate) || lastDate.equals(newLastDate)) {
            values.remove(lastDate);
            lastDate = lastDate.minusDays(1);
        }
        lastDate = newLastDate;
    }

    /**
     * @return value from buffer by date or null if it doesn't exists
     */
    @Nullable
    public T get(LocalDate date) {
        return values.get(date);
    }

    public Stream<T> streamWithinRange(LocalDate from, LocalDate to) {
        return values.entrySet()
                     .stream()
                     .filter(entry -> isWithinRange(entry.getKey(), from, to))
                     .map(Map.Entry::getValue);
    }

    public Stream<T> streamWithinRange(LocalDate from, LocalDate to, Order streamOrder) {
        return streamEntriesWithinRange(from, to, streamOrder)
                .map(Map.Entry::getValue);
    }

    public Stream<Map.Entry<LocalDate, T>> streamEntriesWithinRange(LocalDate from, LocalDate to, Order streamOrder) {
        Comparator<Map.Entry<LocalDate, T>> c = Map.Entry.comparingByKey();

        if (streamOrder == DESC) c = c.reversed();

        return values.entrySet()
                     .stream()
                     .filter(entry -> isWithinRange(entry.getKey(), from, to))
                     .sorted(c);
    }

    public enum Order {
        ASC, DESC
    }
}
