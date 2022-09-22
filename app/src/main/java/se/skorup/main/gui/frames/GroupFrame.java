package se.skorup.main.gui.frames;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.groups.creators.GroupCreator;
import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.gui.panels.CalculatorPanel;
import se.skorup.main.gui.panels.GroupButtonPanel;
import se.skorup.main.gui.panels.StatisticsPanel;
import se.skorup.main.gui.panels.SubgroupPanel;
import se.skorup.main.gui.panels.SubgroupSettingsPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
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
    private boolean shouldUseOneMainGroup = false;
    private boolean shouldOverflow = false;
    private boolean shouldDisplayMainGroups = false;

    private State sizeState = State.NUMBER_GROUPS;

    private final List<ActionCallback> callbacks = new Vector<>();
    private final GroupManager manager;

    private final Container cp = this.getContentPane();
    private final BorderLayout layout = new BorderLayout();

    private JTabbedPane tabs;

    private final SubgroupSettingsPanel sgsp;
    private final SubgroupPanel sgp;
    private final CalculatorPanel calc;
    private final GroupButtonPanel gbp;
    private final StatisticsPanel sp;

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
        this.gbp = new GroupButtonPanel();
        this.sgsp = new SubgroupSettingsPanel(this);
        this.sgp = new SubgroupPanel(this);
        this.calc = new CalculatorPanel(manager);
        this.sp = new StatisticsPanel(manager);

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
        this.setSize(new Dimension(450, 550));
        this.setVisible(true);
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

        gbp.addActionListener(e -> this.dispose(), GroupButtonPanel.Buttons.CLOSE);
        gbp.addActionListener(e -> Utils.openHelpPages(), GroupButtonPanel.Buttons.HELP);
        gbp.addActionListener(e -> {
            if (!(tabs.getSelectedComponent() instanceof SubgroupPanel))
                tabs.setSelectedIndex(2);
        }, GroupButtonPanel.Buttons.CREATE);
        gbp.addActionListener(e -> {
            if (!(tabs.getSelectedComponent() instanceof SubgroupPanel))
                tabs.setSelectedIndex(2);
        }, GroupButtonPanel.Buttons.LOAD);
        gbp.addActionListener(e -> {
            shouldDisplayMainGroups = !shouldDisplayMainGroups;
            ((JButton) e.getSource()).setText(shouldDisplayMainGroups ? "Dölj huvudgrupper" : "Visa huvudgrupper");
            sgp.setMainGroupDisplay(shouldDisplayMainGroups);
        }, GroupButtonPanel.Buttons.TOGGLE_MAIN_GROUPS);
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        tabs.addTab("Inställningar", sgsp);
        tabs.addTab("Miniräknare", calc);
        tabs.addTab("Undergrupper", sgp);
        tabs.addTab("Statistik", sp);

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
     * @param r the action to be run.
     * */
    public void waitCursorAction(Runnable r)
    {
        SwingUtilities.invokeLater(() -> {
            for (var c : this.getComponents())
                c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        });

        try
        {
            r.run();
        }
        catch (Exception e)
        {
            SwingUtilities.invokeLater(() -> {
                for (var c : this.getComponents())
                    c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            });

            throw e;
        }

        SwingUtilities.invokeLater(() -> {
            for (var c : this.getComponents())
                c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        });
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
    public boolean shouldUseOneMainGroup()
    {
        return shouldUseOneMainGroup;
    }

    /**
     * Setter for: shouldUseMainGroups
     *
     * @param shouldUseMainGroups the new value of shouldUseMainGroups.
     * */
    public void shouldUseOneMainGroup(boolean shouldUseMainGroups)
    {
        this.shouldUseOneMainGroup = shouldUseMainGroups;
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
     * Getter for: Manager
     *
     * @return the current group manager.
     * */
    public GroupManager getManager()
    {
        return manager;
    }

    /**
     * Gets the current group selector.
     *
     * @return the currently selected group selector.
     * */
    public GroupCreator getGroupSelectedGroupCreator()
    {
        return sgsp.getGroupSelectedGroupCreator();
    }

    /**
     * Gets the currently selected MainGroup.
     *
     * @return the currently selected MainGroup;
     * */
    public Person.MainGroup getMainGroup()
    {
        return sgsp.getMainGroup();
    }

    /**
     * Gets the user input from the selected size setting.
     *
     * @return a list containing all the different sizes
     *         of the groups. If it isn't the first
     * */
    public List<Integer> getUserInput()
    {
        return sgsp.getUserInput();
    }


    /**
     * Getter for: cbCreators
     *
     * @return the instance of the cbCreators.
     * */
    public JComboBox<GroupCreator> getCbCreators()
    {
        return sgsp.getCbCreators();
    }

    /**
     * Setter for: shouldUseMainGroups
     *
     * @return the state of shouldUseMainGroups.
     * */
    public boolean shouldUseMainGroups()
    {
        return shouldUseMainGroups;
    }

    /**
     * Getter for: shouldUseMainGroups
     *
     * @param shouldUseMainGroups the new state of shouldUseMainGroups.
     * */
    public void shouldUseMainGroups(boolean shouldUseMainGroups)
    {
        this.shouldUseMainGroups = shouldUseMainGroups;
    }

    /**
     * Updates the statistics of the frame.
     *
     * @param sg the subgroups that the statistics should be based on.
     * */
    public void updateStatistics(Subgroups sg)
    {
        sp.updateStatistics(sg);
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
        var dim = Toolkit.getDefaultToolkit().getScreenSize();

        if (tabs.getSelectedComponent() instanceof SubgroupSettingsPanel)
        {
            DebugMethods.log("Selected settings", DebugMethods.LogType.DEBUG);
            gbp.populateButtons(sgsp);
            this.setSize(new Dimension(450, 625));
        }
        else if (tabs.getSelectedComponent() instanceof SubgroupPanel)
        {
            DebugMethods.log("Selected subgroups", DebugMethods.LogType.DEBUG);
            gbp.populateButtons(sgp);
            this.setSize(new Dimension(1200, 685));
        }
        else if (tabs.getSelectedComponent() instanceof CalculatorPanel cp)
        {
            DebugMethods.log("Selected calculator", DebugMethods.LogType.DEBUG);
            gbp.populateButtons(calc);
            cp.updateConstantButtons();
            this.setSize(new Dimension(460, 800));
        }
        else if (tabs.getSelectedComponent() instanceof StatisticsPanel sp)
        {
            DebugMethods.log("Selected statitics", DebugMethods.LogType.DEBUG);
            gbp.populateButtons(sp);
            this.setSize(new Dimension(540, 685));
        }
        else
        {
            DebugMethods.log("Selected no panel, bug", DebugMethods.LogType.ERROR);
        }

        this.setLocation(
            dim.width / 2 - this.getSize().width / 2,
            dim.height / 2 - this.getSize().height / 2
        );
    }
}
