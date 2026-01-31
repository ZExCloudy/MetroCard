package com.example.geektrust.entity;

import com.example.geektrust.enums.PassengerType;
public class Passenger {

    private PassengerType type;
    private MetroCard metroCard;
    private String lastStation;

    public Passenger(PassengerType type, MetroCard metroCard) {
        this.type = type;
        this.metroCard = metroCard;
    }

    public String getLastStation() {
        return lastStation;
    }

    public void setLastStation(String lastStation) {
        this.lastStation = lastStation;
    }

    public PassengerType getType() {
        return type;
    }

    public MetroCard getMetroCard() {
        return metroCard;
    }
}
