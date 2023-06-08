package se.skorup.main.gui.calculator.panels;

import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Panel;
import se.skorup.gui.components.TerminalInput;
import se.skorup.gui.components.TerminalOutput;
import se.skorup.gui.components.TerminalPane;
import se.skorup.gui.helper.syntax_highlighting.ExpressionSyntaxHighlighting;
import se.skorup.util.Log;
import se.skorup.util.Utils;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

/**
 * The IO panel for the calculator.
 * */
public class IOPanel extends Panel implements KeyListener
{
    private final TerminalPane input;
    private final TerminalPane output;

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

        input.addKeyListener(this);

        output.setBorder(BorderFactory.createEmptyBorder());
        output.setBackground(Utils.BACKGROUND_COLOR);
        output.setFocusable(false);

        this.addComponents();
    }

    /**
     * Adds the component to the frame.
     * */
    private void addComponents()
    {
        var cont = new Panel(new BorderLayout());
        cont.add(output, BorderLayout.CENTER);
        cont.add(input, BorderLayout.PAGE_END);
        this.add(new ComponentContainer(cont), BorderLayout.CENTER);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Log.debug("Enter was pressed");
        }
    }
}
