package se.skorup.main.gui.panels;

import se.skorup.API.Utils;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * A setting panel is a panel, with
 *
 * */
public class SettingPanel extends JPanel
{
    /** If the input should be shown or not. */
    private final boolean shouldHaveInput;

    /** The checkbox of it's frame. */
    private final JRadioButton radio;

    /** The input panel that holds the text field and it's label. */
    private final InputPanel pInput;

    /** The layout of this panel. */
    private final FlowLayout layout = new FlowLayout(FlowLayout.LEFT);

    /**
     * Creates a new SettingPanel.
     *
     * @param radioLabel The label of the radiobutton.
     * @param txfLabel The label of the text field.
     * @param chars the number of chars in the text field.
     * @param shouldHaveInput if {@code true} then it will have an input,
     *                        else, i.e. {@code false}, it will have no input.
     * */
    public SettingPanel(String radioLabel, String txfLabel, int chars, boolean shouldHaveInput)
    {
        this.shouldHaveInput = shouldHaveInput;
        this.pInput = new InputPanel(txfLabel, chars);
        this.radio = new JRadioButton(radioLabel);

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties of the frame.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setLayout(layout);

        radio.setBackground(Utils.BACKGROUND_COLOR);
        radio.setForeground(Utils.FOREGROUND_COLOR);
        radio.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 12));
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        this.add(radio);

        if (shouldHaveInput)
            this.add(pInput);
    }

    /**
     * Gets the data from the text field.
     *
     * @return the data of the text field.
     * */
    public String getTextFieldData()
    {
        return pInput.getText();
    }

    /**
     * Gets the status of the checkbox.
     *
     * @return {@code true} iff the checkbox is selected.<br>
     *         {@code false} iff the checkbox isn't selected.
     * */
    public boolean isRadioSelected()
    {
        return radio.isSelected();
    }

    /**
     * Sets the status of the radio button.
     *
     * @param selected if {@code true} then the checkbox will be
     *                 selected, else it will be unselected.
     * */
    public void setRadioSelected(boolean selected)
    {
        radio.setSelected(selected);
    }

    /**
     * Getter for: radio button.
     *
     * @return gets the radio button.
     * */
    public JRadioButton getRadio()
    {
        return radio;
    }
}
