package com.kpmg.agata.parser.combiner;

import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.utils.Utils.checkDate;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BmModelRowCombiner implements ExcelRowCombiner {
    private static final String COUNTERPARTY_PERIOD_HEADER = "Контрагент/Период";
    private static final String COUNTERPARTY_HEADER = "Контрагент";
    private static final String PERIOD_HEADER = "Период";
    private static final String MULTI_TYPES_EXC_PATTERN = "Row type has multiple types: combiner %s; row %s";

    private final Map<String, String> counterpartyRow = new HashMap<>();
    private final Map<String, String> dataRow = new HashMap<>();

    private boolean totalRowPassed = false;

    @Override
    public void add(Map<String, String> row) {
        if (row.isEmpty()) throw new IllegalArgumentException(format("Row is empty: %s", row));
        if (ready()) throw new IllegalStateException(format("Combiner is full: combiner %s; row %s", this, row));
        if (totalRowPassed) return;

        boolean rowUpdated = false;

        if (isTotalRow(row)) {
            totalRowPassed = true;
            counterpartyRow.clear();
            dataRow.clear();
            rowUpdated = true;
        }

        if (isCounterpartyRow(row)) {
            if (rowUpdated) {
                throw new IllegalArgumentException(
                        format(MULTI_TYPES_EXC_PATTERN, this, row));
            }
            counterpartyRow.clear();
            dataRow.clear();
            counterpartyRow.putAll(row);
            rowUpdated = true;
        }

        if (isDataRow(row)) {
            if (rowUpdated) {
                throw new IllegalArgumentException(
                        format(MULTI_TYPES_EXC_PATTERN, this, row));
            }
            dataRow.clear();
            dataRow.putAll(row);
        }
    }

    @Override
    public boolean ready() {
        return !counterpartyRow.isEmpty()
                && !dataRow.isEmpty()
                && !totalRowPassed;
    }

    @Override
    public Map<String, String> combine() {
        if (!ready()) throw new IllegalStateException(format("Combiner is not ready: combiner %s", this));

        Map<String, String> result = new HashMap<>();
        result.put(COUNTERPARTY_HEADER, counterpartyRow.get(COUNTERPARTY_PERIOD_HEADER));
        result.put(PERIOD_HEADER, dataRow.get(COUNTERPARTY_PERIOD_HEADER));
        result.put("Общая задолженность", dataRow.get("Общая задолженность"));
        result.put("Задолженность", dataRow.get("Задолженность"));
        result.put("Просроченная задолженность", dataRow.get("Просроченная задолженность"));
        result.put("Сумма штрафа и пени", dataRow.get("Сумма штрафа и пени"));
        dataRow.clear();
        return result;
    }

    private boolean isCounterpartyRow(Map<String, String> row) {
        return isNotBlank(row.get(COUNTERPARTY_PERIOD_HEADER))
                && !checkDate(row.get(COUNTERPARTY_PERIOD_HEADER), "dd.MM.yyyy")
                && !isTotalRow(row);
    }

    private boolean isDataRow(Map<String, String> row) {
        return checkDate(row.get(COUNTERPARTY_PERIOD_HEADER), "dd.MM.yyyy")
                && !isTotalRow(row);
    }

    private boolean isTotalRow(Map<String, String> row) {
        return "Итого".equals(row.get(COUNTERPARTY_PERIOD_HEADER));
    }

    @Override
    public String toString() {
        return "BmModelRowCombiner{" +
                "counterpartyRow=" + counterpartyRow +
                ", dataRow=" + dataRow +
                ", totalRowPassed=" + totalRowPassed +
                '}';
    }
}
