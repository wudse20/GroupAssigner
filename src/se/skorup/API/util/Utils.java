package se.skorup.API.util;

import se.skorup.API.util.DebugMethods;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
    public static final Color FOREGROUND_COLOR = (Math.random() < 0.01) ? new Color(156, 95, 9) : Color.WHITE;

    /** The color for the group names. */
    public static final Color GROUP_NAME_COLOR = Color.RED;

    /** The color used to show selection. */
    public static final Color SELECTED_COLOR = Color.PINK;

    /** The flashing color. */
    public static final Color FLASH_COLOR = Color.BLUE;

    // About
    /** The version of the program. */
    public static final String VERSION = "0.3.0.2";

    /** The about string. */
    public static final String ABOUT =
            "Gruppskapare v.%s\n\nUtvecklare: Anton Skorup\nIdè:   Sebastian Wahlberg".formatted(VERSION);

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
     * Opens the help pages: https://www.help.skorup.se/
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
}
