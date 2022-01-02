package se.skorup.main.gui.components;

import se.skorup.API.immutable_collections.ImmutableArray;
import se.skorup.API.immutable_collections.ImmutableHashSet;
import se.skorup.API.util.DebugMethods;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * An input text field that syntax highlights math expressions
 * */
public final class TerminalInput extends TerminalPane implements KeyListener
{
    private final ImmutableHashSet<String> keywords;
    private final char cmdChar;

    /**
     * Creates a new TerminalInput.
     *
     * @param keywords the keywords that should be syntax highlighted.
     * @param cmdChar the char that indicates a command and thus should be syntax highlighted.
     * */
    public TerminalInput(ImmutableHashSet<String> keywords, char cmdChar)
    {
        super(new Dimension(380, 20), true);

        this.keywords = keywords;
        this.cmdChar = cmdChar;

        this.setProperties();
    }

    /**
     * Sets the properties of the component.
     * */
    private void setProperties()
    {
        this.setText("");
        this.addKeyListener(this);

        this.setFontSize(12);
    }

    /**
     * Highlights the syntax of the input.
     * */
    private void syntaxHighlighting()
    {
        if (getText().trim().equals(""))
        {
            return;
        }
        else if (getText().indexOf(cmdChar) != -1)
        {
            var txt = getText();
            this.clear();
            this.appendColoredString("<LIGHT_PURPLE>%s</LIGHT_PURPLE>".formatted(txt));
            return;
        }

        DebugMethods.log(
            "Syntax Highlighting: '%s'".formatted(getText()),
            DebugMethods.LogType.DEBUG
        );

        var txt = getText();
        var i = 0;
        var res = new StringBuilder(); // The result.

        while (i < txt.length())
        {
            var c = txt.charAt(i);

            if (isStringPartOfKeyword(c))
                res.append("<yellow>").append(c).append("</yellow>");
            else if (c == '(' || c == ')')
                res.append("<light_blue>").append(c).append("</light_blue>");
            else if (Character.isDigit(c) || c == '.')
                res.append("<blue>").append(c).append("</blue>");
            else if (isOperator(c))
                res.append("<green>").append(c).append("</green>");
            else if (Character.isWhitespace(c))
                res.append(c);
            else
                res.append("<red>").append(c).append("</red>");

            i++;
        }

        DebugMethods.log("Syntax highlighted res: '%s'".formatted(res), DebugMethods.LogType.DEBUG);
        this.clear();
        this.appendColoredString(res.toString()); // Drops the last char.
    }

    /**
     * Checks if the character is a part of a keyword.
     *
     * @param c the character to be tested.
     * @return {@code true} iff the string is part of keyword,
     *         else {@code false}.
     * */
    private boolean isStringPartOfKeyword(char c)
    {
        for (var s : keywords)
        {
            if (s.indexOf(c) != -1)
                return true;
        }

        return false;
    }

    /**
     * Determines whether it should highlight or not.
     *
     * @return {@code true} if it should highlight, else {@code false}.
     * */
    private boolean shouldHighlight(char c)
    {
        return Character.isAlphabetic(c) ||
               Character.isDigit(c)      ||
               Character.isWhitespace(c) ||
               isOperator(c)             ||
               c == cmdChar;
    }

    /**
     * Checks if a char is an operator.
     *
     * @param c the char to be tested.
     * @return {@code true} iff the char is an operator.
     * */
    private boolean isOperator(char c)
    {
        return c == '+' ||
               c == '-' ||
               c == '*' ||
               c == '/';
    }

    @Override
    public String getText()
    {
        var txt = super.getText();
        var res = ImmutableArray.fromArray(txt.split("")).dropMatching("\r", "\n");
        return res.mkString("");
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (shouldHighlight(e.getKeyChar()) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            syntaxHighlighting();
    }
}
