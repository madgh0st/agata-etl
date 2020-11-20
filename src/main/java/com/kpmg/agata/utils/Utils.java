package com.kpmg.agata.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Utils {

    private Utils() {
    }

    /**
     * Wrapper to fast create variable-sized list
     */
    @SafeVarargs
    public static <T> List<T> listOf(T... args) {
        return new ArrayList<>(Arrays.asList(args));
    }

    public static boolean checkDate(String dateString, String pattern) {
        if (dateString == null) return false;

        DateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        try {
            format.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String restoreLineSeparators(String content) {
        return content.replaceAll("\\r\\n?", "\n");
    }

    public static String withSchema(String schema, String table) {
        return schema + "." + table;
    }

    /**
     * @return date ∈ [from;to]
     */
    public static boolean isWithinRange(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from) && !date.isAfter(to);
    }

    public static String getModificationDateTime() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }

    public static String getModificationDate() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
    }

    public static <T> String toStringOrNull(T obj) {
        return obj == null ? null : String.valueOf(obj);
    }

    public static <K, V> void putNotNull(Map<K, V> map, K key, V value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    /**
     * 1.2345 -> 1.23
     * 1.0 -> 1.0
     */
    public static double round(double value, int decimalPoints) {
        if(decimalPoints <= 0) throw new IllegalArgumentException("decimalPoints must be positive");

        double shift = Math.pow(10.0, decimalPoints);
        return Math.round(value * shift) / shift;
    }

    public static <T> List<T> concatLists(List<T> first, List<T> second) {
        return Stream.concat(first.stream(), second.stream())
                     .collect(toList());
    }
}
