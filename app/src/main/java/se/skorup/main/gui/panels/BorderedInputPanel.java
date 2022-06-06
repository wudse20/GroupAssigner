package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;

/**
 * An input panel with a border with a label.
 * */
public class BorderedInputPanel extends JPanel implements DocumentListener
{
    private final JTextField txf;

    /**
     * Creates a new BorderedInputPanel, with a label,
     * a color and a specified amount of characters.
     *
     * @param label the label of the border.
     * @param c the color of the border.
     * @param characters the width of the text field.
     * */
    public BorderedInputPanel(String label, int characters, Color c)
    {
        txf = new JTextField(characters);
        var settingsBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR), label
        );

        settingsBorder.setTitleColor(c);
        txf.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        txf.setForeground(Utils.FOREGROUND_COLOR);
        txf.setCaretColor(Utils.FOREGROUND_COLOR);
        txf.getDocument().addDocumentListener(this);

        this.setBorder(settingsBorder);
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.add(txf);
    }

    /**
     * Getter for: the TextField text.
     *
     * @return the text of the text field.
     * */
    public String getTextFieldText()
    {
        return txf.getText();
    }

    /**
     * Sets the background of the TextField.
     *
     * @param c the new color of the background
     *          of the TextField.
     * */
    public void setTextFieldBackground(Color c)
    {
        txf.setBackground(c);
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        txf.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        txf.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {}
}
