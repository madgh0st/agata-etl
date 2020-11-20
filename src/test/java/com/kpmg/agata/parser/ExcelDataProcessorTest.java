package com.kpmg.agata.parser;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.models.AffilationModel;
import com.kpmg.agata.models.LegalCasesModel;
import com.kpmg.agata.models.SeasonalityModel;
import com.kpmg.agata.models.SparkInterfaxSummaryModel;
import com.kpmg.agata.models.combinable.BmModel;
import com.kpmg.agata.models.combinable.KpModel;
import com.kpmg.agata.models.combinable.MbModel;
import com.kpmg.agata.models.combinable.RpModel;
import com.kpmg.agata.processors.raw.excel.ExcelDataProcessor;
import com.kpmg.agata.test.utils.TestFileSystem;
import com.kpmg.agata.test.utils.TestUtils;
import com.kpmg.agata.utils.filesystem.IFileSystem;
import com.kpmg.agata.utils.filesystem.LFSFacade;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Collections.singletonList;

import static com.kpmg.agata.test.utils.TestUtils.assertEqualsOrNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ExcelDataProcessor.class)
public class ExcelDataProcessorTest {

    private static final IFileSystem FS_MOCK = new TestFileSystem(new LFSFacade(), mock(IFileSystem.class));

    @BeforeClass
    public static void beforeClass() {
        TestUtils.loadTestConfig();
    }

    @Test
    public void parseKpModelSheet() throws Exception {
        String inputFile = "src/test/resources/excel/kp.xlsx";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("TDSheet");
        config.setNameDo("kp");
        config.setHeaderRowNumber(11);
        config.setDataModelInstance(new KpModel());

        ConfigParser parser = new ConfigParser();
        parser.setModelTypeConfiguration(singletonList(config));

        List<KpModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, KpModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(5, reports.size());
        assertKpModels(reports);
    }

    private <T extends AbstractClientDataModel> void mockSequenceWriter(List<T> reports, Class<T> clazz)
            throws Exception {
        ObjectMapper mapper = mock(ObjectMapper.class);
        ObjectMapper originalMapper = new ObjectMapper();
        whenNew(ObjectMapper.class)
                .withNoArguments()
                .thenReturn(mapper);
        when(mapper.convertValue(any(), eq(clazz)))
                .then(invocation -> originalMapper.convertValue(invocation.getArguments()[0], clazz));
        ObjectWriter objectWriter = mock(ObjectWriter.class);
        when(mapper.writer((PrettyPrinter) any()))
                .thenReturn(objectWriter);
        SequenceWriter sequenceWriter = mock(SequenceWriter.class);
        when(objectWriter.writeValues((Writer) any()))
                .thenReturn(sequenceWriter);
        doAnswer(invocation -> writeMock(invocation, reports))
                .when(sequenceWriter)
                .write(any());
    }

    @SuppressWarnings("unchecked cast")
    private <T extends AbstractClientDataModel> Object writeMock(InvocationOnMock invocationOnMock, List<T> reports) {
        T model = (T) invocationOnMock.getArguments()[0];
        reports.add(model);
        return null;
    }

    private void assertKpModels(List<KpModel> models) {
        for (int i = 0; i < models.size(); i++) {
            assertKpModel(models.get(i), i + 1);
        }
    }

    private void assertKpModel(KpModel model, int id) {
        switch (id) {
            case 1:
            case 2:
            case 3:
            case 4:
                assertEquals("department 1", model.getDepartment());
                assertEquals("manager 1", model.getManager());
                break;
            case 5:
                assertEquals("department 2", model.getDepartment());
                assertEquals("manager 2", model.getManager());
                break;
        }

        switch (id) {
            case 1:
            case 2:
            case 3:
                assertEquals("counterparty 1", model.getCounterparty());
                assertEquals("contract 1", model.getContractName());
                break;
            case 4:
                assertEquals("counterparty 2", model.getCounterparty());
                assertEquals("contract 2", model.getContractName());
                break;
            case 5:
                assertEquals("counterparty 3", model.getCounterparty());
                assertEquals("contract 3", model.getContractName());
                break;
        }

        assertEquals("01.01.1970", model.getDate());
        assertEquals(format("data[%s,%s]", id, 7), model.getLimit());
        assertEquals(format("data[%s,%s]", id, 8), model.getDebtOnDate());
        assertEquals(format("data[%s,%s]", id, 9), model.getTotalDebt());
        assertEquals(format("data[%s,%s]", id, 10), model.getTotalGowDebt());
        assertEquals(format("data[%s,%s]", id, 11), model.getAllowedDebt());
        assertEquals(format("data[%s,%s]", id, 12), model.getTotalPastDueDebt());
        assertEquals(format("data[%s,%s]", id, 13), model.getPastDueTenDaysDebt());
        assertEquals(format("data[%s,%s]", id, 14), model.getPastDueTenToThirtyDaysDebt());
        assertEquals(format("data[%s,%s]", id, 15), model.getPastDueThirtyToNinetyDaysDebt());
        assertEquals(format("data[%s,%s]", id, 16), model.getPastDueOverNinetyDaysDebt());
        assertEquals(format("data[%s,%s]", id, 17), model.getPastDueDebt());
        assertEquals(format("data[%s,%s]", id, 18), model.getHardPastDueDebt());
        assertEquals(format("data[%s,%s]", id, 19), model.getLitigationPastDueDebt());
    }

