package com.kpmg.agata;

import com.kpmg.agata.config.Environment;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.combinable.RpModel;
import com.kpmg.agata.processors.validation.ValidationReportProcessor;
import com.kpmg.agata.test.utils.TestUtils;
import com.kpmg.agata.utils.sql.Normalizer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParseStringTest {

    String pattern = "/(\\d{4}-\\d{2}-\\d{2})/";
    Pattern regexPattern = Pattern.compile(pattern);

    @BeforeClass
    public static void beforeClass() {
        TestUtils.loadTestConfig();
    }

    @Test
    public void regExpReplaceTest() {

        String x = "/projects/AGATA/data/json_deltas/2018-09-02/counterparty_contract/bm/2018-09-93.file";
        assertEquals(
                "/projects/AGATA/data/result_jsons/counterparty_contract/bm/2018-09-93.file",
                x.replaceAll(pattern, "/result_jsons/").replace("json_deltas/",""));
    }

    @Test
    public void regExpFindDateTest() {

        Matcher m = regexPattern.matcher("/projects/AGATA/data/json_deltas/2018-09-02/2018-09-03/counterparty_contract/bm/2018-09-93.file");
        assertEquals(
                "2018-09-02",
                m.find() ? m.group(1): "");
    }

    @Test
    public void regExpFindSourceTest(){
        String pattern = "/projects/AGATA/data/src/" + "\\d{4}-\\d{2}-\\d{2}/(.*?)/";
        Pattern regexPattern = Pattern.compile(pattern);

        Matcher m = regexPattern.matcher("hdfs://spb99tp-agnn01.techpark.local:8020/projects/AGATA/data/src/2019-09-23/Еженедельный статус ПДЗ/aero/aero/aero2/aero3/ДЗ_для Qlik_АЭРО_12082019.xlsx");

        assertEquals("Еженедельный статус ПДЗ", m.find() ? m.group(1): "");

    }

    @Test
    public void getValueDateToStringTest(){
        Normalizer norm = new Normalizer();
        assertEquals("2019-08-14", norm.getValueDateToString("43691,50347"));
        assertEquals("2019-08-14", norm.getValueDateToString("43691.50347"));
        assertEquals("", norm.getValueDateToString("abd43691.50347"));
        assertEquals("", norm.getValueDateToString(""));
        assertEquals("", norm.getValueDateToString(null));
    }

    @Test
    public void castToDateTest(){
        Normalizer norm = new Normalizer();
        assertEquals(LocalDate.of(2019, 12, 02), norm.convertToLocalDate("2019-12-02"));
        assertNull(norm.convertToLocalDate("20204-12-02"));
        assertNull(norm.convertToLocalDate("12-04-2016"));
    }

    @Test
    public void getParsedSourceAndDoForSrcChecksumTest(){
        String result = ValidationReportProcessor
                .getParsedSourceAndDoForSrcChecksum("hdfs://spb99tp-agnn01.techpark" +
                ".local:8020/projects/AGATA/data/src/2019-08-01/Анкеты " +
                "экспресс-анализа/bm/Расчет Беатон.xlsx", "Расчет Беатон.xlsx");
        assertEquals("/Анкеты экспресс-анализа/bm", result);
    }

    @Test
    public void getParsedSourceAndDoForConfigTest() {
        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setGlob("test*test");
        config.setDataModelInstance(new RpModel());
        config.setInputDir("/test/path/to/xls/${date}/Договоры контрагентов/sort_do");
        ValidationReportProcessor.RawConfigGlobModel result =
                ValidationReportProcessor.getParsedSourceAndDoForConfig(config);
        assertEquals("test*test", result.getGlob());
        assertEquals("/Договоры контрагентов/sort_do", result.getInputDir());
    }
}
