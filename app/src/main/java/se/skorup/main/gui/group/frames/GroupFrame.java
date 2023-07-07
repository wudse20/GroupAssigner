package se.skorup.main.gui.group.frames;

import se.skorup.group.Group;
import se.skorup.gui.components.containers.Frame;
import se.skorup.gui.dialog.Dialog;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.main.gui.group.panels.GroupPanel;
import se.skorup.main.gui.main.frames.MainFrame;
import se.skorup.util.Log;
import se.skorup.util.Utils;
import se.skorup.util.io.EncryptedSerializationUtil;
import se.skorup.util.io.SerializationUtil;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The frame responsible for handling a group.
 * */
public class GroupFrame extends Frame
{
    /** The path where the save file lies. */
    public static final String SAVES_PATH = "%ssaves/save.data".formatted(Utils.getFolderName());

    private List<Group> groups = new ArrayList<>();

    private final MainFrame mf;

    private final GroupPanel gp;

    /**
     * Creates a new GroupFrame.
     *
     * @param mf the instance of the MainFrame in use
     * */
    public GroupFrame(MainFrame mf)
    {
        super("ui.title.group");

        this.mf = mf;
        this.gp = new GroupPanel(this);
        this.init();

        Runtime.getRuntime().addShutdownHook(new Thread(this::save));
    }

    /**
     * Saves the groups.
     * */
    private void save()
    {
        try
        {
            Log.debug("Starting saving process.");
            SerializationUtil.createFileIfNotExists(new File(SAVES_PATH));
            EncryptedSerializationUtil.serializeObject(SAVES_PATH + ".enc", (Serializable) groups);
            Log.debug("Saving process finished correctly.");
        }
        catch (Exception e) {
            e.printStackTrace();

            Log.errorf("Encrypted saving process failed: %s", e.getLocalizedMessage());

            // If saving failed, then try the unencrypted route.
            try
            {
                SerializationUtil.serializeObject(SAVES_PATH, (Serializable) groups);
                Log.debug("Saving process finished correctly, although unencrypted.");
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                Log.errorf("Unencrypted saving process failed: %s", e.getLocalizedMessage());
            }
        }
    }

    /**
     * Loads the save file.
     * */
    private void loadSave()
    {
        try
        {
            if (new File(SAVES_PATH + ".enc").exists()) // First try the encrypted route.
            {
                groups.addAll(EncryptedSerializationUtil.deserializeObject(SAVES_PATH + ".enc"));
                return;
            }

            if (!new File(SAVES_PATH).exists()) // Then try the unencrypted route.
            {
                Log.debug("No save file found.");
                return;
            }

            groups.addAll(SerializationUtil.deserializeObject(SAVES_PATH));
        }
        catch (Exception e)
        {
            Log.errorf("Failed to load save: %s", e.getLocalizedMessage());

            new Thread(() -> {
                MessageDialog.create()
                             .setLocalizedTitlef("ui.title.dialog.error", e.getLocalizedMessage())
                             .setLocalizedInformationf("ui.title.dialog.error", e.getLocalizedMessage())
                             .setLocalizedButtonText("ui.button.dialog.close")
                             .show(Dialog.ERROR_MESSAGE);
            }, "Error: GroupFrame#loadSave").start();
        }
    }

    @Override
    public void init()
    {
        Log.debug("Starting to load the groups.");
        loadSave();
        gp.setGroups(groups);
        Log.debugf("Finished loading the groups! Groups: %s", groups);
        super.init();
    }

    @Override
    protected void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cp.setLayout(new BorderLayout());
    }

    @Override
    protected void addComponents()
    {
        cp.add(gp, BorderLayout.CENTER);
        this.setSize(new Dimension(900, 450));
    }

    @Override
    public void dispose()
    {
        mf.setVisible(true);
        save();
        super.dispose();
    }
}
