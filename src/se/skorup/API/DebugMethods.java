package se.skorup.API;

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
        System.out.printf("[%s/%s] %s%n", getCurrentTime(), type, message);
    }

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
}