    @Test
    public void parseRpModelSheet() throws Exception {
        String inputFile = "src/test/resources/excel/rp.xlsx";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("Отчет");
        config.setNameDo("mb");
        config.setHeaderRowNumber(8);
        config.setDataModelInstance(new RpModel());

        List<RpModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, RpModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(9, reports.size());
        assertRpModels(reports);
    }

    private void assertRpModels(List<RpModel> reports) {
        for (int i = 0; i < reports.size(); i++) {
            RpModel model = reports.get(i);
            assertEquals("unit 1", model.getBusinessUnit());
            assertEquals("entity 1", model.getLegalEntity());
            assertEquals("region 1", model.getRegion());
            assertEquals("counterparty " + (i / 3 + 1), model.getCounterparty());
            assertEqualsOrNull(String.valueOf(i + 1), model.getPdz());
            if (i % 3 == 0) {
                assertEquals("01/07/01", model.getDate());
            }
            if (i % 3 == 1) {
                assertEquals("01/08/01", model.getDate());
            }
            if (i % 3 == 2) {
                assertEquals("06/08/01", model.getDate());
            }
            assertEquals("млн руб", model.getMeasure());
            assertEquals("06/08/01", model.getReportDate());
        }
    }

    @Test
    public void parseBmModelSheet() throws Exception {
        String inputFile = "src/test/resources/excel/bm.xls";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("TDSheet");
        config.setNameDo("bm");
        config.setHeaderRowNumber(7);
        config.setDataModelInstance(new BmModel());

        List<BmModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, BmModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(6, reports.size());
        assertBmModels(reports);
    }

    private void assertBmModels(List<BmModel> reports) {
        for (int i = 0; i < reports.size(); i++) {
            BmModel report = reports.get(i);
            assertEquals("counterparty " + (i / 3 + 1), report.getCounterparty());
            assertEquals(format("0%s.01.1970", i + 1), report.getPeriod());
            assertEquals(String.valueOf(i * 4 + 1), report.getTotalDept());
            assertEquals(String.valueOf(i * 4 + 2), report.getDept());
            assertEqualsOrNull(String.valueOf(i * 4 + 3), report.getPastDueDebt());
            assertEqualsOrNull(String.valueOf(i * 4 + 4), report.getPenalty());
        }
    }

    @Test
    public void parseMbModelSheet() throws Exception {
        String inputFile = "src/test/resources/excel/mb.xls";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("TDSheet");
        config.setNameDo("mb");
        config.setHeaderRowNumber(10);
        config.setDataModelInstance(new MbModel());

        List<MbModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, MbModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(12, reports.size());
        assertMbModels(reports);
    }

    private void assertMbModels(List<MbModel> reports) {
        for (int i = 0; i < reports.size(); i++) {
            MbModel report = reports.get(i);
            assertEquals(i / 6 == 0 ? "USD" : "руб.", report.getCurrency());
            if (i < 3) {
                assertEquals("counterparty 1", report.getCounterparty());
            } else if (i < 6) {
                assertEquals("counterparty 2", report.getCounterparty());
            } else if (i < 9) {
                assertEquals("counterparty 3", report.getCounterparty());
            }
            assertEquals("deal " + (i + 1), report.getDeal());
            assertEquals("order counterparty " + (i + 1), report.getOrderCounterparty());
            String expectedDate = LocalDate.of(1970, 1, i + 1)
                                           .format(ofPattern("dd.MM.yyyy"));
            assertEquals(expectedDate, report.getDealDate());
            assertEquals("department " + (i + 1), report.getDepartment());
            assertEquals(String.valueOf(i + 1), report.getPastDueDays());
            assertEquals(String.valueOf(i / 3 + 1), report.getDelayDays());
            assertEqualsOrNull(String.valueOf(i * 4 + 1), report.getDebtWithinDelay());
            assertEqualsOrNull(String.valueOf(i * 4 + 2), report.getPastDueFiveDays());
            assertEqualsOrNull(String.valueOf(i * 4 + 3), report.getPastDueFromSixToTwentyDays());
            assertEqualsOrNull(String.valueOf(i * 4 + 4), report.getPastDueOverTwentyDays());
        }
    }

