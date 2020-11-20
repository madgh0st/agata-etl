package com.kpmg.agata.processors.logs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.utils.filesystem.IFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParserProcessor {

    private Long countLine = 0L;
    private String line = "";
    private boolean isConsole = true;
    private boolean isContainer = false;
    private String lastDateTime = "";
    private String lastLogLevel = "";
    private String className = "";
    private long numBlock = 0;
    private boolean isError;

    private static Logger log = LoggerFactory.getLogger(LogParserProcessor.class);
    private IFileSystem fileSystem;
    private static final String LOGLEVEL = "ALL|DEBUG|INFO|WARN|ERROR|FATAL|OFF|TRACE";
    private final Pattern regexPatternConsole = Pattern.compile(String.format(
            "^(\\d{2}.\\d{2}.\\d{2}.\\d{2}:\\d{2}:\\d{2}) (%s).*", LOGLEVEL)
    );
    private final Pattern regexPatternContainer = Pattern.compile(String.format(
            "^AGATA: (\\d{4}-\\d{2}-\\d{2}.\\d{2}:\\d{2}:\\d{2},\\d{3}) (%s).*(com.kpmg.*?):\\d.*? -", LOGLEVEL)
    );

    public LogParserProcessor(IFileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    private String getMatchedGroup(String line, Pattern regexPattern, Integer numGroup) {
        String result = "";
        try {
            Matcher m = regexPattern.matcher(line);
            result = m.find() ? m.group(numGroup).trim() : "";
        } catch (Exception ex) {
            log.info("RegExp is invalid for line:{}", line);
        }
        return result;
    }

    public String getDateTimeConsole(String line) {
        String rawDate = getMatchedGroup(line, regexPatternConsole, 1);
        String formattedDate;
        try {
            formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new SimpleDateFormat("yy/MM/dd HH:mm:ss")
                            .parse(rawDate)
                    );
        } catch (ParseException e) {
            formattedDate = "";
        }
        return formattedDate;
    }

    public String getDateTimeContainer(String line) {
        String rawDate = getMatchedGroup(line, regexPatternContainer, 1);
        String formattedDate;
        try {
            formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS")
                            .parse(rawDate)
                    );
        } catch (ParseException e) {
            formattedDate = "";
        }
        return formattedDate;
    }

    public String getLogLevelConsole(String line) {
        return getMatchedGroup(line, regexPatternConsole, 2);
    }

    public String getLogLevelContainer(String line) {
        return getMatchedGroup(line, regexPatternContainer, 2);
    }

    public String getClassNameContainer(String line) {
        return getMatchedGroup(line, regexPatternContainer, 3);
    }

    public void writeToFile(String inputFile, String outputFile) {
        ObjectMapper mapper = new ObjectMapper();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(fileSystem.reader(inputFile),
                        StandardCharsets.UTF_8));
                Writer wr = fileSystem.writer(outputFile)
        ) {
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) {
                    continue;
                } else {
                    countLine++;
                }
                processLine();

                OutputLogsModel mdl = new OutputLogsModel(
                        inputFile,
                        isConsole ? "console" : "container",
                        countLine,
                        isError,
                        lastDateTime,
                        lastLogLevel,
                        className,
                        line,
                        numBlock
                );
                wr.write(mapper.writeValueAsString(mdl) + "\n");
            }
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", inputFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void processLine() {
        if (line.contains("Container: container_")) {
            isContainer = true;
            isConsole = false;
            lastDateTime = "";
            lastLogLevel = "";
            className = "";
        }

        isError = line.contains("EXCEPTION") || line.contains("ERROR");

        if (isConsole && !getDateTimeConsole(line).equals("")) {
            lastDateTime = getDateTimeConsole(line);
            lastLogLevel = getLogLevelConsole(line);
            numBlock++;
        }

        if (isContainer && !getDateTimeContainer(line).equals("")) {
            lastDateTime = getDateTimeContainer(line);
            lastLogLevel = getLogLevelContainer(line);
            className = getClassNameContainer(line);
            numBlock++;
        }
    }
}
