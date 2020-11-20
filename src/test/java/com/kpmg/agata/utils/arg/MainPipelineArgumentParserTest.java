package com.kpmg.agata.utils.arg;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MainPipelineArgumentParser.class})
public class MainPipelineArgumentParserTest {

    private static final String VALID_DATE_KEY = "all";
    private static final String VALID_STEP = "raw-files";
    private static final String VALID_RAW_FILES_CONF_PATH = "valid-conf-path";
    private static final String VALID_APP_PROP_PATH = "valid-prop-path";

    private MainPipelineArgumentParser argParser;

    @Before
    public void setUp() {
        argParser = new MainPipelineArgumentParser();

        mockStatic(Files.class);
        when(Files.notExists(eq(Paths.get(VALID_RAW_FILES_CONF_PATH))))
                .thenReturn(false);
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
        argParser.run(new String[]{VALID_DATE_KEY, VALID_STEP, VALID_RAW_FILES_CONF_PATH});
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooManyArgs() {
        argParser.run(new String[]{VALID_DATE_KEY, VALID_STEP, VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH,
                VALID_APP_PROP_PATH});
    }

    @Test
    public void validDateKey() {
        argParser.run(new String[]{"all", VALID_STEP, VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH});
        assertEquals("all", argParser.getDateKey());

        argParser.run(new String[]{"latest", VALID_STEP, VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH});
        assertEquals("latest", argParser.getDateKey());

        argParser.run(new String[]{"1970-01-01", VALID_STEP, VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH});
        assertEquals("1970-01-01", argParser.getDateKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateKey() {
        argParser.run(new String[]{null, VALID_STEP, VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH});
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidDateKey() {
        argParser.run(new String[]{"invalid date key", VALID_STEP, VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH});
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidDateInKeyDate() {
        argParser.run(new String[]{"9999-99-99", VALID_STEP, VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH});
    }

    @Test
    public void validStep() {
        List<String> steps = Whitebox.getInternalState(MainPipelineArgumentParser.class, "POSSIBLE_STEPS");
        assertFalse(steps.isEmpty());

        for (String step : steps) {
            argParser.run(new String[]{VALID_DATE_KEY, step, VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH});
            assertEquals(step, argParser.getStep());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStep() {
        argParser.run(new String[]{VALID_DATE_KEY, null, VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH});
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidStep() {
        argParser.run(new String[]{VALID_DATE_KEY, "invalid step", VALID_RAW_FILES_CONF_PATH, VALID_APP_PROP_PATH});
    }


    @Test
    public void validRawFilesConfPath() {
        when(Files.notExists(any()))
                .thenReturn(false);

        argParser.run(new String[]{VALID_DATE_KEY, VALID_STEP, "valid-path", VALID_APP_PROP_PATH});
        assertEquals("valid-path", argParser.getRawFilesConfigPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRawFilesConfPath() {
        when(Files.notExists(eq(Paths.get(VALID_RAW_FILES_CONF_PATH))))
                .thenThrow(new AssertionError("you should check null before getting path"));

        argParser.run(new String[]{VALID_DATE_KEY, VALID_STEP, null, VALID_APP_PROP_PATH});
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRawFilesConfPath() {
        when(Files.notExists(any()))
                .thenReturn(true);

        argParser.run(new String[]{VALID_DATE_KEY, VALID_STEP, "invalid-path", VALID_APP_PROP_PATH});
    }

    @Test
    public void validAppPropPath() {
        when(Files.notExists(any()))
                .thenReturn(false);

        argParser.run(new String[]{VALID_DATE_KEY, VALID_STEP, VALID_RAW_FILES_CONF_PATH, "valid-path"});
        assertEquals("valid-path", argParser.getAppPropertiesPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAppPropPath() {
        when(Files.notExists(eq(Paths.get(VALID_APP_PROP_PATH))))
                .thenThrow(new AssertionError("you should check null before getting path"));

        argParser.run(new String[]{VALID_DATE_KEY, VALID_STEP, VALID_RAW_FILES_CONF_PATH, null});
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidAppPropPath() {
        when(Files.notExists(any()))
                .thenReturn(true);

        argParser.run(new String[]{VALID_DATE_KEY, VALID_STEP, VALID_RAW_FILES_CONF_PATH, "invalid-path"});
    }
}
