package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The panel containing the buttons for constants.
 * */
public class CalculatorConstantPanel extends JPanel
{
    private final List<JButton> buttons = new ArrayList<>();

    /**
     * Creates a new CalculatorConstantPanel.
     *
     * @param constants a set containing the identifiers to all constants.
     * */
    public CalculatorConstantPanel(Set<String> constants)
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
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
        constants.stream().sorted().forEach(s -> buttons.add(new JButton(s)));
        buttons.forEach(this::fixButtonProperties);

        this.removeAll();
        this.setLayout(new GridLayout(2, constants.size()));
        buttons.forEach(this::add);
        buttons.forEach(b -> this.add(new JLabel(" ")));
        this.revalidate();
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
        btn.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createLineBorder(Utils.COMPONENT_BACKGROUND_COLOR, 4)
            )
        );
    }
}
