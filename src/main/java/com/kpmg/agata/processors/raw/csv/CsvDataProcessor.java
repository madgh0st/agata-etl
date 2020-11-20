package com.kpmg.agata.processors.raw.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.kpmg.agata.config.ConfigParser;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.models.AbstractClientDataModel;
import com.kpmg.agata.parser.ConfigType;
import com.kpmg.agata.processors.FileProcessor;
import com.kpmg.agata.utils.filesystem.IFileSystem;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

import static com.kpmg.agata.parser.ConfigType.FNS_DISQ;
import static com.kpmg.agata.parser.ConfigType.SECTORAL_INDICES;

public class CsvDataProcessor extends FileProcessor {

    private static final Logger log = LoggerFactory.getLogger(CsvDataProcessor.class);
    private final CsvMapper mapper = new CsvMapper();
    private final Map<ConfigType, CsvSchema> schemas = new EnumMap<>(ConfigType.class);
    private final Map<ConfigType, Charset> encodings = new EnumMap<>(ConfigType.class);


    public CsvDataProcessor(IFileSystem fileSystem, String inputFilename, ModelTypeConfiguration config, String date) {
        super(fileSystem, inputFilename, config, date);
        encodings.put(SECTORAL_INDICES, Charset.forName("windows-1251"));

        schemas.put(FNS_DISQ, mapper.schemaWithHeader().withColumnSeparator(';').withoutQuoteChar());
        schemas.put(SECTORAL_INDICES, mapper.schemaWithHeader().withColumnSeparator(';'));
    }

    private MappingIterator<Map<String, String>> getCsvMappingIterator(InputStream inputStream) throws IOException {
        CsvSchema schema = schemas.getOrDefault(config.getConfigType(), mapper.schemaWithHeader());
        Charset encoding = encodings.getOrDefault(config.getConfigType(), UTF_8);

        return mapper.reader(schema)
                     .forType(Map.class)
                     .readValues(new InputStreamReader(inputStream, encoding));
    }

    private void processRow(Map<String, String> row, SequenceWriter sequenceWriter) {
        try {
            AbstractClientDataModel model = mapper.convertValue(row, config.getConfigType().getModelClass());
            model.setLoadDate(date);
            model.setFileName(inputFilename);
            model.setName_do(config.getNameDo());
            sequenceWriter.write(model);
        } catch (IOException | IllegalArgumentException e) {
            log.error("Error on model write to json file", e);
        }
    }

    @Override
    public void run() {
        String outputPath = new ConfigParser().getSheetOutputPath(config.getOutputDir(), inputFilename);
        try (InputStream inputStream = fileSystem.reader(inputFilename);
             SequenceWriter sequenceWriter = objectWriter.writeValues(fileSystem.writer(outputPath))) {
            MappingIterator<Map<String, String>> iterator = getCsvMappingIterator(inputStream);
            while (iterator.hasNext()) {
                processRow(iterator.next(), sequenceWriter);
            }
        } catch (IOException e) {
            log.error("Error while parsing CSV file: " + inputFilename, e);
        }
    }
}
