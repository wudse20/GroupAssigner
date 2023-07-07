package se.skorup.gui.components.input;

import se.skorup.gui.helper.syntax_highlighting.SyntaxHighlighting;
import se.skorup.util.collections.ImmutableArray;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * An input text field that syntax highlights math expressions
 * */
public final class TerminalInput extends TerminalPane implements KeyListener
{
    private SyntaxHighlighting sh;

    /**
     * Creates a new TerminalInput.
     *
     * @param sh the syntax highlighting to be used.
     * */
    public TerminalInput(SyntaxHighlighting sh)
    {
        super(new Dimension(380, 20), true);
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
               c == '.';
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
     * Highlights the syntax of the input.
     * */
    public void syntaxHighlighting()
    {
        var caretPos = this.getCaretPosition();

        if (getText().trim().equals(""))
        {
            return;
        }

        var res = sh.syntaxHighlight(getText());
        this.clear();
        this.appendColoredString(res);

        if (caretPos > 0 && caretPos < this.getText().length())
            this.setCaretPosition(caretPos);
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
