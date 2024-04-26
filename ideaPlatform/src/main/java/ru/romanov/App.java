package ru.romanov;

import ru.romanov.service.TicketService;
import ru.romanov.service.dto.TicketDto;


import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger log = Logger.getLogger(String.valueOf(App.class));

    public static void main(String[] args) throws IOException {
        TicketService ticketService = new TicketService("src/main/resources/tickets.json");
        String origin = "VVO";
        String dest = "TLV";

        List<TicketDto> tickets = ticketService.getAllTickets();
        double diffAvgPriceAndMedian = ticketService.avgMedianDiffForCarrier(tickets, origin, dest);
        String minFlightTimeForCarrier = ticketService.minFlightTimeForCarrier(tickets, origin, dest);

        String separator = "\n";
        String sb = String.format("List of all tickets: %s%s",
                tickets, separator) +
                String.format("Difference between average price and median for flight between %s and %s: %.2f%s",
                        origin, dest, diffAvgPriceAndMedian, separator) +
                String.format("Minimum flight time for each carrier between %s and %s: %s%s",
                        origin, dest, minFlightTimeForCarrier, separator);

        log.log(Level.INFO, sb);
    }
}
