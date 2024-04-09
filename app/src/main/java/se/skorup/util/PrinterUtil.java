package se.skorup.util;

import se.skorup.gui.components.output.TerminalOutput;

import java.awt.print.PrinterException;

/**
 * A class that contains the functionality
 * to print a text string using a printer.
 * */
public class PrinterUtil
{
    /**
     * Attempts to print a string to a piece of paper.
     * The text in the parameter can be formatted using
     * tags as in the tags parser.
     *
     * @param text the text that will be printed.
     * @throws PrinterException iff printing fails.
     * */
    public static void print(String text) throws PrinterException
    {
        var canvas = new TerminalOutput();
        canvas.setFontSize(12);
        canvas.setText("");
        canvas.appendColoredString(text);
        canvas.print();
    }
}
