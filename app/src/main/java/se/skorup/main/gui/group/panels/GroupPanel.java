package se.skorup.main.gui.group.panels;

import se.skorup.group.Group;
import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.main.gui.group.frames.GroupFrame;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

/**
 * The panel responsible for displaying the groups.
 * */
public class GroupPanel extends Panel
{
    enum State
    {
        NO_SELECTED, SELECTED
    }

    private State state = State.NO_SELECTED;

    private List<Group> groups;

    private final Label lblTitle = new Label("ui.label.my-groups", true);

    private final GroupDisplayPanel gdp = new GroupDisplayPanel();
    private final PersonPanel pp = new PersonPanel();

    private final GroupFrame gf;

    /**
     * Creates a new GroupPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public GroupPanel(GroupFrame gf)
    {
        super(new BorderLayout());
        this.gf = gf;

        setProperties();
        addComponents();
    }

    /**
     * Sets the properties of the components.
     * */
    private void setProperties()
    {
        lblTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
        gdp.addCallback((p, g) -> {
            if (p == null || g == null)
            {
                state = State.NO_SELECTED;
                addComponents();
                return;
            }

            state = State.SELECTED;
            pp.displayPerson(p, g);
            addComponents();
        });

        pp.addCallback((g, p) -> {
            g.removePerson(p.id());
            gdp.setGroups(groups);
            gdp.clearSelection();
        });
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.removeAll();

        var cont = new Panel(null);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));

        var titleCont = new Panel(new FlowLayout(FlowLayout.LEFT));
        titleCont.add(lblTitle);

        cont.add(titleCont);
        cont.add(gdp);

        this.add(cont, BorderLayout.CENTER);

        if (state.equals(State.SELECTED))
        {
            this.add(new ComponentContainer(pp), BorderLayout.LINE_END);
        }

        this.revalidate();
        gf.pack();
    }

    /**
     * Sets the groups to be displayed.
     *
     * @param groups the groups that are supposed to be displayed.
     * */
    public void setGroups(List<Group> groups)
    {
        gdp.setGroups(groups);
        this.groups = groups;
    }
}
