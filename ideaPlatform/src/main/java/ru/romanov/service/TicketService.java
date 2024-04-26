package ru.romanov.service;

import org.apache.commons.math3.stat.descriptive.rank.Median;
import ru.romanov.service.dto.TicketDto;
import ru.romanov.util.TimeUtil;
import ru.romanov.service.mapper.TicketMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.romanov.util.TimeUtil.durationInMillis;
import static ru.romanov.util.TimeUtil.millisToHHMMSS;

public class TicketService {
    private final TicketMapper ticketMapper;

    public TicketService(String jsonFilePath) {
        this.ticketMapper = new TicketMapper(jsonFilePath);
    }

    /**
     * Convert and put all tickets in list
     *
     * @return List of all tickets
     */
    public List<TicketDto> getAllTickets() throws IOException {
        return ticketMapper.jsonToListDto();
    }

    /**
     * Stream selects only the specified ones from all flights and finds the average and median cost for the filtered list.
     * <br>{@link Median#evaluate()}(from apache.commons.math3) - find median price for flight.<br/>
     *
     * @param tickets list of all tickets
     * @param origin  departure point
     * @param dest    arrival point
     * @return difference average and median price for flight from origin to dest point
     */
    public double avgMedianDiffForCarrier(List<TicketDto> tickets, String origin, String dest) {
        double[] filteredPriceArray = tickets.parallelStream()
                .filter(obj -> origin.equals(obj.origin()) && dest.equals(obj.destination()))
                .mapToDouble(TicketDto::price).toArray();

        Median median = new Median();
        double medianPrice = median.evaluate(filteredPriceArray);
        double averagePrice = Arrays.stream(filteredPriceArray).average().getAsDouble();

        return averagePrice - medianPrice;
    }

    /**
     * <b>Collects a pair of carrier and min flight time in ms<b/>
     * <br>{@link TimeUtil#durationInMillis(LocalDateTime, LocalDateTime)} - converts and gets flight time in ms.<br/>
     * <br>{@link TimeUtil#millisToHHMMSS(Long)} - Converts the resulting duration to HH:MM:SS.<br/>
     * <br>Then collects a pair: origin, minFlightTime.<br/>
     *
     * @param tickets list of all tickets from json
     * @return String containing the min flight time for each type of carrier
     */
    public String minFlightTimeForCarrier(List<TicketDto> tickets, String origin, String dest) {
        StringBuilder sb = new StringBuilder();
        Map<String, Long> minFlightTime = new TreeMap<>();
        for (TicketDto ticket : tickets) {
            LocalDateTime departure = convertToLocalDateTime(ticket.departureDate(), ticket.departureTime());
            LocalDateTime arrival = convertToLocalDateTime(ticket.arrivalDate(), ticket.arrivalTime());

            Long flightTime = durationInMillis(departure, arrival);// Time in ms

            if (isOriginAndDestCorrect(origin, dest, ticket)
                    && (newCarrierOrOldWithBetterTime(minFlightTime, ticket, flightTime)))
                minFlightTime.put(ticket.carrier(), flightTime);
        }

        minFlightTime.forEach((key, value) -> sb.append("\n").append(key).append(": ").append(millisToHHMMSS(value)));
        return sb.toString();
    }

    /**
     * Convert & join departureDate and departureTime to LocalDateTime
     * @return Joined LocalDateTime
     */
    private static LocalDateTime convertToLocalDateTime(String jsonStringDate, String jsonStringTime) {
        var dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        var timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        LocalDate date = LocalDate.parse(jsonStringDate, dateFormatter);
        LocalTime time = LocalTime.parse(jsonStringTime, timeFormatter);

        return date.atTime(time);
    }

    /**
     * Condition.
     * <br>Does a carrier with this name exist in minFlightTime<br/>
     * <br>OR, if exists but has a shorter flight time<br/>
     */
    private static boolean newCarrierOrOldWithBetterTime(Map<String, Long> minFlightTime, TicketDto ticket, Long flightTime) {
        return !minFlightTime.containsKey(ticket.carrier())
                || flightTime < minFlightTime.get(ticket.carrier());
    }

    /**
     * Condition.
     * <br>Does the ticket contain the required origin and destination<br/>
     */
    private static boolean isOriginAndDestCorrect(String origin, String dest, TicketDto ticket) {
        return ticket.origin().equals(origin) && ticket.destination().equals(dest);
    }
}