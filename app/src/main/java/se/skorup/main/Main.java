package se.skorup.main;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.manager.helper.SerializationManager;
import se.skorup.version.VersionChecker;
import se.skorup.version.VersionCheckerSettings;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;

/**
 * The class responsible for starting the program,
 * i.e. the main class.
 * */
public class Main
{
    private static final String MESSAGE =
        "<html>Vill du att programmet ska kolla ifall det har släppts en ny version " +
        "varje gång det startar? <br>Det som krävs för att detta skall fungera är " +
        "en internetuppkoppling. Det enda som den kommer att göra är en förfrågan " +
        "till en webserver. <br> Websevern skickar den senaste versionen och därefter" +
        "kollar programmet lokalt ifall versionerna stämmer överrens. <br> Skulle de " +
        "inte stämma överrens så kommer den att fråga ifall du vill ladda ned den nyare " +
        "versionen. <br><br> Allt den kommer hämta är innehållet av denna fil: " +
        "%s<br> Namnet är lite av ett skämt...".formatted(Utils.VERSION_URL);
    /**
     * The main method starts the program.
     *
     * @param args the args passed to the command line.
     * */
    public static void main(String[] args)
    {
        new Main().startProgram();
    }

    /**
     * The method that does everything to start
     * the program.
     * */
    private void startProgram()
    {
        var path = Utils.getFolderName() + "settings/version_checking.data";
        var settingsFile = new File(path);

        if (settingsFile.exists())
        {
            var shouldCheck = shouldCheckVersion(settingsFile);

            if (shouldCheck)
                VersionChecker.checkVersion(); // Checks the version, if allowed.
        }
        else
        {
            var ans = JOptionPane.showConfirmDialog(
                null, MESSAGE, "Versionskontroll",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE
            );

            VersionCheckerSettings set;
            if (ans == JOptionPane.YES_OPTION)
            {
                set = new VersionCheckerSettings(true);
                VersionChecker.checkVersion();
            }
            else
            {
                set =  new VersionCheckerSettings(false);
            }

            try
            {
                SerializationManager.serializeObject(path, set);
            }
            catch (IOException e)
            {
                DebugMethods.errorF("Fel: %s", e.getLocalizedMessage());
            }
        }

        SwingUtilities.invokeLater(MainFrame::new); // Starts program.
    }

    /**
     * Reads the file and checks if it should check for the version.
     *
     * @param f the file that stores the user's choice.
     * @return {@code true} if it should check for it,
     *         {@code false} if it shouldn't check for it.
     * */
    private boolean shouldCheckVersion(File f)
    {
        try
        {
            var set = (VersionCheckerSettings) SerializationManager.deserializeObject(f.getAbsolutePath());
            return set.shouldCheck();
        }
        catch (IOException | ClassNotFoundException e)
        {
            return false;
        }
    }

}
