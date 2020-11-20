package com.kpmg.agata.processors.raw.xml.handlers;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.models.FnsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

import static com.kpmg.agata.utils.Utils.getModificationDateTime;

class FnsHandler extends DefaultHandler {
    private static final Logger log = LoggerFactory.getLogger(FnsHandler.class);

    private SequenceWriter sequenceWriter;

    private final String inputFilename;
    private final String nameDo;
    private final String loadDate;

    static final String DOC = "Документ";

    static final String DOC_ID = "ИдДок";
    static final String DOC_DATE = "ДатаДок";
    static final String CREATION_DATE = "ДатаСост";

    static final String NP_INFO = "СведНП";

    static final String ORG_NAME = "НаимОрг";
    static final String INNUL = "ИННЮЛ";

    FnsHandler(SequenceWriter sequenceWriter, String inputFilename, String nameDo, String loadDate) {
        this.sequenceWriter = sequenceWriter;
        this.inputFilename = inputFilename;
        this.nameDo = nameDo;
        this.loadDate = loadDate;
    }

    void setBaseInfo(FnsModel model) {
        model.setFileName(inputFilename);
        model.setName_do(nameDo);
        model.setLoadDate(loadDate);
        model.setModificationDate(getModificationDateTime());
    }

    void setDocAttributes(FnsModel model, Attributes attributes) {
        model.setDocId(attributes.getValue(DOC_ID));
        model.setDocDate(attributes.getValue(DOC_DATE));
        model.setCreationDate(attributes.getValue(CREATION_DATE));
    }

    void setNpInfoAttributes(FnsModel model, Attributes attributes) {
        model.setOrgName(attributes.getValue(ORG_NAME));
        model.setInnul(attributes.getValue(INNUL));
    }

    void writeModel(FnsModel model) {
        try {
            sequenceWriter.write(model);
        } catch (IOException e) {
            log.error("Error on model write to json file", e);
        }
    }
}
