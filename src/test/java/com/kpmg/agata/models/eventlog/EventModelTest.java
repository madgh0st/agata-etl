package com.kpmg.agata.models.eventlog;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanCreditCorrectionModel;
import com.kpmg.agata.models.clean.CleanPaymentFromCustomerModel;
import com.kpmg.agata.test.utils.TestUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EventModelTest {

    @Test
    public void dataFieldSupportsAllSubTypes() throws Exception {
        List<Class<?>> expected = TestUtils.getClasses("com.kpmg.agata.models.clean")
                                           .stream()
                                           .filter(clazz -> clazz != AbstractCleanDataModel.class)
                                           .sorted(Comparator.comparing(Class::getSimpleName))
                                           .collect(Collectors.toList());

        JsonSubTypes.Type[] data = Whitebox.getField(EventModel.class, "data")
                                           .getAnnotation(JsonSubTypes.class)
                                           .value();
        List<? extends Class<?>> actual = Arrays.stream(data)
                                                .map(JsonSubTypes.Type::value)
                                                .sorted(Comparator.comparing(Class::getSimpleName))
                                                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    /**
     * Correct name is class name without "Clean" prefix and "Model" postfix
     * CleanGenprocModel -> Genproc
     */
    @Test
    public void allDataSubTypesHasCorrectName() {
        JsonSubTypes.Type[] types = Whitebox.getField(EventModel.class, "data")
                                            .getAnnotation(JsonSubTypes.class)
                                            .value();

        for (JsonSubTypes.Type type : types) {
            String cleanClassName = type.value()
                                        .getSimpleName()
                                        .replaceFirst("^Clean", "")     // starts with "Clean"
                                        .replaceFirst("Model$", "");    // ends with "Model"
            assertEquals(type.name(), cleanClassName);
        }
    }

    @Test
    public void serializeThenDeserializeTest() throws IOException {
        CleanCreditCorrectionModel sourceModel = new CleanCreditCorrectionModel();
        sourceModel.setAffectsAccounting("content");
        EventModel sourceEventModel = new EventModel("code",
                "name_do",
                java.sql.Date.valueOf("2019-03-12"),
                sourceModel);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(sourceEventModel);

        EventModel actualEventModel = mapper.readValue(json, EventModel.class);

        assertEquals(sourceEventModel, actualEventModel);
        assertEquals("CreditCorrection", actualEventModel.getEventType());
        CleanCreditCorrectionModel actualModel = (CleanCreditCorrectionModel) actualEventModel.getData();
        assertEquals("content", actualModel.getAffectsAccounting());
    }

    @Test
    public void serializeBoolTest() throws IOException {
        CleanPaymentFromCustomerModel sourceModel = new CleanPaymentFromCustomerModel();
        sourceModel.setDeleted(true);
        EventModel sourceEventModel = new EventModel("code",
                "name_do",
                java.sql.Date.valueOf("2019-03-12"),
                sourceModel);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(sourceEventModel);

        EventModel actualEventModel = mapper.readValue(json, EventModel.class);
        assertEquals(sourceEventModel, actualEventModel);
    }

    @Test
    public void deserializeBoolTest() throws IOException {
        CleanPaymentFromCustomerModel sourceModel = new CleanPaymentFromCustomerModel();
        sourceModel.setDeleted(true);
        EventModel sourceEventModel = new EventModel("code",
                "name_do",
                java.sql.Date.valueOf("2019-03-12"),
                sourceModel);

        ObjectMapper mapper = new ObjectMapper();
        String jsonEqualsOne =
                "{\"code\":\"code\",\"name_do\":\"name_do\",\"eventType\":\"PaymentFromCustomer\",\"eventDate\":\"2019-03-12\",\"data\":{\"eventType\":\"PaymentFromCustomer\",\"deleted\":1}}";

        String jsonEqualsMoreOne =
                "{\"code\":\"code\",\"name_do\":\"name_do\",\"eventType\":\"PaymentFromCustomer\",\"eventDate\":\"2019-03-12\",\"data\":{\"eventType\":\"PaymentFromCustomer\",\"deleted\":10}}";

        String jsonEqualsNegative =
                "{\"code\":\"code\",\"name_do\":\"name_do\",\"eventType\":\"PaymentFromCustomer\",\"eventDate\":\"2019-03-12\",\"data\":{\"eventType\":\"PaymentFromCustomer\",\"deleted\":-10}}";

        String jsonEqualsZero =
                "{\"code\":\"code\",\"name_do\":\"name_do\",\"eventType\":\"PaymentFromCustomer\",\"eventDate\":\"2019-03-12\",\"data\":{\"eventType\":\"PaymentFromCustomer\",\"deleted\":0}}";


        assertEquals(sourceEventModel, mapper.readValue(jsonEqualsOne, EventModel.class));
        assertEquals(sourceEventModel, mapper.readValue(jsonEqualsMoreOne, EventModel.class));
        assertEquals(sourceEventModel, mapper.readValue(jsonEqualsNegative, EventModel.class));
        assertNotEquals(sourceEventModel, mapper.readValue(jsonEqualsZero, EventModel.class));
    }
}
