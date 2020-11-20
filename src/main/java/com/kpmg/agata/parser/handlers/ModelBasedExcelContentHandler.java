package com.kpmg.agata.parser.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.models.combinable.Combinable;
import com.kpmg.agata.models.combinable.CustomHeader;
import com.kpmg.agata.parser.combiner.CellCache;
import com.kpmg.agata.parser.combiner.CellCacheConsumer;
import com.kpmg.agata.parser.combiner.ExcelRowCombiner;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelBasedExcelContentHandler implements SheetContentsHandler {
    private static final Logger log = LoggerFactory.getLogger(ModelBasedExcelContentHandler.class);
    private final ExcelRowCombiner rowCombiner;
    private final CellCache cellCache = new CellCache();
    private ObjectMapper objectMapper;
    private SequenceWriter writer;
    private ModelTypeConfiguration config;
    private AbstractClientDataModel dataModel;
    private String fileName;

    // columnNumber -> name
    private Map<Short, String> header = new HashMap<>();
    // name -> value
    private Map<String, String> outputBuffer = new HashMap<>();

    private int currentRow = -1;
    private boolean isHeader;

    public ModelBasedExcelContentHandler(ObjectMapper objectMapper, SequenceWriter writer, ModelTypeConfiguration config, AbstractClientDataModel dataModel, String fileName) {
        this.objectMapper = objectMapper;
        this.writer = writer;
        this.config = config;
        this.dataModel = dataModel;
        this.fileName = fileName;

        if (dataModel instanceof Combinable) {
            rowCombiner = ((Combinable) dataModel).createCombiner();
        } else {
            rowCombiner = null;
        }

        if (rowCombiner instanceof CellCacheConsumer) {
            ((CellCacheConsumer) rowCombiner).setCellCache(cellCache);
        }
    }

    private void writeOutputBuffer() {
        try {
            if (outputBuffer.values().stream().allMatch(Objects::isNull)) {
                return;
            }

            if (rowCombiner == null) {
                writeRow(new HashMap<>(outputBuffer));
                return;
            }

            rowCombiner.add(outputBuffer);
            if (!rowCombiner.ready()) {
                return;
            }

            while (rowCombiner.ready()) {
                writeRow(rowCombiner.combine());
            }

        } catch (IOException e) {
            log.error(String.format("Writing result json error: file=%s, sheet=%s, row=%s, header=%s, outputBuffer=%s",
                    fileName, config.getSheetName(), currentRow, header, outputBuffer));
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            log.error(String.format("Object mapping error: file=%s, sheet=%s, row=%s, header=%s, outputBuffer=%s",
                    fileName, config.getSheetName(), currentRow, header, outputBuffer));
            throw new RuntimeException(e);
        }
    }

    private void writeRow(Map<String, String> row) throws IOException {
        row.putAll(config.getDefaultFields());
        row.put("fileName", this.fileName);
        row.put("sheetName", this.config.getSheetName());
        this.writer.write(objectMapper.convertValue(row, dataModel.getClass()));
        this.writer.flush();
    }

    private void putToOutputBuffer(short cellNum, String value) {
        String modelHeader = this.header.get(cellNum);
        if (modelHeader != null) {
            this.outputBuffer.put(modelHeader, value);
        }
    }

    @Override
    public void startRow(int rowNum) {
        this.currentRow = rowNum;

        if (rowNum == this.config.getHeaderRowNumber()) {
            this.isHeader = true;
        }
    }

    @Override
    public void endRow(int rowNum) {
        if (this.isHeader) {
            this.isHeader = false;
        } else {
            writeOutputBuffer();
        }
        outputBuffer.clear();
    }

    @Override
    public void cell(String cellReference, String value, XSSFComment comment) {
        CellReference point = new CellReference(cellReference);
        cellCache.consume(point, value);

        if (value != null) {
            if (!this.header.isEmpty() && !this.isHeader) {
                putToOutputBuffer(point.getCol(), value);
            } else if (this.isHeader) {
                if (dataModel instanceof CustomHeader) {
                    Map<Short, String> customHeader = ((CustomHeader) dataModel).createCustomHeader();
                    header.putAll(customHeader);
                } else {
                    header.put(point.getCol(), value);
                }
            }
        }
    }
}
