package com.example.geektrust.service;

import com.example.geektrust.entity.Passenger;
import com.example.geektrust.entity.Station;
import com.example.geektrust.enums.PassengerType;

import java.util.*;

public class MetroService {

    private final Map<String, Station> stations = new HashMap<>();

    public MetroService() {
        stations.put("CENTRAL", new Station());
        stations.put("AIRPORT", new Station());
    }
    public void travel(Passenger passenger, String stationName) {

        Station station = stations.get(stationName);
        PassengerType type = passenger.getType();

        int baseFare = type.getFare();
        int discount = 0;

        if (passenger.getLastStation() != null &&
                !passenger.getLastStation().equals(stationName)) {

            discount = baseFare / 2;
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

        passenger.setLastStation(stationName);
    }

    public void printSummary() {

        for (String stationName : List.of("CENTRAL", "AIRPORT")) {

            Station station = stations.get(stationName);

            System.out.println(
                    "TOTAL_COLLECTION " + stationName + " " +
                            station.getTotalFareCollected() + " " +
                            station.getTotalDiscount()
            );

            System.out.println("PASSENGER_TYPE_SUMMARY");

            Map<PassengerType, Integer> p = station.getPassengerCount();
            List<Map.Entry<PassengerType, Integer>> list =
                    new ArrayList<>(p.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<PassengerType, Integer>>() {
                @Override
                public int compare(Map.Entry<PassengerType, Integer> e1,
                                   Map.Entry<PassengerType, Integer> e2) {

                    if (!e1.getValue().equals(e2.getValue())) {
                        return e2.getValue() - e1.getValue();
                    }

                    return e1.getKey().name().compareTo(e2.getKey().name());
                }
            });

            for (Map.Entry<PassengerType, Integer> entry : list) {
                System.out.println(entry.getKey().name() + " " + entry.getValue());
            }


        }
    }
}
