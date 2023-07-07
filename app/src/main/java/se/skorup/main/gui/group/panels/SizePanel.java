package se.skorup.main.gui.group.panels;

import se.skorup.gui.callbacks.ActionCallback;
import se.skorup.gui.components.buttons.Button;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.containers.FlowContainer;
import se.skorup.gui.components.output.Label;
import se.skorup.gui.components.input.MathTextField;
import se.skorup.gui.components.containers.Panel;
import se.skorup.gui.components.buttons.RadioButton;
import se.skorup.gui.components.input.TextField;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.util.localization.Localization;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The panel responsible for getting the size
 * for the subgroups from the user.
 * */
public class SizePanel extends Panel
{
    private final List<ActionCallback<Void>> backCallbacks;
    private final List<ActionCallback<List<Integer>>> nextCallbacks;

    private final Label lblHeader = new Label("ui.label.size", true);

    private final MathTextField txfInput = new MathTextField(12);
    private final TextField txfInputMulti = new TextField(12);

    private final Button btnBack = new Button("ui.button.back");
    private final Button btnNext = new Button("ui.button.next");

    private final RadioButton radioSameSize = new RadioButton("ui.radio.same-size");
    private final RadioButton radioDifferentSizes = new RadioButton("ui.radio.different-sizes");

    private final ButtonGroup bg = new ButtonGroup();

    /**
     * Creates a new panel.
     * */
    public SizePanel()
    {
        super(new BorderLayout());
        this.backCallbacks = new ArrayList<>();
        this.nextCallbacks = new ArrayList<>();

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        lblHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 32));

        radioSameSize.setSelected(true);
        radioDifferentSizes.setSelected(false);

        bg.add(radioSameSize);
        bg.add(radioDifferentSizes);

        radioSameSize.addActionListener(e -> radioAction("ui.label.size"));
        radioDifferentSizes.addActionListener(e -> radioAction("ui.label.sizes"));
        btnBack.addActionListener(e -> backCallbacks.forEach(c -> c.action(null)));
        btnNext.addActionListener(e -> nextCallbacks.forEach(c -> {
            try
            {
                var list = new ArrayList<Integer>();

                if (radioSameSize.isSelected())
                {
                    list.add(Integer.parseInt(txfInput.getText()));
                }
                else
                {
                    Arrays.stream(txfInputMulti.getText().split(","))
                          .parallel()
                          .map(String::trim)
                          .mapToInt(Integer::parseInt)
                          .forEach(list::add);
                }

                c.action(list);
            }
            catch (NumberFormatException ex)
            {
                var str = radioSameSize.isSelected() ? txfInput.getText() : txfInputMulti.getText();

                new Thread(() -> {
                    MessageDialog.create()
                                 .setLocalizedTitle("ui.error.not-an-int")
                                 .setLocalizedInformationf("ui.error.not-an-intf", str)
                                 .setLocalizedButtonText("ui.button.close")
                                 .show(MessageDialog.ERROR_MESSAGE);
                }).start();
            }
        }));
    }

    /**
     * The action that happens when a radio button is selected.
     *
     * @param localizationKey the key that will be the new header text.
     * */
    private void radioAction(String localizationKey)
    {
        lblHeader.setText(Localization.getValue(localizationKey));
        addComponents();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.removeAll();

        var bp = new Panel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(btnBack);
        bp.add(btnNext);

        var cont = new Panel(null);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.add(new FlowContainer(new ComponentContainer(lblHeader), FlowLayout.CENTER));
        cont.add(new FlowContainer(radioSameSize, FlowLayout.LEFT));
        cont.add(new FlowContainer(radioDifferentSizes, FlowLayout.LEFT));
        cont.add(new ComponentContainer(radioSameSize.isSelected() ? txfInput : txfInputMulti));
        cont.add(bp);

        this.add(new ComponentContainer(cont), BorderLayout.CENTER);

        this.revalidate();
    }

    /**
     * Adds a callback that will be invoked when
     * the back button is pressed.
     *
     * @param callback the callback to be added.
     * */
    public void addBackCallback(ActionCallback<Void> callback)
    {
        if (callback == null)
            return;

        backCallbacks.add(callback);
    }

    /**
     * Adds a callback that will be invoked when
     * the next button is pressed.
     *
     * @param callback the callback to be added.
     * */
    public void addNextCallback(ActionCallback<List<Integer>> callback)
    {
        if (callback == null)
            return;

        nextCallbacks.add(callback);
    }
}
