package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.HAS_PDZ;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OverdueDebtCounter.class)
public class OverdueDebtCounterTest {
    private OverdueDebtCounter counter = new OverdueDebtCounter();
    private OverdueDebtHandler handlerMock = mock(OverdueDebtHandler.class);

    private static CleanWeeklyStatusPDZFlatModel pdzWithDebt(Double debt) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setOverdueDebt(debt);
        return pdz;
    }

    private void assertCounter(String expected, boolean hasOverdueDebt) {
        when(handlerMock.handleOverdueDebt(any()))
                .thenReturn(hasOverdueDebt);
        assertEquals(expected, counter.process(pdzWithDebt(null)).get(HAS_PDZ));
    }

    @Before
    public void setUp() throws Exception {
        whenNew(OverdueDebtHandler.class)
                .withNoArguments()
                .thenReturn(handlerMock);
        counter = new OverdueDebtCounter();
    }

    @Test
    public void reportTest() {
        assertCounter("0", false);
        assertCounter("1", true);
    }
}
