package se.skorup.main.gui.components;

import se.skorup.API.immutable_collections.ImmutableArray;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * An input text field that syntax highlights math expressions
 * */
public final class TerminalInput extends TerminalPane implements KeyListener
{
    /**
     * Creates a new TerminalInput.
     * */
    public TerminalInput()
    {
        super(new Dimension(380, 20), true);
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
            return;

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

            if (Character.isDigit(c) || c == '.')
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
     * Determines whether it should highlight or not.
     *
     * @return {@code true} if it should highlight, else {@code false}.
     * */
    private boolean shouldHighlight(char c)
    {
        return Character.isAlphabetic(c) ||
               Character.isDigit(c)      ||
               Character.isWhitespace(c) ||
               isOperator(c);
    }

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
