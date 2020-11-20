package com.kpmg.agata.filesystem;

import com.kpmg.agata.utils.filesystem.FileFinder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GlobPatternTest {

    @Test
    public void checkGlobTest() {

        FileFinder fileFinder = new FileFinder();
        assertFalse(fileFinder.checkGlob("file.xls", "file.xlsx"));
        assertFalse(fileFinder.checkGlob("*file.xls", "file.xlsx"));

        assertTrue(fileFinder.checkGlob("*.xls", "file.xls"));
        assertTrue(fileFinder.checkGlob("*.xls", "file.xls"));
        assertTrue(fileFinder.checkGlob("file.xls", "file.xls"));
        assertTrue(fileFinder.checkGlob("*ile.xls", "file.xls"));
        assertTrue(fileFinder.checkGlob("file.{xls,XLS}", "file.xls"));
        assertTrue(fileFinder.checkGlob("file.{xls,XLS}", "file.XLS"));
    }
}
