package se.skorup.main.gui.helper.progress;

public interface Progress
{
    /**
     * Forces some progress to happen.
     *
     * @param progress the progress that has been made.
     * */
    void onProgress(int progress);
}
