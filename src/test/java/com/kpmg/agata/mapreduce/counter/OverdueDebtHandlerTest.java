package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.sql.Date;
import java.util.function.Function;

import static java.sql.Date.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class OverdueDebtHandlerTest {
    private static final Date JAN_1 = valueOf("1970-01-01");
    private static final Date JAN_2 = valueOf("1970-01-02");

    private static double significantDebt;
    private static double notSignificantDebt;
    private OverdueDebtHandler handler;

    @BeforeClass
    public static void beforeClass() throws Exception {
        significantDebt = Whitebox.getField(OverdueDebtHandler.class, "SIGNIFICANT_OVERDUE_DEBT")
                .getDouble(null);
        notSignificantDebt = significantDebt - 1.0;
    }

    private static CleanWeeklyStatusPDZFlatModel pdz(Double debt, boolean blameBanks, Date date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(date);
        pdz.setOverdueDebt(debt);
        pdz.setIs_pdz_because_of_banks(blameBanks);
        return pdz;
    }

    @Before
    public void setUp() {
        handler = new OverdueDebtHandler();
    }

    @Test
    public void significantDebtTest() throws Exception {
        assertOverdueDebt(false, null);
        assertOverdueDebt(false, notSignificantDebt);
        assertOverdueDebt(true, significantDebt);
    }

    private void assertOverdueDebt(boolean expected,
                                   Double debt) throws Exception {
        CleanWeeklyStatusPDZFlatModel pdz = pdz(debt, false, null);
        Function<CleanWeeklyStatusPDZFlatModel, Double> getter = CleanWeeklyStatusPDZFlatModel::getOverdueDebt;
        boolean actual = Whitebox.invokeMethod(handler, "significantDebt", pdz, getter);
        assertEquals(expected, actual);
    }

    @Test
    public void skipOneDayBecauseOfBanksTest() {
        boolean blameBankDebt = handler.handleOverdueDebt(pdz(significantDebt, true, JAN_1));
        assertFalse(blameBankDebt);

        boolean blameBankOneDayDebtAgain = handler.handleOverdueDebt(pdz(significantDebt, true, JAN_1));
        assertFalse(blameBankOneDayDebtAgain);

        boolean blameBankNextDayDebt = handler.handleOverdueDebt(pdz(significantDebt, true, JAN_2));
        assertTrue(blameBankNextDayDebt);
    }

    @Test
    public void notBecauseOfBanksTest() {
        boolean blameBankDebt = handler.handleOverdueDebt(pdz(significantDebt, false, JAN_1));
        assertTrue(blameBankDebt);

        boolean blameBankOneDayDebtAgain = handler.handleOverdueDebt(pdz(significantDebt, false, JAN_1));
        assertTrue(blameBankOneDayDebtAgain);

        boolean blameBankNextDayDebt = handler.handleOverdueDebt(pdz(significantDebt, false, JAN_2));
        assertTrue(blameBankNextDayDebt);
    }
}
