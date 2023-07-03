package se.skorup.main.gui.group.panels;

import se.skorup.group.Group;
import se.skorup.gui.components.Button;
import se.skorup.gui.components.ComboBox;
import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.components.PersonList;
import se.skorup.gui.components.ScrollPane;
import se.skorup.gui.components.TextField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

/**
 * Displays a group.
 * */
public class GroupDisplayPanel extends Panel
{
    private final List<Group> groups;

    private final ComboBox<Group> cbGroups = new ComboBox<>();
    private final PersonList list = new PersonList();
    private final Label lblName = new Label("ui.label.name", true);
    private final TextField txfInput = new TextField(12);
    private final Button btnAdd = new Button("ui.button.add.text");
    private final Button btnMainGroups = new Button("ui.button.main-groups");

    /**
     * Creates a new panel.
     *
     * @param groups the groups to be displayed.
     * */
    public GroupDisplayPanel(List<Group> groups)
    {
        super(new BorderLayout());
        this.groups = groups;

        for (var g : groups)
            cbGroups.addItem(g);

        list.setGroup(groups.get(0));
        addComponents();
    }

    /**
     * Adds the components
     * */
    private void addComponents()
    {
        var cont = new Panel(new FlowLayout(FlowLayout.CENTER));
        cont.add(lblName);
        cont.add(txfInput);
        cont.add(btnAdd);
        cont.add(btnMainGroups);

        this.add(new ComponentContainer(cbGroups), BorderLayout.PAGE_START);
        this.add(new ComponentContainer(new ScrollPane(list)), BorderLayout.CENTER);
        this.add(new ComponentContainer(cont), BorderLayout.PAGE_END);
    }
}
