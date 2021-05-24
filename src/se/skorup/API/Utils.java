package se.skorup.API;

/**
 * Some util methods
 * */
public class Utils
{
    /**
     * Gets the folder name of this project,
     * were it saves all its files. <br><br>
     *
     * Ends with '\'.
     *
     * @return the folder location.
     * */
    public static String getFolderName()
    {
        return "%s\\.%s\\".formatted(System.getenv("APPDATA"), "GroupAssigner");
    }
}
