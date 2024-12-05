package com.simulator.hospital;

import com.simulator.hospital.model.entity.SimulationTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationTimeTest {
    @Test
    void testParameterizedConstructor() {
        double expectedTime = 120.5;

        SimulationTime simulationTime = new SimulationTime(expectedTime);

        assertEquals(expectedTime, simulationTime.getTime(), "The time should be set correctly by the parameterized constructor");
    }

    @Test
    void testGetAndSetTime() {
        SimulationTime simulationTime = new SimulationTime();
        double newTime = 300.75;

        simulationTime.setTime(newTime);
        assertEquals(newTime, simulationTime.getTime(), "The time getter should return the value set by the setter");
    }
}
