package se.skorup.main;

import se.skorup.API.DebugMethods;

/**
 * The class responsible for starting the program,
 * i.e. the main class.
 * */
public class Main
{
    /**
     * The main method starts the program.
     * */
    public static void main(String[] args)
    {
        DebugMethods.log("Hello World", DebugMethods.LogType.DEBUG);
        DebugMethods.log("Hello World", DebugMethods.LogType.ERROR);
    }
}
