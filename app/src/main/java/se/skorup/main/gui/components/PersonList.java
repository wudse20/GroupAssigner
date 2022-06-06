package se.skorup.main.gui.components;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.interfaces.PersonCallback;
import se.skorup.main.gui.models.PersonListModel;
import se.skorup.main.objects.Person;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * A JList wrapper for persons.
 * */
public class PersonList extends JList<Person> implements ListSelectionListener, MouseListener
{
    private final PersonListModel model;
    private final List<PersonCallback> callbacks = new ArrayList<>();
    private int lastIndex = -1;

    /**
     * Creates a new person list.
     *
     * @param persons the list that will be linked to the model.
     * @param selectionMode the model for selecting. Allowed values: <br>
     *                      - {@link javax.swing.ListSelectionModel#SINGLE_SELECTION SINGLE_SELECTION} <br>
     *                      - {@link javax.swing.ListSelectionModel#SINGLE_INTERVAL_SELECTION SINGLE_INTERVAL_SELECTION} <br>
     *                      - {@link javax.swing.ListSelectionModel#MULTIPLE_INTERVAL_SELECTION MULTIPLE_INTERVAL_SELECTION}
     * @throws IllegalArgumentException iff selectionMode isn't in range [0, 2]; i.e. non of the listed values above.
     * */
    public PersonList(List<Person> persons, int selectionMode) throws IllegalArgumentException
    {
        if (selectionMode > 2 || selectionMode < 0)
            throw new IllegalArgumentException("Argument must be in range [0, 2]");

        this.model = new PersonListModel(persons);
        this.setModel(model);
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.addListSelectionListener(this);
        this.addMouseListener(this);
        this.setSelectionMode(selectionMode);
    }

    /**
     * Updates the list.
     *
     * @param persons updates the list with new persons.
     * */
    public void updateList(Set<Person> persons)
    {
        model.removeAll();
        model.addItems(persons);
        this.clearSelection();
    }

    /**
     * Adds a callback iff {@code p != null}.
     *
     * @param p the callback to be added iff {@code p != null}.
     * */
    public void addCallback(PersonCallback p)
    {
        if (p != null)
            callbacks.add(p);
    }

    /**
     * Invokes all callbacks.
     *
     * @param persons the selected persons.
     * */
    private void invokeCallbacks(List<Person> persons)
    {
        callbacks.forEach(p -> p.action(persons));
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        // Preventing double events.
        if (!e.getValueIsAdjusting())
        {
            int index = this.getSelectedIndex();

            if (index != -1)
            {
                lastIndex = index;
                var list = new ArrayList<Person>();
                Arrays.stream(this.getSelectedIndices())
                      .mapToObj(model::getElementAt)
                      .forEach(list::add);

                invokeCallbacks(list); // Selection
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        var point = e.getPoint();
        var index = this.locationToIndex(point);

        if (this.isSelectedIndex(index) && index != lastIndex)
        {
            var arr = Arrays.stream(this.getSelectedIndices()).filter(x -> x != index).toArray();
            this.setSelectedIndices(arr);
            var list = new ArrayList<Person>();
            Arrays.stream(this.getSelectedIndices())
                    .mapToObj(model::getElementAt)
                    .forEach(list::add);
            invokeCallbacks(list); // Deselection
            return;
        }

        lastIndex = -1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
