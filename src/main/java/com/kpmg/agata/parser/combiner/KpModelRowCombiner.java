package com.kpmg.agata.parser.combiner;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class KpModelRowCombiner implements ExcelRowCombiner {
    private static final String MULTI_TYPES_EXC_PATTERN = "Row type has multiple types: combiner %s; row %s";
    private final Map<String, String> departmentRow = new HashMap<>();
    private final Map<String, String> managerRow = new HashMap<>();
    private final Map<String, String> counterpartyRow = new HashMap<>();
    private final Map<String, String> detailsRow = new HashMap<>();

    @Override
    public void add(Map<String, String> row) {
        if (row.isEmpty()) throw new IllegalArgumentException(format("Row is empty: %s", row));
        if (ready()) throw new IllegalStateException(format("Combiner is full: combiner %s; row %s", this, row));
        boolean rowUpdated = false;

        if (isDepartmentRow(row)) {
            departmentRow.clear();
            managerRow.clear();
            counterpartyRow.clear();
            detailsRow.clear();
            departmentRow.putAll(row);
            rowUpdated = true;
        }

        if (isManagerRow(row)) {
            if (rowUpdated) {
                throw new IllegalArgumentException(
                        format(MULTI_TYPES_EXC_PATTERN, this, row));
            }
            managerRow.clear();
            counterpartyRow.clear();
            detailsRow.clear();
            managerRow.putAll(row);
            rowUpdated = true;
        }

        if (isCounterpartyRow(row)) {
            if (rowUpdated) {
                throw new IllegalArgumentException(
                        format(MULTI_TYPES_EXC_PATTERN, this, row));
            }
            counterpartyRow.clear();
            detailsRow.clear();
            counterpartyRow.putAll(row);
            rowUpdated = true;
        }

        if (isDetailsRow(row)) {
            if (rowUpdated) {
                throw new IllegalArgumentException(
                        format(MULTI_TYPES_EXC_PATTERN, this, row));
            }
            detailsRow.clear();
            detailsRow.putAll(row);
            rowUpdated = true;
        }

        if (!rowUpdated) {
            throw new IllegalArgumentException(
                    format("Row type is undetermined: combiner %s; row %s", this, row));
        }
    }

    @Override
    public boolean ready() {
        return !departmentRow.isEmpty()
                && !managerRow.isEmpty()
                && !counterpartyRow.isEmpty()
                && !detailsRow.isEmpty();
    }

    @Override
    public Map<String, String> combine() {
        if (!ready()) throw new IllegalStateException(format("Combiner is not ready: combiner %s", this));

        Map<String, String> result = new HashMap<>();
        result.putAll(departmentRow);
        result.putAll(managerRow);
        result.putAll(counterpartyRow);
        result.putAll(detailsRow);

        detailsRow.clear();
        return result;
    }

    private boolean isDepartmentRow(Map<String, String> row) {
        return checkNullsAndNotNulls(row, singletonList("1"));
    }

    private boolean isManagerRow(Map<String, String> row) {
        return checkNullsAndNotNulls(row, singletonList("2"));
    }

    private boolean isCounterpartyRow(Map<String, String> row) {
        return checkNullsAndNotNulls(row, asList("3", "4", "5"));
    }

    private boolean isDetailsRow(Map<String, String> row) {
        return checkNullsAndNotNulls(row, emptyList())
                && IntStream.iterate(6, i -> ++i)
                .limit(19)
                .mapToObj(key -> row.get(valueOf(key)))
                .anyMatch(StringUtils::isNotBlank);
    }

    private boolean checkNullsAndNotNulls(Map<String, String> source, List<String> notNulls) {
        boolean nullsCorrect = IntStream.iterate(1, i -> ++i)
                .limit(5)
                .filter(i -> !notNulls.contains(valueOf(i)))
                .mapToObj(key -> source.get(valueOf(key)))
                .allMatch(StringUtils::isBlank);

        boolean notNullsCorrect = notNulls.stream()
                .map(source::get)
                .allMatch(StringUtils::isNotBlank);

        return nullsCorrect && notNullsCorrect;
    }

    @Override
    public String toString() {
        return "KpModelRowCombiner{" +
                "departmentRow=" + departmentRow +
                ", managerRow=" + managerRow +
                ", counterpatryRow=" + counterpartyRow +
                ", detailsRow=" + detailsRow +
                '}';
    }
}
