package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.gui.models.PersonListModel;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * The panel that lists the persons.
 * */
public class PersonListPanel extends JPanel implements ListSelectionListener
{
    /** The set of persons in the list. */
    private Set<Person> persons;

    /** The current person. */
    private Person p;

    /** The action callbacks. */
    private final List<ActionCallback> callbacks = new Vector<>();

    /** The label. */
    private final JLabel lblGroupInfo;

    /** The list. */
    private final JList<Person> listPersons = new JList<>();

    /** The list model. */
    private final PersonListModel model;

    /** The scroller for the list. */
    private final JScrollPane scrListPersons = new JScrollPane(listPersons);

    /** The layout of the panel. */
    private final BorderLayout layout = new BorderLayout();

    /**
     * Creates a new PersonPanel.
     *
     * @param label the label of the list.
     * @param persons the persons in the list.
     * */
    public PersonListPanel(String label, Set<Person> persons)
    {
        this.persons = persons;
        this.lblGroupInfo = new JLabel(label);
        this.model = new PersonListModel(persons);

        this.setProperties();
        this.addComponents();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.add(lblGroupInfo, BorderLayout.PAGE_START);
        this.add(scrListPersons, BorderLayout.CENTER);
    }

    /**
     * Updates the list.
     * */
    private void updateList()
    {
        model.removeAll();
        model.addItems(persons);
    }

    /**
     * Updates the the list with a new
     * set of persons.
     *
     * @param persons the new set of persons.
     *                If {@code null} then it
     *                will return without doing
     *                anything.
     * */
    public void updateList(Set<Person> persons)
    {
        if (persons == null)
            return;

        this.persons = persons;
        updateList();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setLayout(layout);

        lblGroupInfo.setForeground(Utils.FOREGROUND_COLOR);

        listPersons.setForeground(Utils.FOREGROUND_COLOR);
        listPersons.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        listPersons.setModel(model);
        listPersons.setBorder(BorderFactory.createEmptyBorder());
        listPersons.addListSelectionListener(this);

        scrListPersons.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));

        this.updateList();
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
     * Deselects everything in this list.
     * */
    public void deselectAll()
    {
        listPersons.clearSelection();
        p = null;

        DebugMethods.log(
            "Deselecting from list %s".formatted(lblGroupInfo.getText()),
            DebugMethods.LogType.DEBUG
        );
    }

    /**
     * Gets the currently selected person.
     *
     * @return the currently selected person;
     *         {@code null} iff there is no
     *         person selected.
     * */
    public Person getCurrentPerson()
    {
        return p;
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        // Preventing double events.
        if (!e.getValueIsAdjusting())
        {
            int index = listPersons.getSelectedIndex();

            if (index != -1)
            {
                p = model.getElementAt(index);

                DebugMethods.log("Selected person: %s".formatted(p), DebugMethods.LogType.DEBUG);
                DebugMethods.log(
                        "Invoking callbacks from list %s".formatted(lblGroupInfo.getText()),
                        DebugMethods.LogType.DEBUG
                );

                callbacks.forEach(ActionCallback::callback);
            }

        }
    }
}
