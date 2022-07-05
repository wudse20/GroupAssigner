package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The panel containing the buttons for constants.
 * */
public class CalculatorConstantPanel extends JPanel
{
    private final CalculatorPanel cp;
    private final List<JButton> buttons = new ArrayList<>();

    private final JPanel container = new JPanel();
    private final JScrollPane scrContainer = new JScrollPane(container);

    /**
     * Creates a new CalculatorConstantPanel.
     *
     * @param constants a set containing the identifiers to all constants.
     * @param cp the instance of the parent calculator panel.
     * */
    public CalculatorConstantPanel(Set<String> constants, CalculatorPanel cp)
    {
        this.cp = cp;

        this.setBackground(Utils.BACKGROUND_COLOR);
        this.add(scrContainer);
        this.setUpButtons(constants);

        scrContainer.setBackground(Utils.BACKGROUND_COLOR);
        scrContainer.setBorder(BorderFactory.createEmptyBorder());

        container.setBackground(Utils.BACKGROUND_COLOR);
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
        buttons.forEach(this::fixButtonProperties);

        container.removeAll();
        container.setLayout(new GridLayout(constants.size(), 1));
        buttons.forEach(container::add);
        container.revalidate();
    }

    /**
     * Fixes the button properties.
     *
     * @param btn the button to be affected.
     * */
    private void fixButtonProperties(JButton btn)
    {
        btn.setForeground(Utils.FOREGROUND_COLOR);
        btn.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
    }
}
