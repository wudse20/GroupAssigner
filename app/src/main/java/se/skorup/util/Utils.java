package se.skorup.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Utility methods that can be used to
 * */
public class Utils
{
    /** You should not be able to instantiate a class of this object. */
    private Utils() {}

    /**
     * Gets the current time and formats it into a string.
     *
     * @return a formatted string of the current time.
     * */
    public static String getCurrentTime()
    {
        var h = (LocalTime.now().getHour() < 10) ?
                "0" + LocalTime.now().getHour()  :
                Integer.toString(LocalTime.now().getHour());

        var m = (LocalTime.now().getMinute() < 10) ?
                "0" + LocalTime.now().getMinute()  :
                Integer.toString(LocalTime.now().getMinute());

        var s = (LocalTime.now().getSecond() < 10) ?
                "0" + LocalTime.now().getSecond()  :
                Integer.toString(LocalTime.now().getSecond());

        return "%s:%s:%s".formatted(h, m, s);
    }

    /**
     * Formats a string to the current date
     *
     * @return the formatted date.
     * */
    public static String getCurrentDate()
    {
        String year = Integer.toString(LocalDateTime.now().getYear());

        String month = (LocalDateTime.now().getMonthValue() < 10) ?
                "0" + LocalDateTime.now().getMonthValue() :
                Integer.toString(LocalDateTime.now().getMonthValue());

        String day = (LocalDateTime.now().getDayOfMonth() < 10) ?
                "0" + LocalDateTime.now().getDayOfMonth() :
                Integer.toString(LocalDateTime.now().getDayOfMonth());

        return "%s-%s-%s".formatted(year, month, day);
    }

    /**
     * Produces a string with the current date and time.
     *
     * @return  the formatted date and time.
     * */
    public static String getCurrentDateAndTime()
    {
        return "%s--%s".formatted(getCurrentDate(), getCurrentTime());
    }
}
