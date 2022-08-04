package se.skorup.network;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;

import static se.skorup.API.util.Utils.getContentOfURL;

/**
 * The code for checking that the latest update is used.
 * */
public class VersionChecker
{


    public static void main(String[] args)
    {
        DebugMethods.logF(
            DebugMethods.LogType.DEBUG, "Data of %s: %s",
            Utils.VERSION_URL, getContentOfURL(Utils.VERSION_URL).orElse("FAILED")
        );
    }
}
