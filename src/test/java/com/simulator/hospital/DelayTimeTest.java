package com.simulator.hospital;

import com.simulator.hospital.model.entity.DelayTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DelayTimeTest {
    private static DelayTime delayTime;

    @BeforeAll
    static void setUpBeforeClass() {
        delayTime = new DelayTime();
    }

    @Test
    void testParameterizedConstructor(){
        DelayTime delayTimeWithParam = new DelayTime(500L);
        assertEquals(
                500L,
                delayTimeWithParam.getTime(),
                "The time should be set correctly by the parameterized constructor"
        );
    }

    @Test
    void testGetAndSetTime(){
        delayTime.setTime(5000L);

        assertEquals(
                5000L,
                delayTime.getTime(),
                "The time getter should return the value set by the setter"
        );
    }
}
