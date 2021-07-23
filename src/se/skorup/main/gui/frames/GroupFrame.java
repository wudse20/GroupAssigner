package se.skorup.main.gui.frames;

import se.skorup.API.DebugMethods;
import se.skorup.API.ImmutableArray;
import se.skorup.API.Utils;
import se.skorup.main.groups.AlternateWishlistGroupCreator;
import se.skorup.main.groups.GroupCreator;
import se.skorup.main.groups.RandomGroupCreator;
import se.skorup.main.groups.WishlistGroupCreator;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.gui.interfaces.GroupGenerator;
import se.skorup.main.gui.panels.SettingPanel;
import se.skorup.main.gui.panels.SubgroupPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.manager.helper.SerializationManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The frame used to create the groups.
 * */
public class GroupFrame extends JFrame implements ComponentListener
{
    /** The common path of all subgroups. */
    private final String BASE_GROUP_PATH;

    /** The group manager in use. */
    private final GroupManager gm;

    /** The random group creator. */
    private final GroupCreator randomCreator;

    /** The wishlist group creator. */
    private final GroupCreator wishlistCreator;

    /** The alt. wishlist group creator. */
    private final GroupCreator altWishlistCreator;

    /** The current SubGroups. */
    private Subgroups currentGroups;

    /** The currently selected MainGroup. */
    private Person.MainGroup mainGroup = Person.MainGroup.MAIN_GROUP_1;

    /** The list with all the callbacks. */
    private final List<ActionCallback> callbacks = new Vector<>();

    /** This frames's container. */
    private final Container cp = this.getContentPane();

    /** The list with the different creators. */
    private final JComboBox<GroupCreator> cbCreator = new JComboBox<>();

    /** The button to create groups. */
    private final JButton btnCreate = new JButton("Skapa undergrupper");

    /** The button for closing. */
    private final JButton btnClose = new JButton("Stäng");

    /** The button for printing. */
    private final JButton btnPrint = new JButton("Skriv ut");

    /** The button for saving. */
    private final JButton btnSave = new JButton("Spara");

    /** The button for loading. */
    private final JButton btnLoad = new JButton("Ladda");

    /** The button for saving to a file. */
    private final JButton btnToFile = new JButton("Spara som textfil");

    /** The help button. */
    private final JButton btnHelp = new JButton("Hjälp!");

    /** The radio button for the first main group. */
    private final JRadioButton radioMainGroup1 = new JRadioButton("Huvudgrupp 1");

    /** The radio button for the first main group. */
    private final JRadioButton radioMainGroup2 = new JRadioButton("Huvudgrupp 2");

    /** The checkbox used for overflow. */
    private final JCheckBox boxOverflow = new JCheckBox("Skapa extra grupper ifall det inte går jämt upp.");

    /** The checkbox used for the MainGroups. */
    private final JCheckBox boxMainGroups = new JCheckBox("Använd huvudgrupper");

    /** The button group for the settings. */
    private final ButtonGroup bgSettings = new ButtonGroup();

    /** The button group for the MainGroups selectors. */
    private final ButtonGroup bgMainGroups = new ButtonGroup();

    /** The panel for pairing a group with the leaders. */
    private final SettingPanel pLeaders =
        new SettingPanel("Para grupper med ledare", null, 0, false);

    /** The panel for setting number of groups. */
    private final SettingPanel pNbrGroups =
        new SettingPanel("%-35s".formatted("Antal grupper"), null, 4, true);

    /** The panel for setting persons/group. */
    private final SettingPanel pNbrMembers =
        new SettingPanel("%-35s".formatted("Antal personer/grupp"), null, 4, true);

    /** The panel for setting different sizes. */
    private final SettingPanel pDifferentSizes =
        new SettingPanel("%-35s".formatted("Olika antal personer/grupp"), null, 4, true);

    /** The panel for the settings. */
    private final JPanel pSettings = new JPanel();

    /** The top of the panel. */
    private final JPanel pTop = new JPanel();

    /** The container panel of the checkbox. */
    private final JPanel pCBContainer = new JPanel();

