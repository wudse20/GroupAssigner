package se.skorup.main.gui.group.panels;

import se.skorup.group.Group;
import se.skorup.gui.callbacks.ActionCallback;
import se.skorup.gui.callbacks.PersonSelectionCallback;
import se.skorup.gui.components.Button;
import se.skorup.gui.components.ComboBox;
import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.components.PersonList;
import se.skorup.gui.components.ScrollPane;
import se.skorup.gui.components.TabbedPane;
import se.skorup.gui.components.TextField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays a group.
 * */
public class GroupDisplayPanel extends Panel
{
    private final List<PersonSelectionCallback> selectionCallbacks;
    private final List<ActionCallback<Void>> createCallbacks;
    private final List<ActionCallback<Void>> mgCallbacks;
    private final List<ActionCallback<Group>> deleteCallbacks;

    private List<Group> groups;

    private final ComboBox<Group> cbGroups = new ComboBox<>();

    private final PersonList list = new PersonList();

    private final Label lblName = new Label("ui.label.name", true);

    private final TextField txfInput = new TextField(12);

    private final Button btnAdd = new Button("ui.button.add.text");
    private final Button btnMainGroups = new Button("ui.button.main-groups");
    private final Button btnCreateGroup = new Button("ui.button.create.group");
    private final Button btnCreateSubgroup = new Button("ui.button.create.subgroup");
    private final Button btnDeleteGroup = new Button("ui.button.delete-group");

    private final TabbedPane tabs = new TabbedPane();

    /**
     * Creates a new panel.
     * */
    public GroupDisplayPanel()
    {
        super(new BorderLayout());
        this.groups = new ArrayList<>();
        this.selectionCallbacks = new ArrayList<>();
        this.createCallbacks = new ArrayList<>();
        this.mgCallbacks = new ArrayList<>();
        this.deleteCallbacks = new ArrayList<>();

        addComponents();
        addListeners();

        btnDeleteGroup.setEnabled(groups.size() != 0);
    }

    /**
     * Adds all the listeners.
     * */
    private void addListeners()
    {
        btnAdd.addActionListener(e -> {
            updateGroup(txfInput.getText().trim());
            txfInput.clear();
        });

        txfInput.addActionListener(e -> {
            updateGroup(txfInput.getText().trim());
            txfInput.clear();
        });

        list.addListSelectionListener(e -> {
            var groupIndex = cbGroups.getSelectedIndex();
            var personIndex = list.getSelectedIndex();

            if (groupIndex == -1 || personIndex == -1 || groupIndex > groups.size())
            {
                selectionCallbacks.forEach(c -> c.personSelected(null, null));
                return;
            }

            selectionCallbacks.forEach(c -> c.personSelected(list.getSelectedValue(), groups.get(groupIndex)));
        });

        cbGroups.addItemListener(e -> {
            if (cbGroups.getSelectedIndex() == -1)
            {
                btnDeleteGroup.setEnabled(false);
                return;
            }

            list.setGroup(groups.get(cbGroups.getSelectedIndex()));
            btnDeleteGroup.setEnabled(true);
        });

        btnCreateGroup.addActionListener(e -> createCallbacks.forEach(c -> c.action(null)));
        btnDeleteGroup.addActionListener(e -> deleteCallbacks.forEach(c -> c.action(groups.get(cbGroups.getSelectedIndex()))));
        btnMainGroups.addActionListener(e -> mgCallbacks.forEach(c -> c.action(null)));
    }

    /**
     * Updates the current group with a person
     * by the name of name.
     *
     * @param name the name of the new person.
     * */
    private void updateGroup(String name)
    {
        if (cbGroups.getSelectedIndex() == -1)
            return;

        if (groups.size() < cbGroups.getSelectedIndex())
            return;

        var group = groups.get(cbGroups.getSelectedIndex());
        group.registerPerson(name);
        list.setGroup(group);
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
        cont.add(btnCreateGroup);
        cont.add(btnDeleteGroup);
        cont.add(btnCreateSubgroup);

        this.add(new ComponentContainer(cbGroups), BorderLayout.PAGE_START);
        this.add(new ComponentContainer(new ScrollPane(list)), BorderLayout.CENTER);
        this.add(new ComponentContainer(cont), BorderLayout.PAGE_END);
    }

    /**
     * Sets the groups to be displayed.
     *
     * @param groups the groups to be displayed.
     * */
    public void setGroups(List<Group> groups)
    {
        this.groups = groups;
        cbGroups.removeAllItems();

        for (var g : groups)
            cbGroups.addItem(g);

        if (groups.size() == 0)
        {
            cbGroups.setSelectedIndex(-1);
            return;
        }

        cbGroups.setSelectedIndex(0);
        list.setGroup(groups.get(0));
    }

    /**
     * Adds a callback that will be invoked when
     * a person is selected in the list.
     *
     * @param callback the callback to be added.
     * */
    public void addSelectionCallback(PersonSelectionCallback callback)
    {
        if (callback == null)
            return;

        selectionCallbacks.add(callback);
    }

    /**
     * Adds a callback that will be invoked when
     * a person is selected in the list.
     *
     * @param callback the callback to be added.
     * */
    public void addCreateGroupCallback(ActionCallback<Void> callback)
    {
        if (callback == null)
            return;

        createCallbacks.add(callback);
    }

    /**
     * Adds a callback that will be invoked when
     * the main-group button is pressed.
     *
     * @param callback the callback to be added.
     * */
    public void addMainGroupCallback(ActionCallback<Void> callback)
    {
        if (callback == null)
            return;

        mgCallbacks.add(callback);
    }

    /**
     * Adds a callback that will be invoked when
     * a group is deleted.
     *
     * @param callback the callback to be added.
     * */
    public void addDeleteGroupCallback(ActionCallback<Group> callback)
    {
        if (callback == null)
            return;

        deleteCallbacks.add(callback);
    }

    /**
     * Clears the selection of the list
     * with persons.
     * */
    public void clearSelection()
    {
        list.clearSelection();
    }

    /**
     * Gets the current group and {@code null}
     * iff there is no groups.
     *
     * @return the current group and {@code null} iff
     *         there are no groups.
     * */
    public Group getCurrentGroup()
    {
        if (groups.isEmpty())
            return null;

        return groups.get(cbGroups.getSelectedIndex());
    }
}
