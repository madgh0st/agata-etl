package com.kpmg.agata.parser;

import java.util.Arrays;
import java.util.List;

public enum FileType {
    EXCEL(Arrays.asList("xls", "xlsx", "xlsb")),
    XML(Arrays.asList("xml")),
    CSV(Arrays.asList("csv"));

    List<String> extensions;

    FileType(List<String> extensions) {
        this.extensions = extensions;
    }

    public static FileType resolveType(String filePath) {
        for (FileType fileType : values()) {
            for (String extension : fileType.getExtensions()) {
                if (filePath.toLowerCase().endsWith(extension)) return fileType;
            }
        }
        throw new IllegalArgumentException("Unsupported file extension");
    }

    public List<String> getExtensions() {
        return extensions;
    }
}