    /** The container panel of the checkbox's container panel. */
    private final JPanel pCBContainerContainer = new JPanel();

    /** The JPanel for the buttons. */
    private final JPanel pButtons = new JPanel();

    /** The container for the label. */
    private final JPanel pLabelContainer = new JPanel();

    /** The layout for the container of the label. */
    private final FlowLayout pLabelContainerLayout = new FlowLayout(FlowLayout.CENTER);

    /** The layout for the button panel. */
    private final FlowLayout pButtonsLayout = new FlowLayout(FlowLayout.RIGHT);

    /** The layout of the container panel for the checkbox's container panel. */
    private final BoxLayout pCBContainerContainerLayout = new BoxLayout(pCBContainerContainer, BoxLayout.Y_AXIS);

    /** The layout of the container panel for the checkbox. */
    private final FlowLayout pCBContainerLayout = new FlowLayout(FlowLayout.LEFT);

    /** The layout of the top panel. */
    private final BoxLayout pTopLayout = new BoxLayout(pTop, BoxLayout.X_AXIS);

    /** The layout for the settings panel. */
    private final BoxLayout pSettingsLayout = new BoxLayout(pSettings, BoxLayout.Y_AXIS);

    /** This is the layout of the frame. */
    private final BorderLayout layout = new BorderLayout();

    /** A spacer in th gui. */
    private final JLabel lblSpacer1 = new JLabel("%-10s".formatted(" "));

    /** A spacer in th gui. */
    private final JLabel lblSpacer2 =
        new JLabel("<html><br><br><br></html>"); // Not hacky at all, good practice :)

    /** The scroll pane for the result. */
    private final JScrollPane scrLabelGroup;

    /** The SubgroupPanel. */
    private final SubgroupPanel sgp;

    /**
     * Creates a new group frame.
     *
     * @param gm the group manager in use.
     * */
    public GroupFrame(GroupManager gm)
    {
        super("Skapa grupper");
        this.gm = gm;
        this.randomCreator = new RandomGroupCreator(gm);
        this.wishlistCreator = new WishlistGroupCreator(gm);
        this.altWishlistCreator = new AlternateWishlistGroupCreator(gm);
        this.BASE_GROUP_PATH = "%ssaves/subgroups/%s/".formatted(Utils.getFolderName(), gm.getName());
        this.sgp = new SubgroupPanel(this, gm);
        this.scrLabelGroup = new JScrollPane(sgp);

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.addComponentListener(this);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);

        cbCreator.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbCreator.setForeground(Utils.FOREGROUND_COLOR);
        cbCreator.addItem(randomCreator);
        cbCreator.addItem(wishlistCreator);
        cbCreator.addItem(altWishlistCreator);
        cbCreator.setAlignmentX(Component.LEFT_ALIGNMENT);

        pSettings.setBackground(Utils.BACKGROUND_COLOR);
        pSettings.setLayout(pSettingsLayout);
        pSettings.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pNbrGroups.setRadioSelected(true);

        bgSettings.add(pNbrGroups.getRadio());
        bgSettings.add(pNbrMembers.getRadio());
        bgSettings.add(pLeaders.getRadio());
        bgSettings.add(pDifferentSizes.getRadio());

        pTop.setBackground(Utils.BACKGROUND_COLOR);
        pTop.setLayout(pTopLayout);

        lblSpacer1.setAlignmentX(Component.CENTER_ALIGNMENT);

        pCBContainer.setBackground(Utils.BACKGROUND_COLOR);
        pCBContainer.setLayout(pCBContainerLayout);

        pCBContainerContainer.setBackground(Utils.BACKGROUND_COLOR);
        pCBContainerContainer.setLayout(pCBContainerContainerLayout);
        pCBContainerContainer.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pButtons.setBackground(Utils.BACKGROUND_COLOR);
        pButtons.setLayout(pButtonsLayout);

        btnClose.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnClose.setForeground(Utils.FOREGROUND_COLOR);
        btnClose.addActionListener((e) -> {
            invokeCallbacks();
            dispose();
        });

