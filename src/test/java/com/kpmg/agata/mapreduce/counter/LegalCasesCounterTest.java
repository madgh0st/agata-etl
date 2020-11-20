package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanLegalCasesModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.sql.Date;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_IS_ANY_CASE_IN_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_IS_ANY_CASE_IN_PAST;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_NUMBER_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_UNCLOSED_COUNT_90D;
import static com.kpmg.agata.mapreduce.counter.CounterConstants.LC_UPHELD_CLAIMS_90D;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LegalCasesCounterTest {

    private static Set<String> uniqueCategoryGroups;
    private LegalCasesCounter counter;

    @BeforeClass
    public static void beforeClass() {
        uniqueCategoryGroups = Whitebox.getInternalState(LegalCasesCounter.class, "UNIQUE_CATEGORY_GROUPS");
    }

    @Before
    public void setUp() {
        counter = new LegalCasesCounter();
    }

    @Test
    public void legalCaseHappens() {
        CleanLegalCasesModel ea1 = new CleanLegalCasesModel();
        ea1.setEventDate(Date.valueOf("1970-01-01"));
        counter.process(ea1);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-03"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("1", reports.get(LC_IS_ANY_CASE_IN_90D));
        assertEquals("1", reports.get(LC_NUMBER_90D));
    }

    @Test
    public void caseGetsOutFrom90DaysRange() {
        CleanLegalCasesModel legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-01"));
        counter.process(legalCase);

        Map<String, String> reports = processPdz("1970-03-31");     // Plus 89 days

        assertEquals("1", reports.get(LC_IS_ANY_CASE_IN_90D));
        assertEquals("1", reports.get(LC_NUMBER_90D));

        Map<String, String> reports1 = processPdz("1970-04-01");     // Plus 90 days

        assertEquals("0", reports1.get(LC_IS_ANY_CASE_IN_90D));
        assertEquals("0", reports1.get(LC_NUMBER_90D));
    }

    @Test
    public void threeCasesHappens() {
        CleanLegalCasesModel legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-01"));
        counter.process(legalCase);

        legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-02"));
        counter.process(legalCase);

        legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-03"));
        counter.process(legalCase);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-03"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("3", reports.get(LC_NUMBER_90D));
    }

    @Test
    public void threeCasesInSameDay() {
        CleanLegalCasesModel legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-01"));
        counter.process(legalCase);

        legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-02"));
        counter.process(legalCase);

        legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-03"));
        counter.process(legalCase);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-03"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("3", reports.get(LC_NUMBER_90D));
    }

    @Test
    public void isAnyLegalCase() {
        CleanLegalCasesModel legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-01"));
        counter.process(legalCase);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1980-01-01"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("1", reports.get(LC_IS_ANY_CASE_IN_PAST));
    }

    @Test
    public void noAnyLegalCase() {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1980-01-01"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("0", reports.get(LC_IS_ANY_CASE_IN_PAST));
    }

    @Test
    public void twoCasesOneDayDays() {
        CleanLegalCasesModel legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-01"));
        counter.process(legalCase);

        legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf("1970-01-01"));
        counter.process(legalCase);

        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf("1970-01-02"));
        Map<String, String> reports = counter.process(pdz);

        assertEquals("1", reports.get(LC_IS_ANY_CASE_IN_90D));
        assertEquals("2", reports.get(LC_NUMBER_90D));
    }

    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        return counter.process(pdz);
    }

    private void processLegalCaseWithStatus(String date, String number, String status) {
        CleanLegalCasesModel legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf(date));
        legalCase.setCaseNumber(number);
        legalCase.setStatus(status);
        counter.process(legalCase);
    }

    @Test
    public void hasNotUnclosedCase() {
        assertEquals("0", processPdz("1970-01-01").get(LC_UNCLOSED_COUNT_90D));
    }

    @Test
    public void hasUnclosedCase() {
        processLegalCaseWithStatus("1970-01-01", "case 1", "Рассматривается");
        assertEquals("1", processPdz("1970-01-02").get(LC_UNCLOSED_COUNT_90D));
        processLegalCaseWithStatus("1970-01-03", "case 1", "Завершено");
        assertEquals("0", processPdz("1970-01-04").get(LC_UNCLOSED_COUNT_90D));
    }

    @Test
    public void hasTwoUnclosedCases() {
        processLegalCaseWithStatus("1970-01-01", "case 1", "Рассматривается");
        assertEquals("1", processPdz("1970-01-02").get(LC_UNCLOSED_COUNT_90D));
        processLegalCaseWithStatus("1970-01-03", "case 2", "Рассматривается");
        assertEquals("2", processPdz("1970-01-04").get(LC_UNCLOSED_COUNT_90D));
        processLegalCaseWithStatus("1970-01-05", "case 1", "Завершено");
        assertEquals("1", processPdz("1970-01-06").get(LC_UNCLOSED_COUNT_90D));
        processLegalCaseWithStatus("1970-01-07", "case 2", "Завершено");
        assertEquals("0", processPdz("1970-01-08").get(LC_UNCLOSED_COUNT_90D));
    }

    @Test
    public void closeUnknownCase() {
        processLegalCaseWithStatus("1970-01-01", "unknown case", "Завершено");

        Map<String, String> reports = processPdz("1970-01-02");
        assertEquals("0", reports.get(LC_UNCLOSED_COUNT_90D));
    }

    @Test
    public void nullNumberLegalCase() {
        processLegalCaseWithStatus("1970-01-01", null, "Рассматривается");

        assertEquals("0", processPdz("1970-01-02").get(LC_UNCLOSED_COUNT_90D));
    }

    @Test
    public void sameUnclosedCaseTwice() {
        processLegalCaseWithStatus("1970-01-01", "case 1", "Рассматривается");
        assertEquals("1", processPdz("1970-01-02").get(LC_UNCLOSED_COUNT_90D));
        processLegalCaseWithStatus("1970-01-03", "case 1", "Рассматривается");
        assertEquals("1", processPdz("1970-01-04").get(LC_UNCLOSED_COUNT_90D));
    }

    @Test
    public void unclosedCaseGetsOutOf90DaysRange() {
        processLegalCaseWithStatus("1970-01-01", "case 1", "Рассматривается");
        assertEquals("1", processPdz("1970-03-31").get(LC_UNCLOSED_COUNT_90D)); // plus 89 days
        assertEquals("0", processPdz("1970-04-01").get(LC_UNCLOSED_COUNT_90D)); // plus 90 days
    }

    private void processLegalCaseWithOutcome(String date, String outcome) {
        CleanLegalCasesModel legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf(date));
        legalCase.setOutcome(outcome);
        counter.process(legalCase);
    }

    @Test
    public void hasNotUpheld() {
        Map<String, String> reportsBefore = processPdz("1970-01-01");
        assertEquals("0", reportsBefore.get(LC_UPHELD_CLAIMS_90D));
    }

    @Test
    public void upheldClaimIn90Days() {
        processLegalCaseWithOutcome("1970-01-01", "Иск удовлетворен полностью");
        assertEquals("1", processPdz("1970-01-02").get(LC_UPHELD_CLAIMS_90D));
        assertEquals("1", processPdz("1970-02-02").get(LC_UPHELD_CLAIMS_90D));
    }

    @Test
    public void after90DaysOfUpheldClaim() {
        processLegalCaseWithOutcome("1970-01-01", "Иск удовлетворен полностью");
        assertEquals("1", processPdz("1970-03-31").get(LC_UPHELD_CLAIMS_90D));  // plus 89 days
        assertEquals("0", processPdz("1970-04-01").get(LC_UPHELD_CLAIMS_90D));  // plus 90 days
    }

    @Test
    public void twoUpheldClaimsIn90Days() {
        processLegalCaseWithOutcome("1970-01-01", "Иск удовлетворен полностью");
        processLegalCaseWithOutcome("1970-01-02", "Иск удовлетворен частично");

        assertEquals("2", processPdz("1970-01-03").get(LC_UPHELD_CLAIMS_90D));
        assertEquals("1", processPdz("1970-04-01").get(LC_UPHELD_CLAIMS_90D));  // plus 89 days
        assertEquals("0", processPdz("1970-04-02").get(LC_UPHELD_CLAIMS_90D));  // plus 90 days
    }


    private void processLegalCaseWithCategory(String date, String category) {
        CleanLegalCasesModel legalCase = new CleanLegalCasesModel();
        legalCase.setEventDate(Date.valueOf(date));
        legalCase.setCategory(category);
        counter.process(legalCase);
    }

    @Test
    public void allCategoriesStartsWithCommonPrefix() {
        assertTrue(uniqueCategoryGroups.stream()
                                       .allMatch(key -> key.startsWith("lc_cat_")));
    }

    @Test
    public void categoryGroupsWhenNoLegalCasesHappened() {
        Map<String, String> reports = processPdz("1970-01-01");
        assertTrue(uniqueCategoryGroups.stream()
                                       .allMatch(group -> "0".equals(reports.get(group))));
    }


    @Test
    public void caseWithCategory() {
        processLegalCaseWithCategory("1970-01-01", "Банкротство");
        Map<String, String> reports = processPdz("1970-01-02");
        assertEquals("1", reports.get("lc_cat_bankruptcy_liquidation"));
        assertTrue(uniqueCategoryGroups.stream()
                                       .filter(group -> !"lc_cat_bankruptcy_liquidation".equals(group))
                                       .allMatch(group -> "0".equals(reports.get(group))));
    }

    @Test
    public void caseWithCategoryTwoTimes() {
        processLegalCaseWithCategory("1970-01-01", "Банкротство");
        processLegalCaseWithCategory("1970-01-01", "Банкротство");
        Map<String, String> reports = processPdz("1970-01-02");
        assertEquals("2", reports.get("lc_cat_bankruptcy_liquidation"));
        assertTrue(uniqueCategoryGroups.stream()
                                       .filter(group -> !"lc_cat_bankruptcy_liquidation".equals(group))
                                       .allMatch(group -> "0".equals(reports.get(group))));
    }

    @Test
    public void twoCasesWithCategory() {
        processLegalCaseWithCategory("1970-01-01", "Банкротство");
        processLegalCaseWithCategory("1970-01-02", "Возмещение вреда");
        Map<String, String> reports = processPdz("1970-01-03");
        assertEquals("1", reports.get("lc_cat_bankruptcy_liquidation"));
        assertEquals("1", reports.get("lc_cat_compensations"));
        assertTrue(uniqueCategoryGroups.stream()
                                       .filter(group -> !"lc_cat_bankruptcy_liquidation".equals(group))
                                       .filter(group -> !"lc_cat_compensations".equals(group))
                                       .allMatch(group -> "0".equals(reports.get(group))));
    }

    @Test
    public void caseWithUnknownCategory() {
        processLegalCaseWithCategory("1970-01-01", "unknown category");
        Map<String, String> reports = processPdz("1970-01-02");
        assertTrue(uniqueCategoryGroups.stream()
                                       .allMatch(group -> "0".equals(reports.get(group))));
    }
}
