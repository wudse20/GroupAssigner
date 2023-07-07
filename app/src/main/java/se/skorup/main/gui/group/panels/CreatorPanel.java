package se.skorup.main.gui.group.panels;

import se.skorup.gui.callbacks.ActionCallback;
import se.skorup.gui.components.buttons.Button;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.containers.FlowContainer;
import se.skorup.gui.components.output.Label;
import se.skorup.gui.components.containers.Panel;
import se.skorup.gui.components.buttons.RadioButton;
import se.skorup.main.gui.group.helper.Creator;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * The panel responsible for selecting the group creator.
 * */
public class CreatorPanel extends Panel
{
    private final List<ActionCallback<Creator>> callbacks;

    private final Label lblHeader = new Label("ui.label.creator", true);

    private final Button btnNext = new Button("ui.button.next");

    private final RadioButton radioRandom = new RadioButton("ui.radio.random");
    private final RadioButton radioWish = new RadioButton("ui.radio.wish");

    private final ButtonGroup bg = new ButtonGroup();

    /**
     * Creates a new panel.
     * */
    public CreatorPanel()
    {
        super(new BorderLayout());
        this.callbacks = new ArrayList<>();
        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        radioRandom.setSelected(true);
        radioWish.setSelected(false);

        bg.add(radioRandom);
        bg.add(radioWish);

        lblHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 32));
        btnNext.addActionListener(e ->
            callbacks.forEach(c -> c.action(radioRandom.isSelected() ? Creator.RANDOM : Creator.WISH)
        ));
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        var cont = new Panel(null);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.add(new ComponentContainer(lblHeader));
        cont.add(new FlowContainer(radioRandom, FlowLayout.LEFT));
        cont.add(new FlowContainer(radioWish, FlowLayout.LEFT));
        cont.add(new FlowContainer(btnNext, FlowLayout.RIGHT));

        this.add(new ComponentContainer(cont), BorderLayout.CENTER);
    }

    /**
     * Adds a callback that will be invoked when
     * the next button is pressed.
     *
     * @param callback the callback to be added.
     * */
    public void addCallback(ActionCallback<Creator> callback)
    {
        if (callback == null)
            return;

        callbacks.add(callback);
    }
}