        btnCreate.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCreate.setForeground(Utils.FOREGROUND_COLOR);
        btnCreate.addActionListener(e -> waitCursorAction(this::generateGroups));

        btnPrint.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnPrint.setForeground(Utils.FOREGROUND_COLOR);
        btnPrint.addActionListener(e -> print());

        btnSave.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnSave.setForeground(Utils.FOREGROUND_COLOR);
        btnSave.addActionListener(e -> saveLastGroup());

        btnLoad.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnLoad.setForeground(Utils.FOREGROUND_COLOR);
        btnLoad.addActionListener(e -> loadGroups());

        btnHelp.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnHelp.setForeground(Utils.FOREGROUND_COLOR);
        btnHelp.addActionListener(e -> Utils.openHelpPages());

        pLabelContainer.setLayout(pLabelContainerLayout);
        pLabelContainer.setBackground(Utils.BACKGROUND_COLOR);

        boxOverflow.setBackground(Utils.BACKGROUND_COLOR);
        boxOverflow.setForeground(Utils.FOREGROUND_COLOR);

        boxMainGroups.setBackground(Utils.BACKGROUND_COLOR);
        boxMainGroups.setForeground(Utils.FOREGROUND_COLOR);
        boxMainGroups.addActionListener(e -> {
            radioMainGroup1.setEnabled(boxMainGroups.isSelected());
            radioMainGroup2.setEnabled(boxMainGroups.isSelected());
        });

        radioMainGroup1.setSelected(true);
        radioMainGroup1.setEnabled(false);
        radioMainGroup1.setForeground(Utils.FOREGROUND_COLOR);
        radioMainGroup1.setBackground(Utils.BACKGROUND_COLOR);
        radioMainGroup1.addActionListener(e -> mainGroup = Person.MainGroup.MAIN_GROUP_1);

        radioMainGroup2.setSelected(false);
        radioMainGroup2.setEnabled(false);
        radioMainGroup2.setForeground(Utils.FOREGROUND_COLOR);
        radioMainGroup2.setBackground(Utils.BACKGROUND_COLOR);
        radioMainGroup2.addActionListener(e -> mainGroup = Person.MainGroup.MAIN_GROUP_2);

        bgMainGroups.add(radioMainGroup1);
        bgMainGroups.add(radioMainGroup2);

        scrLabelGroup.getViewport().setBackground(Utils.BACKGROUND_COLOR);
        scrLabelGroup.getViewport().setForeground(Utils.FOREGROUND_COLOR);
        scrLabelGroup.setBorder(BorderFactory.createEmptyBorder());

        btnToFile.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnToFile.setForeground(Utils.FOREGROUND_COLOR);
        btnToFile.addActionListener(e -> toFile());
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        pSettings.add(pNbrGroups);
        pSettings.add(pNbrMembers);
        pSettings.add(pDifferentSizes);
        pSettings.add(pLeaders);

        pCBContainer.add(cbCreator);

        pCBContainerContainer.add(lblSpacer2);
        pCBContainerContainer.add(pCBContainer);

        pTop.add(pCBContainerContainer);
        pTop.add(lblSpacer1);
        pTop.add(pSettings);

        pButtons.add(radioMainGroup1);
        pButtons.add(radioMainGroup2);
        pButtons.add(boxMainGroups);
        pButtons.add(boxOverflow);
        pButtons.add(btnClose);
        pButtons.add(btnHelp);
        pButtons.add(btnToFile);
        pButtons.add(btnLoad);
        pButtons.add(btnSave);
        pButtons.add(btnPrint);
        pButtons.add(btnCreate);

