package se.skorup.main.gui.frames;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.panels.ButtonPanel;
import se.skorup.main.gui.panels.ControlPanel;
import se.skorup.main.gui.panels.PersonPanel;
import se.skorup.main.gui.panels.SidePanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * The MainFrame of the GUI.
 * */
public class MainFrame extends JFrame
{
    /** The group managers. */
    private final List<GroupManager> managers = new ArrayList<>();

    private GroupManager currentGroupManager;

    /** Adds a demo group iff {@code true}. */
    private final boolean debug = true;

    /** The frame's container. */
    private final Container cp = this.getContentPane();

    /** The control panel of this frame. */
    private ControlPanel ctrPanel;

    /** The button panel of this frame. */
    private ButtonPanel btnPanel;

    /** The side panel. */
    private SidePanel sidePanel;

    /** The panel for persons. */
    private PersonPanel personPanel;

    /** The container for the person panel. */
    private final JPanel pPersonContainer = new JPanel();

    /** The container for the person container panel. */
    private final JPanel pPersonContainerContainer = new JPanel();

    /** The container panel. */
    private final JPanel pContainer = new JPanel();

    /** The layout for pPersonContainerContainer. */
    private final BoxLayout pPersonContainerContainerLayout =
            new BoxLayout(pPersonContainerContainer, BoxLayout.Y_AXIS);

    /** The layout of the frame. */
    private final BorderLayout layout = new BorderLayout();

    /** The layout of the container. */
    private final BorderLayout pContainerLayout = new BorderLayout();

    /** The layout for the person container. */
    private final FlowLayout pPersonContainerLayout = new FlowLayout(FlowLayout.LEFT);

    /** Spacer */
    private final JLabel lblSpacer1 = new JLabel(" ");

    /** Spacer */
    private final JLabel lblSpacer2 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer3 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer4 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer5 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer6 = new JLabel("<html><br><br></html>");

    /**
     * Creates a new MainFrame.
     * */
    public MainFrame()
    {
        super("Gruppskapare");
        this.init();
    }

    /**
     * Adds a demo group if debug param is {@code true}.
     * */
    private void demoGroup()
    {
        if (debug)
        {
            var gm = new GroupManager("DEMO - Grupp");
            gm.registerPerson("Anton", Person.Role.LEADER);
            gm.registerPerson("Sebbe", Person.Role.CANDIDATE);

            managers.add(gm);
        }
    }

    /**
     * Initializes the frame.
     * */
    private void init()
    {
        DebugMethods.log("Starting initialization of MainFrame.", DebugMethods.LogType.DEBUG);
        this.demoGroup();
        this.currentGroupManager = (managers.size() != 0) ? managers.get(0) : null;
        DebugMethods.log("GroupManagers initialized.", DebugMethods.LogType.DEBUG);
        this.setProperties();
        DebugMethods.log("The properties has been set.", DebugMethods.LogType.DEBUG);
        this.addComponents();
        DebugMethods.log("The components has been added.", DebugMethods.LogType.DEBUG);
        this.setVisible(true);
        DebugMethods.log("Frame is now initialized", DebugMethods.LogType.DEBUG);
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        pPersonContainer.add(lblSpacer5);
        pPersonContainer.add(personPanel);

        pPersonContainerContainer.add(lblSpacer6);
        pPersonContainerContainer.add(pPersonContainer);

        pContainer.add(ctrPanel, BorderLayout.PAGE_START);
        pContainer.add(sidePanel, BorderLayout.LINE_START);
        pContainer.add(pPersonContainerContainer, BorderLayout.CENTER);
        pContainer.add(btnPanel, BorderLayout.PAGE_END);

        cp.add(lblSpacer1, BorderLayout.PAGE_START);
        cp.add(lblSpacer2, BorderLayout.LINE_START);
        cp.add(pContainer, BorderLayout.CENTER);
        cp.add(lblSpacer3, BorderLayout.LINE_END);
        cp.add(lblSpacer4, BorderLayout.PAGE_END);
    }

    /**
     * Sets the properties of the frame and
     * its components.
     * */
    private void setProperties()
    {
        ctrPanel = new ControlPanel(this, managers);
        btnPanel = new ButtonPanel(this);
        sidePanel = new SidePanel(this);
        personPanel = new PersonPanel(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(1200, 600));
        this.addWindowStateListener(sidePanel);

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);

//        var pArr = new Person[0];
//        personPanel.setPerson(getCurrentGroup().getAllPersons().toArray(pArr)[0]);
        personPanel.setPerson(null);

        pContainer.setBackground(Utils.BACKGROUND_COLOR);
        pContainer.setLayout(pContainerLayout);

        pPersonContainer.setBackground(Utils.BACKGROUND_COLOR);
        pPersonContainer.setLayout(pPersonContainerLayout);

        pPersonContainerContainer.setBackground(Utils.BACKGROUND_COLOR);
        pPersonContainerContainer.setLayout(pPersonContainerContainerLayout);
    }

    /**
     * Sets the current group manager.
     *
     * @param index the index of the current group manager.
     * */
    public void setCurrentGroupManager(int index)
    {
        this.currentGroupManager = managers.get(index);
    }

    /**
     * Gets the current group manager.
     *
     * @return the current group manager, iff no exist
     *         managers it will return {@code null}.
     * */
    public GroupManager getCurrentGroup()
    {
        return currentGroupManager;
    }
}
