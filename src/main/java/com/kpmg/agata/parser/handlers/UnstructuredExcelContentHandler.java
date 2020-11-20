package com.kpmg.agata.parser.handlers;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.config.ModelTypeConfiguration;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UnstructuredExcelContentHandler implements SheetContentsHandler {
    private static Logger log = LoggerFactory.getLogger(UnstructuredExcelContentHandler.class);
    private final ModelTypeConfiguration modelTypeConfiguration;
    private SequenceWriter writer;
    private int currentRow = -1;
    private int currentCol = -1;
    private Map<String, String> outputBuffer  = new HashMap<>();

    public UnstructuredExcelContentHandler(SequenceWriter writer, ModelTypeConfiguration modelTypeConfiguration) {
        this.writer = writer;
        this.modelTypeConfiguration = modelTypeConfiguration;
    }

    private void insertMissingRowsToBuffer(int number) {
        this.outputBuffer.clear();
        for (int i = 0; i < number; i++) {
            currentRow += 1;
            writeOutputBuffer();
        }
    }

    private void writeOutputBuffer() {
        try {
            Map<String, String> result = new HashMap<>();
            result.putAll(this.outputBuffer);
            result.putAll(this.modelTypeConfiguration.getDefaultFields());
            result.put("rowid", Integer.toString(currentRow));
            writer.write(result);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        this.outputBuffer.clear();
    }

    private void addToOutputBuffer(String formattedValue) {
        this.outputBuffer.put(String.format("col_%d", this.currentCol), formattedValue);
    }

    @Override
    public void startRow(int rowNum) {
        // If there were gaps, output the missing rows
        insertMissingRowsToBuffer(rowNum - currentRow - 1);
        currentRow = rowNum;
        currentCol = -1;
    }

    @Override
    public void endRow(int rowNum) {
        writeOutputBuffer();
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        this.currentCol =(new CellReference(cellReference)).getCol();
        addToOutputBuffer(formattedValue);
    }
}
