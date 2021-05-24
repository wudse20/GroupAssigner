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
        return "%s\\.%s\\".formatted(System.getenv("APPDATA"), "GroupAssigner");
    }
}
