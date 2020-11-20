package com.kpmg.agata.utils.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;

public interface IFileSystem {

    InputStream reader(String fileName) throws IOException;

    Writer writer(String fileName) throws IOException;

    List<String> globMatch(String directory, String glob);

    List<String> getAvailableDateForPath(String sourcePath);

    void copyFiles(String pathToSourceFile, String pathToTargetFile);

    void deleteFolderOrFile(String path);

    void setChmod(String path);

    void createFolder(String path);

    String getFileChecksum(String path);
}
