package com.kpmg.agata.processors.raw;

import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.Environment;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.WhiteListDoModel;
import com.kpmg.agata.parser.FileType;
import com.kpmg.agata.processors.FileProcessor;
import com.kpmg.agata.processors.raw.csv.CsvDataProcessor;
import com.kpmg.agata.processors.raw.excel.ExcelDataProcessor;
import com.kpmg.agata.processors.raw.xml.XmlDataProcessor;
import com.kpmg.agata.utils.filesystem.HDFSFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.kpmg.agata.constant.LogConstants.END_OF;
import static com.kpmg.agata.constant.LogConstants.RAW_DATA_PROCESSING;
import static com.kpmg.agata.constant.LogConstants.START_OF;

public class RawDataToJsonProcessor {

    private static final Logger log = LoggerFactory.getLogger(RawDataToJsonProcessor.class);
    private final String hdfsDirListDoWhiteList = Environment.getConfig().getProperty("hdfs.dir.list_do.whitelist");
    private HDFSFacade hdfsFacade;

    public RawDataToJsonProcessor() {
        this.hdfsFacade = new HDFSFacade();
    }

    public void parseWhiteList() {

        if (StringUtils.isBlank(hdfsDirListDoWhiteList)) {
            throw new IllegalArgumentException("hdfsDirListDoWhiteList property can't be empty");
        }

        log.info("Parse white list");
        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("Sheet1");
        config.setDataModelInstance(new WhiteListDoModel());
        config.setHeaderRowNumber(0);
        config.setOutputDir(hdfsDirListDoWhiteList + "/json/");
        config.setInputDir(hdfsDirListDoWhiteList);
        config.setGlob("white_list_do.xlsx");
        new ExcelDataProcessor(hdfsFacade, hdfsDirListDoWhiteList + "white_list_do.xlsx", config).run();
    }

    public void parseData(List<String> dateList, ConfigParser config) {
        dateList
                .forEach(date -> {
                    log.info("{}{}: {}", START_OF, RAW_DATA_PROCESSING, date);
                    try {
                        config.withDate(date).parallelStream().forEach(modelConfig -> processData(modelConfig, date));
                    } catch (Exception e) {
                        log.error("Excel config can't be parsed");
                    } finally {
                        log.info("{}{}: {}", END_OF, RAW_DATA_PROCESSING, date);
                    }
                });
    }

    private void processData(ModelTypeConfiguration config, String date) {
        try {
            hdfsFacade
                    .globMatch(config.getInputDir(), config.getGlob())
                    .parallelStream()
                    .forEach(path -> {
                        FileType fileType = FileType.resolveType(path);
                        FileProcessor fileProcessor;
                        switch (fileType) {
                            case EXCEL:
                                fileProcessor = new ExcelDataProcessor(hdfsFacade, path, config);
                                break;
                            case XML:
                                fileProcessor = new XmlDataProcessor(hdfsFacade, path, config, date);
                                break;
                            case CSV:
                                fileProcessor = new CsvDataProcessor(hdfsFacade, path, config, date);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected file extension");
                        }
                        fileProcessor.run();
                    });
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
