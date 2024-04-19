package se.skorup.main.gui.group.helper;

import se.skorup.group.progress.Progress;
import se.skorup.gui.components.progress.ProgressBar;

import javax.swing.SwingUtilities;

/**
 * A monitor for a progress bar.
 * */
public class ProgressReport implements Progress
{
    private final ProgressBar p;

    private int ppm;

    /**
     * Creates a new progress report monitor.
     *
     * @param p the progressbar to be updated.
     * */
    public ProgressReport(ProgressBar p)
    {
        this.ppm = 0;
        this.p = p;
        SwingUtilities.invokeLater(() -> {
            p.setMaximum(1_000_000_000);
            p.setValue(0);
        });
    }

    @Override
    public synchronized void onProgress(int progress)
    {
        progress = Math.min(progress, 1000000);
        ppm += progress;
        SwingUtilities.invokeLater(() -> p.setValue(ppm));
    }
}
