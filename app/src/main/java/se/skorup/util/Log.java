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
        Console.setColor(ConsoleColor.RED);
        log("ERROR", message);
        Console.resetColor();
    }

    /**
     * Logs a formatted message to the console. This
     * will work like {@link java.io.PrintStream#printf System.out.printf}.
     * It uses {@link Log#debug(Object) debug(message)} with the message:
     * message.{@link String#formatted formatted}(args).
     *
     * @param message the string with format symbols.
     * @param args the arguments to the format string.
     * */
    public static void debugf(String message, Object... args)
    {
        debug(message.formatted(args));
    }

    /**
     * Logs a formatted error message to the console. This
     * will work like {@link java.io.PrintStream#printf System.out.printf}.
     * It uses {@link Log#error(Object) error(message)} with the message:
     * message.{@link String#formatted formatted}(args).
     *
     * @param message the string with format symbols.
     * @param args the arguments to the format string.
     * */
    public static void errorf(String message, Object... args)
    {
        error(message.formatted(args));
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