    @Test
    public void parseSparkInterfaxSummary() throws Exception {
        String inputFile = "src/test/resources/excel/spark_interfax_summary.xlsx";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("Шаблон для анализа КА");
        config.setNameDo("sort_do");
        config.setHeaderRowNumber(0);
        config.setDataModelInstance(new SparkInterfaxSummaryModel());

        List<SparkInterfaxSummaryModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, SparkInterfaxSummaryModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(1, reports.size());

        SparkInterfaxSummaryModel expected = new SparkInterfaxSummaryModel();
        expected.setFileName("src/test/resources/excel/spark_interfax_summary.xlsx");
        expected.setSheetName("Шаблон для анализа КА");

        expected.setName("ООО \"Company\"");
        expected.setInn("201");
        expected.setStatus("Действующая");
        expected.setSparkInterfaxCreditLimit("1 000");
        expected.setCautionIndex("1");
        expected.setFinancialRiskIndex("2");
        expected.setPaymentDisciplineIndex("3");
        expected.setRiskFactors("Низкий риск");
        expected.setNegativeRegistries("reg01,reg02");
        expected.setPledges("Есть");
        expected.setLegalCasesCountTwoYears("4");
        expected.setLegalCasesClaimsSumTwoYears("5");
        expected.setLegalCasesDecisionsSumTwoYears("6");
        expected.setNews1("news01");
        expected.setNews2("news02");
        expected.setNews3("news03");
        expected.setNews4("news04");
        expected.setIsDishonestSupplier("В реестре не значится");

        assertEquals(expected, reports.get(0));
    }

    @Test
    public void parseLegalCases() throws Exception {
        String inputFile = "src/test/resources/excel/legal_cases.xlsx";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("Арбитражные дела");
        config.setNameDo("sort_do");
        config.setHeaderRowNumber(0);
        config.setDataModelInstance(new LegalCasesModel());

        List<LegalCasesModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, LegalCasesModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(1, reports.size());

        LegalCasesModel expected = new LegalCasesModel();
        expected.setFileName("src/test/resources/excel/legal_cases.xlsx");
        expected.setSheetName("Арбитражные дела");

        expected.setContractorID("101");
        expected.setName("ООО «Company 1»");
        expected.setInn("201");
        expected.setNameSpark("COMPANY, ООО");
        expected.setCaseNumber("А-01");
        expected.setCategory("Исполнение обязательств");
        expected.setStatus("Завершено");
        expected.setOutcome("Иск не удовлетворен");
        expected.setClaimDate("1/1/70");
        expected.setOutcomeDate("2/1/70");
        expected.setClaimAmount("301");
        expected.setOutcomeAmount("401");
        expected.setClaimCharge("о взыскании 301 рублей");
        expected.setDictum("01.03.1970 В удовлетворении исковых требований отказать.");

        assertEquals(expected, reports.get(0));
    }

    @Test
    public void parseAffilation() throws Exception {
        String inputFile = "src/test/resources/excel/affilation.xlsx";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("report");
        config.setNameDo("sort_do");
        config.setHeaderRowNumber(3);
        config.setDataModelInstance(new AffilationModel());

        List<AffilationModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, AffilationModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(2, reports.size());

        AffilationModel expected = new AffilationModel();
        expected.setFileName("src/test/resources/excel/affilation.xlsx");
        expected.setSheetName("report");

        expected.setFullName("Компания 1, ОАО");
        expected.setOgrn("101");
        expected.setShortName("ОАО \"Компания 1\"");
        expected.setName("ОТКРЫТОЕ АКЦИОНЕРНОЕ ОБЩЕСТВО \"Компания 1\"");
        expected.setChief("Иванов Иван Иванович");
        expected.setChiefPosition("руководитель");
        expected.setChiefInn("201");
        expected.setAge("5");
        expected.setStatus("Действующая");
        expected.setInn("301");
        expected.setActivitySector("Деятельность по обеспечению общественного порядка и безопасности");
        expected.setOkved("84.24");
        expected.setPreviousInns("401");
        expected.setInfo("some important info");

        assertEquals(expected, reports.get(0));
    }

    @Test
    public void parseSeasonality() throws Exception {
        String inputFile = "src/test/resources/excel/seasonality.xlsx";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("Sheet1");
        config.setNameDo("sort_do");
        config.setHeaderRowNumber(0);
        config.setDataModelInstance(new SeasonalityModel());

        List<SeasonalityModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, SeasonalityModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(1, reports.size());

        SeasonalityModel expected = new SeasonalityModel();
        expected.setFileName(inputFile);
        expected.setSheetName("Sheet1");

        expected.setSeasonalityOfDo("mb");
        expected.setMonth("1");
        expected.setSeasonality("0");

        assertEquals(expected, reports.get(0));
    }

