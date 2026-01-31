package com.example.geektrust;

import com.example.geektrust.entity.MetroCard;
import com.example.geektrust.entity.Passenger;
import com.example.geektrust.enums.JourneyType;
import com.example.geektrust.enums.PassengerType;
import com.example.geektrust.service.MetroService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Map<String, MetroCard> metroCard = new HashMap<>();
        Map<String, Passenger> passengers = new HashMap<>();

        MetroService metroService = new MetroService();

        try {
            FileInputStream fis = new FileInputStream(args[0]);
            Scanner sc = new Scanner(fis);

            while (sc.hasNextLine()) {

                String line = sc.nextLine();
                String[] tokens = line.split(" ");

                switch (tokens[0]) {

                    case "BALANCE": {
                        String cardId = tokens[1];
                        int balance = Integer.parseInt(tokens[2]);

                        metroCard.put(
                                cardId,
                                new MetroCard(cardId, balance)
                        );
                        break;
                    }
                    case "CHECK_IN": {
                        String cardId = tokens[1];
                        PassengerType passengerType = PassengerType.valueOf(tokens[2]);
                        String station = tokens[3];

                        Passenger passenger = passengers.get(cardId);

                        if (passenger == null) {
                            passenger = new Passenger(
                                    passengerType,
                                    metroCard.get(cardId)
                            );
                            passengers.put(cardId, passenger);
                        }


                        metroService.travel(passenger, station);
                        break;
                    }

                    case "PRINT_SUMMARY": {
                        metroService.printSummary();
                        break;
                    }
                }
            }

            sc.close();
        } catch (IOException e) {
            // Geektrust expects no output on exception
        }
    }
}
