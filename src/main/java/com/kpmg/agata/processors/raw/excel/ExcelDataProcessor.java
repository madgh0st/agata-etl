package com.kpmg.agata.processors.raw.excel;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.parser.handlers.ModelBasedExcelContentHandler;
import com.kpmg.agata.parser.handlers.UnstructuredExcelContentHandler;
import com.kpmg.agata.processors.FileProcessor;
import com.kpmg.agata.utils.filesystem.IFileSystem;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.binary.XSSFBSharedStringsTable;
import org.apache.poi.xssf.binary.XSSFBSheetHandler;
import org.apache.poi.xssf.binary.XSSFBStylesTable;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFBReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;

public class ExcelDataProcessor extends FileProcessor {

    private static final Logger log = LoggerFactory.getLogger(ExcelDataProcessor.class);

    public ExcelDataProcessor(IFileSystem fileSystem, String inputFilename, ModelTypeConfiguration config) {
        super(fileSystem, inputFilename, config, null);
    }

    private DataFormatter createDataFormatter() {
        return new DataFormatter(new Locale.Builder().setLanguage("ru").setScript("Cyrl").build()) {
            public String formatRawCellContents(double value, int formatIndex, String formatString) {
                if (DateUtil.isADateFormat(formatIndex, formatString)) {
                    return super.formatRawCellContents(value, formatIndex, formatString);
                } else {
                    String result;
                    BigDecimal num = new BigDecimal(String.valueOf(value));
                    try {
                        result = num.toBigIntegerExact().toString();
                    } catch (ArithmeticException e) {
                        result = num.toPlainString();
                    }
                    return result;
                }
            }
        };
    }

    private SheetContentsHandler getSheetContentsHandler(SequenceWriter sequenceWriter) {
        AbstractClientDataModel dataModel = config.getDataModelInstance();

        Map<String, String> hm = this.config.getDefaultFields();
        hm.put("fileName", this.inputFilename);
        config.setDefaultFields(hm);

        if (dataModel == null) {
            return new UnstructuredExcelContentHandler(sequenceWriter, config);
        }
        return new ModelBasedExcelContentHandler(objectMapper, sequenceWriter, config, dataModel, inputFilename);
    }


    private void processSheet(ReadOnlySharedStringsTable stringsTable,
                              StylesTable styles,
                              InputStream sheetInputStream) {

        log.info("Processing sheet: '{}' in excel file: '{}'", config.getSheetName(), this.inputFilename);
        String outputPath = new ConfigParser().getSheetOutputPath(config.getOutputDir(), this.inputFilename);

        try (SequenceWriter sequenceWriter = this.objectWriter.writeValues(this.fileSystem.writer(outputPath))) {
            SheetContentsHandler sheetContentsHandler = getSheetContentsHandler(sequenceWriter);
            XMLReader sheetParser = SAXHelper.newXMLReader();
            DataFormatter dataFormatter = createDataFormatter();

            sheetParser.setContentHandler(
                    new XSSFSheetXMLHandler(
                            styles,
                            null,
                            stringsTable,
                            sheetContentsHandler,
                            dataFormatter,
                            false
                    )
            );
            sheetParser.parse(new InputSource(sheetInputStream));
        } catch (ParserConfigurationException e) {
            log.error("Error in SAX parser configuration", e);
            throw new RuntimeException("Critical error in Excel XLSX SAX parser" + e.getMessage());
        } catch (SAXException e) {
            log.error("Exception during parsing sheet: {} from excel file: {} as XLSX",
                    this.config.getSheetName(), this.inputFilename, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Exception during writing json file: '{}' from sheet: '{}' in Excel file: '{}'", outputPath,
                    this.config.getSheetName(), this.inputFilename, e);
            throw new RuntimeException(e);
        }
    }

