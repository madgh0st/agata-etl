package com.kpmg.agata.models.clean;

public class CleanCounterpartyContractModel extends AbstractCleanDataModel {

    public CleanCounterpartyContractModel() {
    }

    private Integer paymentTerm;

    public Integer getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(Integer paymentTerm) {
        this.paymentTerm = paymentTerm;
    }
}
