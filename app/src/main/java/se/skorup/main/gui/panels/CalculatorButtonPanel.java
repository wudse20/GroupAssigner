package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * The buttons for the calculator
 * */
public class CalculatorButtonPanel extends JPanel
{
    private final JButton btn0 = new JButton("0");
    private final JButton btn1 = new JButton("1");
    private final JButton btn2 = new JButton("2");
    private final JButton btn3 = new JButton("3");
    private final JButton btn4 = new JButton("4");
    private final JButton btn5 = new JButton("5");
    private final JButton btn6 = new JButton("6");
    private final JButton btn7 = new JButton("7");
    private final JButton btn8 = new JButton("8");
    private final JButton btn9 = new JButton("9");
    private final JButton btnEquals = new JButton("=");
    private final JButton btnDot = new JButton(".");

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
        this.setLayout(new GridLayout(4, 3));
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));

        setButtonProperties(btn0);
        setButtonProperties(btn1);
        setButtonProperties(btn2);
        setButtonProperties(btn3);
        setButtonProperties(btn4);
        setButtonProperties(btn5);
        setButtonProperties(btn6);
        setButtonProperties(btn7);
        setButtonProperties(btn8);
        setButtonProperties(btn9);
        setButtonProperties(btnEquals);
        setButtonProperties(btnDot);
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        this.add(btn7);
        this.add(btn8);
        this.add(btn9);
        this.add(btn4);
        this.add(btn5);
        this.add(btn6);
        this.add(btn1);
        this.add(btn2);
        this.add(btn3);
        this.add(btnDot);
        this.add(btn0);
        this.add(btnEquals);
    }

    /**
     * Sets the properties of a JButton.
     * */
    private void setButtonProperties(JButton button)
    {
        button.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        button.setForeground(Utils.FOREGROUND_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder());
    }
}
