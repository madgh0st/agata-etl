package com.kpmg.agata.utils.filesystem;

import com.kpmg.agata.config.Environment;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.fs.permission.FsPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HDFSFacade implements IFileSystem {

    private static final Logger log = LoggerFactory.getLogger(HDFSFacade.class);

    private FileSystem fileSystem;
    private FsShell fsShell;
    private Configuration conf;

    public HDFSFacade() {
        conf = new Configuration();
        conf.set("fs.defaultFS", Environment.getConfig().getProperty("fs.defaultFS"));
        conf.set("mapreduce.framework.name", Environment.getConfig().getProperty("mapreduce.framework.name"));
        conf.set("yarn.resourcemanager.address", Environment.getConfig().getProperty("yarn.resourcemanager.address"));
        init(conf);
    }


    public HDFSFacade(Configuration conf) {
        init(conf);
    }

    private void init(Configuration conf) {
        conf.set("filesystem.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("filesystem.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        this.fsShell = new FsShell(conf);
        try {
            fileSystem = FileSystem.get(conf);
        } catch (IOException e) {
            log.error("Error occurred during reaching HDFS", e);
        }
    }

    @Override
    public InputStream reader(String fileName) throws IOException {
        return fileSystem.open(new Path(fileName));
    }

    @Override
    public Writer writer(String path) throws IOException {
        Path file = new Path(path);
        FSDataOutputStream outputStream = fileSystem.create(file);
        fileSystem.setPermission(file, FsPermission.valueOf("-rw-rw-rw-"));
        return new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
    }

    @Override
    public List<String> globMatch(String directory, String glob) {
        List<String> paths = new ArrayList<>();
        String wildcard = directory + Path.SEPARATOR + glob;
        try {
            for (FileStatus status : fileSystem.globStatus(new Path(wildcard))) {
                paths.add(status.getPath().toString());
            }
        } catch (IOException e) {
            log.error("Error occurred during glob matching in HDFS. Wildcard: {}", wildcard, e);
        }
        return paths;
    }

    public List<Path> getFilesRecursively(String directory) {
        List<Path> fullPathsToFiles = new ArrayList<>();

        try {
            RemoteIterator<LocatedFileStatus> fileStatusListIterator =
                    this.fileSystem.listFiles(new Path(directory), true);
            while (fileStatusListIterator.hasNext()) {
                fullPathsToFiles.add(fileStatusListIterator.next().getPath());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return fullPathsToFiles;
    }

    @Override
    public List<String> getAvailableDateForPath(String sourcePath) {

        String pattern = "^(\\d{4})-(\\d{2})-(\\d{2})$";
        Pattern regexPattern = Pattern.compile(pattern);

        List<String> list = new ArrayList<>();

        try {
            list = Arrays.stream(this.fileSystem.listStatus(new Path(sourcePath)))
                         .filter(FileStatus::isDirectory)
                         .map(FileStatus::getPath)
                         .map(Path::getName)
                         .filter(name -> regexPattern.matcher(name).find())
                         .collect(Collectors.toList());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void copyFiles(String pathToSourceFile, String pathToTargetFile) {
        try {
            log.debug("Copy file: {} -> {}", pathToSourceFile, pathToTargetFile);
            FileUtil.copy(
                    this.fileSystem,
                    new Path(pathToSourceFile),
                    this.fileSystem,
                    new Path(pathToTargetFile),
                    false,
                    true,
                    conf);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteFolderOrFile(String path) {
        try {
            log.info("Delete: {}", path);
            this.fileSystem.delete(new Path(path), true);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void setChmod(String path) {
        try {
            log.info("Chmod for: {}", path);
            fsShell.run(new String[]{"-chmod", "-R", Environment.getConfig().getProperty("hdfs.dir.permission"), path});
            fsShell.close();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void createFolder(String path) {
        try {
            if (!this.fileSystem.exists(new Path(path))) {
                log.info("Create HDFS directory: {}", path);
                this.fileSystem.mkdirs(new Path(path));
                setChmod(path);
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public String getFileChecksum(String path) {
        String checkSum = "NONE";
        try {
            checkSum = Base64.getEncoder().encodeToString(this.fileSystem.getFileChecksum(new Path(path)).getBytes());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        return checkSum;
    }
}
