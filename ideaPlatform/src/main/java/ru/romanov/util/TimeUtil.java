package ru.romanov.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * <h3>Time Util methods for Ticket Service<h3/>
 */
public class TimeUtil {
    private TimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * <b>Convert millis to HH:mm:SS format<b/>
     *
     * @param flightTime
     * @return String time format
     */
    public static String millisToHHMMSS(Long flightTime) {
        long hours = TimeUnit.MILLISECONDS.toHours(flightTime);
        long minutes = (TimeUnit.MILLISECONDS.toMinutes(flightTime) % 60);
        long seconds = (TimeUnit.MILLISECONDS.toSeconds(flightTime) % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * <b>Join date and time of arrival and departure.
     * <br>Calculates flight duration in millis</br><b/>
     *
     * @param departure departure dateTime
     * @param arrival   arrival dateTime
     * @return flight time in millis
     */
    public static Long durationInMillis(LocalDateTime departure, LocalDateTime arrival) {
        var flightTime = Duration.between(departure, arrival);
        return flightTime.toMillis();
    }
}
