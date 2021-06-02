package se.skorup.main.gui.panels;

import se.skorup.API.Utils;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.FlowLayout;

/**
 * A setting panel is a panel, with
 *
 * */
public class SettingPanel extends JPanel
{
    /** The checkbox of it's frame. */
    private final JRadioButton radio;

    /** The input panel that holds the text field and it's label. */
    private final InputPanel pInput;

    /** The layout of this panel. */
    private final FlowLayout layout = new FlowLayout(FlowLayout.RIGHT);

    /**
     * Creates a new SettingPanel.
     *
     * @param radioLabel The label of the radiobutton.
     * @param txfLabel The label of the text field.
     * @param chars the number of chars in the text field.
     * */
    public SettingPanel(String radioLabel, String txfLabel, int chars)
    {
        pInput = new InputPanel(txfLabel, chars);
        radio = new JRadioButton(radioLabel);

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
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        this.add(radio);
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
