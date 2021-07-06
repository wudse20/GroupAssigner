package se.skorup.main.gui.frames;

import se.skorup.API.DebugMethods;
import se.skorup.API.ImmutableArray;
import se.skorup.API.ImmutableHashSet;
import se.skorup.API.Utils;
import se.skorup.main.groups.GroupCreator;
import se.skorup.main.groups.RandomGroupCreator;
import se.skorup.main.groups.WishlistGroupCreator;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.gui.interfaces.GroupGenerator;
import se.skorup.main.gui.panels.SettingPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.manager.helper.SerializationManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.SubGroup;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
public class GroupFrame extends JFrame
{
    /** The common path of all subgroups. */
    private final String BASE_GROUP_PATH;

    /** The group manager in use. */
    private final GroupManager gm;

    /** The random group creator. */
    private final GroupCreator randomCreator;

    /** The wishlist group creator. */
    private final GroupCreator wishlistCreator;

    /** The groups that were generated last. */
    private List<Set<Integer>> lastGroups;

    /** {@code true} = last generation was generated with leaders, else not. */
    private boolean lastWerePairWithLeaders = false;

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
    private final JButton btnToFile = new JButton("Till fil");

    /** The checkbox used for overflow. */
    private final JCheckBox boxOverflow = new JCheckBox("Skapa extra grupper ifall det inte går jämt upp.");

    /** The label for displaying the groups.*/
    private final JLabel lblGroup = new JLabel("");