    @Test
    public void parseLegalCasesV2() throws Exception {
        String inputFile = "src/test/resources/excel/legal_cases_v2.xlsb";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("2.Арбитражные дела");
        config.setNameDo("sort_do");
        config.setHeaderRowNumber(2);
        config.setDataModelInstance(new LegalCasesModel());

        List<LegalCasesModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, LegalCasesModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(1, reports.size());

        LegalCasesModel expected = new LegalCasesModel();
        expected.setFileName("src/test/resources/excel/legal_cases_v2.xlsb");
        expected.setSheetName("2.Арбитражные дела");

        expected.setContractorID("102");
        expected.setName("ООО «Company 2»");
        expected.setInn("202");
        expected.setNameSpark("COMPANY 2, ООО");
        expected.setCaseNumber("А-02");
        expected.setCategory("Банкротство");
        expected.setStatus("Завершено");
        expected.setOutcome("Иск удовлетворен");
        expected.setClaimDate("1/2/70");
        expected.setOutcomeDate("2/2/70");
        expected.setClaimAmount("302");
        expected.setOutcomeAmount("402");
        expected.setClaimCharge("о взыскании 302 рублей");
        expected.setDictum("02.03.1970 В удовлетворении исковых требований отказать.");

        assertEquals(expected, reports.get(0));
    }

    @Test
    public void parseSparkInterfaxSummaryV2() throws Exception {
        String inputFile = "src/test/resources/excel/spark_interfax_summary_v2.xlsb";

        ModelTypeConfiguration config = new ModelTypeConfiguration();
        config.setSheetName("1.Шаблон для анализа КА");
        config.setNameDo("sort_do");
        config.setHeaderRowNumber(2);
        config.setDataModelInstance(new SparkInterfaxSummaryModel());

        List<SparkInterfaxSummaryModel> reports = new ArrayList<>();
        mockSequenceWriter(reports, SparkInterfaxSummaryModel.class);

        new ExcelDataProcessor(FS_MOCK, inputFile, config).run();

        assertEquals(1, reports.size());

        SparkInterfaxSummaryModel expected = new SparkInterfaxSummaryModel();
        expected.setFileName("src/test/resources/excel/spark_interfax_summary_v2.xlsb");
        expected.setSheetName("1.Шаблон для анализа КА");

        expected.setName("ООО \"Company 1\"");
        expected.setInn("101");
        expected.setStatus("Действующая");
        expected.setCautionIndex("601");
        expected.setFinancialRiskIndex("701");
        expected.setPaymentDisciplineIndex("801");
        expected.setSparkInterfaxCreditLimit("901");
        expected.setRiskFactors("risk 1");
        expected.setNegativeRegistries("reg 1, reg 2");
        expected.setPledges("pledge 1");
        expected.setLegalCasesCountTwoYears("1001");
        expected.setLegalCasesClaimsSumTwoYears("1101");
        expected.setLegalCasesDecisionsSumTwoYears("1201");
        expected.setNews1("news 1");
        expected.setNews2("news 2");
        expected.setNews3("news 3");
        expected.setNews4("news 4");
        expected.setIsDishonestSupplier("reg 3");

        expected.setRegistrationDate("01.01.1970 (301)");
        expected.setRegistrationRegion("region 1");
        expected.setCounterpartyAddress("address 1");
        expected.setSite("site 1");
        expected.setActivityType("activity 1");
        expected.setActivityCode("401");
        expected.setChiefPosition("person 1");
        expected.setPropertyType("property 1");
        expected.setCompanySize("size 1");
        expected.setEmployeesCount("501");
        expected.setInfo("info 1");
        expected.setIsInBankruptcyRegister("value 1");
        expected.setBankruptcyRegisterLink("value 2");
        expected.setDisqualifiedPersons("value 3");
        expected.setChiefIsInFnsRegistry("value 4");
        expected.setBailiffCasesSum("1301");
        expected.setArrears("1401");
        expected.setPenaltySum("1501");
        expected.setPenaltyDebtSum("1601");
        expected.setLastReportPeriod("1/2/70");
        expected.setLastReportPeriodRevenue("1701");
        expected.setLastReportPeriodNetProfit("1801");
        expected.setLastReportPeriodNetAssets("1901");
        expected.setLastReportPeriodMainAssets("2001");
        expected.setPreLastReportPeriod("1/3/70");
        expected.setPreLastReportPeriodRevenue("2101");
        expected.setPreLastReportPeriodNetProfit("2201");
        expected.setPreLastReportPeriodNetAssets("2301");
        expected.setPreLastReportPeriodMainAssets("2401");

        assertEquals(expected, reports.get(0));
    }
}
