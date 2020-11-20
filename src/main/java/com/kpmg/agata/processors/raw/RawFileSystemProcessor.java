package com.kpmg.agata.processors.raw;

import com.kpmg.agata.config.Environment;
import com.kpmg.agata.models.JsonDeltaModel;
import com.kpmg.agata.models.fs.CheckSumModel;
import com.kpmg.agata.utils.filesystem.HDFSFacade;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.kpmg.agata.constant.database.BaseConstants.CHECK_SUM;
import static com.kpmg.agata.constant.database.BaseConstants.PARQUET;
import static com.kpmg.agata.config.Environment.getSchemaPrefix;

public class RawFileSystemProcessor {

    private static Logger log = LoggerFactory.getLogger(RawFileSystemProcessor.class);

    private HDFSFacade hdfsFacade;
    private SparkSession sparkSession;
    private String hdfsDirJsonDeltas;
    private String hdfsDirResultJson;
    private static final String PATTERN_DATE = "/(\\d{4}-\\d{2}-\\d{2})/";
    private List<String> availableDateListXls;
    private List<Path> listFilesRecursivelyForDirSrcXls;

    public RawFileSystemProcessor(String hdfsDirJsonDeltas, String hdfsDirSrcXls, String hdfsDirResultJson, SparkSession sparkSession) {
        this.hdfsFacade = new HDFSFacade();
        this.sparkSession = sparkSession;
        hdfsFacade.createFolder(hdfsDirJsonDeltas);
        hdfsFacade.createFolder(hdfsDirResultJson);

        this.hdfsDirJsonDeltas = hdfsDirJsonDeltas;
        this.hdfsDirResultJson = hdfsDirResultJson;
        this.availableDateListXls = hdfsFacade.getAvailableDateForPath(hdfsDirSrcXls);
        this.listFilesRecursivelyForDirSrcXls = hdfsFacade.getFilesRecursively(hdfsDirSrcXls);
    }

    public List<String> getAvailableDateListXls() {
        return availableDateListXls;
    }

    private String getSourceCategory(String path) {
        String pattern = Environment.getConfig().getProperty("hdfs.dir.src.xls") + "\\d{4}-\\d{2}-\\d{2}/(.*?)/";
        Pattern regexPattern = Pattern.compile(pattern);

        Matcher m = regexPattern.matcher(path);
        return m.find() ? m.group(1) : "";
    }

    private List<CheckSumModel> getListCheckSumModel() {
        return listFilesRecursivelyForDirSrcXls
                .parallelStream()
                .map(file -> new CheckSumModel(
                                file.toString(),
                                file.getName(),
                                hdfsFacade.getFileChecksum(file.toString()),
                                getSourceCategory(file.toString())
                        )
                )
                .collect(Collectors.toList());
    }

    public void createTableCheckSumToHive() {
        sparkSession
                .sqlContext()
                .createDataFrame(getListCheckSumModel(), CheckSumModel.class)
                .toDF()
                .write()
                .format(PARQUET)
                .mode(SaveMode.Overwrite)
                .option("path", Environment.getConfig().getProperty("hdfs.dir.parquet.aggregate") + CHECK_SUM)
                .saveAsTable(getSchemaPrefix() + CHECK_SUM);
    }

    public void removeInconsistentFilesJsonDeltas() {
        List<String> availableDateJsonDeltas = hdfsFacade.getAvailableDateForPath(hdfsDirJsonDeltas);
        List<String> availableDateXls = availableDateListXls;

        availableDateJsonDeltas.removeAll(availableDateXls);

        if (!availableDateJsonDeltas.isEmpty()) {
            log.info("Remove inconsistent date from {} for date: {}",
                    hdfsDirJsonDeltas, String.join(",", availableDateJsonDeltas)
            );
            availableDateJsonDeltas
                    .stream()
                    .map(date -> hdfsDirJsonDeltas + date)
                    .collect(Collectors.toList())
                    .forEach(oldDir -> hdfsFacade.deleteFolderOrFile(oldDir));
        } else {
            log.info("All date for {}} is valid", hdfsDirJsonDeltas);
        }
    }

    public void removeDirsJsonDeltasAndResultJson() {
        hdfsFacade.deleteFolderOrFile(hdfsDirJsonDeltas);
        hdfsFacade.deleteFolderOrFile(hdfsDirResultJson);
        hdfsFacade.createFolder(hdfsDirJsonDeltas);
        hdfsFacade.createFolder(hdfsDirResultJson);
    }

    public void removeDirsJsonDeltas(List<String> dateList) {
        dateList
                .parallelStream()
                .forEach(date -> {
                    String hdfsDirJsonDeltasWithDate = hdfsDirJsonDeltas + date;
                    hdfsFacade.deleteFolderOrFile(hdfsDirJsonDeltasWithDate);
                    hdfsFacade.createFolder(hdfsDirJsonDeltasWithDate);

                });
    }

    public void removeFilesInResultJsons(List<String> dateList) {

        List<Path> listFilesRecursivelyForResultJson = hdfsFacade.getFilesRecursively(hdfsDirResultJson);
        dateList
                .parallelStream()
                .forEach(date -> listFilesRecursivelyForResultJson
                        .stream()
                        .filter(x -> x.getName().startsWith(date))
                        .forEach(x -> hdfsFacade.deleteFolderOrFile(x.toString())));
    }

    private List<JsonDeltaModel> getListFormattedPathForJsonResult() {

        return hdfsFacade.getFilesRecursively(hdfsDirJsonDeltas)
                .stream()
                .map(path -> {
                    String fullPathToJsonDeltasFile = path.toString();
                    String nameJsonDeltasFile = path.getName();
                    Matcher m = Pattern.compile(PATTERN_DATE).matcher(fullPathToJsonDeltasFile);
                    String dateMatch = m.find() ? m.group(1) : "";

                    String targetName = dateMatch + "_" + nameJsonDeltasFile;

                    String targetPath = fullPathToJsonDeltasFile
                            .replaceAll(PATTERN_DATE, "/result_jsons/")
                            .replace("json_deltas/", "")
                            .replace(nameJsonDeltasFile, "");

                    return new JsonDeltaModel(fullPathToJsonDeltasFile, nameJsonDeltasFile, targetPath, targetName, dateMatch);

                })
                .collect(Collectors.toList());
    }

    public void copyJsonDeltasToResultDir(List<String> dateList) {

        List<JsonDeltaModel> getListFormattedPathForJsonResult = getListFormattedPathForJsonResult();

        // create folders structure
        getListFormattedPathForJsonResult
                .stream()
                .map(JsonDeltaModel::getTargetPath)
                .distinct()
                .forEach(targetpath -> hdfsFacade.createFolder(targetpath));

        // copy jsons to result_jsons folder
        dateList
                .parallelStream()
                .forEach(date -> getListFormattedPathForJsonResult
                        .parallelStream()
                        .filter(x -> x.getDateMatch().equals(date))
                        .forEach(mdl ->
                                hdfsFacade.copyFiles(
                                        mdl.getFullPathToJsonDeltasFile(),
                                        mdl.getTargetPath() + mdl.getTargetName()
                                )
                        ));
    }
}
