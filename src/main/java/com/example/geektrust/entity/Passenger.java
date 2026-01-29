package com.example.geektrust.entity;

import com.example.geektrust.enums.PassengerType;

public class Passenger {
    private final PassengerType type;
    private final MetroCard metroCard;
    private boolean returnUsed = false;

    public Passenger(PassengerType type, MetroCard metroCard) {
        this.type = type;
        this.metroCard = metroCard;
    }

    public PassengerType getType() {
        return type;
    }

    public MetroCard getMetroCard() {
        return metroCard;
    }

    public boolean isReturnUsed() {
        return returnUsed;
    }

    public void markReturnUsed() {
        this.returnUsed = true;
    }
}
