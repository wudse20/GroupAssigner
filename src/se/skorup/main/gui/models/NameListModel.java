package se.skorup.main.gui.models;

import se.skorup.API.ImmutableArray;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The list model used for the names.
 * */
public class NameListModel extends AbstractListModel<String>
{
    /** The list of names. */
    private final List<String> names;

    /**
     * Creates a new name list model.
     *
     * @param names the names of the initial list.
     *              If {@code null} then it will
     *              create an empty list.
     * */
    public NameListModel(Collection<String> names)
    {
        if (names != null)
            this.names = new ArrayList<>(names);
        else
            this.names = new ArrayList<>();
    }

    /**
     * Adds an item to the list.
     *
     * @param name the new item added.
     * */
    public void addItem(String name)
    {
        this.names.add(name);
        fireContentsChanged(this, 0, names.size());
    }

    /**
     * Removes an item.
     *
     * @param name the item to be removed.
     * */
    public void removeItem(String name)
    {
        this.names.remove(name);
        fireContentsChanged(this, 0, names.size());
    }

    /**
     * Returns an ImmutableArray of all the items.
     *
     * @return an ImmutableArray of all the items.
     * */
    public ImmutableArray<String> getItems()
    {
        return ImmutableArray.fromList(names);
    }

    @Override
    public int getSize()
    {
        return names.size();
    }

    @Override
    public String getElementAt(int index)
    {
        return names.get(index);
    }
}
