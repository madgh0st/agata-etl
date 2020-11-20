package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.sql.Date;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.PDZ_21;
import static org.junit.Assert.assertEquals;

public class Overdue21DebtCounterTest {
    private static final String REPORT = PDZ_21;
    private static double significantDebt;
    private static double notSignificantDebt;
    private Overdue21DebtCounter counter;

    @BeforeClass
    public static void beforeClass() throws Exception {
        significantDebt = Whitebox.getField(OverdueDebtHandler.class, "SIGNIFICANT_OVERDUE_DEBT")
                .getDouble(null);
        notSignificantDebt = significantDebt - 1.0;
    }

    private static CleanWeeklyStatusPDZFlatModel pdzWithDebt(Double debt,
                                                             Double from5To30Debt,
                                                             Double from30Debt,
                                                             String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setOverdueDebt(debt);
        pdz.setOverdueDebtBetween5and30Days(from5To30Debt);
        pdz.setOverdueDebtMore30Days(from30Debt);
        pdz.setEventDate(Date.valueOf(date));
        return pdz;
    }

    private void assertPdz21By30Debt(String expected, Double debt, String date) {
        assertPdz(expected, pdzWithDebt(notSignificantDebt, notSignificantDebt, debt, date));
    }

    private void assertPdz(String expected, CleanWeeklyStatusPDZFlatModel pdz) {
        assertEquals(expected, counter.process(pdz).get(REPORT));
    }

    @Before
    public void setUp() {
        counter = new Overdue21DebtCounter();
    }

    @Test
    public void debtMore30DaysTest() {
        assertPdz21By30Debt("0", notSignificantDebt, "1970-01-01");
        assertPdz21By30Debt("1", significantDebt, "1970-01-02");
        assertPdz21By30Debt("0", notSignificantDebt, "1970-01-03");
    }

    @Test
    public void sustainableDebtTest() {
        CleanWeeklyStatusPDZFlatModel pdz =
                pdzWithDebt(significantDebt, notSignificantDebt, notSignificantDebt, "1970-01-01");
        assertPdz("0", pdz);

        pdz = pdzWithDebt(significantDebt, significantDebt, notSignificantDebt, "1970-01-02");
        assertPdz("0", pdz);

        pdz = pdzWithDebt(significantDebt, significantDebt, notSignificantDebt, "1970-01-03");
        assertPdz("1", pdz);
    }
}
