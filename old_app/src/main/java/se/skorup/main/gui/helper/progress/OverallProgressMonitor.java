package se.skorup.main.gui.helper.progress;

import se.skorup.API.util.DebugMethods;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * Thread-safe overall progress monitor.
 * */
public class OverallProgressMonitor implements Progress
{
    private int max = 0;
    private int current = 0;

    private final JProgressBar progress;

    /**
     * Creates a new progress monitor.
     *
     * @param progress The progress bar to update.
     * */
    public OverallProgressMonitor(JProgressBar progress)
    {
        this.progress = progress;
    }

    /**
     * Registers a new progress task.
     *
     * @param max the maximum progress of that task.
     * */
    public synchronized void registerProgress(int max)
    {
        this.max += max;
        progress.setMaximum(this.max);
    }

    @Override
    public synchronized void onProgress(int progress)
    {
        progress = Math.min(progress, max - progress);
        current += progress;
        SwingUtilities.invokeLater(() -> this.progress.setValue(current));
        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Progress: %d / %d%n", current, max);
    }
}
