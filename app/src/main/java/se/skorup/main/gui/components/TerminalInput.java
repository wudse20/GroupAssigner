package se.skorup.main.gui.components;

import se.skorup.API.immutable_collections.ImmutableArray;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TerminalInput extends TerminalPane implements KeyListener
{
    /**
     * Creates a new TerminalInput.
     *
     * @param manager The manager used for the commands.
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
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);

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
            {
                var sb = new StringBuilder().append(">blue>");

                while (Character.isDigit(c) || c == '.')
                {
                    sb.append(c);
                    c = txt.charAt(++i);
                }

                sb.append("</blue>");
                res.append(sb);
            }
            else if (isOperator(c))
            {
                res.append("<green>").append(c).append("</green>");
                i++;
            }
            else
            {
                res.append("<red>").append(c).append("</red>");
            }
        }

        DebugMethods.log("Syntax highlighted res: %s".formatted(res), DebugMethods.LogType.DEBUG);
        this.clear();
        this.appendColoredString(res.substring(0, res.length() - 1)); // Drops the last char.
    }

    /**
     * Determines whether it should highlight or not.
     *
     * @return {@code true} if it should highlight, else {@code false}.
     * */
    private boolean shouldHighlight(char c)
    {
        return Character.isAlphabetic(c) || Character.isDigit(c);
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
