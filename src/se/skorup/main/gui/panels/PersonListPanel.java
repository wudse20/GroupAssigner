package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.models.PersonListModel;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.util.Set;

/**
 * The panel that lists the persons.
 * */
public class PersonListPanel extends JPanel
{
    /** The set of persons in the list. */
    private final Set<Person> persons;

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

        scrListPersons.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));

        this.updateList();
    }
}
