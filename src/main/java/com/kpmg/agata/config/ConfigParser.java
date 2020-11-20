package com.kpmg.agata.config;

import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.parser.ConfigType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kpmg.agata.constant.database.BaseConstants.SORT_DO;
import static com.kpmg.agata.utils.Utils.getModificationDateTime;
import static java.util.stream.Collectors.toList;

public class ConfigParser {

    private final String dirSrcXlsSpecifier;
    private final String dirParquetRawSpecifier;
    private final String dirJsonDeltasSpecifier;
    private static final String DATE_SPECIFIER = "${date}";
    private static final int CONFIG_START = 1;
    private static final int INPUT_DIR_COL = 0;
    private static final int NAME_DO_COL = 1;
    private static final int TYPE_COL = 2;
    private static final int GLOB_COL = 3;
    private static final int HEADER_ROW_NUM_COL = 4;
    private static final int SHEET_NAME_COL = 5;
    private static final String CONFIG_SHEET_NAME = "Config";

    private static final Logger log = LoggerFactory.getLogger(ConfigParser.class);

    private List<ModelTypeConfiguration> modelTypeConfiguration = new ArrayList<>();

    public ConfigParser() {
        this.dirSrcXlsSpecifier = Environment.getConfig().getProperty("hdfs.dir.src.xls");
        this.dirParquetRawSpecifier = Environment.getConfig().getProperty("hdfs.dir.parquet.raw");
        this.dirJsonDeltasSpecifier = Environment.getConfig().getProperty("hdfs.dir.json_deltas");
    }

    public List<ModelTypeConfiguration> getModelTypeConfiguration() {
        return modelTypeConfiguration;
    }

    public void setModelTypeConfiguration(List<ModelTypeConfiguration> modelTypeConfiguration) {
        this.modelTypeConfiguration = modelTypeConfiguration;
    }

    public ConfigParser getRawConfigAgataXls(String pathToFile) throws IOException {

        List<ModelTypeConfiguration> list = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(pathToFile);
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheet(CONFIG_SHEET_NAME);

            for (int rowNum = CONFIG_START; rowNum <= sheet.getLastRowNum(); rowNum++) {
                XSSFRow row = sheet.getRow(rowNum);
                String inputDir = getNotNullStringCellValue(row.getCell(INPUT_DIR_COL));
                String nameDo = getNameDo(row.getCell(NAME_DO_COL));
                String rawType = getNotNullStringCellValue(row.getCell(TYPE_COL));
                int headerRowNumber = Integer.parseInt(getCellValue(row.getCell(HEADER_ROW_NUM_COL))) - 1;

                String glob = getNotNullStringCellValue(row.getCell(GLOB_COL));
                String sheetName = getNotNullStringCellValue(row.getCell(SHEET_NAME_COL));


                ConfigType type = ConfigType.getConfigTypeForRawValue(rawType);

                ModelTypeConfiguration config = new ModelTypeConfiguration(
                        nameDo,
                        rawType,
                        type,
                        glob,
                        getInputDirPath(inputDir, nameDo),
                        sheetName,
                        getHiveTableName(type, nameDo),
                        getPathToParquet(type, nameDo),
                        headerRowNumber,
                        getOutputDirPath(type, nameDo)
                );

                config.setDataModelInstance(getDataModelInstance(type.getModelClass()));
                list.add(config);
            }

            setModelTypeConfiguration(list);
            return this;
        }
    }

    public List<ModelTypeConfiguration> withDate(String date) {
        return modelTypeConfiguration
                .stream()
                .map(oldConfig -> {
                    ModelTypeConfiguration config = new ModelTypeConfiguration(oldConfig);
                    config.setInputDir(config.getInputDir().replace(DATE_SPECIFIER, date));
                    config.setOutputDir(config.getOutputDir().replace(DATE_SPECIFIER, date).trim());
                    config.setPathToParquet(config.getPathToParquet().replace(DATE_SPECIFIER, date).trim());
                    HashMap<String, String> defaults = new HashMap<>();
                    defaults.put("loadDate", date);
                    defaults.put("sheetName", config.getSheetName());
                    defaults.put("modificationDate", getModificationDateTime());
                    config.setDefaultFields(defaults);
                    return config;
                })
                .collect(toList());
    }

    private AbstractClientDataModel getDataModelInstance(Class<? extends AbstractClientDataModel> modelClass) {
        if (modelClass != null) {
            try {
                return modelClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
               log.error("Can't create instance of '{}'", modelClass, e);
            }
        }
        return null;
    }

    private String getNameDo(XSSFCell cell) {
        String result = getCellValue(cell);
        if (result != null) {
            result = result.toLowerCase();
            if (!result.equals(SORT_DO)) {
                result = result.replace("_", "");
            }
        }
        return result;
    }

    public String getInputDirPath(String source, String nameDo) {
        String result = dirSrcXlsSpecifier + DATE_SPECIFIER + "/" + source + "/" + nameDo;
        return result.replace("\\", "/");
    }

    private String getNotNullStringCellValue(Cell cell) {
        String result = getCellValue(cell);
        if (result == null) {
            throw new IllegalArgumentException("Value must be text or number. Cell row: " + cell.getRowIndex() +
                    "col" + cell.getColumnIndex());
        }
        return result;
    }

    public String getSheetOutputPath(String outputDir, String inputFilename) {
        String stem = new File(inputFilename).getName();
        return Paths.get(outputDir, stem + ".json").toString();
    }

    private String getCellValue(Cell cell) {
        String result;
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                result = cell.getStringCellValue();
                break;
            case NUMERIC:
                result = String.valueOf((int) cell.getNumericCellValue());
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    public void validateNameDo(String nameDo) {
        if (nameDo == null) throw new IllegalArgumentException("nameDo must be specified");
    }

    public String getHiveTableName(ConfigType type, String nameDo) {
        validateNameDo(nameDo);
        String result = StringUtils.isBlank(nameDo) ? type.name().toLowerCase() : type.name().toLowerCase() + "_" + nameDo;
        return result.trim().toLowerCase();
    }

    public String getPathToParquet(ConfigType type, String nameDo) {
        validateNameDo(nameDo);
        return dirParquetRawSpecifier + getHiveTableName(type, nameDo);
    }

    public String getOutputDirPath(ConfigType type, String nameDo) {
        validateNameDo(nameDo);
        return dirJsonDeltasSpecifier + DATE_SPECIFIER + "/" + getHiveTableName(type, nameDo);
    }

}
