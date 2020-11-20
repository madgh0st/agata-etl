package com.kpmg.agata.processors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kpmg.agata.config.ModelTypeConfiguration;
import com.kpmg.agata.utils.filesystem.IFileSystem;

public abstract class FileProcessor {
    protected IFileSystem fileSystem;
    protected String inputFilename;
    protected ModelTypeConfiguration config;
    protected ObjectWriter objectWriter;
    protected ObjectMapper objectMapper;
    protected final String date;

    public FileProcessor(IFileSystem fileSystem, String inputFilename, ModelTypeConfiguration config, String date) {
        this.fileSystem = fileSystem;
        this.inputFilename = inputFilename;
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, false);
        this.objectWriter = objectMapper.writer(new MinimalPrettyPrinter("\r\n"));
        this.date = date;
    }

    public abstract void run();
}
