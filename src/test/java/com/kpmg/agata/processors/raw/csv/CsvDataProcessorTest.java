package com.kpmg.agata.processors.raw.csv;

import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.FsspModel;
import com.kpmg.agata.models.FnsDisqModel;
import com.kpmg.agata.models.GenprocModel;
import com.kpmg.agata.models.SectoralIndexModel;
import com.kpmg.agata.parser.ConfigType;
import com.kpmg.agata.test.utils.TestFileSystem;
import com.kpmg.agata.test.utils.TestUtils;
import com.kpmg.agata.utils.filesystem.IFileSystem;
import com.kpmg.agata.utils.filesystem.LFSFacade;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CsvDataProcessor.class)
public class CsvDataProcessorTest {

    private static IFileSystem fs;

    @Before
    public void beforeClass() throws Exception {
        ConfigParser configParser = mock(ConfigParser.class);
        when(configParser.getSheetOutputPath(any(), any()))
                .thenReturn("some-out");
        whenNew(ConfigParser.class)
                .withNoArguments()
                .thenReturn(configParser);
        fs = new TestFileSystem(new LFSFacade(), mock(IFileSystem.class));
    }

    @Test
    public void fsspTest() throws Exception {
        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setConfigType(ConfigType.FSSP);
        List<FsspModel> rows = new ArrayList<>();
        TestUtils.mockSequenceWriterInFileProcessor(rows, FsspModel.class);

        String filepath = "src/test/resources/csv/fssp/fssp_raw.csv";
        String loadDate = "1970-01-01";

        new CsvDataProcessor(fs, filepath, config, loadDate).run();
        assertEquals(2, rows.size());

        FsspModel expectedFssp1 = new FsspModel();
        expectedFssp1.setLoadDate(loadDate);
        expectedFssp1.setFileName(filepath);
        expectedFssp1.setDebtorName("company1");
        expectedFssp1.setProceedingInstitutionDate("2019-12-19");
        expectedFssp1.setExecutiveDocumentType("Акт органа, осуществляющего контрольные функции");
        expectedFssp1.setExecutiveDocumentDate("2019-12-11");
        expectedFssp1.setExecutiveDocumentObject("Задолженность");
        expectedFssp1.setExecutionObject("Взыскание налогов и сборов, включая пени");
        expectedFssp1.setDebt("464346.55");
        expectedFssp1.setRemainingDebt("464346.55");
        expectedFssp1.setDateAndCompletionReason("2020-03-11, ст. 46 п. 1 п.п. 3");
        assertEquals(expectedFssp1, rows.get(0));

        FsspModel expectedFssp2 = new FsspModel();
        expectedFssp2.setLoadDate(loadDate);
        expectedFssp2.setFileName(filepath);
        expectedFssp2.setDebtorName("ООО \"company2\"");
        expectedFssp2.setProceedingInstitutionDate("2016-10-21");
        expectedFssp2.setExecutiveDocumentType("Исполнительный лист");
        expectedFssp2.setExecutiveDocumentDate("2016-03-01");
        expectedFssp2.setExecutiveDocumentObject("Постановление о взыскании исполнительского сбора");
        expectedFssp2.setExecutionObject("");
        expectedFssp2.setDebt("464346.55");
        expectedFssp2.setRemainingDebt("20731.45");
        expectedFssp2.setDateAndCompletionReason("2020-03-11, ст. 46 п. 1 п.п. 3");
        assertEquals(expectedFssp2, rows.get(1));
    }

