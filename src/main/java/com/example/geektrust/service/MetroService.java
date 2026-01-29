package com.example.geektrust.service;

import com.example.geektrust.entity.Passenger;
import com.example.geektrust.entity.Station;
import com.example.geektrust.enums.JourneyType;
import com.example.geektrust.enums.PassengerType;

import java.util.*;

public class MetroService {

    private final Map<String, Station> stations = new HashMap<>();

    public MetroService() {
        stations.put("CENTRAL", new Station());
        stations.put("AIRPORT", new Station());
    }

    public void travel(Passenger passenger, JourneyType journeyType, String stationName) {

        Station station = stations.get(stationName);
        PassengerType type = passenger.getType();

        int baseFare = type.getFare();
        int discount = 0;

        if (journeyType == JourneyType.RETURN && !passenger.isReturnUsed()) {
            discount = baseFare / 2;
            passenger.markReturnUsed();
        }

        int finalFare = baseFare - discount;

        if (passenger.getMetroCard().getBalance() < finalFare) {
            double rechargeAmount =
                    finalFare - passenger.getMetroCard().getBalance();
            int serviceFee = (int) Math.ceil(rechargeAmount * 0.02);

            passenger.getMetroCard().recharge(rechargeAmount);
            station.addCollection(serviceFee);
        }

        passenger.getMetroCard().deduct(finalFare);

        station.addCollection(finalFare);
        station.addDiscount(discount);
        station.addPassenger(type);
    }

    public void printSummary() {

        for (String stationName : List.of("CENTRAL", "AIRPORT")) {

            Station station = stations.get(stationName);

            System.out.println(
                    "TOTAL_COLLECTION " + stationName + " " +
                            station.getTotalCollection() + " " +
                            station.getTotalDiscount()
            );

            System.out.println("PASSENGER_TYPE_SUMMARY");

            station.getPassengerCount()
                    .entrySet()
                    .stream()
                    .sorted((a, b) -> {
                        int cmp = b.getValue() - a.getValue();
                        return cmp != 0
                                ? cmp
                                : a.getKey().name().compareTo(b.getKey().name());
                    })
                    .forEach(e ->
                            System.out.println(e.getKey() + " " + e.getValue())
                    );
        }
    }
}
