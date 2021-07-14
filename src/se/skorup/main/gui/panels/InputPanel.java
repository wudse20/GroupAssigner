package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.interfaces.ActionCallback;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;

/**
 * A basic input panel.
 * */
public class InputPanel extends JPanel implements DocumentListener
{
    /** The layout of the panel. */
    private final FlowLayout layout = new FlowLayout(FlowLayout.LEFT);

    /** The input field. */
    private final JTextField txfInput;

    /** The info label. */
    private final JLabel lblInfo;

    /** The list containing all the ActionCallbacks. */
    private final List<ActionCallback> callbacks = new Vector<>();

    /**
     * Creates a new InputPanel.
     *
     * @param label the text of the label.
     * @param nbrCharacters the number of characters in the text field.
     * */
    public InputPanel(String label, int nbrCharacters)
    {
        this.lblInfo = new JLabel(label);
        this.txfInput = new JTextField(nbrCharacters);

        this.setProperties();
        this.addComponents();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.add(lblInfo);
        this.add(txfInput);
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        super.setBackground(Utils.BACKGROUND_COLOR);

        this.setLayout(layout);

        txfInput.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        txfInput.setForeground(Utils.FOREGROUND_COLOR);
        txfInput.setCaretColor(Utils.FOREGROUND_COLOR);
        txfInput.getDocument().addDocumentListener(this);

        lblInfo.setForeground(Utils.FOREGROUND_COLOR);
    }

    /**
     * Adds an ActionCallback to the panel.
     * If the provided callback is null, then
     * it will do nothing.
     *
     * @param c the callback to be added, if {@code null}
     *          then it will do nothing and just return.
     * */
    public void addActionCallback(ActionCallback c)
    {
        if (c == null)
            return;

        callbacks.add(c);
    }

    /**
     * Clears the text field.
     * */
    public void clear()
    {
        txfInput.setText("");
    }

    /**
     * Gets the text from the text field.
     *
     * @return the text of the text field.
     * */
    public String getText()
    {
        return txfInput.getText();
    }

    /**
     * Sets the text of the text field.
     *
     * @param text the new text of the text field.
     * */
    public void setText(String text)
    {
        txfInput.setText(text);
    }

    /**
     * Sets the color of the text field.
     *
     * @param c the new color of the text field.
     * */
    public void setTextFieldBackground(Color c)
    {
        txfInput.setBackground(c);
    }

    /**
     * Adds the key listener to the text field
     * instead of the panel
     *
     * @param k the key listener that will be added.
     *          if {@code null} then nothing happens.
     * */
    @Override
    public void addKeyListener(KeyListener k)
    {
        if (k == null)
            return;

        txfInput.addKeyListener(k);
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        callbacks.forEach(ActionCallback::callback);
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        callbacks.forEach(ActionCallback::callback);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {}
}
