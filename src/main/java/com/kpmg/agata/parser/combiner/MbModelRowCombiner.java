package com.kpmg.agata.parser.combiner;

import java.util.HashMap;
import java.util.Map;

import static com.kpmg.agata.utils.Utils.checkDate;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MbModelRowCombiner implements ExcelRowCombiner {
    private static final String CUR_CP_CONT_DEAL_HEADER = "Валюта/Контрагент, Код/Договор контрагента, Код/Сделка";
    private static final String CURRENCY_ORDER_CP_HEADER = "Валюта/Контрагент заказа";
    private static final String DEAL_DATE_HEADER = "Дата сделки";
    private static final String DEPARTMENT_HEADER = "Подразделение";
    private static final String PAST_DUE_DAYS_HEADER = "Просрочка, дней";
    private static final String DELAY_DAYS_HEADER = "Дней отсрочки";
    private static final String DEBT_WITHIN_DELAY_HEADER = "Сумма ДЗ в валюте договора/Сумма в пределах отсрочки";
    private static final String PAST_DUE_5_HEADER = "Сумма ДЗ в валюте договора/Сумма просрочки <5 дней";
    private static final String PAST_DUE_6_20_HEADER = "Сумма ДЗ в валюте договора/Сумма просрочки 6-20 дней";
    private static final String PAST_DUE_OVER_20_HEADER = "Сумма ДЗ в валюте договора/Сумма просрочки >20 дней";

    private static final String MULTI_TYPES_EXC_PATTERN = "Row type has multiple types: combiner %s; row %s";


    private final Map<String, String> currencyRow = new HashMap<>();
    private final Map<String, String> counterpartyRow = new HashMap<>();
    private final Map<String, String> contractRow = new HashMap<>();
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
            currencyRow.clear();
            counterpartyRow.clear();
            contractRow.clear();
            dataRow.clear();
            rowUpdated = true;
        }

        if (isCurrencyRow(row)) {
            if (rowUpdated) {
                throw new IllegalArgumentException(
                        format(MULTI_TYPES_EXC_PATTERN, this, row));
            }
            currencyRow.clear();
            counterpartyRow.clear();
            contractRow.clear();
            dataRow.clear();
            currencyRow.putAll(row);
            rowUpdated = true;
        }

        if (isCounterpartyRow(row)) {
            if (rowUpdated) {
                throw new IllegalArgumentException(
                        format(MULTI_TYPES_EXC_PATTERN, this, row));
            }
            counterpartyRow.clear();
            contractRow.clear();
            dataRow.clear();
            counterpartyRow.putAll(row);
            rowUpdated = true;
        }

        if (isContractRow(row)) {
            if (rowUpdated) {
                throw new IllegalArgumentException(
                        format(MULTI_TYPES_EXC_PATTERN, this, row));
            }
            contractRow.clear();
            dataRow.clear();
            contractRow.putAll(row);
            rowUpdated = true;
        }

        if (isDataRow(row)) {
            if (rowUpdated) {
                throw new IllegalArgumentException(
                        format(MULTI_TYPES_EXC_PATTERN, this, row));
            }
            dataRow.clear();
            dataRow.putAll(row);
            rowUpdated = true;
        }

        if (!rowUpdated) {
            throw new IllegalArgumentException(
                    format("Row type is undetermined: combiner %s; row %s", this, row));
        }
    }

    @Override
    public boolean ready() {
        return !currencyRow.isEmpty()
                && !counterpartyRow.isEmpty()
                && !contractRow.isEmpty()
                && !dataRow.isEmpty()
                && !totalRowPassed;
    }

    @Override
    public Map<String, String> combine() {
        if (!ready()) throw new IllegalStateException(format("Combiner is not ready: combiner %s", this));

        Map<String, String> result = new HashMap<>();
        result.put("Валюта", currencyRow.get(CUR_CP_CONT_DEAL_HEADER));
        result.put("Контрагент", counterpartyRow.get(CUR_CP_CONT_DEAL_HEADER));
        result.put("Договор контрагента", contractRow.get(CUR_CP_CONT_DEAL_HEADER));
        result.put("Сделка", dataRow.get(CUR_CP_CONT_DEAL_HEADER));
        result.put("Контрагент заказа", dataRow.get(CURRENCY_ORDER_CP_HEADER));
        result.put(DEAL_DATE_HEADER, dataRow.get(DEAL_DATE_HEADER));
        result.put(DEPARTMENT_HEADER, dataRow.get(DEPARTMENT_HEADER));
        result.put(PAST_DUE_DAYS_HEADER, dataRow.get(PAST_DUE_DAYS_HEADER));
        result.put(DELAY_DAYS_HEADER, contractRow.get(DELAY_DAYS_HEADER));
        result.put(DEBT_WITHIN_DELAY_HEADER, dataRow.get(DEBT_WITHIN_DELAY_HEADER));
        result.put(PAST_DUE_5_HEADER, dataRow.get(PAST_DUE_5_HEADER));
        result.put(PAST_DUE_6_20_HEADER, dataRow.get(PAST_DUE_6_20_HEADER));
        result.put(PAST_DUE_OVER_20_HEADER, dataRow.get(PAST_DUE_OVER_20_HEADER));

        dataRow.clear();
        return result;
    }

    private boolean isCurrencyRow(Map<String, String> row) {
        return isCurrency(row.get(CUR_CP_CONT_DEAL_HEADER))
                && isBlank(row.get(CURRENCY_ORDER_CP_HEADER))
                && isBlank(row.get(DEAL_DATE_HEADER))
                && isBlank(row.get(DEPARTMENT_HEADER))
                && isBlank(row.get(PAST_DUE_DAYS_HEADER));
    }

    private boolean isCounterpartyRow(Map<String, String> row) {
        return !isCurrency(row.get(CUR_CP_CONT_DEAL_HEADER))
                && isBlank(row.get(CURRENCY_ORDER_CP_HEADER))
                && isBlank(row.get(DEAL_DATE_HEADER))
                && isBlank(row.get(DEPARTMENT_HEADER))
                && isBlank(row.get(PAST_DUE_DAYS_HEADER))
                && !isTotalRow(row);
    }

    private boolean isContractRow(Map<String, String> row) {
        return isNotBlank(row.get(CUR_CP_CONT_DEAL_HEADER))
                && isCurrency(row.get(CURRENCY_ORDER_CP_HEADER))
                && isBlank(row.get(DEAL_DATE_HEADER))
                && isBlank(row.get(DEPARTMENT_HEADER))
                && isBlank(row.get(PAST_DUE_DAYS_HEADER));
    }

    private boolean isDataRow(Map<String, String> row) {
        return isNotBlank(row.get(CUR_CP_CONT_DEAL_HEADER))
                && isNotBlank(row.get(CURRENCY_ORDER_CP_HEADER))
                && checkDate(row.get(DEAL_DATE_HEADER), "dd.MM.yyyy")
                && isNotBlank(row.get(PAST_DUE_DAYS_HEADER));
    }

    private boolean isTotalRow(Map<String, String> row) {
        return "Итого".equals(row.get(CUR_CP_CONT_DEAL_HEADER))
                && isBlank(row.get(CURRENCY_ORDER_CP_HEADER))
                && isBlank(row.get(DEAL_DATE_HEADER))
                && isBlank(row.get(DEPARTMENT_HEADER))
                && isBlank(row.get(PAST_DUE_DAYS_HEADER));
    }

    private boolean isCurrency(String value) {
        return asList("EUR", "USD", "руб.").contains(value);
    }

    @Override
    public String toString() {
        return "MbModelRowCombiner{" +
                "currencyRow=" + currencyRow +
                ", counterpartyRow=" + counterpartyRow +
                ", contractRow=" + contractRow +
                ", dataRow=" + dataRow +
                ", totalRowPassed=" + totalRowPassed +
                '}';
    }
}
