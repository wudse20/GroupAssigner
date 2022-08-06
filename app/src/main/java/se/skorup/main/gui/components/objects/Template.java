package se.skorup.main.gui.components.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A template in the CSVFrame.
 * */
public class Template implements Iterable<TemplateItem>
{
    private final List<TemplateItem> items;

    /**
     * Creates a new template.
     * */
    public Template()
    {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a template to the list.
     *
     * @param item the item to be added.
     * */
    public void addTemplateItem(TemplateItem item)
    {
        if (item != null)
            items.add(item);
    }

    /**
     * Gets the size, i.e. the amount of templates items.
     *
     * @return the number of template items in this template.
     * */
    public int size()
    {
        return items.size();
    }

    @Override
    public int hashCode()
    {
        return items.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof Template t &&
               t.items.equals(items);
    }

    @Override
    public String toString()
    {
        return "Template: %s".formatted(items);
    }

    @Override
    public Iterator<TemplateItem> iterator()
    {
        return items.iterator();
    }
}
