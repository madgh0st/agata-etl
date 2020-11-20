package com.kpmg.agata.utils.arg;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LogParserArgumentParser.class})
public class LogParserArgumentParserTest {
    private static final String VALID_APP_PROP_PATH = "valid-prop-path";
    private static final String VALID_INPUT_PATH = "valid-input-path";
    private static final String VALID_OUTPUT_PATH = "valid-ouput-path";

    private LogParserArgumentParser argParser;

    @Before
    public void setUp() {
        argParser = new LogParserArgumentParser();

        mockStatic(Files.class);
        when(Files.notExists(eq(Paths.get(VALID_APP_PROP_PATH))))
                .thenReturn(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullArgs() {
        argParser.run(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyArgs() {
        argParser.run(new String[]{});
    }


    @Test(expected = IllegalArgumentException.class)
    public void notEnoughArgs() {
        argParser.run(new String[]{VALID_APP_PROP_PATH});
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooManyArgs() {
        argParser.run(new String[]{VALID_APP_PROP_PATH, VALID_INPUT_PATH, VALID_OUTPUT_PATH, VALID_OUTPUT_PATH});
    }

    @Test
    public void validAppPropPath() {
        when(Files.notExists(any()))
                .thenReturn(false);

        argParser.run(new String[]{"valid-path", VALID_INPUT_PATH, VALID_OUTPUT_PATH});
        assertEquals("valid-path", argParser.getAppPropertiesPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAppPropPath() {
        when(Files.notExists(eq(Paths.get(VALID_APP_PROP_PATH))))
                .thenThrow(new AssertionError("you should check null before getting path"));

        argParser.run(new String[]{null, VALID_INPUT_PATH, VALID_OUTPUT_PATH});
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidAppPropPath() {
        when(Files.notExists(any()))
                .thenReturn(true);

        argParser.run(new String[]{"invalid-path", VALID_INPUT_PATH, VALID_OUTPUT_PATH});
    }

    @Test
    public void validInputLogPath() {
        when(Files.notExists(any()))
                .thenReturn(false);

        argParser.run(new String[]{VALID_APP_PROP_PATH, "valid-path", VALID_OUTPUT_PATH});
        assertEquals("valid-path", argParser.getInputLogPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullInputLogPath() {
        argParser.run(new String[]{VALID_APP_PROP_PATH, null, VALID_OUTPUT_PATH});
    }

    @Test
    public void validOutputLogPath() {
        when(Files.notExists(any()))
                .thenReturn(false);

        argParser.run(new String[]{VALID_APP_PROP_PATH, VALID_INPUT_PATH, "valid-path"});
        assertEquals("valid-path", argParser.getOutputLogPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullOutputLogPath() {
        argParser.run(new String[]{VALID_APP_PROP_PATH, VALID_INPUT_PATH, null});
    }
}
