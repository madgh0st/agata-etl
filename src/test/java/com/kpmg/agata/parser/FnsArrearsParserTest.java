package com.kpmg.agata.parser;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.FnsArrearsModel;
import com.kpmg.agata.processors.FileProcessor;
import com.kpmg.agata.processors.raw.xml.XmlDataProcessor;
import com.kpmg.agata.test.utils.TestUtils;
import com.kpmg.agata.utils.filesystem.IFileSystem;
import com.kpmg.agata.utils.filesystem.LFSFacade;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static com.kpmg.agata.parser.ConfigType.FNS_ARREARS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest(XmlDataProcessor.class)
public class FnsArrearsParserTest {

    private static final String NAME_DO = "sort_do";
    private FileProcessor fileProcessor;
    private SequenceWriter mockedSequenceWriter;
    private String filename = "src/test/resources/xml/fns-arrears-test.xml";
    private String testDate = "test date";

    @BeforeClass
    public static void beforeClass() {
        TestUtils.loadTestConfig();
    }

    @Before
    public void init() throws IOException {
        IFileSystem fileSystem = new LFSFacade();
        fileSystem = spy(fileSystem);
        doReturn(null).when(fileSystem).writer(anyString());
        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setConfigType(FNS_ARREARS);
        config.setOutputDir("");
        config.setNameDo(NAME_DO);

        fileProcessor = new XmlDataProcessor(fileSystem, filename, config, testDate);
        fileProcessor = spy(fileProcessor);

        mockedSequenceWriter = mock(SequenceWriter.class);

        ObjectWriter mockedObjectWriter = Mockito.mock(ObjectWriter.class);
        Whitebox.setInternalState(fileProcessor, "objectWriter", mockedObjectWriter);
        when(mockedObjectWriter.writeValues(any(Writer.class))).thenReturn(mockedSequenceWriter);
    }

    @Test
    public void parseTest() throws IOException {
        ArgumentCaptor<FnsArrearsModel> captor = ArgumentCaptor.forClass(FnsArrearsModel.class);

        fileProcessor.run();

        verify(mockedSequenceWriter, times(3)).write(captor.capture());
        List<FnsArrearsModel> actualList = captor.getAllValues();

        FnsArrearsModel expectedModel1 = new FnsArrearsModel();
        expectedModel1.setModificationDate(actualList.get(0).getModificationDate());
        expectedModel1.setLoadDate(testDate);
        expectedModel1.setFileName(filename);
        expectedModel1.setDocId("docId0");
        expectedModel1.setDocDate("01.01.1970");
        expectedModel1.setCreationDate("01.01.1970");
        expectedModel1.setOrgName("org1");
        expectedModel1.setInnul("100");
        expectedModel1.setName_do(NAME_DO);
        expectedModel1.setTaxName("Налог на воздух");
        expectedModel1.setTaxArrears("0.00");
        expectedModel1.setPenalties("1.00");
        expectedModel1.setArrearsFine("2.00");
        expectedModel1.setTotalArrears("3.00");

        FnsArrearsModel expectedModel2 = new FnsArrearsModel();
        expectedModel2.setModificationDate(actualList.get(1).getModificationDate());
        expectedModel2.setLoadDate(testDate);
        expectedModel2.setFileName(filename);
        expectedModel2.setDocId("docId0");
        expectedModel2.setDocDate("01.01.1970");
        expectedModel2.setCreationDate("01.01.1970");
        expectedModel2.setOrgName("org1");
        expectedModel2.setInnul("100");
        expectedModel2.setName_do(NAME_DO);
        expectedModel2.setTaxName("Налог на кофе из автомата");
        expectedModel2.setTaxArrears("4.00");
        expectedModel2.setPenalties("5.00");
        expectedModel2.setArrearsFine("6.00");
        expectedModel2.setTotalArrears("7.00");

        FnsArrearsModel expectedModel3 = new FnsArrearsModel();
        expectedModel3.setModificationDate(actualList.get(2).getModificationDate());
        expectedModel3.setLoadDate(testDate);
        expectedModel3.setFileName(filename);
        expectedModel3.setDocId("docId1");
        expectedModel3.setDocDate("31.12.1970");
        expectedModel3.setCreationDate("31.12.1970");
        expectedModel3.setOrgName("org2");
        expectedModel3.setInnul("101");
        expectedModel3.setName_do(NAME_DO);
        expectedModel3.setTaxName("Налог на плоские шутки");
        expectedModel3.setTaxArrears("8.00");
        expectedModel3.setPenalties("9.00");
        expectedModel3.setArrearsFine("10.00");
        expectedModel3.setTotalArrears("11.00");

        List<FnsArrearsModel> expectedList = new ArrayList<>();
        expectedList.add(expectedModel1);
        expectedList.add(expectedModel2);
        expectedList.add(expectedModel3);

        assertEquals(actualList, expectedList);
    }
}
