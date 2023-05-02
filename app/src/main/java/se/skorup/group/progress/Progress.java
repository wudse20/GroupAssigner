package se.skorup.group.progress;

/**
 * An interface for progress tracking.
 * */
public interface Progress
{
    /**
     * Forces some progress to happen.
     *
     * @param progress the progress that has been made.
     * */
    void onProgress(int progress);
}