        this.add(pTop, BorderLayout.PAGE_START);
        this.add(scrLabelGroup, BorderLayout.CENTER);
        this.add(pButtons, BorderLayout.PAGE_END);
    }

    /**
     * Loads the different groups; that are
     * saved under this GroupManager.
     * */
    private void loadGroups()
    {
        SwingUtilities.invokeLater(() -> {
            var frame = new SubGroupListFrame(BASE_GROUP_PATH);

            frame.addActionCallback(() -> {
                var f = frame.getSelectedFile();
                frame.dispose();

                Subgroups sg;
                try
                {
                    sg = (Subgroups) SerializationManager.deserializeObject(f.getAbsolutePath());
                }
                catch (IOException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                    DebugMethods.log(e, DebugMethods.LogType.ERROR);

                    JOptionPane.showMessageDialog(
                        this, "Kunde inte läsa gruppen!\nFel: %s".formatted(e.getLocalizedMessage()),
                        "Misslyckades att läsa från fil.", JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                if (sg.isWishListMode())
                    cbCreator.setSelectedItem(wishlistCreator);
                else
                    cbCreator.setSelectedItem(randomCreator);

                currentGroups = sg;
                sgp.setCurrentGroups(sg);
                sgp.drawGroups();
            });
        });
    }

    /**
     * Saves the last group.
     * */
    private void saveLastGroup()
    {
        // If no groups error msg + return
        if (currentGroups == null)
        {
            JOptionPane.showMessageDialog(
                this, "Det finns inga grupper att spara",
                "INGA GRUPPER!", JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        var name =
                JOptionPane.showInputDialog(
                    this, "Vad heter gruppen?",
                    "Gruppens namn", JOptionPane.INFORMATION_MESSAGE
                );

        if (name == null)
            return;

        while (name.trim().length() < 3)
        {
            JOptionPane.showMessageDialog(
                    this, "Namnet måste vara minst tre tecken långt.",
                    "För kort namn!", JOptionPane.ERROR_MESSAGE
            );

            name = JOptionPane.showInputDialog(
                    this, "Vad heter gruppen?", "Gruppens namn", JOptionPane.INFORMATION_MESSAGE
            );

            if (name == null)
                return;
        }

        currentGroups = currentGroups.changeName(name);
        sgp.setCurrentGroups(currentGroups);

        var path = "%s%s".formatted(BASE_GROUP_PATH, "%s.data".formatted(currentGroups.name()));

        try
        {
            SerializationManager.createFileIfNotExists(new File(path));
            SerializationManager.serializeObject(path, currentGroups.changeName(name));

            JOptionPane.showMessageDialog(
                this, "Du har sparat undergruppen!",
                "Du har sparat!", JOptionPane.INFORMATION_MESSAGE
            );
        }
        catch (IOException e)
        {
            e.printStackTrace();
            DebugMethods.log(e, DebugMethods.LogType.ERROR);

            JOptionPane.showMessageDialog(
                this, "Fel vid sparning!\nFel: %s".formatted(e.getLocalizedMessage()),
                "Sparningen misslyckades!", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Prints the groups to a file.
     * */
    private void toFile()
    {
        var fc = new JFileChooser(".");
        var selection = fc.showDialog(this, "Välj");

        if (selection == JFileChooser.APPROVE_OPTION)
        {
            var file = fc.getSelectedFile();
            var sb = new StringBuilder();
            var leaders = new ArrayList<>(gm.getAllOfRoll(Person.Role.LEADER));

            // Formats the text to the file format.
            for (int i = 0; i < currentGroups.groups().size(); i++)
            {
                if (currentGroups.isLeaderMode())
                    sb.append(leaders.remove(0).getName()).append(":\n");
                else
                    sb.append("Grupp ").append(i + 1).append(':').append('\n');

                for (var id : currentGroups.groups().get(i))
                    sb.append('\t').append(gm.getPersonFromId(id).getName()).append('\n');
            }

            for (var g : currentGroups.groups())
            {
                sb.append('\n');

                for (var id : g)
                    sb.append(gm.getPersonFromId(id).getName()).append('\n');
            }

            writeToFile(sb.toString(), file);
        }
    }

    /**
     * Writes a string to a file.
     *
     * @param data the data to be written.
     * @param file the file to be written to.
     * */
    private void writeToFile(String data, File file)
    {
        BufferedWriter bw = null;

        try
        {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(data);
        }
        catch (IOException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.ERROR);

            JOptionPane.showMessageDialog(
                this, "Kunde inte spara filen!\nFel: %s".formatted(e.getLocalizedMessage()),
                "Sparning misslyckades", JOptionPane.ERROR_MESSAGE
            );
        }
        finally
        {
            try
            {
                if (bw != null)
                    bw.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints the groups.
     * */
    private void print()
    {
        if (currentGroups == null)
        {
            JOptionPane.showMessageDialog(
                this, "Det finns inga skapade grupper!",
                "Inga skapade grupper!", JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        var canvas = new JTextArea();
        var leaders = new ArrayList<>(gm.getAllOfRoll(Person.Role.LEADER));
        var groups =
            currentGroups.groups()
                         .stream()
                         .map(x -> new ArrayList<>(x.stream().map(gm::getPersonFromId).collect(Collectors.toList())))
                         .collect(Collectors.toCollection(ArrayList::new));

        canvas.setTabSize(4);
        canvas.setLineWrap(true);

        // Fist the overview
        for (var i = 0; i < groups.size(); i++)
        {
            if (currentGroups.labels().size() == 0)
            {
                canvas.append(
                    "%s:\n".formatted(
                        currentGroups.isLeaderMode() ? leaders.remove(0).getName() : "Grupp %d".formatted(i + 1)
                ));
            }
            else
            {
                try
                {
                    canvas.append(currentGroups.labels().get(i) + '\n');
                }
                catch (IndexOutOfBoundsException e)
                {
                    canvas.append(
                        "%s:\n".formatted(
                            currentGroups.isLeaderMode() ?
                            leaders.remove(0).getName() :
                            "Grupp %d".formatted(i + 1)
                    ));
                }
            }

            for (var p : groups.get(i))
                canvas.append("\t%s\n".formatted(p.getName()));

            canvas.append("\n");
        }

        try
        {
            canvas.print();
        }
        catch (PrinterException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.ERROR);
            return;
        }

        // Then print the big group signs.
        canvas.setFont(new Font(Font.DIALOG, Font.BOLD, 36));

        for (var g : groups)
        {
            canvas.setText("");
            canvas.append(ImmutableArray.fromCollection(g).map(Person::getName).mkString("\n"));

            try
            {
                canvas.print();
            }
            catch (PrinterException e)
            {
                DebugMethods.log(e, DebugMethods.LogType.ERROR);
                return;
            }
        }
    }

    /**
     * Invokes all the callbacks.
     * */
    private void invokeCallbacks()
    {
        callbacks.forEach(ActionCallback::callback);
    }

    /**
     * Runs an action with the spiny, spiny
     * cursor.
     *
     * @param r the action to be ran.
     * */
    private void waitCursorAction(Runnable r)
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
     * Gets the user input from the program,
     * to determine sizes.
     *
     * @return a list containing the sizes of
     *         the groups.
     * */
    private List<Integer> getUserInput()
    {
        try
        {
            // Leader mode
            if (pLeaders.isRadioSelected())
                return Collections.singletonList(gm.getAllOfRoll(Person.Role.LEADER).size());

            // Number of groups mode
            if (pNbrGroups.isRadioSelected())
                return Collections.singletonList(Integer.parseInt(pNbrGroups.getTextFieldData()));

            // Number of members mode
            if (pNbrMembers.isRadioSelected())
                return Collections.singletonList(Integer.parseInt(pNbrMembers.getTextFieldData()));

            // Different sizes mode.
            return Arrays.stream(pDifferentSizes.getTextFieldData().split(",")) // Splitting
                    .map(String::trim) // Trimming
                    .map(Integer::parseInt) // Parsing
                    .collect(Collectors.toList()); // Convert stream into list.
        }
        catch (NumberFormatException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.DEBUG);

            JOptionPane.showMessageDialog(
                    this, "Felaktig indata: %s".formatted(e.getLocalizedMessage()),
                    "Felaktig indata", JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }

    /**
     * Does the actual group generation.
     *
     * @param gc the group creator in use.
     * @param sizes the sizes of the groups.
     * @return the generated groups.
     * */
    private List<Set<Integer>> createGroups(GroupCreator gc, List<Integer> sizes)
    {
        try
        {
            if (pLeaders.isRadioSelected())
                return tryGenerateGroup(() -> {
                    assert gc != null;
                    return gc.generateGroup((short) ((int) sizes.get(0)), boxOverflow.isSelected());
                });

            if (pNbrMembers.isRadioSelected())
                return tryGenerateGroup(() -> {
                    assert gc != null;
                    return gc.generateGroup((byte) ((int) sizes.get(0)), boxOverflow.isSelected());
                });

            if (pNbrGroups.isRadioSelected())
            {
                return tryGenerateGroup(() -> {
                    assert gc != null;
                    return gc.generateGroup((short) ((int) sizes.get(0)), boxOverflow.isSelected());
                });
            }

            return tryGenerateGroup(() -> {
                assert gc != null;
                return gc.generateGroup(sizes);
            });
        }
        catch (IllegalArgumentException e)
        {
            DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

            JOptionPane.showMessageDialog(
                this, "Felaktig indata, fel: %s".formatted(e.getLocalizedMessage()),
                "Felaktig indata", JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
        catch (NoGroupAvailableException e)
        {
            DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

            JOptionPane.showMessageDialog(
                this, "Kunde inte skapa grupper, fel: %s".formatted(e.getLocalizedMessage()),
                "Kunde inte skapa grupper", JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }

    /**
     * Generates a group.
     * */
    private void generateGroups()
    {
        var gc = (GroupCreator) cbCreator.getSelectedItem();

        // Input
        var sizes = getUserInput();

        if (sizes == null || sizes.size() == 0)
            return;

        // Generation
        List<Set<Integer>> groups;

        if (boxMainGroups.isSelected())
        {
            var persons = gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, mainGroup);
            var gm = new GroupManager(mainGroup.toString());
            persons.forEach(gm::registerPerson);
            groups = createGroups(
                gc instanceof RandomGroupCreator ? new RandomGroupCreator(gm) : new WishlistGroupCreator(gm), sizes
            );
        }
        else
        {
            groups = createGroups(gc, sizes);
        }

        if (groups == null)
            return;

        // Saving
        currentGroups = new Subgroups(
            null, groups, pLeaders.isRadioSelected(),
            gc instanceof WishlistGroupCreator, new Vector<>()
        );

        // Drawing
        sgp.setCurrentGroups(currentGroups);
        sgp.drawGroups();
    }

    /**
     * Tries to generate groups. It will try to generate the group 1000 times,
     * if it fails then NoGroupAvailableException will be thrown.
     *
     * @param gg the runnable that runs the code.
     * @throws IllegalArgumentException iff {@link GroupCreator#generateGroup(byte, boolean)} or
     *                                  {@link GroupCreator#generateGroup(short, boolean)} does it.
     * @throws NoGroupAvailableException iff {@link GroupCreator#generateGroup(byte, boolean)} or
     *                                   {@link GroupCreator#generateGroup(short, boolean)} does it.
     * @throws NullPointerException iff gc is {@code null}.
     * */
    private List<Set<Integer>> tryGenerateGroup(GroupGenerator gg)
            throws IllegalArgumentException, NoGroupAvailableException, NullPointerException
    {
        int i = 0;

        while (i < 1000)
        {
            try
            {
                return gg.generate();
            }
            catch (NoGroupAvailableException e)
            {
                i++;
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);
            }
        }

        throw new NoGroupAvailableException("There are no possible groups, too many denylist items.");
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
     * Getter for: currentGroups.
     *
     * @return the current groups.
     * */
    public Subgroups getCurrentGroups()
    {
        return this.currentGroups;
    }

    /**
     * Setter for: currentGroups.
     *
     * @param currentGroups the new set of groups.
     * */
    public void setCurrentGroups(Subgroups currentGroups)
    {
        this.currentGroups = currentGroups;
    }

    @Override
    public void dispose()
    {
        invokeCallbacks();
        super.dispose();
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        if (sgp != null)
            sgp.drawGroups();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e)
    {
        if (sgp != null)
            sgp.drawGroups();
    }

    @Override
    public void componentHidden(ComponentEvent e) {}
}
