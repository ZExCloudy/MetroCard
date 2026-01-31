package com.example.geektrust.entity;

public class MetroCard {
    private final String cardNumber;
    private double balance;

    public MetroCard(String cardNumber, double balance) {
        this.cardNumber = cardNumber;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deduct(double amount) {
        balance -= amount;
    }

    public double recharge(double amount) {
        double serviceFee = amount * 0.02;
        balance += amount;
        return balance;
    }
}

