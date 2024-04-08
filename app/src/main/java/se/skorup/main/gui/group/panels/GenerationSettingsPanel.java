package se.skorup.main.gui.group.panels;

import se.skorup.gui.callbacks.ActionCallback;
import se.skorup.gui.components.buttons.Button;
import se.skorup.gui.components.buttons.CheckBox;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.containers.FlowContainer;
import se.skorup.gui.components.output.Label;
import se.skorup.gui.components.input.MathTextField;
import se.skorup.gui.components.containers.Panel;
import se.skorup.gui.components.buttons.RadioButton;
import se.skorup.gui.components.input.TextField;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.main.gui.group.frames.SubgroupFrame;
import se.skorup.main.gui.group.helper.GenerationSettings;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The panel responsible for getting the size
 * for the subgroups from the user.
 * */
public class GenerationSettingsPanel extends Panel
{
    private final SubgroupFrame sgf;

    private final List<ActionCallback<Void>> backCallbacks;
    private final List<ActionCallback<GenerationSettings>> nextCallbacks;

    private final Label lblHeader = new Label("ui.label.settings", true);

    private final Button btnBack = new Button("ui.button.back");
    private final Button btnNext = new Button("ui.button.next");

    private final CheckBox cbMainGroups = new CheckBox("ui.cb.use-main-groups", true);

    private final SizeSelectionPanel sizePanel = new SizeSelectionPanel();
    private final SizeSelectionPanel sizeMg1Panel = new SizeSelectionPanel();
    private final SizeSelectionPanel sizeMg2Panel = new SizeSelectionPanel();

    /**
     * Creates a new panel.
     *
     * @param sgf the instance of the SubgroupFrame in charge.
     * */
    public GenerationSettingsPanel(SubgroupFrame sgf)
    {
        super(new BorderLayout());
        this.sgf = sgf;
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

        btnBack.addActionListener(e -> backCallbacks.forEach(c -> c.action(null)));
        btnNext.addActionListener(e -> nextCallbacks.forEach(c -> {
            if (cbMainGroups.isSelected())
            {
                var mg1 = sizeMg1Panel.getSizes();
                var mg2 = sizeMg2Panel.getSizes();

                if (mg1.isEmpty() || mg2.isEmpty())
                    return;

                c.action(new GenerationSettings(true, List.of(), mg1, mg2));
            }
            else
            {
                var sizes = sizePanel.getSizes();

                if (sizes.isEmpty())
                    return;

                c.action(new GenerationSettings(false, sizes, List.of(), List.of()));
            }
        }));

        var b1 = BorderFactory.createTitledBorder(Localization.getValue("ui.label.main-group-1"));
        var b2 = BorderFactory.createTitledBorder(Localization.getValue("ui.label.main-group-2"));

        b1.setTitleColor(Utils.FOREGROUND_COLOR);
        b2.setTitleColor(Utils.FOREGROUND_COLOR);

        sizeMg1Panel.setBorder(b1);
        sizeMg2Panel.setBorder(b2);

        cbMainGroups.addActionListener(e -> addComponents());
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
        cont.add(new ComponentContainer(new FlowContainer(lblHeader, FlowLayout.CENTER)));
        cont.add(new Label(" "));
        cont.add(new FlowContainer(cbMainGroups, FlowLayout.LEFT));
        cont.add(new Label(" "));

        if (cbMainGroups.isSelected())
        {
            var container = new Panel(new GridLayout(1, 2));
            container.add(sizeMg1Panel);
            container.add(sizeMg2Panel);
            cont.add(container);
        }
        else
        {
            cont.add(sizePanel);
        }

        cont.add(bp);

        this.add(new ComponentContainer(cont), BorderLayout.CENTER);
        sgf.pack();
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
    public void addNextCallback(ActionCallback<GenerationSettings> callback)
    {
        if (callback == null)
            return;

        nextCallbacks.add(callback);
    }
}
