package com.kpmg.agata.test.utils;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.kpmg.agata.config.Environment;
import com.kpmg.agata.models.AbstractClientDataModel;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

public class TestUtils {

    public static <T> boolean checkOrder(List<T> source, List<T> sequence) {
        List<T> sequenceCopy = new ArrayList<>(sequence);
        if (sequenceCopy.isEmpty()) return true;

        for (T item : source) {
            if (sequenceCopy.get(0).equals(item)) {
                sequenceCopy.remove(0);
                if (sequenceCopy.isEmpty()) return true;
            }
        }
        return false;
    }

    public static <T> boolean checkAllBefore(List<T> source, List<T> firstItems, T lastItem) {
        List<T> firstItemsCopy = new ArrayList<>(firstItems);

        for (T item : source) {
            firstItemsCopy.remove(item);

            if (lastItem.equals(item)) {
                return firstItemsCopy.isEmpty();
            }
        }
        return false;
    }

    public static <T> boolean checkAllAfter(List<T> source, T firstItem, List<T> lastItems) {
        List<T> lastItemsCopy = new ArrayList<>(lastItems);

        boolean firstItemFound = false;
        for (T item : source) {
            if (firstItemFound) {
                lastItemsCopy.remove(item);
                if (lastItemsCopy.isEmpty()) return true;
            } else {
                firstItemFound = firstItem.equals(item);
            }
        }
        return lastItemsCopy.isEmpty();
    }

    public static <T extends Throwable> void assertException(Class<T> expected, Runnable method) {
        try {
            method.run();
        } catch (Throwable actual) {
            if (actual.getClass().equals(expected)) return;
            fail(String.format("Expected: %s; Actual: %s", expected.getName(), actual));
        }
        fail(String.format("Expected: %s; Actual: run without exceptions", expected.getName()));
    }

    public static void assertEqualsOrNull(Object expected, Object actual) {
        if (expected == null) throw new IllegalArgumentException("Expected value cannot be null");
        if (actual == null) return;
        assertEquals(expected, actual);
    }

    public static <T extends AbstractClientDataModel> void mockSequenceWriterInFileProcessor(List<T> reports,
                                                                                             Class<T> clazz)
            throws Exception {
        ObjectMapper mapper = mock(ObjectMapper.class);
        ObjectMapper originalMapper = new ObjectMapper();
        whenNew(ObjectMapper.class)
                .withNoArguments()
                .thenReturn(mapper);
        when(mapper.convertValue(any(), eq(clazz)))
                .then(invocation -> originalMapper.convertValue(invocation.getArguments()[0], clazz));
        ObjectWriter objectWriter = mock(ObjectWriter.class);
        when(mapper.writer((PrettyPrinter) any()))
                .thenReturn(objectWriter);
        SequenceWriter sequenceWriter = mock(SequenceWriter.class);
        when(objectWriter.writeValues((Writer) any()))
                .thenReturn(sequenceWriter);
        doAnswer(invocation -> writeReport(invocation, reports))
                .when(sequenceWriter)
                .write(any());
    }

    @SuppressWarnings("unchecked cast")
    private static <T extends AbstractClientDataModel> Object writeReport(InvocationOnMock invocation,
                                                                          List<T> reports) {
        T model = (T) invocation.getArguments()[0];
        reports.add(model);
        return null;
    }

    public static void loadTestConfig() {
        Environment.setConfig("src/test/resources/application-test.properties");
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     */
    public static List<Class<?>> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private static List<Class<?>> findClasses(File directory, String packageName)
            throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) throw new IOException();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
