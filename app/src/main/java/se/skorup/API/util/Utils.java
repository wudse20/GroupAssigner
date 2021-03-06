package se.skorup.API.util;

import se.skorup.API.collections.immutable_collections.ImmutableArray;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Some util methods
 * */
public class Utils
{
    //Backgrounds
    /** The background color. */
    public static final Color BACKGROUND_COLOR = new Color(64, 67, 71);

    /** The component background color. */
    public static final Color COMPONENT_BACKGROUND_COLOR = BACKGROUND_COLOR.brighter();

    //Foregrounds
    /** The foreground color. */
    public static final Color FOREGROUND_COLOR = (Math.random() < 0.005) ? new Color(156, 95, 9) : Color.WHITE;

    /** The color for the group names. */
    public static final Color GROUP_NAME_COLOR = Color.RED;

    /** The color used to show selection. */
    public static final Color SELECTED_COLOR = Color.PINK;

    /** The flashing color. */
    public static final Color FLASH_COLOR = Color.BLUE;

    /** The MainGroup 1 color. */
    public static final Color MAIN_GROUP_1_COLOR = Color.GREEN;

    /** The MainGroup 2 color. */
    public static final Color MAIN_GROUP_2_COLOR = Color.CYAN;

    // About
    /** The version of the program. */
    public static final String VERSION = "0.4.2";

    /** The about string. */
    public static final String ABOUT =
            "Gruppskapare v.%s\n\nUtvecklare: Anton Skorup\nIdè:   Sebastian Wahlberg".formatted(VERSION);

    /** The regex pattern for a double. Stolen from: https://stackoverflow.com/a/16078719 */
    private static final Pattern DOUBLE_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\d+)(\\.)?((\\d+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\d+))([eE][+-]?(\\d+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\d+)))[fFdD]?))[\\x00-\\x20]*");

    /**
     * Checks if the code is running on a window system or not.
     *
     * @return {@code true} iff it's runing on a windows system.
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
            DebugMethods.log("Is windows system", DebugMethods.LogType.DEBUG);
            return "%s\\.%s\\".formatted(System.getenv("APPDATA"), "group-assigner");
        }
        else
        {
            DebugMethods.log("Is not windows", DebugMethods.LogType.DEBUG);
            return "./.group_assigner/";
        }
    }

    /**
     * Opens the help pages: <a href="https://www.help.skorup.se/">help.skorup.se</a>
     * */
    public static void openHelpPages()
    {
        try
        {
            Desktop.getDesktop().browse(new URI("https://www.help.skorup.se/"));
        }
        catch (IOException | URISyntaxException ex)
        {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                null, "Kunde inte öppna webläsaren!\nFel: %s".formatted(ex),
                "Kunde inte öppna webläsaren!", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Converts a String to name case.
     *
     * anton s -> Anton S
     * ANTON S -> Anton S
     * AnToN s -> Anton S
     *
     * @param input the string to be converted.
     * @return the converted string.
     * */
    public static String toNameCase(String input)
    {
        var names = input.split(" ");
        var sb = new StringBuilder();

        for (var name : names)
            sb.append(new StringBuilder(
                name.toLowerCase()).replace(0, 1, Character.toString(name.charAt(0)).toUpperCase()
            )).append(' ');

        return sb.toString().trim();
    }

    /**
     * Writes a string to a file.
     *
     * @param data the data to be written.
     * @param file the file to be written to.
     * */
    public static void writeToFile(String data, File file)
    {
        BufferedWriter bw = null;

        try
        {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(data);
        }
        catch (IOException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.ERROR);
            DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

            JOptionPane.showMessageDialog(
                null, "Kunde inte spara filen!\nFel: %s".formatted(e.getLocalizedMessage()),
                "Sparning misslyckades", JOptionPane.ERROR_MESSAGE
            );
        }
        finally
        {
            try
            {
                if (bw != null)
                    bw.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if a string is a valid double.
     *
     * @param str the string to be tested.
     * @return {@code true} iff str is a valid double.
     * */
    public static boolean isValidDouble(String str)
    {
        return DOUBLE_PATTERN.matcher(str).matches();
    }

    /**
     * Checks if a character is a digit.
     *
     * @param c the char to be tested.
     * @return {@code true} iff it is a digit.
     * */
    public static boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    /**
     * Checks if a string is a valid integer
     *
     * @param str the string to be tested.
     * @return {@code true} iff str is a valid integer.
     * */
    public static boolean isValidInteger(String str)
    {
        return ImmutableArray.fromCollection(str.chars().boxed().toList())
                             .forAll(c -> Utils.isDigit((char) (int) c));
    }

    /**
     * The map with all colors of tags.
     * The key is the name of the color.
     *
     * @return the map with all the colors.
     * */
    public static HashMap<String, Color> colorMap()
    {
        var _colorMap = new HashMap<String, Color>();

        _colorMap.put("LIGHT_GREEN", new Color(194, 255, 190));
        _colorMap.put("GREEN", Color.GREEN);
        _colorMap.put("DARK_GREEN", Color.GREEN.darker());
        _colorMap.put("WHITE", Color.WHITE);
        _colorMap.put("BLUE", Color.BLUE);
        _colorMap.put("DARK_BLUE", Color.BLUE.darker());
        _colorMap.put("LIGHT_BLUE", new Color(0, 187, 255));
        _colorMap.put("IDEA_PURPLE", new Color(208, 86, 154));
        _colorMap.put("PURPLE", new Color(117, 50, 168));
        _colorMap.put("LIGHT_PURPLE", new Color(161, 79, 224));
        _colorMap.put("YELLOW", Color.YELLOW);
        _colorMap.put("LIGHT_RED", new Color(245, 37, 85));
        _colorMap.put("RED", Color.RED);
        _colorMap.put("DARK_RED", Color.RED.darker());

        return _colorMap;
    }

    /**
     * Pads a string to a specified length. If
     * {@code org.length() >= length} then the
     * org will be return. So if we have a length
     * that is larger or equal to the legnth of the
     * string then it won't do anything.
     *
     * @param org the String to be padded.
     * @param pad the char to pad with.
     * @param length the new length of the String.
     * @return A string padded to length: length
     *         if {@code org.length() >= length}
     *         then it will send the original
     *         string back.
     * */
    public static String padString(String org, char pad, int length)
    {
        if (org.length() >= length)
            return org;

        return org + Character.toString(pad).repeat(length - org.length());
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
     * Formats a string to print all evenly divisible sizes with a header.
     * It will create HTML-code with a color if a color is passed as it will
     * give the p-element a color attribute of the passed color.
     *
     * @param header the header of the string.
     * */
    public static String formatSizeString(String header, Optional<String> color, int size)
    {
        var sb = new StringBuilder().append("<html>");

        if (color.orElse("").trim().length() != 0)
            sb.append("<p color=\"")
              .append(color.get())
              .append("\">")
              .append(header)
              .append(' ');
        else
            sb.append("<p>")
              .append(header)
              .append(' ');

        var count = 0;
        for (int i = 1; i <= size; i++)
        {
            if (size % i == 0)
            {
                sb.append(i)
                  .append(", ");
                count++;
            }
        }

        if (count == 0)
            return sb.append("0</p></html>").toString();

        return sb.delete(sb.length() - 2, sb.length()).append("</p></html>").toString();
    }
}
