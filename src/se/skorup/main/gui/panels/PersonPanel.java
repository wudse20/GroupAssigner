package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.objects.Person;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * The panel that lists the persons.
 * */
public class PersonPanel extends JPanel
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
    public PersonPanel(String label, Set<Person> persons)
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

    /**
     * The list model for the persons list.
     * */
    private static class PersonListModel extends AbstractListModel<Person>
    {
        /** The list of persons. */
        private final List<Person> persons;

        /**
         * Creates a new PersonListModel.
         *
         * @param persons the persons in the list.
         * */
        public PersonListModel(Collection<Person> persons)
        {
            this.persons = new ArrayList<>(persons);
        }

        /**
         * Adds an item to the list and
         * sorts the data.
         *
         * @param p The person to be added.
         * */
        public void addItem(Person p)
        {
            this.persons.add(p);
            this.sort();
        }

        /**
         * Adds all the items to the list
         * and sorts it.
         *
         * @param persons the persons to be added.
         * */
        public void addItems(Collection<Person> persons)
        {
            this.persons.addAll(persons);
            this.sort();
        }

        /**
         * Sorts the data.
         * */
        public void sort()
        {
            this.persons.sort(Comparator.comparing(Person::getName));
            fireContentsChanged(this, 0, persons.size());
        }

        /**
         * Removes all persons.
         * */
        public void removeAll()
        {
            this.persons.clear();
            // Index1 is 0, since java persons will be cleared.
            fireContentsChanged(this, 0, 0);
        }

        @Override
        public int getSize()
        {
            return persons.size();
        }

        @Override
        public Person getElementAt(int index)
        {
            return persons.get(index);
        }
    }
}
