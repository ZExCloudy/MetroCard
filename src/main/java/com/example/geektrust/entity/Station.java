package com.example.geektrust.entity;

import com.example.geektrust.enums.PassengerType;
import java.util.HashMap;
import java.util.Map;

public class Station {

    private int totalFareCollected;
    private int totalDiscount;
    private final Map<PassengerType, Integer> passengers = new HashMap<>();

    public void addCollection(int amount) {
        totalFareCollected += amount;
    }

    public void addDiscount(int discount) {
        totalDiscount += discount;
    }

    public void addPassenger(PassengerType type) {
        if(type == null) {
            throw new NullPointerException();
        }
        passengers.put(type,
                passengers.getOrDefault(type, 0) + 1);
    }

    public int getTotalFareCollected() {
        return totalFareCollected;
    }

    public int getTotalDiscount() {
        return totalDiscount;
    }

    public Map<PassengerType, Integer> getPassengerCount () {
        return passengers;
    }
}
