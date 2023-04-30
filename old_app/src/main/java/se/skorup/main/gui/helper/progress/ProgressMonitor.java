package se.skorup.main.gui.helper.progress;

import se.skorup.API.util.DebugMethods;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * A thread-safe implementation of the progress interface.
 * */
public class ProgressMonitor implements Progress
{
    private final JProgressBar progress;
    private int ppm;

    /**
     * Creates a new progress monitor.
     *
     * @param progress the JProgressBar that this class is updating.
     * */
    public ProgressMonitor(JProgressBar progress)
    {
        this.progress = progress;
        this.ppm = 0;
        SwingUtilities.invokeLater(() -> this.progress.setValue(0)); // Reset the progress.
    }

    @Override
    public synchronized void onProgress(int progress)
    {
        progress = Math.min(progress, 1_000_000 - progress);
        ppm += progress;
        SwingUtilities.invokeLater(() -> this.progress.setValue(ppm));
        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Progress: %d / 1_000_000%n", ppm);
    }
}
