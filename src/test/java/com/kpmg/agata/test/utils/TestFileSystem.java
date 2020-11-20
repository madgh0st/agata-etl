package com.kpmg.agata.test.utils;

import com.kpmg.agata.utils.filesystem.IFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestFileSystem implements IFileSystem {
    private final IFileSystem reader;
    private final IFileSystem writer;

    public TestFileSystem(IFileSystem reader, IFileSystem writer) {

        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public InputStream reader(String fileName) throws IOException {
        return reader.reader(fileName);
    }

    @Override
    public Writer writer(String fileName) throws IOException {
        return writer.writer(fileName);
    }

    @Override
    public List<String> globMatch(String directory, String glob) {
        return reader.globMatch(directory, glob);
    }

    @Override
    public List<String> getAvailableDateForPath(String sourcePath) {
        return null;
    }

    @Override
    public void copyFiles(String pathToSourceFile, String pathToTargetFile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteFolderOrFile(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setChmod(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createFolder(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFileChecksum(String path) {
        throw new UnsupportedOperationException();
    }
}
