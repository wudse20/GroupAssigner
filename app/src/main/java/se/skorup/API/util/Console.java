package se.skorup.API.util;

/**
 * An API that expands the functionality of the
 * System.out printing.
 *
 * TODO: bold and underlined
 * */
public class Console
{
    // shamelessly stole from the internet.
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    private static final String RESET = "\033[0m";

    private static final String BLACK = "\033[0;30m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";
    private static final String PURPLE = "\033[0;35m";
    private static final String CYAN = "\033[0;36m";
    private static final String WHITE = "\033[0;37m";

    private static final String BLACK_BOLD = "\033[1;30m";  
    private static final String RED_BOLD = "\033[1;31m";    
    private static final String GREEN_BOLD = "\033[1;32m";  
    private static final String YELLOW_BOLD = "\033[1;33m"; 
    private static final String BLUE_BOLD = "\033[1;34m";   
    private static final String PURPLE_BOLD = "\033[1;35m"; 
    private static final String CYAN_BOLD = "\033[1;36m";   
    private static final String WHITE_BOLD = "\033[1;37m";

    private static final String BLACK_UNDERLINED = "\033[4;30m";  
    private static final String RED_UNDERLINED = "\033[4;31m";    
    private static final String GREEN_UNDERLINED = "\033[4;32m";  
    private static final String YELLOW_UNDERLINED = "\033[4;33m"; 
    private static final String BLUE_UNDERLINED = "\033[4;34m";   
    private static final String PURPLE_UNDERLINED = "\033[4;35m"; 
    private static final String CYAN_UNDERLINED = "\033[4;36m";   
    private static final String WHITE_UNDERLINED = "\033[4;37m";

    private static ConsoleColor current = ConsoleColor.RESET;

    static {
        // Resets the console color after the program finished running.
        Runtime.getRuntime().addShutdownHook(new Thread(Console::resetColor));
    }

    /**
     * Sets the new console color.
     *
     * @param c the new console color.
     * */
    public static void setColor(ConsoleColor c)
    {
        // I know this breaks OCP, so shame on me :)
        System.out.print(switch (c) {
            case BLACK -> BLACK;
            case RED -> RED;
            case GREEN -> GREEN;
            case YELLOW -> YELLOW;
            case BLUE -> BLUE;
            case PURPLE -> PURPLE;
            case CYAN -> CYAN;
            case WHITE -> WHITE;
            case RESET -> RESET;
        });
        System.out.flush();

        current = c;
    }

    /**
     * Gets the current console color.
     *
     * @return the current console color.
     * */
    public static ConsoleColor getColor()
    {
        return current;
    }

    /**
     * Resets the color to the default value.
     * */
    public static void resetColor()
    {
        setColor(ConsoleColor.RESET);
    }

    /**
     * Clears the console.
     * */
    public static void clear()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
