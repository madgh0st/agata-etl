package com.kpmg.agata.parser;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.config.Environment;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.FnsTaxViolationModel;
import com.kpmg.agata.processors.FileProcessor;
import com.kpmg.agata.processors.raw.xml.XmlDataProcessor;
import com.kpmg.agata.test.utils.TestUtils;
import com.kpmg.agata.utils.filesystem.IFileSystem;
import com.kpmg.agata.utils.filesystem.LFSFacade;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static com.kpmg.agata.parser.ConfigType.FNS_TAX_VIOLATION;
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
public class FnsTaxViolationParserTest {

    private FileProcessor fileProcessor;
    private SequenceWriter mockedSequenceWriter;
    private String filename = "src/test/resources/xml/fns-tax-violation-test.xml";
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
        config.setConfigType(FNS_TAX_VIOLATION);
        config.setOutputDir("");

        fileProcessor = new XmlDataProcessor(fileSystem, filename, config, testDate);
        fileProcessor = spy(fileProcessor);

        mockedSequenceWriter = mock(SequenceWriter.class);

        ObjectWriter mockedObjectWriter = Mockito.mock(ObjectWriter.class);
        Whitebox.setInternalState(fileProcessor, "objectWriter", mockedObjectWriter);
        when(mockedObjectWriter.writeValues(any(Writer.class))).thenReturn(mockedSequenceWriter);
    }

    @Test
    public void parseTest() throws IOException {
        ArgumentCaptor<FnsTaxViolationModel> captor = ArgumentCaptor.forClass(FnsTaxViolationModel.class);

        fileProcessor.run();

        verify(mockedSequenceWriter, times(2)).write(captor.capture());
        List<FnsTaxViolationModel> actualList = captor.getAllValues();

        FnsTaxViolationModel expectedModel1 = new FnsTaxViolationModel();
        expectedModel1.setModificationDate(actualList.get(0).getModificationDate());
        expectedModel1.setLoadDate(testDate);
        expectedModel1.setFileName(filename);
        expectedModel1.setDocId("docId0");
        expectedModel1.setDocDate("01.01.1970");
        expectedModel1.setCreationDate("01.01.1970");
        expectedModel1.setOrgName("org1");
        expectedModel1.setInnul("100");
        expectedModel1.setFine("42.0000");

        FnsTaxViolationModel expectedModel2 = new FnsTaxViolationModel();
        expectedModel2.setModificationDate(actualList.get(1).getModificationDate());
        expectedModel2.setLoadDate(testDate);
        expectedModel2.setFileName(filename);
        expectedModel2.setDocId("docId1");
        expectedModel2.setDocDate("31.12.1970");
        expectedModel2.setCreationDate("31.12.1970");
        expectedModel2.setOrgName("org2");
        expectedModel2.setInnul("101");
        expectedModel2.setFine("100500.0000");

        List<FnsTaxViolationModel> expectedList = new ArrayList<>();
        expectedList.add(expectedModel1);
        expectedList.add(expectedModel2);

        assertEquals(actualList, expectedList);
    }
}
