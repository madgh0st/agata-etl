package com.kpmg.agata.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(MockitoJUnitRunner.class)
public class EventLogDriverTest {

    @Test
    public void getConfigurationTest() {
        Configuration originConfig = new Configuration();
        originConfig.set("a.key1", "value1");
        originConfig.set("a.key2", "value2");
        originConfig.set("key1", "previousValue");
        originConfig.set("b.key1", "value3");

        EventLogDriver driver = PowerMockito.spy(new EventLogDriver());
        PowerMockito.when(driver.getConf()).thenReturn(originConfig);

        Configuration configuration = null;
        try {
            configuration = Whitebox.invokeMethod(driver, "getConfigurationForJob", "a");
        } catch (Exception e) {
            fail("Expected invocation without any exceptions");
        }

        final String defaultValue = "defaultValue";

        // Ensure that values are set
        assertEquals("value1", Optional.of(configuration.get("key1")).orElse(defaultValue));
        assertEquals("value2", Optional.of(configuration.get("key2")).orElse(defaultValue));

        // Other values not removed
        assertEquals("value3", Optional.of(configuration.get("b.key1")).orElse(defaultValue));
    }

}
