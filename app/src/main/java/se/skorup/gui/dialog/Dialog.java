package se.skorup.gui.dialog;

import se.skorup.util.Log;
import se.skorup.util.Utils;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

/**
 * An abstract base class for a Dialog.
 * */
public sealed abstract class Dialog<E> permits MessageDialog, InputDialog
{
    private final DialogMonitor mon;
    private final DialogFrame frame;

    protected E result;

    protected static ImageIcon informationIcon;

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
     * Shows the message dialog.
     * */
    public E show()
    {
        try
        {
            SwingUtilities.invokeAndWait(() -> {
                this.setupFrame(frame);
                frame.setVisible(true);
            });

            mon.start();

            return result;
        }
        catch (InterruptedException | InvocationTargetException unexpected)
        {
            Log.errorf("Unexpected exception: %s", unexpected);
            return null;
        }
    }

    /**
     * Loads the icon.
     *
     * @param icon the icon.
     * */
    public static void loadIcon(ImageIcon icon)
    {
        informationIcon = icon;
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
