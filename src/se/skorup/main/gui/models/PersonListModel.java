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
