package se.skorup.main.gui.group.panels;

import se.skorup.gui.components.buttons.RadioButton;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.containers.FlowContainer;
import se.skorup.gui.components.containers.Panel;
import se.skorup.gui.components.input.MathTextField;
import se.skorup.gui.components.input.TextField;
import se.skorup.gui.components.output.Label;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.util.localization.Localization;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

/**
 * The panel used for selecting sizes when generating groups.
 * */
public class SizeSelectionPanel extends Panel
{
    private final Label lblHeader = new Label("ui.label.size", true);

    private final RadioButton radioSameSize = new RadioButton("ui.radio.same-size");
    private final RadioButton radioDifferentSizes = new RadioButton("ui.radio.different-sizes");

    private final ButtonGroup bg = new ButtonGroup();

    private final MathTextField txfInput = new MathTextField(12);
    private final TextField txfInputMulti = new TextField(12);

    /**
     * Creates a new panel.
     * */
    public SizeSelectionPanel()
    {
        super(null);
        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        radioSameSize.setSelected(true);
        radioDifferentSizes.setSelected(false);

        bg.add(radioSameSize);
        bg.add(radioDifferentSizes);

        radioSameSize.addActionListener(e -> radioAction("ui.label.size"));
        radioDifferentSizes.addActionListener(e -> radioAction("ui.label.sizes"));

        lblHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.add(new FlowContainer(lblHeader, FlowLayout.LEFT));
        this.add(new FlowContainer(radioSameSize, FlowLayout.LEFT));
        this.add(new FlowContainer(radioDifferentSizes, FlowLayout.LEFT));
        this.add(new ComponentContainer(radioSameSize.isSelected() ? txfInput : txfInputMulti));
    }

    /**
     * The action that happens when a radio button is selected.
     *
     * @param localizationKey the key that will be the new header text.
     * */
    private void radioAction(String localizationKey)
    {
        lblHeader.setText(Localization.getValue(localizationKey));
    }

    public List<Integer> getSizes()
    {
        try
        {
            if (radioSameSize.isSelected())
                return List.of(Integer.parseInt(txfInput.getText()));

            return Arrays.stream(txfInputMulti.getText().split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
        }
        catch (NumberFormatException e)
        {
            var str = radioSameSize.isSelected() ? txfInput.getText() : txfInputMulti.getText();

                new Thread(() -> {
                    MessageDialog.create()
                                 .setLocalizedTitle("ui.error.not-an-int")
                                 .setLocalizedInformationf("ui.error.not-an-intf", "'%s'".formatted(str))
                                 .setLocalizedButtonText("ui.button.close")
                                 .show(MessageDialog.ERROR_MESSAGE);
                }, "SizeSelectionPanel::getSizes-InvalidUserInput").start();

            return List.of();
        }
    }
}
