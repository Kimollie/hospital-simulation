package com.simulator.hospital;

import com.simulator.hospital.model.entity.ServicePointTypes;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServicePointTypesTest {
    @Test
    void testParameterizedConstructor() {
        String expectedTypeName = "Registration";
        int expectedNumberPoints = 3;

        ServicePointTypes servicePointTypes = new ServicePointTypes(expectedTypeName, expectedNumberPoints);

        assertEquals(expectedTypeName, servicePointTypes.getTypeName(), "The typeName should be set correctly by the parameterized constructor");
        assertEquals(expectedNumberPoints, servicePointTypes.getNumberPoints(), "The numberPoints should be set correctly by the parameterized constructor");
    }

    @Test
    void testGetTypeName() {
        String expectedTypeName = "General Health Exam";
        ServicePointTypes servicePointTypes = new ServicePointTypes(expectedTypeName, 2);

        assertEquals(expectedTypeName, servicePointTypes.getTypeName(), "The getTypeName method should return the correct typeName value");
    }

    @Test
    void testGetAndSetNumberPoints() {
        ServicePointTypes servicePointTypes = new ServicePointTypes();
        int newNumberPoints = 5;

        servicePointTypes.setNumberPoints(newNumberPoints);
        assertEquals(newNumberPoints, servicePointTypes.getNumberPoints(), "The numberPoints getter should return the value set by the setter");
    }

    @Test
    void testGetId() {
        ServicePointTypes servicePointTypes = new ServicePointTypes();
        assertEquals(0, servicePointTypes.getId(), "The id should be 0 by default before persistence");
    }
}
