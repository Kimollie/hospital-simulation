package com.simulator.hospital;

import com.simulator.hospital.model.entity.Intervals;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntervalsTest {
    @Test
    void testParameterizedConstructor() {
        String expectedType = "Service";
        String expectedCategory = "Health";
        double expectedTime = 15.5;

        Intervals intervals = new Intervals(expectedType, expectedCategory, expectedTime);

        assertEquals(expectedType, intervals.getType(), "The type should be set correctly by the parameterized constructor");
        assertEquals(expectedCategory, intervals.getCategory(), "The category should be set correctly by the parameterized constructor");
        assertEquals(expectedTime, intervals.getTime(), "The time should be set correctly by the parameterized constructor");
    }

    @Test
    void testGetAndSetTime() {
        Intervals intervals = new Intervals();
        double newTime = 20.0;

        intervals.setTime(newTime);
        assertEquals(newTime, intervals.getTime(), "The time getter should return the value set by the setter");
    }

    @Test
    void testGetId() {
        Intervals intervals = new Intervals();
        assertEquals(0, intervals.getId(), "The id should be 0 by default before persistence");
    }

    @Test
    void testGetType() {
        String expectedType = "arrival";
        Intervals intervals = new Intervals(expectedType, "Arrival", 10.0);

        assertEquals(expectedType, intervals.getType(), "The getType method should return the correct type value");
    }

    @Test
    void testGetCategory() {
        String expectedCategory = "Registration";
        Intervals intervals = new Intervals("service", expectedCategory, 10.0);

        assertEquals(expectedCategory, intervals.getCategory(), "The getCategory method should return the correct category value");
    }
}
