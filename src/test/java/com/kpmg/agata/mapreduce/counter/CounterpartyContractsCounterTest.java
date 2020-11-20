package com.kpmg.agata.mapreduce.counter;

import com.kpmg.agata.models.clean.CleanCounterpartyContractModel;
import com.kpmg.agata.models.clean.CleanRealizationServicesModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.sql.Date;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static com.kpmg.agata.mapreduce.counter.CounterConstants.CONTRACT_DAYS_FROM_EXPECTED_PAYMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CounterpartyContractsCounterTest {

    private CounterpartyContractsCounter counter;

    @Before
    public void setUp() {
        counter = new CounterpartyContractsCounter();
    }

    @Test
    public void neverWasInRegistryTest() {
        Map<String, String> reports = processPdz("1970-01-01");
        assertTrue(reports.isEmpty());
    }

    @Test
    public void contractWithoutRealizationTest() {
        processContract(10);
        Map<String, String> reports = processPdz("1970-01-01");
        assertTrue(reports.isEmpty());
    }

    @Test
    public void positiveDaysFromExpectedPaymentTest() {
        processContract(10);
        processRealization("1970-01-01");
        Map<String, String> reports = processPdz("1970-01-05");
        assertEquals("6", reports.get(CONTRACT_DAYS_FROM_EXPECTED_PAYMENT));
    }

    @Test
    public void sameDayAsExpectedPaymentTest() {
        processContract(10);
        processRealization("1970-01-01");
        Map<String, String> reports = processPdz("1970-01-11");
        assertEquals("0", reports.get(CONTRACT_DAYS_FROM_EXPECTED_PAYMENT));
    }

    @Test
    public void negativeDaysFromExpectedPaymentTest() {
        processContract(10);
        processRealization("1970-01-01");
        Map<String, String> reports = processPdz("1970-01-16");
        assertEquals("-5", reports.get(CONTRACT_DAYS_FROM_EXPECTED_PAYMENT));
    }

    @Test
    public void tooMuchDaysFromRealizationTest() {
        processContract(10);
        processRealization("1971-01-01");
        Map<String, String> reports = processPdz("1971-06-01");
        assertTrue(reports.isEmpty());
        reports = processPdz("1970-06-01");
        assertTrue(reports.isEmpty());
    }


    private Map<String, String> processPdz(String date) {
        CleanWeeklyStatusPDZFlatModel pdz = new CleanWeeklyStatusPDZFlatModel();
        pdz.setEventDate(Date.valueOf(date));
        return counter.process(pdz);
    }

    private void processContract(Integer paymentTerm) {
        CleanCounterpartyContractModel contract = new CleanCounterpartyContractModel();
        contract.setPaymentTerm(paymentTerm);
        counter.process(contract);
    }

    private void processRealization(String date) {
        CleanRealizationServicesModel realization = new CleanRealizationServicesModel();
        realization.setEventDate(Date.valueOf(date));
        counter.process(realization);
    }
}
