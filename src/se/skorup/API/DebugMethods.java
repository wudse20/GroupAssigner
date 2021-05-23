package se.skorup.API;

import java.time.LocalDateTime;

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
        ERROR, DEBUG
    }

    /**
     * Logs a formatted message to the console.
     *
     * @param message the message.
     * @param type the type of the message.
     * */
    public static void log(String message, LogType type)
    {
        System.out.println("[%s/%s] %s".formatted(getCurrentTime(), type, message));
    }

    public static String getCurrentTime()
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
}