    @Test
    public void genprocTest() throws Exception {
        String filepath = "src/test/resources/csv/genproc/genproc_raw.csv";
        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setConfigType(ConfigType.GENPROC);
        config.setNameDo("do1");
        List<GenprocModel> writings = new ArrayList<>();
        TestUtils.mockSequenceWriterInFileProcessor(writings, GenprocModel.class);

        new CsvDataProcessor(fs, filepath, config, "1970-01-01").run();

        assertEquals(2, writings.size());

        GenprocModel expectedGenproc1 = new GenprocModel();
        expectedGenproc1.setCompanyName("company1");
        expectedGenproc1.setInn("001");
        expectedGenproc1.setOgrn("101");
        expectedGenproc1.setKpp("201");
        expectedGenproc1.setCreationDate("01.01.1970");
        expectedGenproc1.setDecisionDate("02.01.1970");
        expectedGenproc1.setFileName(filepath);
        expectedGenproc1.setLoadDate("1970-01-01");
        expectedGenproc1.setName_do("do1");
        assertEquals(expectedGenproc1, writings.get(0));

        GenprocModel expectedGenproc2 = new GenprocModel();
        expectedGenproc2.setCompanyName("company2");
        expectedGenproc2.setInn("002");
        expectedGenproc2.setOgrn("102");
        expectedGenproc2.setKpp("202");
        expectedGenproc2.setCreationDate("03.01.1970");
        expectedGenproc2.setDecisionDate("04.01.1970");
        expectedGenproc2.setFileName(filepath);
        expectedGenproc2.setLoadDate("1970-01-01");
        expectedGenproc2.setName_do("do1");
        assertEquals(expectedGenproc2, writings.get(1));
    }

    @Test
    public void disqualificationTest() throws Exception {
        String filepath = "src/test/resources/csv/fns_disq/data-01011970-structure-24062015.csv";
        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setConfigType(ConfigType.FNS_DISQ);
        config.setNameDo("do1");
        List<FnsDisqModel> writings = new ArrayList<>();
        TestUtils.mockSequenceWriterInFileProcessor(writings, FnsDisqModel.class);

        new CsvDataProcessor(fs, filepath, config, "1970-01-02").run();

        assertEquals(2, writings.size());

        FnsDisqModel expected1 = new FnsDisqModel();
        expected1.setOrganizationFullName("company1");
        expected1.setOgrn("100");
        expected1.setInn("101");
        expected1.setKpp("102");
        expected1.setAddress("address1");
        expected1.setFileName("src/test/resources/csv/fns_disq/data-01011970-structure-24062015.csv");
        expected1.setLoadDate("1970-01-02");
        expected1.setName_do("do1");
        assertEquals(expected1, writings.get(0));

        FnsDisqModel expected2 = new FnsDisqModel();
        expected2.setOrganizationFullName("company2");
        expected2.setOgrn("200");
        expected2.setInn("201");
        expected2.setKpp("202");
        expected2.setAddress("address2");
        expected2.setFileName("src/test/resources/csv/fns_disq/data-01011970-structure-24062015.csv");
        expected2.setLoadDate("1970-01-02");
        expected2.setName_do("do1");
        assertEquals(expected2, writings.get(1));
    }

    @Test
    public void sectoralIndicesTest() throws Exception {
        String filepath = "src/test/resources/csv/sectoral_indices/sectoral_indices_raw.csv";
        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setConfigType(ConfigType.SECTORAL_INDICES);
        config.setNameDo("do1");
        List<SectoralIndexModel> writings = new ArrayList<>();
        TestUtils.mockSequenceWriterInFileProcessor(writings, SectoralIndexModel.class);

        new CsvDataProcessor(fs, filepath, config, "1970-01-02").run();

        assertEquals(1, writings.size());

        SectoralIndexModel expected = new SectoralIndexModel();
        expected.setFileName("src/test/resources/csv/sectoral_indices/sectoral_indices_raw.csv");
        expected.setLoadDate("1970-01-02");
        expected.setName_do("do1");
        expected.setId("ID-1");
        expected.setName("Индекс гречки");
        expected.setTradeDate("1970-01-01");
        expected.setOpen("1.1");
        expected.setHigh("2.2");
        expected.setLow("3.3");
        expected.setClose("4.4");
        expected.setValue("5.5");
        expected.setDuration("duration");
        expected.setYield("yield");
        assertEquals(expected, writings.get(0));
    }
}
