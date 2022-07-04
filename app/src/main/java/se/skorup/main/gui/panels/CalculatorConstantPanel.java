package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The panel containing the buttons for constants.
 * */
public class CalculatorConstantPanel extends JScrollPane
{
    private final List<JButton> buttons = new ArrayList<>();

    private final JPanel container = new JPanel();
    private final JScrollPane scrContainer = new JScrollPane(container);

    /**
     * Creates a new CalculatorConstantPanel.
     *
     * @param constants a set containing the identifiers to all constants.
     * */
    public CalculatorConstantPanel(Set<String> constants)
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.add(scrContainer);
        this.setUpButtons(constants);
    }

    /**
     * Updates the buttons in the GUI with a new set of constants.
     *
     * @param constants the constants to be updated.
     * */
    public void setUpButtons(Set<String> constants)
    {
        buttons.clear();
        constants.forEach(s -> buttons.add(new JButton(s)));

        container.removeAll();
        container.setLayout(new GridLayout(constants.size() / 2, 2));
        buttons.forEach(container::add);
        container.revalidate();
        this.revalidate();
    }
}
