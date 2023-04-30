package se.skorup.util;

/**
 * Some logging utils.
 * */
public class Log
{
    /** You should not be able to instantiate a class of this object. */
    private Log() {}

    /**
     * Logs a debug message to the console.
     *
     * @param <T> the type of the message.
     * @param message the message.
     * */
    public static <T> void debug(T message)
    {
        log("DEBUG", message);
    }

    /**
     * Logs an error-message to the console.
     *
     * @param <T> the type of the message.
     * @param message the message.
     * */
    public static <T> void error(T message)
    {
        log("ERROR", message);
    }

    /**
     * Logs a message to the console.
     *
     * @param <T> The type of the message.
     * @param type the type of the message.
     * @param message the message.
     * */
    private static <T> void log(String type, T message)
    {
        System.out.printf("[%s/%s] %s%n", Utils.getCurrentDateAndTime(), type, message == null ? "null" : message);
    }
}
