package se.skorup.main;

import se.skorup.main.gui.frames.MainFrame;

import javax.swing.SwingUtilities;

/**
 * The class responsible for starting the program,
 * i.e. the main class.
 * */
public class Main
{
    /**
     * The main method starts the program.
     *
     * @param args the args passed to the command line.
     * */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
