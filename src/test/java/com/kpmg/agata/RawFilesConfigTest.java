package com.kpmg.agata;

import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.Environment;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.CreditLimitModel;
import com.kpmg.agata.test.utils.TestUtils;
import java.io.IOException;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RawFilesConfigTest {

    @BeforeClass
    public static void beforeClass() {
        TestUtils.loadTestConfig();
    }

    @Test
    public void readPropertyEnvRelativesPathTest() {
        List<String> list = Environment.getListPrettyPrintAllProperties();
        assertTrue(list.size() > 0);
    }

    @Test
    public void readConfigForDifferentDate() throws IOException {
        ConfigParser config = new ConfigParser().getRawConfigAgataXls("./src/test/resources/raw/test_ConfigAgata.xlsx");

        asList("2019-01-01", "2019-01-02", "2019-01-03").forEach(date -> {
            List<ModelTypeConfiguration> listConfig = config.withDate(date);
            assertTrue(listConfig.get(0).getInputDir().contains(date));
        });
    }

    @Test
    public void specifyDateJsonTest() {

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("sheet_1");
        config.setNameDo("aero");
        config.setInputDir("/projects/AGATA/data/src/${date}/Кредитные лимиты/aero");
        config.setOutputDir("/projects/AGATA/data/json_deltas/${date}/credit_limit_aero");
        config.setPathToParquet("/projects/AGATA/data/parquet/raw/credit_limit_aero");
        config.setGlob("*");
        config.setDataModelInstance(new CreditLimitModel());

        ConfigParser parser = new ConfigParser();
        parser.setModelTypeConfiguration(singletonList(config));
        ModelTypeConfiguration r = parser.withDate("01-01-1970").get(0);

        assertEquals("/projects/AGATA/data/src/01-01-1970/Кредитные лимиты/aero", r.getInputDir());
        assertEquals("/projects/AGATA/data/parquet/raw/credit_limit_aero", r.getPathToParquet());
        assertEquals("/projects/AGATA/data/json_deltas/01-01-1970/credit_limit_aero", r.getOutputDir());
        assertEquals("/projects/AGATA/data/json_deltas/01-01-1970/credit_limit_aero/file.xls.json",
                new ConfigParser().getSheetOutputPath(r.getOutputDir(), "file.xls").replace("\\", "/"));
    }
}
