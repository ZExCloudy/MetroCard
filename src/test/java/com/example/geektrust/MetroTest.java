package com.example.geektrust;

import com.example.geektrust.entity.MetroCard;
import com.example.geektrust.entity.Passenger;
import com.example.geektrust.entity.Station;
import com.example.geektrust.enums.PassengerType;
import com.example.geektrust.service.MetroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MetroTest {

    private MetroService metroService;
    private Passenger passenger;
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

    @Test
    void shouldDeductBalanceWhenEnoughAmount() {
        MetroCard card = new MetroCard("MC1", 500);

        card.deduct(200);

        assertEquals(300, card.getBalance());
    }

    @Test
    void shouldRechargeWhenBalanceInsufficient() {
        MetroCard card = new MetroCard("MC2", 50);

        double recharge = card.recharge(200);

        assertEquals(250, recharge);
        assertEquals(250, card.getBalance());
    }


    @Test
    void shouldThrowExceptionForInvalidPassengerType() {
        assertThrows(IllegalArgumentException.class, () -> {
            PassengerType.valueOf("INVALID");
        });
    }
    @BeforeEach
    void setUp() {
       metroService = new MetroService();
        MetroCard card = new MetroCard("MC1", 100);
    passenger = new Passenger(PassengerType.ADULTS, card);
    }

    @Test
    void shouldTravelAndDeductFare() {
        metroService.travel(passenger, "CENTRAL");

        assertEquals(0, passenger.getMetroCard().getBalance());
    }

    @Test
    void shouldApplyDiscountForReturnJourney() {
        metroService.travel(passenger, "CENTRAL");
        passenger.setLastStation("CENTRAL");
        passenger.getMetroCard().recharge(100);

        metroService.travel(passenger, "AIRPORT");

        assertEquals(0, passenger.getMetroCard().getBalance());
    }

    @Test
    void shouldRechargeWhenBalanceIsInsufficient() {
        passenger.getMetroCard().deduct(90); // Reduce balance to 10
        metroService.travel(passenger, "CENTRAL");

        assertEquals(0, passenger.getMetroCard().getBalance());
    }




}

