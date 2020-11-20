package com.kpmg.agata.processors.raw.xml.handlers;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.models.FnsArrearsModel;
import org.xml.sax.Attributes;

public class FnsArrearsXmlHandler extends FnsHandler {

    private FnsArrearsModel model;

    private static final String ARREARS_INFO = "СведНедоим";

    private static final String TAX_NAME = "НаимНалог";
    private static final String TAX_ARREARS = "СумНедНалог";
    private static final String PENALTIES = "СумПени";
    private static final String FINE = "СумШтраф";
    private static final String TOTAL_ARREARS = "ОбщСумНедоим";

    private String lastDocId;
    private String lastDocDate;
    private String lastCreationDate;

    private String lastOrgName;
    private String lastInnul;

    public FnsArrearsXmlHandler(SequenceWriter sequenceWriter, String inputFilename, String nameDo, String date) {
        super(sequenceWriter, inputFilename, nameDo, date);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (DOC.equals(qName)) {
            clearLastVales();
            model = new FnsArrearsModel();
            setBaseInfo(model);
            saveLastDocAttributes(attributes);
            setDocAttributes();
        } else if (NP_INFO.equals(qName)) {
            saveNpAttributes(attributes);
            setNpInfoAttributes();
        } else if (ARREARS_INFO.equals(qName)) {
            setArrears(attributes);
            writeModel(model);
            model = new FnsArrearsModel();
            initAndSetLastValues();
        }
    }

    private void saveLastDocAttributes(Attributes attributes) {
        lastDocId = attributes.getValue(DOC_ID);
        lastDocDate = attributes.getValue(DOC_DATE);
        lastCreationDate = attributes.getValue(CREATION_DATE);
    }

    private void setDocAttributes() {
        model.setDocId(lastDocId);
        model.setDocDate(lastDocDate);
        model.setCreationDate(lastCreationDate);
    }

    private void saveNpAttributes(Attributes attributes) {
        lastOrgName = attributes.getValue(ORG_NAME);
        lastInnul = attributes.getValue(INNUL);
    }

    private void setNpInfoAttributes() {
        model.setOrgName(lastOrgName);
        model.setInnul(lastInnul);
    }

    private void clearLastVales() {
        lastDocId = null;
        lastDocDate = null;
        lastCreationDate = null;
        lastOrgName = null;
        lastInnul = null;
    }

    private void setArrears(Attributes attributes) {
        model.setTaxName(attributes.getValue(TAX_NAME));
        model.setTaxArrears(attributes.getValue(TAX_ARREARS));
        model.setPenalties(attributes.getValue(PENALTIES));
        model.setArrearsFine(attributes.getValue(FINE));
        model.setTotalArrears(attributes.getValue(TOTAL_ARREARS));
    }

    private void initAndSetLastValues() {
        setBaseInfo(model);
        setDocAttributes();
        setNpInfoAttributes();
    }
}
