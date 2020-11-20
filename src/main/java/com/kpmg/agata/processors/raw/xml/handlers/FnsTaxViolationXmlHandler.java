package com.kpmg.agata.processors.raw.xml.handlers;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.models.FnsTaxViolationModel;
import org.xml.sax.Attributes;

public class FnsTaxViolationXmlHandler extends FnsHandler {

    private FnsTaxViolationModel model;

    private static final String VIOLATION_INFO = "СведНаруш";
    private static final String FINE = "СумШтраф";

    public FnsTaxViolationXmlHandler(SequenceWriter sequenceWriter, String inputFilename, String nameDo,
                                     String loadDate) {
        super(sequenceWriter, inputFilename, nameDo, loadDate);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        commonFields(qName, attributes);
        if (VIOLATION_INFO.equals(qName)) {
            setFine(attributes);
            writeModel(model);
        }
    }

    private void commonFields(String qName, Attributes attributes) {
        if (DOC.equals(qName)) {
            model = new FnsTaxViolationModel();
            setBaseInfo(model);
            setDocAttributes(model, attributes);
        } else if (NP_INFO.equals(qName)) {
            setNpInfoAttributes(model, attributes);
        }
    }

    private void setFine(Attributes attributes) {
        model.setFine(attributes.getValue(FINE));
    }
}
