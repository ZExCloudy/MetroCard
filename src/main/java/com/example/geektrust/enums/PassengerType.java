package com.example.geektrust.enums;

public enum PassengerType {
    ADULTS(200),
    SENIOR_CITIZENS(100),
    KIDS(50);

    private final int fare;

    PassengerType(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }
}
