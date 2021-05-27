package se.skorup.main.gui.models;

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
     * Adds all the items to the list.
     *
     * @param names the persons to be added.
     * */
    public void addItems(Collection<String> names)
    {
        this.names.addAll(names);
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
     * Removes all the items.
     * */
    public void removeAll()
    {
        this.names.clear();

        // fireContentsChange(..., 0, 0) since the size of the list will
        // always be zero, no other possibilities.
        fireContentsChanged(this, 0, 0);
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
