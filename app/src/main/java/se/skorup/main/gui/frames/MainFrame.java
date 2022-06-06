package se.skorup.main.gui.frames;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.panels.ButtonPanel;
import se.skorup.main.gui.panels.ControlPanel;
import se.skorup.main.gui.panels.PersonPanel;
import se.skorup.main.gui.panels.SidePanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.manager.helper.SerializationManager;
import se.skorup.main.objects.Person;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The MainFrame of the GUI.
 * */
public class MainFrame extends JFrame
{
    private static final String savePath =
        "%ssaves/save.data".formatted(Utils.getFolderName());

    private final List<GroupManager> managers = new ArrayList<>();

    private GroupManager currentGroupManager;

    private final Container cp = this.getContentPane();

    private ControlPanel ctrPanel;
    private ButtonPanel btnPanel;
    private SidePanel sidePanel;
    private PersonPanel personPanel;

    private final JPanel pPersonContainer = new JPanel();
    private final JPanel pPersonContainerContainer = new JPanel();
    private final JPanel pContainer = new JPanel();

    private final BorderLayout layout = new BorderLayout();

    /**
     * Creates a new MainFrame.
     * */
    public MainFrame()
    {
        super("Gruppskapare");
        this.init();
    }

    /**
     * Adds a demo group if debug param is {@code true},
     * else it will try to read from the file.
     * */
    @SuppressWarnings("unchecked")
    private void addGroups()
    {
        try
        {
            managers.addAll((List<GroupManager>) SerializationManager.deserializeObject(savePath));
        }
        catch (Exception e)
        {
            // TODO: Handle Error
            DebugMethods.log(
                "Failed to load save: %s".formatted(e.getLocalizedMessage()),
                DebugMethods.LogType.ERROR
            );
        }
    }

    /**
     * Initializes the frame.
     * */
    private void init()
    {
        DebugMethods.log("Starting initialization of MainFrame.", DebugMethods.LogType.DEBUG);
        this.addGroups();
        this.currentGroupManager = (managers.size() != 0) ? managers.get(0) : null;
        DebugMethods.log("GroupManagers initialized.", DebugMethods.LogType.DEBUG);
        this.setProperties();
        DebugMethods.log("The properties has been set.", DebugMethods.LogType.DEBUG);
        sidePanel.refreshLists();
        DebugMethods.log("Added list data.", DebugMethods.LogType.DEBUG);
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
        pPersonContainer.add(new JLabel("   "));
        pPersonContainer.add(personPanel);

        pPersonContainerContainer.add(new JLabel("<html><br><br><br></html>")); // Not hacky at all :)
        pPersonContainerContainer.add(pPersonContainer);

        pContainer.add(ctrPanel, BorderLayout.PAGE_START);
        pContainer.add(sidePanel, BorderLayout.LINE_START);
        pContainer.add(pPersonContainerContainer, BorderLayout.CENTER);
        pContainer.add(btnPanel, BorderLayout.PAGE_END);

        cp.add(new JLabel(" "), BorderLayout.PAGE_START);
        cp.add(new JLabel("   "), BorderLayout.LINE_START);
        cp.add(pContainer, BorderLayout.CENTER);
        cp.add(new JLabel("   "), BorderLayout.LINE_END);
        cp.add(new JLabel(" "), BorderLayout.PAGE_END);
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
        this.setSize(new Dimension(1200, 685));
        this.setResizable(false);
        this.addWindowStateListener(sidePanel);
        this.addWindowStateListener(personPanel);
        this.addComponentListener(personPanel);

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);

        pContainer.setBackground(Utils.BACKGROUND_COLOR);
        pContainer.setLayout(new BorderLayout());

        pPersonContainer.setBackground(Utils.BACKGROUND_COLOR);
        pPersonContainer.setLayout(new FlowLayout(FlowLayout.LEFT));

        pPersonContainerContainer.setBackground(Utils.BACKGROUND_COLOR);
        pPersonContainerContainer.setLayout(new BoxLayout(pPersonContainerContainer, BoxLayout.Y_AXIS));

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveGroupManagers));
    }

    /**
     * Saves the group managers iff debug is false.
     *
     * @return {@code true} iff it saved correctly.
     * */
    public boolean saveGroupManagers()
    {
        try
        {
            DebugMethods.log("Starting saving process.", DebugMethods.LogType.DEBUG);
            SerializationManager.createFileIfNotExists(new File(savePath));
            SerializationManager.serializeObject(savePath, managers);
            DebugMethods.log("Saving process finished correctly.", DebugMethods.LogType.DEBUG);

            return true;
        }
        catch (Exception e)
        {
            // TODO: handle error
            e.printStackTrace();

            DebugMethods.log(
                "Saving process failed: %s".formatted(e.getLocalizedMessage()),
                DebugMethods.LogType.ERROR
            );

            return false;
        }
    }

    /**
     * Sets the current group manager and refreshes the lists.
     * If the passed index is out of bounds it will return
     * without doing anything.
     *
     * @param index the index of the current group manager.
     * */
    public void setCurrentGroupManager(int index)
    {
        if (index < 0 || index >= managers.size())
            return;

        this.currentGroupManager = managers.get(index);

        if (sidePanel != null && personPanel != null)
        {
            sidePanel.refreshLists();
            personPanel.setPerson(null);
        }
    }

    /**
     * Refreshes the list data.
     * */
    public void refreshSidePanel()
    {
        sidePanel.refreshLists();
        personPanel.setPerson(null);
    }

    /**
     * Updates the person being edited.
     *
     * @param p the person being edited.
     *          If {@code null} then it
     *          will clear the selection.
     * */
    public void updatePerson(Person p)
    {
        personPanel.setPerson(p);
    }

    /**
     * Adds a new GroupManager to the frame.
     *
     * @param manager the new GroupManager.
     * */
    public void addGroupManager(GroupManager manager)
    {
        this.managers.add(manager);
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

    /**
     * Removes the current group manager and
     * updates the GUI accordingly.
     * */
    public void removeCurrentGroupManager()
    {
        if (currentGroupManager == null)
            return;

        managers.remove(currentGroupManager);
        currentGroupManager = null;

        if (managers.size() > 0)
            currentGroupManager = managers.get(0);

        refreshSidePanel();
        ctrPanel.updateManagers();
    }

    /**
     * Sets the data of the two lists.
     *
     * @param candidates the persons in the candidates list.
     * @param leaders the persons in the leaders list.
     * */
    public void setSideListData(Set<Person> candidates, Set<Person> leaders)
    {
        if (sidePanel != null)
        {
            sidePanel.setListData(candidates, leaders);
        }
    }
}
