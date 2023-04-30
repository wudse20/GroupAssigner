package se.skorup.version;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;

import javax.swing.JOptionPane;

import static se.skorup.API.util.Utils.VERSION;
import static se.skorup.API.util.Utils.getContentOfURL;

/**
 * The code for checking that the latest update is used.
 * */
public class VersionChecker
{
    /** The message in the text box. */
    private static final String MESSAGE =
            "<html>Du har inte den senaste versionen!<br>" +
            "<p>Din version: <p color='red'>%s</p></p>" +
            "<p>Senaste versionen: <p color='green'>%s</p></p></html>";

    /** The fail message. */
    private static final String FAILED = "Huston, we have a problem! AKA FAILED TO GET DATA";

    /**
     * Checks the version of the program.
     * */
    public static void checkVersion()
    {
        var ver = getContentOfURL(Utils.VERSION_URL).orElse(FAILED);

        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Current version: %s", VERSION);
        DebugMethods.logF(DebugMethods.LogType.NETWORK, "Newest version: %s", ver);

        if (VERSION.toUpperCase().contains("INDEV"))
        {
            DebugMethods.log("Indev version no check!", DebugMethods.LogType.DEBUG);
            return;
        }

        if (ver.equalsIgnoreCase(FAILED))
        {
            DebugMethods.log("Failed to get version!", DebugMethods.LogType.ERROR);
            return;
        }

        if (ver.equalsIgnoreCase(VERSION))
        {
            DebugMethods.log("We have a version match!", DebugMethods.LogType.DEBUG);
            return;
        }

        var data = JOptionPane.showConfirmDialog(
            null, MESSAGE.formatted(VERSION, ver),
            "Varning gamal version", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );

        if (data == JOptionPane.YES_OPTION)
        {
            Utils.openWebpage("https://www.skorup.se/download/");
            DebugMethods.log("Opening download website", DebugMethods.LogType.NETWORK);
            DebugMethods.log("Closing application", DebugMethods.LogType.DEBUG);
            System.exit(0);
        }

        DebugMethods.log("User wants to use old version!", DebugMethods.LogType.DEBUG);
    }

    /**
     * Runs a test to get content of a URL.
     *
     * @param args the passed arguments to the program.
     * */
    public static void main(String[] args)
    {
        DebugMethods.logF(
            DebugMethods.LogType.DEBUG, "Data of %s: %s",
            Utils.VERSION_URL, getContentOfURL(Utils.VERSION_URL).orElse("FAILED")
        );
    }
}