    public void run() {
        if (inputFilename.toLowerCase().endsWith(".xlsb")) {
            processXlsb();
            return;
        }

        log.info("Open Excel file: '{}' as XLSX", this.inputFilename);

        try (OPCPackage opcPackage = OPCPackage.open(this.fileSystem.reader(this.inputFilename))) {
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcPackage);
            XSSFReader xssfReader = new XSSFReader(opcPackage);
            StylesTable styles = xssfReader.getStylesTable();
            SheetIterator sheetIterator = (SheetIterator) xssfReader.getSheetsData();

            while (sheetIterator.hasNext()) {
                try (InputStream inputStream = sheetIterator.next()) {
                    String sheetName = sheetIterator.getSheetName();
                    if (this.config.getSheetName().equals(sheetName)) {
                        processSheet(strings, styles, inputStream);
                    }
                } catch (Exception e) {
                    log.error("Error while trying to process sheet in file as XLSX: '{}'", this.inputFilename);
                }
            }
        } catch (IOException | InvalidFormatException e) {
            log.error("Exception during opening Excel file: '{}'", this.inputFilename, e);
        } catch (OLE2NotOfficeXmlFileException e) {
            log.info("Failed to open Excel file as XLSX. File: '{}'", this.inputFilename);
            processHssf();
        } catch (Exception e) {
            log.error("Error while opening Excel file as XLSX. File: '{}'", this.inputFilename, e);
        }
    }

    private void processHssf() {
        log.info("Open Excel file: '{}' as XLS", this.inputFilename);
        try (POIFSFileSystem fs = new POIFSFileSystem(fileSystem.reader(inputFilename))) {
            // There is no reason to use event listener because whole file will be loaded in memory anyway.
            // See POIFSFileSystem.POIFSFileSystem:299
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            String sheetName = config.getSheetName();
            HSSFSheet sheet = workbook.getSheet(sheetName);

            if (sheet != null) {
                processHssfSheet(sheet);
            }
        } catch (IOException e) {
            log.error("Error while opening Excel file as XLS. File: '{}'", this.inputFilename, e);
        }
    }

    private void processHssfSheet(HSSFSheet sheet) {
        log.info("Processing sheet: '{}' in excel file: '{}'", config.getSheetName(), this.inputFilename);
        String outputPath = new ConfigParser().getSheetOutputPath(config.getOutputDir(), this.inputFilename);
        try (SequenceWriter sequenceWriter = objectWriter.writeValues(fileSystem.writer(outputPath))) {
            SheetContentsHandler contentsHandler = getSheetContentsHandler(sequenceWriter);
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (row == null) continue;
                processHssfRow(rowIndex, row, contentsHandler);
            }
        } catch (IOException e) {
            log.error("Exception while processing sheet from XLS file: '{}'", this.inputFilename, e);
        }
    }

    private void processHssfRow(int rowIndex, HSSFRow row, SheetContentsHandler contentsHandler) {
        contentsHandler.startRow(rowIndex);
        for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
            HSSFCell cell = row.getCell(colIndex);
            String value = getHssfCellValue(cell);
            if (value == null) continue;
            CellReference cellReference = new CellReference(cell.getRowIndex(), cell.getColumnIndex());
            contentsHandler.cell(cellReference.formatAsString(), value, null);
        }
        contentsHandler.endRow(rowIndex);
    }

    private String getHssfCellValue(HSSFCell cell) {
        if (cell == null) return null;
        String value = null;
        switch (cell.getCellType()) {
            case NUMERIC:
                value = String.valueOf(cell.getNumericCellValue());
                if (value.endsWith(".0")) {
                    value = value.replace(".0", "");
                }
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case NUMERIC:
                        value = String.valueOf(cell.getNumericCellValue());
                        if (value.endsWith(".0")) {
                            value = value.replace(".0", "");
                        }
                        break;
                    case STRING:
                        value = cell.getStringCellValue();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return value;
    }

    private void processXlsb() {
        log.info("Open Excel file: '{}' as XLSB", this.inputFilename);
        try (OPCPackage opcPackage = OPCPackage.open(this.fileSystem.reader(this.inputFilename))) {
            XSSFBSharedStringsTable strings = new XSSFBSharedStringsTable(opcPackage);
            XSSFBReader xssfReader = new XSSFBReader(opcPackage);
            XSSFBStylesTable styles = xssfReader.getXSSFBStylesTable();
            XSSFBReader.SheetIterator sheetIterator = (XSSFBReader.SheetIterator) xssfReader.getSheetsData();

            while (sheetIterator.hasNext()) {
                try (InputStream inputStream = sheetIterator.next()) {
                    String sheetName = sheetIterator.getSheetName();
                    if (this.config.getSheetName().equals(sheetName)) {
                        processXlsbSheet(strings, styles, inputStream);
                    }
                } catch (Exception e) {
                    log.error("Error while trying to process sheet in file as XLSB: '{}'", this.inputFilename);
                }
            }
        } catch (Exception e) {
            log.error("Error while opening Excel file as XLSB. File: '{}'", this.inputFilename, e);
        }
    }

    private void processXlsbSheet(XSSFBSharedStringsTable strings, XSSFBStylesTable styles, InputStream inputStream) {
        log.info("Processing sheet: '{}' in excel file: '{}'", config.getSheetName(), this.inputFilename);
        String outputPath = new ConfigParser().getSheetOutputPath(config.getOutputDir(), this.inputFilename);

        try (SequenceWriter sequenceWriter = this.objectWriter.writeValues(this.fileSystem.writer(outputPath))) {
            XSSFBSheetHandler sheetHandler = new XSSFBSheetHandler(inputStream,
                    styles,
                    null,
                    strings,
                    getSheetContentsHandler(sequenceWriter),
                    createDataFormatter(),
                    false);
            sheetHandler.parse();
        } catch (IOException e) {
            log.error("Exception during writing json file: '{}' from sheet: '{}' in Excel file: '{}'", outputPath,
                    this.config.getSheetName(), this.inputFilename, e);
            throw new RuntimeException(e);
        }
    }
}
