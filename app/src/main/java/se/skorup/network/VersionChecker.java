package se.skorup.network;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;

/**
 * The code for checking that the latest update is used.
 * */
public class VersionChecker
{
    /**
     * Downloads the content of a website and returns
     * the contents of the URL if sucessfull wrapped
     * in an Optional, if it fails it will return
     * {@code Optional.empty}.
     *
     * @param url The URL of the target.
     * @return the content of the target, {@code Optional.empty}
     *         if it fails.
     * */
    private static Optional<String> getContentOfURL(String url)
    {
        try
        {
            var u = new URL(url);
            var sc = new Scanner(u.openStream());
            var sb = new StringBuilder();

            while (sc.hasNext())
                sb.append(sc.next());

            DebugMethods.logF(
                DebugMethods.LogType.DEBUG,
                "Grabbed data: %s",
                sb
            );

            return Optional.of(sb.toString());
        }
        catch (IOException e)
        {
            DebugMethods.logF(
                DebugMethods.LogType.ERROR,
                "Error geting version: %s",
                e.getLocalizedMessage()
            );

            return Optional.empty();
        }
    }

    public static void main(String[] args)
    {
        DebugMethods.logF(
            DebugMethods.LogType.DEBUG, "Data of %s: %s",
            Utils.VERSION_URL, getContentOfURL(Utils.VERSION_URL).orElse("FAILED")
        );
    }
}
