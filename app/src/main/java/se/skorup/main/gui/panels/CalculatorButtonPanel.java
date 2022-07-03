package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;

/**
 * The buttons for the calculator
 * */
public class CalculatorButtonPanel extends JPanel
{
    private String data;

    private final JButton[] buttons = {
        new JButton("7"), new JButton("8"), new JButton("9"), new JButton("+"),
        new JButton("4"), new JButton("5"), new JButton("6"), new JButton("-"),
        new JButton("1"), new JButton("2"), new JButton("3"), new JButton("*"),
        new JButton("."), new JButton("0"), new JButton("%"), new JButton("/")
    };

    /**
     * Creates a new CalculatorButtonPanel.
     * */
    public CalculatorButtonPanel()
    {
        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setLayout(new GridLayout(4, 4));
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));

        Arrays.stream(buttons).forEachOrdered(this::setButtonProperties);
        Arrays.stream(buttons).forEach(b -> b.addActionListener(e -> data += b.getText()));
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        Arrays.stream(buttons).forEach(this::add);
    }

    /**
     * Sets the properties of a JButton.
     * */
    private void setButtonProperties(JButton button)
    {
        button.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        button.setForeground(Utils.FOREGROUND_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(new Font(Font.DIALOG, Font.BOLD, 32));
    }

    /**
     * Gets the created data.
     *
     * @return the created data.
     * */
    public String getData()
    {
        return data;
    }

    /**
     * Resets the data.
     * */
    public void resetData()
    {
        data = "";
    }
}
