package se.skorup.main.gui.frames;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.panels.ControlPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * The MainFrame of the GUI.
 * */
public class MainFrame extends JFrame
{
    /** The group managers. */
    private final List<GroupManager> managers = new ArrayList<>();

    /** Adds a demo group if {@code true}. */
    private final boolean debug = true;

    /** The frame's container. */
    private final Container cp = this.getContentPane();

    /** The layout of the frame. */
    private final BorderLayout layout = new BorderLayout();

    /** The layout of the container. */
    private final BorderLayout pContainerLayout = new BorderLayout();

    /** The control panel of this frame. */
    private ControlPanel ctrPanel;

    /** The container panel. */
    private final JPanel pContainer = new JPanel();

    /** Spacer */
    private final JLabel lblSpacer1 = new JLabel(" ");

    /** Spacer */
    private final JLabel lblSpacer2 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer3 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer4 = new JLabel("   ");

    /**
     * Creates a new MainFrame.
     * */
    public MainFrame()
    {
        super("Gruppskapare");
        this.demoGroup();
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
        pContainer.add(ctrPanel, BorderLayout.PAGE_START);

        cp.add(lblSpacer1, BorderLayout.PAGE_START);
        cp.add(lblSpacer2, BorderLayout.LINE_START);
        cp.add(ctrPanel, BorderLayout.CENTER);
        cp.add(lblSpacer3, BorderLayout.LINE_END);
        cp.add(lblSpacer4, BorderLayout.PAGE_END);
    }

    /**
     * Sets the properties of the frame and
     * its components.
     * */
    private void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(400, 300));

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);

        ctrPanel = new ControlPanel(this, managers);

        pContainer.setBackground(Utils.BACKGROUND_COLOR);
        pContainer.setLayout(pContainerLayout);
    }
}
