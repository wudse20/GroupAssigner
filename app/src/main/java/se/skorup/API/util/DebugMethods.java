package se.skorup.API.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Some debug methods used in the program.
 * */
public class DebugMethods
{
    /**
     * The different log types.
     * */
    public enum LogType
    {
        /** The error status. */
        ERROR,
        /** The debug status. */
        DEBUG
    }

    /**
     * Logs a formatted message to the console.
     *
     * @param message the message.
     * @param type the type of the message.
     * */
    public static void log(String message, LogType type)
    {
        var oldColor = Console.getColor();
        if (type.equals(LogType.ERROR))
            Console.setColor(ConsoleColor.RED);

        System.out.printf("[%s/%s] %s%n", getCurrentTime(), type, message);

        Console.setColor(oldColor);
    }

    /**
     * Logs a formatted message to the console.
     *
     * @param o the object to be logged.
     * @param type the type of the message.
     * */
    public static void log(Object o, LogType type)
    {
        log(o.toString(), type);
    }

    /**
     * Logs a formatted message to the console.
     *
     * @param i the number to be logged.
     * @param type the type of the message.
     * */
    public static void log(int i, LogType type)
    {
        log(Integer.toString(i), type);
    }

    /**
     * Logs a formatted message to the console. This
     * will work like {@link java.io.PrintStream#printf System.out.printf}.
     * It uses {@link DebugMethods#log log(message, type)} with the message:
     * fString.{@link String#formatted formatted}(args).
     *
     * @param type The type of message.
     * @param fString the string with format symbols.
     * @param args the arguments to the fString.
     * */
    public static void logF(LogType type, String fString, Object... args)
    {
        log(fString.formatted(args), type);
    }

    /**
     * Logs a blank message.
     *
     * @param type the type of the log.
     * */
    public static void log(LogType type)
    {
        DebugMethods.log("", type);
    }

    /**
     * Logs an error to the console.
     *
     * @param message the message of the error.
     * */
    public static void error(String message)
    {
        log(message, LogType.ERROR);
    }

    /**
     * Logs an error to the console. It utilizes
     * {@link String#formatted(Object...) formatted} to
     * use for formatting, so it has to abide by its laws.
     *
     * @param fString the string with the inserted format
     *                messages.
     * @param args the arguments to the formatting.
     * */
    public static void errorF(String fString, Object... args)
    {
        logF(LogType.ERROR, fString, args);
    }

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
