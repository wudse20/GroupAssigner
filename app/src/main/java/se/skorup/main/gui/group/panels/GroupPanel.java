package se.skorup.main.gui.group.panels;

import se.skorup.group.Group;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;

import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

/**
 * The panel responsible for displaying the groups.
 * */
public class GroupPanel extends Panel
{
    private final Label lblTitle = new Label("ui.label.my-groups", true);
    private final GroupDisplayPanel gdp = new GroupDisplayPanel(List.of(new Group("kaka"), new Group("Banan")));

    /**
     * Creates a new GroupPanel.
     * */
    public GroupPanel()
    {
        super(null);
        setProperties();
        addComponents();
    }

    /**
     * Sets the properties of the components.
     * */
    private void setProperties()
    {
        lblTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        var cont = new Panel(new FlowLayout(FlowLayout.CENTER));
        cont.add(lblTitle);
        this.add(cont);
        this.add(gdp);
    }
}
