package com.kpmg.agata.utils.filesystem;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class LFSFacade implements IFileSystem {

    @Override
    public InputStream reader(String fileName) throws IOException {
        return new FileInputStream(fileName);
    }

    @Override
    public Writer writer(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        if (!path.toFile().exists()) {
            Files.createDirectories(path.getParent());
        }
        return new BufferedWriter
                (new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
    }

    @Override
    public List<String> globMatch(String directory, String glob) {
        return FileFinder.find(directory, glob).stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public List<String> getAvailableDateForPath(String sourcePath) {
        return null;
    }

    @Override
    public void copyFiles(String pathToSourceFile, String pathTotargetFile) {

    }

    @Override
    public void deleteFolderOrFile(String path) {

    }

    @Override
    public void setChmod(String path) {

    }

    @Override
    public void createFolder(String path) {

    }

    @Override
    public String getFileChecksum(String path) {
        return null;
    }
}
