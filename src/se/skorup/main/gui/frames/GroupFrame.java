package se.skorup.main.gui.frames;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.gui.panels.GroupButtonPanel;
import se.skorup.main.gui.panels.SubgroupPanel;
import se.skorup.main.gui.panels.SubgroupSettingsPanel;
import se.skorup.main.manager.GroupManager;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

/**
 * The frame used to create the groups.
 * */
public class GroupFrame extends JFrame implements ChangeListener
{
    /** The state of the size setting. */
    public enum State
    {
        NUMBER_PERSONS, NUMBER_GROUPS,
        DIFFERENT_GROUP_SIZES, PAIR_WITH_LEADERS
    }

    /** The common path of all subgroups. */
    public final String BASE_GROUP_PATH;

    private boolean shouldUseMainGroups = false;
    private boolean shouldOverflow = false;

    private State sizeState = State.NUMBER_GROUPS;

    private final List<ActionCallback> callbacks = new Vector<>();

    private final GroupManager manager;

    private final Container cp = this.getContentPane();

    private JTabbedPane tabs;

    private final BorderLayout layout = new BorderLayout();

    private final SubgroupSettingsPanel sgsp;

    private final SubgroupPanel sgp;

    private final JScrollPane scrSgp;

    private final GroupButtonPanel gbp = new GroupButtonPanel();

    /**
     * Creates a new group frame.
     *
     * @param manager the group manager in use.
     * */
    public GroupFrame(GroupManager manager)
    {
        super("Skapa undergrupper!");

        this.manager = manager;
        this.BASE_GROUP_PATH = "%ssaves/subgroups/%s/".formatted(Utils.getFolderName(), manager.getName());
        this.sgsp = new SubgroupSettingsPanel(this);
        this.sgp = new SubgroupPanel(this);
        this.scrSgp = new JScrollPane(sgp);

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        var dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(424, 527));
        this.setVisible(true);
        this.setResizable(false);
        this.setLocation(
            dim.width / 2 - this.getSize().width / 2,
            dim.height / 2 - this.getSize().height / 2
        );

        cp.setLayout(layout);
        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setForeground(Utils.FOREGROUND_COLOR);

        UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource(Utils.FOREGROUND_COLOR)); // TYPO in JAVA???
        UIManager.put("TabbedPane.darkShadow", new ColorUIResource(Utils.FOREGROUND_COLOR));
        UIManager.put("TabbedPane.selected", new ColorUIResource(Utils.COMPONENT_BACKGROUND_COLOR));

        tabs = new JTabbedPane();
        tabs.setBackground(Utils.BACKGROUND_COLOR);
        tabs.setForeground(Utils.FOREGROUND_COLOR);
        tabs.addChangeListener(this);

        scrSgp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrSgp.setForeground(Utils.FOREGROUND_COLOR);
        scrSgp.setBackground(Utils.BACKGROUND_COLOR);

        gbp.addActionListener(e -> this.dispose(), GroupButtonPanel.Buttons.CLOSE);
        gbp.addActionListener(e -> Utils.openHelpPages(), GroupButtonPanel.Buttons.HELP);
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        tabs.addTab("Inställningar", sgsp);
        tabs.addTab("Undergrupper", scrSgp);

        this.add(tabs, BorderLayout.CENTER);
        this.add(gbp, BorderLayout.PAGE_END);
    }

    /**
     * Invokes the action callbacks.
     * */
    private void invokeActionCallbacks()
    {
        for (var c : callbacks)
            if (c != null)
                c.callback();
    }

    /**
     * Runs an action with the spiny, spiny
     * cursor.
     *
     * @param r the action to be ran.
     * */
    public void waitCursorAction(Runnable r)
    {
        for (var c : this.getComponents())
            c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try
        {
            r.run();
        }
        catch (Exception e)
        {
            for (var c : this.getComponents())
                c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            throw e;
        }

        for (var c : this.getComponents())
            c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }


    /**
     * Adds an action callback to the frame.
     *
     * @param ac the callback to be added. If {@code null}
     *           then it will do noting.
     * */
    public void addActionCallback(ActionCallback ac)
    {
        if (ac == null)
            return;

        callbacks.add(ac);
    }

    /**
     * Adds an action listener to a button.
     *
     * @param al the action listener to be added. If
     *           al is {@code null} then it will do
     *           nothing and just return.
     * @param button the button the action listener to
     *               be added.
     * */
    public void addActionListener(ActionListener al, GroupButtonPanel.Buttons button)
    {
        gbp.addActionListener(al, button);
    }

    /**
     * Getter for: shouldUseMainGroups
     *
     * @return the value of shouldUseMainGroups.
     * */
    public boolean shouldUseMainGroups()
    {
        return shouldUseMainGroups;
    }

    /**
     * Setter for: shouldUseMainGroups
     *
     * @param shouldUseMainGroups the new value of shouldUseMainGroups.
     * */
    public void shouldUseMainGroups(boolean shouldUseMainGroups)
    {
        this.shouldUseMainGroups = shouldUseMainGroups;
    }

    /**
     * Getter for: stateSize
     *
     * @return the current state setting.
     * */
    public State getSizeState()
    {
        return sizeState;
    }

    /**
     * Setter for: stateSize
     *
     * @param sizeState the new state setting.
     * */
    public void setSizeState(State sizeState)
    {
        this.sizeState = sizeState;
    }

    /**
     * Getter for: shouldOverflow.
     *
     * @return the value of overflow.
     * */
    public boolean shouldOverflow()
    {
        return shouldOverflow;
    }

    /**
     * Setter for: shouldOverflow.
     *
     * @param shouldOverflow the new value for shouldOverflow.
     * */
    public void setOverflow(boolean shouldOverflow)
    {
        this.shouldOverflow = shouldOverflow;
    }

    /**
     * Getter for: groupButtonPanel
     *
     * @return the instance of the groupButtonPanel.
     * */
    public GroupButtonPanel getGroupButtonPanel()
    {
        return gbp;
    }

    /**
     * Getter for: Manager
     *
     * @return the current group manager.
     * */
    public GroupManager getManager()
    {
        return manager;
    }

    @Override
    public void dispose()
    {
        this.invokeActionCallbacks();
        super.dispose();
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        if (tabs.getSelectedComponent() instanceof SubgroupSettingsPanel)
        {
            DebugMethods.log("Selected settings", DebugMethods.LogType.DEBUG);
            this.setSize(new Dimension(424, 527));
            gbp.populateButtons(sgsp);
        }
        else if (tabs.getSelectedComponent() instanceof JScrollPane)
        {
            DebugMethods.log("Selected subgroups", DebugMethods.LogType.DEBUG);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            gbp.populateButtons(sgp);
        }
        else
        {
            DebugMethods.log("Selected no panel, bug", DebugMethods.LogType.ERROR);
        }
    }
}
