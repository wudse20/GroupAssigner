package se.skorup.gui.dialog;

import se.skorup.util.Log;
import se.skorup.util.Utils;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * An abstract base class for a Dialog.
 *
 * @param <E> The type of the dialog, i.e., the type of the result.
 * */
public sealed abstract class Dialog<E> permits MessageDialog, InputDialog, FileDialog, ConfirmDialog
{
    private final DialogMonitor mon;
    private final DialogFrame frame;

    protected E result;

    protected static ImageIcon informationIcon;
    protected static ImageIcon warningIcon;
    protected static ImageIcon errorIcon;
    protected static ImageIcon fileIcon;
    protected static ImageIcon folderIcon;
    protected static ImageIcon homeIcon;
    protected static ImageIcon returnIcon;

    protected int type = Dialog.INFORMATION_MESSAGE;


    /** Will display no ICON on the dialog. */
    public static int NO_ICON = -1;

    /** Will use the information icon in the dialog. */
    public static int INFORMATION_MESSAGE = 0;

    /** Will use the warning icon in the dialog. */
    public static int WARNING_MESSAGE = 1;

    /** Will use the error icon in the dialog. */
    public static int ERROR_MESSAGE = 2;

    /** Will use the file icon as the icon in the dialog. */
    protected static int FILE_DIALOG = 3;

    /**
     * No one outside this package should
     * instantiate a class of this type.
     * */
    protected Dialog()
    {
        this.mon = new DialogMonitor();
        this.frame = new DialogFrame();
    }

    /**
     * Setup for the dialog frame.
     *
     * @param frame the container to add the objects to.
     * */
    protected abstract void setupFrame(DialogFrame frame);

    /**
     * Closes the frame.
     * */
    protected void close()
    {
        frame.dispose();
    }

    /**
     * Shows the dialog.
     *
     * @return the value from the dialog.
     * */
    public E show()
    {
        try
        {
            SwingUtilities.invokeLater(() -> {
                this.setupFrame(frame);
                frame.setIconImage(getIcon().getImage());
                frame.setVisible(true);
            });

            mon.start();

            return result;
        }
        catch (InterruptedException unexpected)
        {
            Log.errorf("Unexpected exception: %s", unexpected);
            return null;
        }
    }

    /**
     * Gets the currently selected icon.
     *
     * @return the icon that is selected.
     * */
    protected ImageIcon getIcon()
    {
        return type == INFORMATION_MESSAGE ? informationIcon :
               type == WARNING_MESSAGE ? warningIcon :
               type == ERROR_MESSAGE ? errorIcon :
               type == FILE_DIALOG ? fileIcon : new ImageIcon(); // new ImageIcon is for no icon.
    }

    /**
     * Shows the dialog and specifies the type
     * to be used.
     *
     * @param type the type of the message.
     * @see Dialog#INFORMATION_MESSAGE
     * @see Dialog#WARNING_MESSAGE
     * @see Dialog#ERROR_MESSAGE
     * */
    public E show(int type)
    {
        this.type = type;
        return show();
    }

    /**
     * Loads the type icon.
     *
     * @param info the information icon.
     * @param err the error icon.
     * @param warn the warning icon.
     * */
    public static void loadTypeIcons(ImageIcon info, ImageIcon warn, ImageIcon err)
    {
        informationIcon = info;
        warningIcon = warn;
        errorIcon = err;
    }

    /**
     * Loads the file icons for the file dialog.
     *
     * @param folder the folder icon.
     * @param file the file icon.
     * @param returnIcon the return icon.
     * @param home the home icon
     * */
    public static void loadFileIcons(ImageIcon folder, ImageIcon file, ImageIcon returnIcon, ImageIcon home)
    {
        Dialog.folderIcon = folder;
        Dialog.fileIcon = file;
        Dialog.returnIcon = returnIcon;
        Dialog.homeIcon = home;
    }

    protected final class DialogFrame extends JFrame
    {
        private DialogFrame()
        {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setResizable(false);
            this.setIconImage(informationIcon.getImage());
            this.getContentPane()
                .setBackground(Utils.BACKGROUND_COLOR);
        }

        @Override
        public void dispose()
        {
            mon.end();
            super.dispose();
        }
    }

    private static final class DialogMonitor
    {
        private boolean shouldWait;

        private DialogMonitor()
        {
            this.shouldWait = false;
        }

        private synchronized void start() throws InterruptedException
        {
            this.shouldWait = true;

            while(this.shouldWait)
                wait();
        }

        private synchronized void end()
        {
            this.shouldWait = false;
            notifyAll();
        }
    }
}
