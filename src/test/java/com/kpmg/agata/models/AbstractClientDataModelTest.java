package com.kpmg.agata.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.test.utils.TestUtils;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Map;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertTrue;

public class AbstractClientDataModelTest {

    @Test
    public void allChildrenHasSameSerialization() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();

        TestUtils.getClasses("com.kpmg.agata.models")
                 .stream()
                 .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                 .filter(AbstractClientDataModel.class::isAssignableFrom)
                 .map(Whitebox::newInstance)
                 .map(obj -> mapper.convertValue(obj, Map.class))
                 .forEach(map -> {
                     assertTrue(map.containsKey("fileName"));
                     assertTrue(map.containsKey("sheetName"));
                     assertTrue(map.containsKey("modificationDate"));
                     assertTrue(map.containsKey("loadDate"));
                     assertTrue(map.containsKey("code"));
                     assertTrue(map.containsKey("eventDate"));
                     assertTrue(map.containsKey("name_do"));
                 });
    }
}
