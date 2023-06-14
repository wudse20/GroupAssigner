package se.skorup.main.gui.calculator.panels;

import se.skorup.gui.callbacks.ActionCallback;
import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.components.ScrollPane;
import se.skorup.gui.components.TerminalInput;
import se.skorup.gui.components.TerminalOutput;
import se.skorup.gui.components.TerminalPane;
import se.skorup.gui.helper.syntax_highlighting.ExpressionSyntaxHighlighting;
import se.skorup.util.Utils;
import se.skorup.util.tag_parser.TextSegment;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The IO panel for the calculator.
 * */
public class IOPanel extends Panel implements KeyListener
{
    private final TerminalInput input;
    private final TerminalPane output;
    private final ScrollPane scrOutput;

    private final List<ActionCallback<?>> callbacks;

    /**
     * Creates a new panel.
     *
     * @param keywords the keywords that are supposed to be highlighted.
     * */
    public IOPanel(Set<String> keywords)
    {
        super(new BorderLayout());
        this.input = new TerminalInput(new ExpressionSyntaxHighlighting(keywords));
        this.output = new TerminalOutput(new Dimension(200, 300));
        this.scrOutput = new ScrollPane(output);
        this.callbacks = new ArrayList<>();

        input.addKeyListener(this);

        output.setBorder(BorderFactory.createEmptyBorder());
        output.setBackground(Utils.BACKGROUND_COLOR);
        output.setFocusable(false);
        output.setFontSize(16);

        scrOutput.setBorder(BorderFactory.createEmptyBorder());

        this.addComponents();
    }

    /**
     * Adds the component to the frame.
     * */
    private void addComponents()
    {
        var input = new Panel(new GridLayout(2, 1));
        input.add(new Label(" "));
        input.add(this.input);

        var cont = new Panel(new BorderLayout());
        cont.add(scrOutput, BorderLayout.CENTER);
        cont.add(input, BorderLayout.PAGE_END);
        this.add(new ComponentContainer(cont), BorderLayout.CENTER);
    }

    /**
     * Appends a string into to the input box.
     *
     * @param str the string to be appended.
     * */
    public void appendInputString(String str)
    {
        input.setText(input.getText() + str);
        input.syntaxHighlighting();
    }

    /**
     * Appends a colored output string to the output.
     *
     * @param str the string to be added to the output.
     * */
    public void appendOutputString(String str)
    {
        output.appendColoredString(str);
    }

    /**
     * Adds a callback that will be invoked when enter is pressed.
     *
     * @param callback the callback to be added.
     * */
    public void addEnterCallback(ActionCallback<?> callback)
    {
        if (callback == null)
            return;

        callbacks.add(callback);
    }

    /**
     * Gets the input from the input field.
     *
     * @return the inputted text in the input field.
     * */
    public String getInputText()
    {
        return input.getText();
    }

    /**
     * Clears the input field.
     * */
    public void clearInput()
    {
        input.setText("");
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            this.callbacks.forEach(cb -> cb.action(null));
    }
}
