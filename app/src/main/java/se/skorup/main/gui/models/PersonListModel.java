package se.skorup.main.gui.models;

import se.skorup.main.objects.Person;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * The list model for the persons list.
 * */
public class PersonListModel extends AbstractListModel<Person>
{
    /** The list of persons. */
    private final List<Person> persons;

    /**
     * Creates a new PersonListModel. If persons
     * is null an empty array list will take its
     * place.
     *
     * @param persons the persons in the list. If
     *                {@code null} an empty ArrayList
     *                will be created.
     * */
    public PersonListModel(Collection<Person> persons)
    {
        if (persons != null)
            this.persons = new ArrayList<>(persons);
        else
            this.persons = new ArrayList<>();
    }

    /**
     * Adds an item to the list and
     * sorts the data.
     *
     * @param p The person to be added.
     * */
    public void addItem(Person p)
    {
        var index = persons.size();

        persons.add(p);
        this.persons.sort(Comparator.comparing(Person::getName));

        fireIntervalAdded(this, index, index);
    }

    /**
     * Adds all the items to the list
     * and sorts it.
     *
     * @param persons the persons to be added.
     * */
    public void addItems(Collection<? extends Person> persons)
    {
        if (persons.isEmpty())
            return;

        int startIndex = getSize();

        this.persons.addAll(persons);
        this.persons.sort(Comparator.comparing(Person::getName));
        fireIntervalAdded(this, startIndex, getSize() - 1);
    }

    /**
     * Removes all persons.
     * */
    public void removeAll()
    {
        var index1 = persons.size()-1;
        persons.clear();

        if (index1 >= 0)
            fireIntervalRemoved(this, 0, index1);
    }

    /**
     * Removes an item from the List and
     * then sorts the list.
     *
     * @param p the person to be removed.
     * */
    public void removeItem(Person p)
    {
        var index = persons.indexOf(p);

        if (index == -1)
            return;

        persons.remove(index);
        fireIntervalRemoved(this, index, index);
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
