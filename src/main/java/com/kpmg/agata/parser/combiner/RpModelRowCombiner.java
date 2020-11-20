package com.kpmg.agata.parser.combiner;

import org.apache.poi.ss.util.CellReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RpModelRowCombiner implements ExcelRowCombiner, CellCacheConsumer {
    private static final CellReference DATE_3_POINT = new CellReference(3, 4);
    private static final CellReference DATE_2_POINT = new CellReference(4, 4);
    private static final CellReference DATE_1_POINT = new CellReference(5, 4);
    private static final CellReference MEASURE_POINT = new CellReference(6, 4);
    private static final CellReference REPORT_DATE_POINT = DATE_3_POINT;


    private static final String NOT_DATA_CELL_VALUE = "ИТОГО";
    private static final String BUSINESS_UNIT_HEADER = "БЕ";
    private static final String LEGAL_ENTITY_HEADER = "ЮЛ";
    private static final String REGION_HEADER = "Регион";
    private static final String COUNTERPARTY_HEADER = "Контрагент";
    private static final String PDZ_HEADER = "ПДЗ";
    private static final String DATE_HEADER = "Дата";
    private static final String MEASURE_HEADER = "Единица измерения";
    private static final String REPORT_DATE_HEADER = "Отчетная дата";

    private static final int DESCRIPTION_ROW_NUMBER = 0;

    private final Map<String, String> dataRow = new HashMap<>();
    private final List<Map<String, String>> resultRows = new ArrayList<>();

    private CellCache cache;
    private int rowCounter = 0;

    @Override
    public void add(Map<String, String> row) {
        if (row.isEmpty()) throw new IllegalArgumentException(format("Row is empty: %s", row));
        if (ready()) throw new IllegalStateException(format("Combiner is full: combiner %s; row %s", this, row));

        if (isDataRow(row)) {
            dataRow.clear();
            dataRow.putAll(row);
        }

        rowCounter++;
    }

    @Override
    public boolean ready() {
        return cache != null
                && cache.containsValueInPoint(DATE_1_POINT)
                && cache.containsValueInPoint(DATE_2_POINT)
                && cache.containsValueInPoint(DATE_3_POINT)
                && cache.containsValueInPoint(MEASURE_POINT)
                && (!dataRow.isEmpty() || !resultRows.isEmpty());
    }

    @Override
    public Map<String, String> combine() {
        if (!ready()) throw new IllegalStateException(format("Combiner is not ready: combiner %s", this));

        if (resultRows.isEmpty()) {
            createResultRows();
            dataRow.clear();
        }

        Map<String, String> result = resultRows.get(0);
        resultRows.remove(0);
        return result;
    }

    @Override
    public void setCellCache(CellCache cache) {
        this.cache = cache
                .subscribeToPoint(MEASURE_POINT)
                .subscribeToPoint(DATE_1_POINT)
                .subscribeToPoint(DATE_2_POINT)
                .subscribeToPoint(DATE_3_POINT);
    }

    private boolean isDataRow(Map<String, String> row) {
        return isNotBlank(row.get(BUSINESS_UNIT_HEADER))
                && isNotBlank(row.get(LEGAL_ENTITY_HEADER))
                && isNotBlank(row.get(REGION_HEADER))
                && isNotBlank(row.get(COUNTERPARTY_HEADER))
                && !NOT_DATA_CELL_VALUE.equals(row.get(LEGAL_ENTITY_HEADER))
                && !NOT_DATA_CELL_VALUE.equals(row.get(REGION_HEADER))
                && !NOT_DATA_CELL_VALUE.equals(row.get(COUNTERPARTY_HEADER))
                && !isDescriptionRow();
    }

    private boolean isDescriptionRow() {
        return rowCounter == DESCRIPTION_ROW_NUMBER;
    }

    private void createResultRows() {
        Map<String, String> firstRow = new HashMap<>();
        firstRow.put(BUSINESS_UNIT_HEADER, dataRow.get(BUSINESS_UNIT_HEADER));
        firstRow.put(LEGAL_ENTITY_HEADER, dataRow.get(LEGAL_ENTITY_HEADER));
        firstRow.put(REGION_HEADER, dataRow.get(REGION_HEADER));
        firstRow.put(COUNTERPARTY_HEADER, dataRow.get(COUNTERPARTY_HEADER));
        firstRow.put(MEASURE_HEADER, cache.get(MEASURE_POINT));
        firstRow.put(REPORT_DATE_HEADER, cache.get(REPORT_DATE_POINT));
        Map<String, String> secondRow = new HashMap<>(firstRow);
        Map<String, String> thirdRow = new HashMap<>(firstRow);

        firstRow.put(PDZ_HEADER, dataRow.get(buildPdzHeader(DATE_1_POINT)));
        firstRow.put(DATE_HEADER, cache.get(DATE_1_POINT));

        secondRow.put(PDZ_HEADER, dataRow.get(buildPdzHeader(DATE_2_POINT)));
        secondRow.put(DATE_HEADER, cache.get(DATE_2_POINT));

        thirdRow.put(PDZ_HEADER, dataRow.get(buildPdzHeader(DATE_3_POINT)));
        thirdRow.put(DATE_HEADER, cache.get(DATE_3_POINT));

        resultRows.clear();
        resultRows.add(firstRow);
        resultRows.add(secondRow);
        resultRows.add(thirdRow);
    }

    private String buildPdzHeader(CellReference datePoint) {
        String dateString = cache.get(datePoint);
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yy"));
        return "ПДЗ на " + date.format(DateTimeFormatter.ofPattern("d.MM"));
    }

    @Override
    public String toString() {
        return "RpModelRowCombiner{" +
                "dataRow=" + dataRow +
                ", resultRows=" + resultRows +
                ", cache=" + cache +
                ", rowCounter=" + rowCounter +
                '}';
    }
}
