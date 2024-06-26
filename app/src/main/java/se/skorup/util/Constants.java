package se.skorup.util;

import se.skorup.util.localization.Localization;

import java.awt.Color;
import java.util.regex.Pattern;

/**
 * An interface only used to move out some constants from
 * the Utils class to make it shorter.
 * */
interface Constants
{
    //Backgrounds
    /** The background color. */
    Color BACKGROUND_COLOR = new Color(64, 67, 71);

    /** The component background color. */
    Color COMPONENT_BACKGROUND_COLOR = BACKGROUND_COLOR.brighter();

    //Foregrounds
    /** The foreground color. */
    Color FOREGROUND_COLOR = (Math.random() < 0.005) ? new Color(156, 95, 9) : Color.WHITE;

    /** The color for the group names. */
    Color GROUP_NAME_COLOR = Color.RED;

    /** The color used to show selection. */
    Color SELECTED_COLOR = Color.PINK;

    /** The flashing color. */
    Color FLASH_COLOR = Color.BLUE;

    /** The MainGroup 1 color. */
    Color MAIN_GROUP_1_COLOR = Color.GREEN;

    /** The MainGroup 2 color. */
    Color MAIN_GROUP_2_COLOR = Color.CYAN;

    /** The light-green color.*/
    Color LIGHT_GREEN = new Color(194, 255, 190);

    /** The light blue color. */
    Color LIGHT_BLUE = new Color(0, 187, 255);

    /** The light red color. */
    Color LIGHT_RED = new Color(245, 37, 85);

    /** The black color. */
    Color BLACK = new Color(0, 0, 0);

    // About
    /** The version of the program. */
    String VERSION = "0.5.1 - Indev";

    /** The about-string. */
    String ABOUT = Localization.getValue("ui.info.about").formatted(VERSION);

    /** The url of the version. */
    String VERSION_URL = "https://www.skorup.se/top_secret/secret_please_dont_steal_you_will_not_gain_anything_from_this";
}
