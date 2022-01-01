package se.skorup.main.gui.components;

import se.skorup.API.immutable_collections.ImmutableArray;
import se.skorup.API.tag_parser.Lexer;
import se.skorup.API.tag_parser.Parser;
import se.skorup.API.tag_parser.TextSegment;
import se.skorup.API.util.DebugMethods;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The super-type of all terminal text pains.
 * */
public abstract class TerminalPane extends JTextPane
{
    private int fontSize;
    private ImmutableArray<TextSegment> lastParseResult;

    /**
     * Creates a new terminal pane.
     *
     * @param d the dimensions of the TerminalPane.
     * @param isEditable if {@code true} then the text pane is editable,
     *                   else if {@code false} then it isn't.
     * */
    public TerminalPane(Dimension d, boolean isEditable)
    {
        this.setPreferredSize(d);
        this.setEditable(true);
    }

    /**
     * Appends a string to the content of the TerminalOutput
     *
     * @param s the text to be added.
     * @param c the color of the text.
     * */
    private void append(String s, Color c)
    {
        var sc = StyleContext.getDefaultStyleContext();
        var aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, Font.DIALOG);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        aset = sc.addAttribute(aset, StyleConstants.Size, fontSize);

        var doc = this.getDocument();

        try
        {
            doc.insertString(doc.getLength(), s, aset);
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Adds a text segment to the TerminalOutput
     *
     * @param ts the text segment to be added.
     * */
    void addLine(TextSegment ts)
    {
        this.append(ts.text(), ts.color());
        this.append("", Color.WHITE);
    }

    /**
     * Creates a new line
     * */
    protected void appendLine()
    {
        this.append("\n", Color.WHITE);
    }

    /**
     * Appends a multicolored string
     *
     * @param s the text to be parsed and added.
     * @return {@code true} if the string was correctly appended,
     *         else {@code false}.
     * */
    public boolean appendColoredString(String s)
    {
        Matcher matcher = Pattern.compile("<[A-Za-z_]+>").matcher(s);

        if (matcher.lookingAt())
        {
            var lexer = new Lexer(s);
            var lexResult = lexer.lex();
            var parser = new Parser(lexResult);
            var parseResult = parser.parse();
            parseResult.forEach(this::addLine);
            lastParseResult = parseResult;
        }
        else
        {
            addLine(s);
        }

        return true;
    }

    /**
     * Adds a white line.
     *
     * @param s the text of the line.
     * */
    public void addLine(String s)
    {
        this.addLine(s, Color.WHITE);
    }

    /**
     * Adds a colored line
     *
     * @param s the text of the line.
     * @param c the color of the line.
     * */
    public void addLine(String s, Color c)
    {
        this.append(s + "\n", c);
    }

    /**
     * Clears the terminal.
     * */
    public void clear()
    {
        this.setText("");
    }

    /**
     * Setter for font size.
     *
     * @param fontSize the new font-size.
     * @throws IllegalArgumentException if font size &lt;= 0.
     * */
    public void setFontSize(int fontSize) throws IllegalArgumentException
    {
        if (fontSize <= 0)
            throw new IllegalArgumentException("%d is not an accepted font size.".formatted(fontSize));

        DebugMethods.log("Old font-size: %s".formatted(this.fontSize), DebugMethods.LogType.DEBUG);
        DebugMethods.log("New font-size: %s".formatted(fontSize), DebugMethods.LogType.DEBUG);
        this.fontSize = fontSize;
        DebugMethods.log("Font Size updated", DebugMethods.LogType.DEBUG);
        this.repaint();
    }


    /**
     * Getter for: the last parse result.
     *
     * @return an immutable array with the last parse result.
     * */
    public ImmutableArray<TextSegment> getLastParseResult()
    {
        return lastParseResult;
    }
}
