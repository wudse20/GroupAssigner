package se.skorup.main;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.manager.helper.SerializationManager;
import se.skorup.version.VersionChecker;
import se.skorup.version.VersionCheckerSettings;
import se.skorup.version.gui.VersionCheckerTermsFrame;

import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;

/**
 * The class responsible for starting the program,
 * i.e. the main class.
 * */
public class Main
{
    private static final String VERSION_PATH = Utils.getFolderName() + "settings/version_checking.data";

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
        var settingsFile = new File(VERSION_PATH);

        if (settingsFile.exists())
        {
            var shouldCheck = shouldCheckVersion(settingsFile);

            if (shouldCheck)
                VersionChecker.checkVersion(); // Checks the version, if allowed.
        }
        else
        {
            final var mon = new VersionCheckerMonitor();

            SwingUtilities.invokeLater(() -> {
                new VersionCheckerTermsFrame(shouldCheck -> {
                    var set = new VersionCheckerSettings(shouldCheck);
                    new Thread(() -> { // No running this on the EDT.
                        try
                        {
                            SerializationManager.serializeObject(VERSION_PATH, set);
                        }
                        catch (IOException e)
                        {
                            DebugMethods.errorF("Fel: %s", e.getLocalizedMessage());
                        }
                        finally
                        {
                            mon.reportFinished();
                        }
                    }, "Serializer").start();
                });
            });

            try
            {
                mon.awaitAnswer();
                var shouldCheck = shouldCheckVersion(new File(VERSION_PATH));

                if (shouldCheck)
                    VersionChecker.checkVersion();
            }
            catch (InterruptedException unexpected)
            {
                // Should never happen, but I want an error
                // if it by some unknown reason happens.
                throw new RuntimeException(unexpected);
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

    /**
     * A simple monitor to wait for the user's answer.
     * */
    private static class VersionCheckerMonitor
    {
        private boolean shouldWait = true;

        /**
         * Awaits an answer from the user.
         *
         * @throws InterruptedException iff the thread is interrupted.
         * */
        private synchronized void awaitAnswer() throws InterruptedException
        {
            while (shouldWait)
                wait();
        }

        /**
         * Reports finished to the monitor and
         * wakes the waiting threads.
         * */
        private synchronized void reportFinished()
        {
            shouldWait = false;
            notifyAll(); // Could probably be the method notify, but no risks taken here.
        }
    }
}