    /** The button group for the settings. */
    private final ButtonGroup bgSettings = new ButtonGroup();

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
    private final JScrollPane scrLabelGroup = new JScrollPane(pLabelContainer);

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
        this.BASE_GROUP_PATH = "%ssaves/subgroups/%s/".formatted(Utils.getFolderName(), gm.getName());

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setSize(new Dimension(1200, 685));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);

        cbCreator.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbCreator.setForeground(Utils.FOREGROUND_COLOR);
        cbCreator.addItem(randomCreator);
        cbCreator.addItem(wishlistCreator);
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
        btnCreate.addActionListener((e) -> generateGroups());

        btnPrint.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnPrint.setForeground(Utils.FOREGROUND_COLOR);
        btnPrint.addActionListener((e) -> print());

        btnSave.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnSave.setForeground(Utils.FOREGROUND_COLOR);
        btnSave.addActionListener((e) -> saveLastGroup());

        btnLoad.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnLoad.setForeground(Utils.FOREGROUND_COLOR);
        btnLoad.addActionListener((e) -> loadGroups());

        pLabelContainer.setLayout(pLabelContainerLayout);
        pLabelContainer.setBackground(Utils.BACKGROUND_COLOR);

        lblGroup.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        lblGroup.setForeground(Utils.FOREGROUND_COLOR);

        boxOverflow.setBackground(Utils.BACKGROUND_COLOR);
        boxOverflow.setForeground(Utils.FOREGROUND_COLOR);

        scrLabelGroup.getViewport().setBackground(Utils.BACKGROUND_COLOR);
        scrLabelGroup.getViewport().setForeground(Utils.FOREGROUND_COLOR);
        scrLabelGroup.setBorder(BorderFactory.createEmptyBorder());

        btnToFile.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnToFile.setForeground(Utils.FOREGROUND_COLOR);
        btnToFile.addActionListener((e) -> toFile());
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

        pButtons.add(boxOverflow);
        pButtons.add(btnClose);
        pButtons.add(btnToFile);
        pButtons.add(btnLoad);
        pButtons.add(btnSave);
        pButtons.add(btnPrint);
        pButtons.add(btnCreate);

        pLabelContainer.add(lblGroup);

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

                SubGroup sg;
                try
                {
                    sg = (SubGroup) SerializationManager.deserializeObject(f.getAbsolutePath());
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

                formatGroup(sg.groups(), sg.isLeaderMode());
                lastGroups = sg.groups();
                lastWerePairWithLeaders = sg.isLeaderMode();
            });
        });
    }

    /**
     * Saves the last group.
     * */
    private void saveLastGroup()
    {
        if (lastGroups == null)
        {
            JOptionPane.showMessageDialog(
                this, "Det finns inga skapade grupper!",
                "Finns inga skapade grupper", JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        var name =
            JOptionPane.showInputDialog(
                this, "Vad heter gruppen?", "Gruppens namn", JOptionPane.INFORMATION_MESSAGE
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

        var subGroups = new SubGroup(name, lastGroups, lastWerePairWithLeaders);
        var path = "%s%s".formatted(BASE_GROUP_PATH, "%s.data".formatted(name));

        try
        {
            SerializationManager.createFileIfNotExists(new File(path));
            SerializationManager.serializeObject(path, subGroups);

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
            for (int i = 0; i < lastGroups.size(); i++)
            {
                if (lastWerePairWithLeaders)
                    sb.append(leaders.remove(0).getName()).append(":\n");
                else
                    sb.append("Grupp ").append(i + 1).append(':').append('\n');

                for (var id : lastGroups.get(i))
                    sb.append('\t').append(gm.getPersonFromId(id).getName()).append('\n');
            }

            for (var g : lastGroups)
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
        if (lastGroups == null)
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
            lastGroups.stream()
                      .map(x -> new ArrayList<>(x.stream().map(gm::getPersonFromId).collect(Collectors.toList())))
                      .collect(Collectors.toCollection(ArrayList::new));

        canvas.setTabSize(4);
        canvas.setLineWrap(true);

        // Fist the overview
        for (var i = 0; i < groups.size(); i++)
        {
            canvas.append(
                "%s:\n"
                .formatted(lastWerePairWithLeaders ? leaders.remove(0).getName() : "Grupp %d".formatted(i + 1))
            );

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
     * Generates a group.
     * */
    private void generateGroups()
    {
        var gc = (GroupCreator) cbCreator.getSelectedItem();
        List<Set<Integer>> list = null; // Just to have initialized.
        lastWerePairWithLeaders = pLeaders.isRadioSelected();

        // Checks for leader mode
        if (pLeaders.isRadioSelected())
        {
            var leaders = gm.getAllOfRoll(Person.Role.LEADER);
            var groups = leaders.size();

            // If there are to few groups.
            if (groups < 2)
            {
                JOptionPane.showMessageDialog(
                    this, "Det finns för få leadare. Det finns bara %d leadare.".formatted(groups),
                    "För få grupper!", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            try
            {
                list = tryGenerateGroup(() -> gc.generateGroup((short) groups, boxOverflow.isSelected()));
            }
            catch (IllegalArgumentException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                    this, "Felaktig indata, fel: %s".formatted(e.getLocalizedMessage()),
                    "Felaktig indata", JOptionPane.ERROR_MESSAGE
                );

                return;
            }
            catch (NoGroupAvailableException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                    this, "Kunde inte skapa grupper, fel: %s".formatted(e.getLocalizedMessage()),
                    "Kunde inte skapa grupper", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            DebugMethods.log("Created groups: %s".formatted(list), DebugMethods.LogType.DEBUG);
            this.lastGroups = list;
            formatGroup(list, true);
            return;
        }
        else if (pNbrGroups.isRadioSelected())
        {
            int groups;

            try
            {
                groups = Integer.parseInt(pNbrGroups.getTextFieldData());
            }
            catch (NumberFormatException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                    this, "Antalet grupper är inget nummer fel: %s".formatted(e.getLocalizedMessage()),
                    "Inget nummer", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            try
            {
                list = tryGenerateGroup(() -> gc.generateGroup((short) groups, boxOverflow.isSelected()));
            }
            catch (IllegalArgumentException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                        this, "Felaktig indata, fel: %s".formatted(e.getLocalizedMessage()),
                        "Felaktig indata", JOptionPane.ERROR_MESSAGE
                );

                return;
            }
            catch (NoGroupAvailableException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                        this, "Kunde inte skapa grupper, fel: %s".formatted(e.getLocalizedMessage()),
                        "Kunde inte skapa grupper", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            DebugMethods.log("Created groups: %s".formatted(list), DebugMethods.LogType.DEBUG);
        }
        else if (pNbrMembers.isRadioSelected())
        {
            int persons;

            try
            {
                persons = Integer.parseInt(pNbrMembers.getTextFieldData());
            }
            catch (NumberFormatException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                        this, "Antalet personer är inget nummer fel: %s".formatted(e.getLocalizedMessage()),
                        "Inget nummer", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            try
            {
                list = tryGenerateGroup(() -> gc.generateGroup((byte) persons, boxOverflow.isSelected()));
            }
            catch (IllegalArgumentException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                        this, "Felaktig indata, fel: %s".formatted(e.getLocalizedMessage()),
                        "Felaktig indata", JOptionPane.ERROR_MESSAGE
                );

                return;
            }
            catch (NoGroupAvailableException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                        this, "Kunde inte skapa grupper, fel: %s".formatted(e.getLocalizedMessage()),
                        "Kunde inte skapa grupper", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            DebugMethods.log("Created groups: %s".formatted(list), DebugMethods.LogType.DEBUG);
        }
        else
        {
            var strSizes = new ArrayList<>(Arrays.asList(pDifferentSizes.getTextFieldData().split(",")));
            List<Integer> sizes;

            try
            {
                sizes = strSizes.stream()
                                .map(String::trim)
                                .map(Integer::parseInt)
                                .collect(Collectors.toCollection(ArrayList::new));
            }
            catch (NumberFormatException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                        this, "Felaktig indata, fel: %s".formatted(e.getLocalizedMessage()),
                        "Felaktig indata", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            try
            {
                final var finalSizes = sizes; // Since it has to be final or effectively final.
                list = tryGenerateGroup(() -> gc.generateGroup(finalSizes));
            }
            catch (IllegalArgumentException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                        this, "Felaktig indata, fel: %s".formatted(e.getLocalizedMessage()),
                        "Felaktig indata", JOptionPane.ERROR_MESSAGE
                );

                return;
            }
            catch (NoGroupAvailableException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);

                JOptionPane.showMessageDialog(
                        this, "Kunde inte skapa grupper, fel: %s".formatted(e.getLocalizedMessage()),
                        "Kunde inte skapa grupper", JOptionPane.ERROR_MESSAGE
                );

                return;
            }
        }

        this.lastGroups = list;
        formatGroup(list, false);
    }

    /**
     * Formats the groups and prints them to the GUI.
     *
     * @param groups the generated groups from a GroupCreator.
     * @param leaderMode iff {@code true} it will map each group to a leader.
     * */
    private void formatGroup(List<Set<Integer>> groups, boolean leaderMode)
    {
        // The persons to be printed.
        var persons =
            groups.stream()
                  .map(x -> x.stream().map(gm::getPersonFromId).collect(Collectors.toSet()))
                  .collect(Collectors.toList());

        var sb = new StringBuilder().append("<html><table>");
        var leaders = new ArrayList<>(gm.getAllOfRoll(Person.Role.LEADER));
        int count = 0;
        int max = Collections.max(groups.stream().map(Set::size).collect(Collectors.toList()));

        for (int i = 0; i < persons.size(); i++)
        {
            var s = persons.get(i);

            if (count++ % 2 == 0)
                sb.append("<tr>").append("<td>");
            else
                sb.append("<td>");

            if (leaderMode && leaders.size() >= 1)
                sb.append("<font color=RED>")
                  .append(leaders.remove(0).getName())
                  .append("&emsp;&emsp;&emsp;&emsp;").append("</font>");
            else if (!leaderMode)
                sb.append("<font color=RED>")
                  .append("Grupp ").append(count).append(':')
                  .append("&emsp;&emsp;&emsp;&emsp;").append("</font>");

            for (var p : s)
            {
                if (cbCreator.getSelectedItem() instanceof WishlistGroupCreator)
                {
                    var groupIds = new ImmutableHashSet<>(groups.get(i));
                    groupIds = groupIds.intersection(
                        Arrays.stream(p.getWishlist()).boxed().collect(Collectors.toSet())
                    );

                    DebugMethods.log("%s wishes granted for %s".formatted(groupIds, p), DebugMethods.LogType.DEBUG);

                    sb.append("<br>").append(p.getName()).append(" (Önskningar: ")
                      .append(groupIds.size()).append(")").append("&emsp;&emsp;&emsp;&emsp;");
                }
                else
                {
                    sb.append("<br>").append(p.getName())
                      .append("&emsp;&emsp;&emsp;&emsp;");
                }
            }

            if (s.size() < max)
            {
                int diff = max - s.size();

                sb.append("<br>".repeat(diff + 1));
            }

            sb.append("</td>");
        }

        sb.append("</table></html>");

        lblGroup.setText(sb.toString());
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
     * Getter for: lastSubGroups
     *
     * @return the list containing the last subgroups.
     * */
    public List<Set<Integer>> getLastSubgroups()
    {
        return lastGroups;
    }

    @Override
    public void dispose()
    {
        invokeCallbacks();
        super.dispose();
    }
}
