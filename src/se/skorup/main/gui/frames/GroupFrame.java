package se.skorup.main.gui.frames;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.gui.panels.SubgroupPanel;
import se.skorup.main.gui.panels.SubgroupSettingsPanel;
import se.skorup.main.manager.GroupManager;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

/**
 * The frame used to create the groups.
 * */
public class GroupFrame extends JFrame implements ChangeListener
{
    /** The common path of all subgroups. */
    public final String BASE_GROUP_PATH;

    /** The list with all the callbacks. */
    private final List<ActionCallback> callbacks = new Vector<>();

    /** The current group. */
    private final GroupManager manager;

    /** The container of the frame. */
    private final Container cp = this.getContentPane();

    /** The tabs. */
    private JTabbedPane tabs;

    /** The layout of the frame. */
    private final BorderLayout layout = new BorderLayout();

    /** The settings panel. */
    private final SubgroupSettingsPanel sgsp;

    /** The subgroup panel. */
    private final SubgroupPanel sgp;

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

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(424, 416));
        this.setVisible(true);
        this.setResizable(false);

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
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        tabs.addTab("Inställningar", sgsp);
        tabs.addTab("Undergrupper", sgp);

        this.add(tabs, BorderLayout.CENTER);
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
            this.setSize(new Dimension(424, 416));
        }
        else if (tabs.getSelectedComponent() instanceof SubgroupPanel)
        {
            DebugMethods.log("Selected subgroups", DebugMethods.LogType.DEBUG);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        else
        {
            DebugMethods.log("Selected no panel bug", DebugMethods.LogType.ERROR);
        }
    }
}
