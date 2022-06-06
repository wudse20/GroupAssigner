package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.PersonList;
import se.skorup.main.gui.interfaces.PersonCallback;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

/**
 * The panel that lists the persons.
 * */
public class PersonListPanel extends JPanel
{
    private final JLabel lblGroupInfo;
    private final PersonList listPersons;
    private final JScrollPane scrListPersons;
    private final BorderLayout layout = new BorderLayout();

    /**
     * Creates a new PersonPanel.
     *
     * @param label the label of the list.
     * @param persons the persons in the list.
     * */
    public PersonListPanel(String label, Set<Person> persons)
    {
        this.listPersons = new PersonList(new ArrayList<>(), ListSelectionModel.SINGLE_SELECTION);
        this.scrListPersons = new JScrollPane(listPersons);
        this.lblGroupInfo = new JLabel(label);

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
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setLayout(layout);

        lblGroupInfo.setForeground(Utils.FOREGROUND_COLOR);
        scrListPersons.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
    }

    /**
     * Adds callback p. Wrapper method calls:
     * {@link PersonList#addCallback addCallback}
     *
     * @param p the callback to be added.
     * */
    public void addCallback(PersonCallback p)
    {
        listPersons.addCallback(p);
    }

    /**
     * Updates the list with a new
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

        listPersons.updateList(persons);
    }

    /**
     * Deselects all selected items.
     * */
    public void deselectAll()
    {
        listPersons.setSelectedIndices(new int[0]);
    }

    @Override
    public void setForeground(Color c)
    {
        if (lblGroupInfo != null)
            lblGroupInfo.setForeground(c);

        super.setForeground(c);
    }
}
