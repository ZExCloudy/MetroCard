package com.example.geektrust;


import com.example.geektrust.entity.Passenger;
import com.example.geektrust.entity.MetroCard;
import com.example.geektrust.enums.PassengerType;
import com.example.geektrust.service.MetroService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MetroServiceTest {

    private final PrintStream originalOut = System.out;

    private Passenger newPassenger(PassengerType type, String cardId, int balance) {
        MetroCard card = new MetroCard(cardId, balance);
        return new Passenger(type, card);
    }

    @AfterEach
    void restoreStdOut() {
        System.setOut(originalOut);
    }

    // --- 1) Single journey: full fare, no discount, no recharge
    @Test
    void travel_singleJourney_fullFare_noDiscount() {
        MetroService service = new MetroService();
        Passenger p = newPassenger(PassengerType.ADULTS, "MC1", 500);

        service.travel(p, "CENTRAL");

        // Capture summary
        String out = captureSummary(service);

        // CENTRAL should have 200 collected, 0 discount, ADULTS 1
        assertTrue(out.contains("TOTAL_COLLECTION CENTRAL 200 0"));
        assertTrue(out.contains("PASSENGER_TYPE_SUMMARY"));
        assertTrue(out.contains("ADULTS 1"));

        // AIRPORT should be zeroed
        assertTrue(out.contains("TOTAL_COLLECTION AIRPORT 0 0"));
    }

    // --- 2) Return journey: 50% discount applied on second leg
    @Test
    void travel_returnJourney_appliesHalfDiscount() {
        MetroService service = new MetroService();
        Passenger p = newPassenger(PassengerType.ADULTS, "MC1", 600);

        service.travel(p, "CENTRAL"); // 200
        service.travel(p, "AIRPORT"); // 100 discount on base 200

        String out = captureSummary(service);

        // CENTRAL: first leg only
        assertTrue(out.contains("TOTAL_COLLECTION CENTRAL 200 0"));

        // AIRPORT: second leg with discount 100, fare 100
        assertTrue(out.contains("TOTAL_COLLECTION AIRPORT 100 100"));
        assertTrue(out.contains("ADULTS 1"));
    }

    // --- 3) Auto recharge + 2% service fee (ceil)
    @Test
    void travel_insufficientBalance_rechargesAndAddsServiceFee() {
        MetroService service = new MetroService();
        Passenger p = newPassenger(PassengerType.ADULTS, "MC3", 50);

        // Needs 200; recharge 150 -> fee = ceil(150 * 0.02) = 3
        service.travel(p, "CENTRAL");

        String out = captureSummary(service);

        // CENTRAL should collect 200 (fare) + 3 (service fee) = 203, discount 0
        assertTrue(out.contains("TOTAL_COLLECTION CENTRAL 203 0"));
        assertTrue(out.contains("ADULTS 1"));
    }

    // --- 4) Passenger counting for multiple types
    @Test
    void travel_countsPassengersByType() {
        MetroService service = new MetroService();
        Passenger a = newPassenger(PassengerType.ADULTS, "MC1", 500);
        Passenger k = newPassenger(PassengerType.KIDS, "MC2", 200);
        Passenger s = newPassenger(PassengerType.SENIOR_CITIZENS, "MC3", 200);

        service.travel(a, "CENTRAL");
        service.travel(k, "CENTRAL");
        service.travel(s, "CENTRAL");

        String out = captureSummary(service);

        assertTrue(out.contains("TOTAL_COLLECTION CENTRAL 350 0")); // 200 + 50 + 100
        assertTrue(out.contains("ADULTS 1"));
        assertTrue(out.contains("KIDS 1"));
        assertTrue(out.contains("SENIOR_CITIZENS 1"));
    }

    // --- 5) Same station twice: NO return discount
    @Test
    void travel_sameStationTwice_noReturnDiscount() {
        MetroService service = new MetroService();
        Passenger p = newPassenger(PassengerType.SENIOR_CITIZENS, "MC1", 500);

        service.travel(p, "CENTRAL"); // 100
        service.travel(p, "CENTRAL"); // 100 again, no discount

        String out = captureSummary(service);

        // CENTRAL: 200 total, 0 discount, count 2
        assertTrue(out.contains("TOTAL_COLLECTION CENTRAL 200 0"));
        assertTrue(out.contains("SENIOR_CITIZENS 2"));
    }

    // --- 6) Sorting rule: desc by count, then asc by type name
    @Test
    void printSummary_sortsByCountThenTypeName() {
        MetroService service = new MetroService();
        Passenger a1 = newPassenger(PassengerType.ADULTS, "MC1", 500);
        Passenger a2 = newPassenger(PassengerType.ADULTS, "MC2", 500);
        Passenger k1 = newPassenger(PassengerType.KIDS, "MC3", 200);
        Passenger s1 = newPassenger(PassengerType.SENIOR_CITIZENS, "MC4", 200);

        service.travel(a1, "CENTRAL");
        service.travel(a2, "CENTRAL");
        service.travel(k1, "CENTRAL");
        service.travel(s1, "CENTRAL");

        String out = captureSummary(service);

        // For CENTRAL, order should be:
        // ADULTS 2  (highest)
        // KIDS 1 and SENIOR_CITIZENSSS 1 -> tie, then alphabetical by enum name
        int idxADULTS = out.indexOf("ADULTS 2");
        int idxKIDS = out.indexOf("KIDS 1");
        int idxSenior = out.indexOf("SENIOR_CITIZENS 1");

        assertTrue(idxADULTS != -1 && idxKIDS != -1 && idxSenior != -1);
        assertTrue(idxADULTS < idxKIDS);
        assertTrue(idxKIDS < idxSenior); // "KIDS" comes before "SENIOR_CITIZENSSS"
    }

    // --- helper to capture System.out from printSummary()
    private String captureSummary(MetroService service) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        service.printSummary();
        return baos.toString().replace("\r\n", "\n");
    }
}

