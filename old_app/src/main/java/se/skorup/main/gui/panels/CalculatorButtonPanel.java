package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.interfaces.ActionCallback;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The buttons for the calculator
 * */
public class CalculatorButtonPanel extends JPanel
{
    private String data = "";

    private final List<ActionCallback> callbacks = new ArrayList<>();

    private final JButton[] buttons = {
        new JButton("7"), new JButton("8"), new JButton("9"),
        new JButton("4"), new JButton("5"), new JButton("6"),
        new JButton("1"), new JButton("2"), new JButton("3"),
        new JButton("."), new JButton("0"), new JButton("+"),
        new JButton("-"), new JButton("*"), new JButton("/"),
        new JButton("%"), new JButton("("), new JButton(")")
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
        this.setLayout(new GridLayout(6, 3));
        this.setBackground(Utils.BACKGROUND_COLOR);

        Arrays.stream(buttons).forEachOrdered(this::setButtonProperties);
        Arrays.stream(buttons).forEach(b -> b.addActionListener(e -> {
            data += b.getText();
            callbacks.forEach(ActionCallback::callback);
        }));
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
        button.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        button.setFont(new Font(Font.DIALOG, Font.BOLD, 26));
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
     * Sets the created data.
     *
     * @param data the new data.
     * */
    public void setData(String data)
    {
        this.data = data;
    }

    /**
     * Resets the data.
     * */
    public void resetData()
    {
        data = "";
    }

    /**
     * Adds an action callback.
     *
     * @param ac the new action callback.
     * */
    public void addActionCallback(ActionCallback ac)
    {
        if (ac == null)
            return;

        callbacks.add(ac);
    }
}
