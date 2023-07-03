package se.skorup.gui.components;

import se.skorup.group.Group;
import se.skorup.group.Person;
import se.skorup.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * A wrapper for a JList&lt;Person&gt;.
 * */
public class PersonList extends JList<Person>
{
    /**
     * Creates a new PersonList.
     * */
    public PersonList()
    {
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Sets the item of the list to the
     * members of the group.
     *
     * @param g the members of the group.
     * */
    public void setGroup(Group g)
    {
        var people = g.getPersons();
        Collections.sort((List<Person>) people);
        this.setListData(new Vector<>(people));
    }
}
