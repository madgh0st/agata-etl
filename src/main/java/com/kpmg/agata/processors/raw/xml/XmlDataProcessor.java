package com.kpmg.agata.processors.raw.xml;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.processors.FileProcessor;
import com.kpmg.agata.processors.raw.xml.handlers.FnsArrearsXmlHandler;
import com.kpmg.agata.processors.raw.xml.handlers.FnsTaxViolationXmlHandler;
import com.kpmg.agata.utils.filesystem.IFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class XmlDataProcessor extends FileProcessor {
    private static final Logger log = LoggerFactory.getLogger(XmlDataProcessor.class);

    public XmlDataProcessor(IFileSystem iFileSystem, String path, ModelTypeConfiguration config, String date) {
        super(iFileSystem, path, config, date);
    }

    @Override
    public void run() {
        String outputPath = new ConfigParser().getSheetOutputPath(config.getOutputDir(), inputFilename);
        try (InputStream inputStream = fileSystem.reader(inputFilename);
             SequenceWriter sequenceWriter = objectWriter.writeValues(fileSystem.writer(outputPath))) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler;
            switch (config.getConfigType()) {
                case FNS_TAX_VIOLATION:
                    handler = new FnsTaxViolationXmlHandler(sequenceWriter, inputFilename, config.getNameDo(), date);
                    break;
                case FNS_ARREARS:
                    handler = new FnsArrearsXmlHandler(sequenceWriter, inputFilename, config.getNameDo(), date);
                    break;
                default:
                    throw new IllegalStateException("Unknown source type");
            }
            saxParser.parse(inputStream, handler);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("Error while parsing XML file: " + inputFilename, e);
        }
    }
}
