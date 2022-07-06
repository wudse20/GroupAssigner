package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.ExpressionSyntaxHighlighting;
import se.skorup.main.gui.components.TerminalInput;
import se.skorup.main.gui.components.TerminalOutput;
import se.skorup.main.gui.components.TerminalPane;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

    private final JScrollPane scrOutput;

    /**
     * Creates a new CalculatorIOPanel.
     *
     * @param keywords the keywords to be highlighted.
     * */
    public CalculatorIOPanel(Set<String> keywords)
    {
        this.input = new TerminalInput('!', new ExpressionSyntaxHighlighting(keywords));
        this.output = new TerminalOutput(new Dimension(200, 300));
        this.scrOutput = new JScrollPane(output);

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

        this.output.setFontSize(14);
        this.output.setBorder(BorderFactory.createEmptyBorder());
        this.output.setBackground(Utils.BACKGROUND_COLOR);

        this.scrOutput.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        var cont = new JPanel();
        cont.setBackground(Utils.BACKGROUND_COLOR);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));

        cont.add(scrOutput);
        cont.add(new JLabel(" "));

        this.add(cont, BorderLayout.CENTER);
        this.add(input, BorderLayout.PAGE_END);
    }

    /**
     * Sets the text of the input.
     * */
    public void setInputText(String text)
    {
        input.clear();
        input.setText(text);
        input.syntaxHighlighting();
    }

    /**
     * Appends a String to the output.
     * */
    public void appendOutputText(String text)
    {
        output.appendColoredString(text);
    }

    /**
     * Gets the output text.
     *
     * @return the text of the output.
     * */
    public String getOutputText()
    {
        return output.getText();
    }

    /**
     * Gets the input text.
     *
     * @return the text of the input.
     * */
    public String getInputText()
    {
        return input.getText();
    }

    /**
     * Getter for: output
     *
     * @return the instance of the output window.
     * */
    public TerminalPane getOutput()
    {
        return output;
    }

    /**
     * Clears the input.
     * */
    public void clearInput()
    {
        input.clear();
    }
}
