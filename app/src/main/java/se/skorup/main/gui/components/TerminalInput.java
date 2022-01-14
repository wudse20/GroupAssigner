package se.skorup.main.gui.components;

import se.skorup.API.collections.immutable_collections.ImmutableArray;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * An input text field that syntax highlights math expressions
 * */
public final class TerminalInput extends TerminalPane implements KeyListener
{
    private final char cmdChar;

    private SyntaxHighlighting sh;

    /**
     * Creates a new TerminalInput.
     *
     * @param cmdChar the char that indicates a command and thus should be syntax highlighted.
     * @param sh the syntax highlighting to be used.
     * */
    public TerminalInput(char cmdChar, SyntaxHighlighting sh)
    {
        super(new Dimension(380, 20), true);

        this.cmdChar = cmdChar;
        this.sh = sh;

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

    /**
     * Setter for: sh
     *
     * @param sh the new syntax highlighting iff sh != {@code null}.
     * */
    public void setSyntaxHighlighting(SyntaxHighlighting sh)
    {
        if (sh != null)
            this.sh = sh;
    }

    /**
     * Highlights the syntax of the input.
     * */
    public void syntaxHighlighting()
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

        var res = sh.syntaxHighlight(getText());
        this.clear();
        this.appendColoredString(res);
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
        else
            e.consume();
    }
}
