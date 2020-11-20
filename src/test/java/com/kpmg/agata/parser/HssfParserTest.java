package com.kpmg.agata.parser;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.processors.raw.excel.ExcelDataProcessor;
import com.kpmg.agata.test.utils.TestFileSystem;
import com.kpmg.agata.test.utils.TestUtils;
import com.kpmg.agata.utils.filesystem.IFileSystem;
import com.kpmg.agata.utils.filesystem.LFSFacade;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ExcelDataProcessor.class)
public class HssfParserTest {
    private IFileSystem fs = new TestFileSystem(new LFSFacade(), mock(IFileSystem.class));
    private ModelTypeConfiguration modelTypeConfiguration;

    @BeforeClass
    public static void beforeClass() {
        TestUtils.loadTestConfig();
    }

    @Before
    public void prepare() {
        this.modelTypeConfiguration = new ModelTypeConfiguration();
    }

    @Test
    public void readTestSheet1() throws Exception {
        List<Map<String, String>> reports = new ArrayList<>();

        String pathToHssf = "src/test/resources/raw/hssf/in/test.xls";
        modelTypeConfiguration.setSheetName("1");
        mockSequenceWriter(reports);
        ExcelDataProcessor dataProcessor = new ExcelDataProcessor(fs, pathToHssf, modelTypeConfiguration);
        dataProcessor.run();
        List<Map<String, String>> firstPage = reports;
        assertEquals(4, firstPage.size());
        for (float i = 0; i < 5; i++) {
            String col = String.format("col_%.0f", i);
            assertEquals(i, Float.valueOf(firstPage.get(0).get(col)));
            assertEquals(5 + i, Float.valueOf(firstPage.get(1).get(col)));
        }
    }

    @Test
    public void readTestSheet2() throws Exception {
        List<Map<String, String>> reports = new ArrayList<>();
        modelTypeConfiguration.setSheetName("2");
        mockSequenceWriter(reports);
        String pathToHssf = "src/test/resources/raw/hssf/in/test.xls";
        ExcelDataProcessor dataProcessor = new ExcelDataProcessor(fs, pathToHssf, modelTypeConfiguration);
        dataProcessor.run();
        List<Map<String, String>> secondPage = reports;
        for (int i = 0; i < 5; i++) {
            String col = String.format("col_%d", i);
            assertEquals("test data " + i, secondPage.get(i).get(col));
        }
    }

    private void mockSequenceWriter(List<Map<String, String>> report) throws Exception {
        ObjectMapper mapper = mock(ObjectMapper.class);
        whenNew(ObjectMapper.class)
                .withNoArguments()
                .thenReturn(mapper);
        ObjectWriter objectWriter = mock(ObjectWriter.class);
        when(mapper.writer((PrettyPrinter) any()))
                .thenReturn(objectWriter);
        SequenceWriter sequenceWriter = mock(SequenceWriter.class);
        when(objectWriter.writeValues((Writer) any()))
                .thenReturn(sequenceWriter);
        doAnswer(invocation -> writeMock(invocation, report))
                .when(sequenceWriter)
                .write(any());
    }




    @SuppressWarnings("unchecked cast")
    private Object writeMock(InvocationOnMock invocationOnMock, List<Map<String, String>> reports) {
        Map<String, String> model = (Map<String, String>) invocationOnMock.getArguments()[0];
        String sheetName = model.get("sheetName");
        List<Map<String, String>> sheet = reports;
        if (sheet == null) sheet = new ArrayList<>();
        sheet.add(model);
        //reports.add(sheet);
        return null;
    }
}
