package se.skorup.util;

import se.skorup.gui.dialog.Dialog;
import se.skorup.gui.dialog.MessageDialog;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utility methods that can be used to
 * */
public class Utils implements Constants
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
        return "%s %s".formatted(getCurrentDate(), getCurrentTime());
    }

    /**
     * Converts a String to a name case.
     *
     * <code>
     *  anton s -> Anton S,
     *  ANTON S -> Anton S,
     *  AnToN s -> Anton S
     *</code>
     *
     * @param input the string to be converted.
     * @return the converted string.
     * */
    public static String toNameCase(String input)
    {
        if (input.length() == 0)
            return input;

        var names = input.split("\\s+");
        var sb = new StringBuilder();

        // Removing empty strings that snuck in from the splitter.
        names = Arrays.stream(names)
                      .filter(s -> !s.trim().isEmpty())
                      .toArray(String[]::new);

        for (var name : names)
            sb.append(new StringBuilder(
                name.toLowerCase()).replace(0, 1, Character.toString(name.charAt(0)).toUpperCase()
            )).append(' ');

        return sb.toString().trim();
    }

    /**
     * Checks if a string is a valid double.
     *
     * @param str the string to be tested.
     * @return {@code true} iff str is a valid double.
     * */
    public static boolean isValidDouble(String str)
    {
        var DOUBLE_PATTERN = Pattern.compile(
        "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\d+)(\\.)?((\\d+)?)" +
            "([eE][+-]?(\\d+))?)|(\\.(\\d+)([eE][+-]?(\\d+))?)|" +
            "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
            "[pP][+-]?(\\d+)))[fFdD]?))[\\x00-\\x20]*"
        );
        return DOUBLE_PATTERN.matcher(str).matches();
    }

    /**
     * Computes base raised to the exp:th power.
     *
     * @param base the base of the power.
     * @param exp  the exponent of the power.
     * @return the mathematical value of base ^ exp.
     * */
    public static int pow(int base, int exp)
    {
        var e = Math.abs(exp);
        var val = recPow(base, e);
        return exp < 0 ? 1 / val : val;
    }

    /**
     * Recursive integer powers. O(log n)
     *
     * @param base the base of the power.
     * @param exp  the exponent of the power.
     * @return the mathematical value of base ^ exp.
     * */
    private static int recPow(int base, int exp)
    {
        if (exp == 0)
            return 1;

        if (exp == 1)
            return base;

        if (base == 1)
            return 1;

        var pow = recPow(base, exp / 2);
        return exp % 2 == 0 ? pow * pow : base * pow * pow;
    }

    /**
     * Opens a webpage in the default browser.
     *
     * @param url the url of the website.
     * */
    public static void openWebpage(String url)
    {
        try
        {
            Desktop.getDesktop().browse(new URI(url));
            Log.networkf("Opening website: %s", url);
        }
        catch (IOException | URISyntaxException ex)
        {
            Log.error(ex);
            MessageDialog.create()
                         .setLocalizedTitle("ui.error.web-browser")
                         .setInformation("Kunde inte öppna webläsaren!\nFel: %s".formatted(ex))
                         .setLocalizedButtonText("ui.button.dialog.close")
                         .show(Dialog.ERROR_MESSAGE);
        }
    }

    /**
     * Checks if the code is running on a window system or not.
     *
     * @return {@code true} iff it's running on a Windows system.
     * */
    public static boolean isWindowsSystem()
    {
        return System.getProperty("os.name").startsWith("Windows");
    }

    /**
     * Gets the folder name of this project,
     * were it saves all its files. <br><br>
     *
     * Ends with '\'.
     *
     * @return the folder location.
     * */
    public static String getFolderName()
    {
        if (isWindowsSystem())
        {
            Log.debug("Is windows system");
            return "%s\\.%s\\".formatted(System.getenv("APPDATA"), "group-assigner");
        }
        else
        {
            Log.debug("Is not windows");
            return "./.group_assigner/";
        }
    }

    /**
     * Downloads the content of a website and returns
     * the contents of the URL if successful wrapped
     * in an Optional, if it fails it will return
     * {@code Optional.empty}.
     *
     * @param url The URL of the target.
     * @return the content of the target, {@code Optional.empty}
     *         if it fails.
     * */
    public static Optional<String> getContentOfURL(String url)
    {
        try
        {
            var u = URI.create(url).toURL();
            var sc = new Scanner(u.openStream());
            var sb = new StringBuilder();

            while (sc.hasNext())
                sb.append(sc.next());

            Log.networkf("Grabbed data: %s", sb);

            return Optional.of(sb.toString());
        }
        catch (IOException e)
        {
            Log.errorf("Error getting version: %s", e.getLocalizedMessage());

            return Optional.empty();
        }
    }
}
