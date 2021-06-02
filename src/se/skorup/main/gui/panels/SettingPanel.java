package se.skorup.main.gui.panels;

import se.skorup.API.Utils;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * A setting panel is a panel, with
 *
 * */
public class SettingPanel extends JPanel
{
    /** The checkbox of it's frame. */
    private final JCheckBox cb;

    /** The input panel that holds the text field and it's label. */
    private final InputPanel pInput;

    /** The layout of this panel. */
    private final FlowLayout layout = new FlowLayout(FlowLayout.CENTER);

    /**
     * Creates a new SettingPanel.
     *
     * @param cbLabel The label of the checkbox.
     * @param txfLabel The label of the text field.
     * @param chars the number of chars in the text field.
     * */
    public SettingPanel(String cbLabel, String txfLabel, int chars)
    {
        pInput = new InputPanel(txfLabel, chars);
        cb = new JCheckBox(cbLabel);

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties of the frame.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);

        cb.setBackground(Utils.BACKGROUND_COLOR);
        cb.setForeground(Utils.FOREGROUND_COLOR);
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        this.add(cb);
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
    public boolean isCheckboxSelected()
    {
        return cb.isSelected();
    }

    /**
     * Sets the status of the checkbox.
     *
     * @param selected if {@code true} then the checkbox will be
     *                 selected, else it will be unselected.
     * */
    public void setCheckboxSelected(boolean selected)
    {
        cb.setSelected(selected);
    }
}
