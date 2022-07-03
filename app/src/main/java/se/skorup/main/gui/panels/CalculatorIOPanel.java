package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.ExpressionSyntaxHighlighting;
import se.skorup.main.gui.components.TerminalInput;
import se.skorup.main.gui.components.TerminalOutput;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Set;

/**
 * The I/O panel for the calculator.
 * */
public class CalculatorIOPanel extends JPanel
{
    private final TerminalInput input;
    private final TerminalOutput output;

    /**
     * Creates a new CalculatorIOPanel.
     *
     * @param keywords the keywords to be highlighted.
     * */
    public CalculatorIOPanel(Set<String> keywords)
    {
        this.input = new TerminalInput('!', new ExpressionSyntaxHighlighting(keywords));
        this.output = new TerminalOutput(new Dimension(400, 300)); // TODO!

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties of the panel.
     * */
    private void setProperties()
    {
        this.setLayout(new BorderLayout());
        this.setBackground(Utils.BACKGROUND_COLOR);

        input.setBackground(Utils.BACKGROUND_COLOR);
        input.setBorder(BorderFactory.createEmptyBorder());

        output.setBackground(Utils.BACKGROUND_COLOR);
        output.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.add(output, BorderLayout.CENTER);
        this.add(input, BorderLayout.PAGE_END);
    }

    /**
     * Sets the text of the input.
     * */
    public void setText(String text)
    {
        input.clear();
        input.setText(text);
        input.syntaxHighlighting();
    }
}
