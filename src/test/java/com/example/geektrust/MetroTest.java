package com.example.geektrust;

import com.example.geektrust.entity.Station;
import com.example.geektrust.enums.PassengerType;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MetroTest {

    @Test
    void shouldReturnPassengerCountCorrectly() {

        Station station = new Station();

        station.addPassenger(PassengerType.ADULTS);
        station.addPassenger(PassengerType.ADULTS);
        station.addPassenger(PassengerType.KIDS);

        Map<PassengerType, Integer> result = station.getPassengerCount();

        assertEquals(2, result.get(PassengerType.ADULTS));
        assertEquals(1, result.get(PassengerType.KIDS));
    }

    @Test
    void shouldHandleEmptyPassengerList() {

        Station station = new Station();

        Map<PassengerType, Integer> result = station.getPassengerCount();

        assertTrue(result.isEmpty() || result.values().stream().allMatch(v -> v == 0));
    }

    @Test
    void shouldIncreaseCountWhenMultiplePassengersAdded() {

        Station station = new Station();

        station.addPassenger(PassengerType.SENIOR_CITIZENS);
        station.addPassenger(PassengerType.SENIOR_CITIZENS);
        station.addPassenger(PassengerType.SENIOR_CITIZENS);

        Map<PassengerType, Integer> result = station.getPassengerCount();

        assertEquals(3, result.get(PassengerType.SENIOR_CITIZENS));
    }

    @Test
    void shouldNotBreakIfPassengerTypeNotAdded() {

        Station station = new Station();

        station.addPassenger(PassengerType.ADULTS);

        Map<PassengerType, Integer> result = station.getPassengerCount();

        assertNull(result.get(PassengerType.KIDS));
    }

    @Test
    void shouldHandleNullPassengerTypeGracefully() {

        Station station = new Station();


        assertThrows(NullPointerException.class, () -> {
            station.addPassenger(null);
        });
    }
}

