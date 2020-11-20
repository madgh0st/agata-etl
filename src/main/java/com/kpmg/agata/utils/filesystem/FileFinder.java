package com.kpmg.agata.utils.filesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;


public class FileFinder  implements Serializable {

    private static Logger log = LoggerFactory.getLogger(FileFinder.class);

    public static class Finder extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;
        private int numMatches = 0;
        private List<Path> matches;

        Finder(String pattern) {
            log.debug("Finder with `glob:{}` initialized", pattern);
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
            matches = new ArrayList<>();
        }

        /**
         * Compares the glob pattern against the file or directory name.
         *
         * @param file file to match
         */
        void find(Path file) {
            Path fileName = file.getFileName();
            if (fileName != null && matcher.matches(fileName)) {
                matches.add(file);
                numMatches++;
            }
        }

        /**
         * Prints the total number of matches to standard out.
         */
        void done() {
            log.debug("Matched: {}",numMatches);
        }

        /**
         * Invoke the pattern matching  method on each file.
         */
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            find(file);
            return CONTINUE;
        }

        /**
         * Invoke the pattern matching method on each directory.
         */
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            find(dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            log.error("Exception during visiting files occurred!", exc);
            return CONTINUE;
        }
    }

    /**
     * Find all files that matches the glob
     * More info about glob syntax: https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob
     *
     * @param glob      Glob regexp
     * @param directory Starting directory
     * @return Array of matching Paths objects
     */
    public static List<Path> find(String directory, String glob) {
        Finder finder = new Finder(glob);
        try {
            log.debug("Walking directory: {}", directory);
            Files.walkFileTree(Paths.get(directory), finder);
        } catch (IOException e) {
            log.error("Error during walking file tree occurred!", e);
        }
        log.debug("Found {} matching files", finder.numMatches);
        return finder.matches;
    }

    public boolean checkGlob(String pattern, String filename) {
        return FileSystems.getDefault()
                .getPathMatcher("glob:" + pattern)
                .matches(Paths.get(filename));
    }
}