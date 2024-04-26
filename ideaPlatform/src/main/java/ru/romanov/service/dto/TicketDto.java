package ru.romanov.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TicketDto(String origin,
                        @JsonProperty("origin_name")
                        String originName,
                        String destination,
                        @JsonProperty("destination_name")
                        String destinationName,
                        @JsonProperty("departure_date")
                        String departureDate,
                        @JsonProperty("departure_time")
                        String departureTime,
                        @JsonProperty("arrival_date")
                        String arrivalDate,
                        @JsonProperty("arrival_time")
                        String arrivalTime,
                        String carrier,
                        int stops,
                        int price
) {
    @Override
    public String toString() {
        return "\n-----------------------------------------------------------------------------------------------" +
                "\norigin = '" + origin + '\'' +
                ", originName = '" + originName + '\'' +
                ", destination = '" + destination + '\'' +
                ", destinationName = '" + destinationName + '\'' +
                ", \ndepartureDate = '" + departureDate + '\'' +
                ", departureTime = '" + departureTime + '\'' +
                ", \narrivalDate = '" + arrivalDate + '\'' +
                ", arrivalTime = '" + arrivalTime + '\'' +
                ", \ncarrier = '" + carrier + '\'' +
                ", stops = " + stops +
                ", price = " + price;
    }
}

