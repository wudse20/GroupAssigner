package se.skorup.main.gui.group.panels;

import se.skorup.group.Group;
import se.skorup.group.MainGroup;
import se.skorup.gui.components.buttons.Button;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.containers.FlowContainer;
import se.skorup.gui.components.containers.Panel;
import se.skorup.gui.components.list.PersonList;
import se.skorup.gui.components.containers.ScrollPane;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Vector;

/**
 * The panel responsible for handling main groups.
 * */
public class MainGroupPanel extends Panel
{
    private Group g;

    private final PersonList plMg1 = new PersonList();
    private final PersonList plMg2 = new PersonList();

    private final Button btnRight = new Button("ui.button.right-arrow");
    private final Button btnLeft = new Button("ui.button.left-arrow");

    /**
     * Creates a new panel.
     * */
    public MainGroupPanel()
    {
        super(new BorderLayout());
        this.addComponents();

        btnLeft.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        btnRight.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        btnRight.addActionListener(e -> handleButtonPress(plMg1, MainGroup.TWO));
        btnLeft.addActionListener(e -> handleButtonPress(plMg2, MainGroup.ONE));
    }

    /**
     * The code for handling a button press.
     *
     * @param list the affected list.
     * @param mg the new main-group.
     * */
    private void handleButtonPress(PersonList list, MainGroup mg)
    {
        if (list.getSelectedIndex() == -1 || g == null)
            return;

        var p = list.getSelectedValue();
        g.setMainGroup(p.id(), mg);
        populateGroup(g);
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        var bp = new Panel(null);
        bp.setLayout(new BoxLayout(bp, BoxLayout.Y_AXIS));
        bp.add(new ComponentContainer(new FlowContainer(btnRight, FlowLayout.CENTER)));
        bp.add(new ComponentContainer(new FlowContainer(btnLeft, FlowLayout.CENTER)));

        var cont = new Panel(new BorderLayout());
        cont.add(fixList(plMg1), BorderLayout.LINE_START);
        cont.add(new ComponentContainer(bp), BorderLayout.CENTER);
        cont.add(fixList(plMg2), BorderLayout.LINE_END);

        this.add(new ComponentContainer(cont), BorderLayout.CENTER);
    }

    /**
     * Fixes a list sizing and fitting, so it
     * looks nice.
     *
     * @param list the list to be fixed.
     * @return the correctly fixed list.
     * */
    private Component fixList(PersonList list)
    {
        var scr = new ScrollPane(list);
        scr.setPreferredSize(new Dimension(scr.getPreferredSize().width, scr.getPreferredSize().height));

        var p = new Panel(null);
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.add(scr);
        return p;
    }

    /**
     * Populates a group in the GUI.
     *
     * @param g the group to be populated.
     * */
    public void populateGroup(Group g)
    {
        if (g == null)
            return;

        this.g = g;

        // Safe since it's a new instance.
        var mg1 = g.getMainGroupOne();
        var mg2 = g.getMainGroupTwo();

        plMg1.setListData(new Vector<>(mg1));
        plMg2.setListData(new Vector<>(mg2));
    }
}
