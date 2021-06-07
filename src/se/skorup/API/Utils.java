package se.skorup.API;

import java.awt.Color;

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
    public static final Color FOREGROUND_COLOR = Color.WHITE;

    // About
    /** The version of the program. */
    public static final String VERSION = "0.2-INDEV";

    /** The about string. */
    public static final String ABOUT =
            "Gruppskapare v.%s\n\nUtvecklare: Anton Skorup\nIdè:   Sebastian Wahlberg".formatted(VERSION);

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

        if (System.getProperty("os.name").startsWith("Windows"))
        {
            DebugMethods.log("Is windows system", DebugMethods.LogType.DEBUG);
            return "%s\\.%s\\".formatted(System.getenv("APPDATA"), "group-assigner");
        }
        else
        {
            DebugMethods.log("Is not windows", DebugMethods.LogType.DEBUG);
            return "./.group_assigner";
        }
    }
}
