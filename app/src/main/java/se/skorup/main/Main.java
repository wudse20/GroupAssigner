package se.skorup.main;

import se.skorup.gui.dialog.ConfirmDialog;
import se.skorup.gui.dialog.Dialog;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.main.gui.main.frames.MainFrame;
import se.skorup.util.Log;
import se.skorup.util.Utils;
import se.skorup.util.io.SerializationUtil;
import se.skorup.util.resource.ResourceLoader;

import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;

/**
 * The main class and the main entry point for the program.
 * */
public class Main
{
    /** The path to the version check. */
    private static final String VERSION_PATH = "%sversion".formatted(Utils.getFolderName());

    /**
     * The main method.
     *
     * @param args the arguments sent to this program are ignored.
     * */
    public static void main(String[] args)
    {
        loadResources();
        versionCheck();
        SwingUtilities.invokeLater(MainFrame::new);
    }

    /**
     * Does a version check.
     * */
    private static void versionCheck()
    {
        if (Utils.VERSION.toLowerCase().contains("indev"))
            return;

        var permission = false;
        if (!new File(VERSION_PATH).exists()) // We need to ask permission.
        {
            permission = ConfirmDialog.create()
                                      .setLocalizedQuestion("ui.question.version-checking")
                                      .setLocalizedApproveButtonText("ui.button.dialog.approve")
                                      .setLocalizedDisapproveButtonText("ui.button.dialog.disapprove")
                                      .setLocalizedTitle("ui.title.version-checking")
                                      .show();

            try
            {
                SerializationUtil.serializeObject(VERSION_PATH, permission);
            }
            catch (IOException e)
            {
                Log.errorf("Filed to save permission: %s", e.getLocalizedMessage());
            }
        }
        else
        {
            try
            {
                permission = SerializationUtil.deserializeObject(VERSION_PATH);
            }
            catch (IOException | ClassNotFoundException e)
            {
                Log.errorf("Filed to load permission: %s", e.getLocalizedMessage());
                Log.error("Skips version check...");
                return;
            }
        }

        if (!permission)
        {
            Log.debug("No version check, we don't have user permission.");
            return;
        }

        var version = Utils.getContentOfURL(Utils.VERSION_URL);

        if (version.isPresent())
        {
            var ver = version.get();
            if (!ver.equalsIgnoreCase(Utils.VERSION))
            {
                MessageDialog.create()
                             .setLocalizedTitle("ui.title.wrong-version")
                             .setLocalizedInformationf("ui.info.wrong-version", ver, Utils.VERSION)
                             .setLocalizedButtonText("ui.button.dialog.close")
                             .show(Dialog.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Loads the resources from the Jar-file.
     * */
    private static void loadResources()
    {
        var rl = ResourceLoader.getBuilder()
                               .initLangFile("SV_se.lang")
                               .loadIcons()
                               .build();

        try
        {
            rl.loadResources();
        }
        catch (IOException e)
        {
            Log.error(e);
        }
    }
}
