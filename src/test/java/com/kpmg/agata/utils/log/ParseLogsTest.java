package com.kpmg.agata.utils.log;

import com.kpmg.agata.processors.logs.LogParserProcessor;
import com.kpmg.agata.utils.filesystem.LFSFacade;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseLogsTest {

    private String rawExample1 = "AGATA: 2019-10-01 18:31:39,152 INFO  [thread-1] com.kpmg.agata.processors.raw.excel" +
            ".ExcelDataProcessor:96 - " +
            "Sheet 'МСФО отчетность' configuration: SheetConfiguration{sheetName='МСФО отчетность', " +
            "fileName='hdfs://spb99tp-agnn01.techpark.local:8020/projects/AGATA/data/src/2019-09-23/" +
            "Анкеты экспресс-анализа/mb/Оценка МАГЭ.xlsx', hiveTableName='express_analysis_MSFO_reports_mb', " +
            "pathToParquet='/projects/AGATA/data/parquet/raw/express_analysis/mb/express_analysis_MSFO_reports_mb', " +
            "headerRowNumber=0, outputDir='/projects/AGATA/data/json_deltas/2019-09-23/express_analysis/mb/" +
            "express_analysis_MSFO_reports_mb', dataModel='null', dateStr='2019-09-23', minColumnsNum=50}\n";

    private String rawExample2 = "AGATA: 2019-10-09 00:53:37,379 INFO  [thread-2] com.kpmg.agata.processors.raw.excel" +
            ".ExcelDataProcessor:98 -" +
            " Sheet 'TDSheet' configuration: ModelTypeConfiguration{nameDo='bm', rawType='Реализации товаров и услуг'," +
            " configType=REALIZATION_SERVICES, glob='*.{xls,xlsx,XLS,XLSX}', inputDir='/projects/AGATA/data/src/2019-08-01/" +
            "Реализации товаров и услуг/bm', dataModelInstance=com.kpmg.agata.models.RealizationServicesModel@584e50c7," +
            " defaultFields={modificationDate=2019-10-09 00:53:03, sheetName=TDSheet, loadDate=2019-08-01}, " +
            "sheetName='TDSheet', hiveTableName='realization_services_bm', " +
            "pathToParquet='/projects/AGATA/data/parquet/raw/realization_services_bm', headerRowNumber=0, " +
            "outputDir='/projects/AGATA/data/json_deltas/2019-08-01/realization_services_bm', dataModel='null'}";

    @Test
    public void getConsoleErrorLevelTest(){
        String raw = "19/10/01 18:06:09 INFO Client: Application report for application_1569582799776_0136 (state: ACCEPTED)";

        LogParserProcessor logParser = new LogParserProcessor(new LFSFacade());
        assertEquals("2019-10-01 18:06:09", logParser.getDateTimeConsole(raw));
        assertEquals("INFO",logParser.getLogLevelConsole(raw));
    }

    @Test
    public void getContainerErrorLevelTest(){

        LogParserProcessor logParser = new LogParserProcessor(new LFSFacade());
        assertEquals("2019-10-01 18:31:39", logParser.getDateTimeContainer(rawExample1));
        assertEquals("INFO",logParser.getLogLevelContainer(rawExample1));
    }

    @Test
    public void getContainerClassNameTest(){
        LogParserProcessor logParser = new LogParserProcessor(new LFSFacade());
        assertEquals("com.kpmg.agata.processors.raw.excel.ExcelDataProcessor", logParser.getClassNameContainer(rawExample1));
    }

    @Test
    public void getContainerClassNameLazyFinderTest(){
        LogParserProcessor logParser = new LogParserProcessor(new LFSFacade());
        assertEquals("com.kpmg.agata.processors.raw.excel.ExcelDataProcessor", logParser.getClassNameContainer(rawExample2));
    }
}

