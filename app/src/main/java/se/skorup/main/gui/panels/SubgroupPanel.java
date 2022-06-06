package se.skorup.main.gui.panels;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.groups.creators.AlternateWishlistGroupCreator;
import se.skorup.main.groups.creators.GroupCreator;
import se.skorup.main.groups.creators.MultiWishlistCreator;
import se.skorup.main.groups.creators.RandomGroupCreator;
import se.skorup.main.groups.creators.WishlistGroupCreator;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.gui.frames.SubgroupListFrame;
import se.skorup.main.gui.interfaces.GroupGenerator;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.manager.helper.SerializationManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The panel that holds the drawing of subgroups.
 * */
public class SubgroupPanel extends JPanel
{
    private final GroupFrame gf;
    private final GroupManager gm;
    private final SubgroupDisplayPanel sdp;
    private Subgroups current;

    /**
     * Creates a new SubgroupSettingsPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public SubgroupPanel(GroupFrame gf)
    {
        this.gf = gf;
        this.gm = gf.getManager();
        this.sdp = new SubgroupDisplayPanel(this);

        this.setProperties();

        var cont = new JPanel();
        cont.setBackground(Utils.BACKGROUND_COLOR);
        cont.setLayout(new BorderLayout());

        var scr = new JScrollPane(sdp);
        scr.setBorder(BorderFactory.createEmptyBorder());
        scr.getVerticalScrollBar().setUnitIncrement(16);
        cont.add(scr, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(cont, BorderLayout.CENTER);
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);

        gf.addActionListener(e -> gf.waitCursorAction(this::generateGroups), GroupButtonPanel.Buttons.CREATE);
        gf.addActionListener(e -> toDenylist(), GroupButtonPanel.Buttons.TO_DENYLIST);
        gf.addActionListener(e -> toFile(), GroupButtonPanel.Buttons.TO_FILE);
        gf.addActionListener(e -> print(), GroupButtonPanel.Buttons.PRINT);
        gf.addActionListener(e -> saveLastGroup(), GroupButtonPanel.Buttons.SAVE);
        gf.addActionListener(e -> loadGroups(), GroupButtonPanel.Buttons.LOAD);
    }

    /**
     * Loads the different groups; that are
     * saved under this GroupManager.
     * */
    private void loadGroups()
    {
        SwingUtilities.invokeLater(() -> {
            var frame = new SubgroupListFrame(gf.BASE_GROUP_PATH);

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

                gf.getCbCreators().setSelectedIndex(sg.isWishListMode() ? 2 : 0);
                current = sg;
                sdp.reset();
                sdp.displaySubgroup(current, gm);
            });
        });
    }

    /**
     * Saves the last group.
     * */
    private void saveLastGroup()
    {
        // If no groups error msg + return
        if (current == null)
        {
            JOptionPane.showMessageDialog(
                this, "Det finns inga grupper att spara.",
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

        current = current.changeName(name);
        var path = "%s%s".formatted(gf.BASE_GROUP_PATH, "%s.data".formatted(current.name()));

        try
        {
            SerializationManager.createFileIfNotExists(new File(path));
            SerializationManager.serializeObject(path, current.changeName(name));

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
     * Prints the groups.
     * */
    private void print()
    {
        if (current == null)
        {
            JOptionPane.showMessageDialog(
                this, "Det finns inga skapade grupper!",
                "Inga skapade grupper!", JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        var canvas = new JTextArea();
        var groups =
                current.groups()
                       .stream()
                       .map(x -> new ArrayList<>(x.stream().map(gm::getPersonFromId).collect(Collectors.toList())))
                       .collect(Collectors.toCollection(ArrayList::new));

        canvas.setTabSize(4);
        canvas.setLineWrap(true);

        // Fist the overview
        for (var i = 0; i < groups.size(); i++)
        {
            canvas.append("%s:\n".formatted(current.getLabel(i)));
            groups.get(i).forEach(p -> canvas.append("\t%s\n".formatted(p.getName())));
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
    }

    /**
     * The action for the toDenylist-button.
     * */
    private void toDenylist()
    {
        if (current == null)
        {
            JOptionPane.showMessageDialog(
                this, "Det finns inga genreade grupper.",
                "Inga genererade grupper", JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        for (var group : current.groups())
        {
            for (var id : group)
            {
                var p = gm.getPersonFromId(id);
                group.forEach(x -> { if (x != p.getId()) p.addDenylistId(x); });
            }
        }

        JOptionPane.showMessageDialog(
            this, "Nu finns varje persons gruppmeddlämmar på denylistan.",
            "Denylistor uppdaterad", JOptionPane.INFORMATION_MESSAGE
        );
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

            // Formats the text to the file format.
            for (int i = 0; i < current.groups().size(); i++)
            {
                sb.append(current.getLabel(i)).append(":\n");

                for (var id : current.groups().get(i))
                    sb.append('\t').append(gm.getPersonFromId(id).getName()).append('\n');
            }

            Utils.writeToFile(sb.toString(), file);
        }
    }

    /**
     * Gets the correct group generator,
     * based on all inputs.
     *
     * @param shouldUseOneMainGroup if {code true} it will create a new GroupCreator with
     *                              only the provided main group, else it keep the selected
     *                              GroupCreator.
     * @param mg the main group.
     * */
    private GroupCreator getGroupCreator(boolean shouldUseOneMainGroup, Person.MainGroup mg)
    {
        var gc = gf.getGroupSelectedGroupCreator();

        if (shouldUseOneMainGroup)
        {
            var persons = gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, mg);
            var gm = new GroupManager(mg.toString());
            persons.forEach(gm::registerPerson);

            return gc instanceof RandomGroupCreator   ?
                   new RandomGroupCreator(gm)         :
                   gc instanceof WishlistGroupCreator ?
                   new WishlistGroupCreator(gm)       :
                   new AlternateWishlistGroupCreator(gm);
        }

        return gc;
    }

    /**
     * Tries to generate groups. It will try to generate the group 1000 times,
     * if it fails then NoGroupAvailableException will be thrown.
     *
     * @param gg the generator that generates the groups.
     * @return the generated groups.
     * @throws NoGroupAvailableException iff the group creation failed.
     * */
    private List<Set<Integer>> tryGenerateGroups(GroupGenerator gg) throws NoGroupAvailableException
    {
        for (int i = 0; i < 1000; i++)
        {
            try
            {
                return gg.generate();
            }
            catch (NoGroupAvailableException e)
            {
                DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);
            }
        }

        throw new NoGroupAvailableException("There are no possible groups, too many denylist items.");
    }

    /**
     * Figures out which group generator to use.
     *
     * @param gc the group creator in use.
     * @param sizes the sizes of the groups.
     * @return the correct GroupGenerator.
     * */
    private GroupGenerator getGroupGenerator(GroupCreator gc, List<Integer> sizes)
    {
        return switch (gf.getSizeState()) {
            case NUMBER_GROUPS -> () -> gc.generateGroupNbrGroups(sizes.get(0), gf.shouldOverflow(), gm);
            case NUMBER_PERSONS -> () -> gc.generateGroupNbrPeople(sizes.get(0), gf.shouldOverflow());
            case PAIR_WITH_LEADERS -> {
                var shouldOverflow = gm.getMemberCountOfRole(Person.Role.CANDIDATE) % sizes.get(0) != 0;
                yield () -> gc.generateGroupNbrGroups(sizes.get(0), shouldOverflow, gm);
            }
            case DIFFERENT_GROUP_SIZES -> () -> gc.generateGroupNbrGroups(sizes);
        };
    }

    /**
     * Generates a singe subgroup.
     *
     * @param gc the group creator that should be used.
     * @return the List of Sets consisting of
     *         the newly created subgroups.
     * */
    private List<Set<Integer>> generateSingleSubgroup(GroupCreator gc)
    {
        final var sizes = gf.getUserInput();

        if (sizes.size() == 0)
            return List.of();

        try
        {
            return tryGenerateGroups(getGroupGenerator(gc, sizes));
        }
        catch (NoGroupAvailableException | IllegalArgumentException e)
        {
            JOptionPane.showMessageDialog(
                this,
                "Misslyckades att generera grupper.\nFelmeddeleande: %s".formatted(e.getLocalizedMessage()),
                "Gruppgeneration misslyckades", JOptionPane.ERROR_MESSAGE
            );
            return List.of();
        }
    }

    /**
     * Used to generate two sets of groups at the
     * same time and concatenating them.
     *
     * @return the generated groups.
     * */
    private List<Set<Integer>> generateMultipleSubgroup()
    {
        final var sizes = gf.getUserInput();

        if (sizes.size() == 0)
            return List.of();

        var splitIndex = sizes.indexOf(Integer.MIN_VALUE);
        var mg1Sizes = sizes.subList(0, splitIndex);
        var mg2Sizes = sizes.subList(splitIndex + 1, sizes.size());

        try
        {
            var groups =
               tryGenerateGroups(
                   getGroupGenerator(
                       getGroupCreator(true, Person.MainGroup.MAIN_GROUP_1), mg1Sizes));

            groups.addAll(
                tryGenerateGroups(
                    getGroupGenerator(
                        getGroupCreator(true, Person.MainGroup.MAIN_GROUP_2), mg2Sizes)));

            return groups;
        }
        catch (NoGroupAvailableException | IllegalArgumentException e)
        {
            JOptionPane.showMessageDialog(
                this,
                "Misslyckades att generera grupper.\nFelmeddeleande: %s".formatted(e.getLocalizedMessage()),
                "Gruppgeneration misslyckades", JOptionPane.ERROR_MESSAGE
            );
            return List.of();
        }
    }

    /**
     * Generates the group.
     * */
    private void generateGroups()
    {
        final var gc = getGroupCreator(gf.shouldUseOneMainGroup(), gf.getMainGroup());
        var groups =
            gf.shouldUseMainGroups()   ?
            generateMultipleSubgroup() :
            generateSingleSubgroup(gc);

        if (groups.size() == 0)
            return;

        current = new Subgroups(
           null, groups, gf.getSizeState().equals(GroupFrame.State.PAIR_WITH_LEADERS),
            gc instanceof WishlistGroupCreator || gc instanceof MultiWishlistCreator,
            new String[groups.size()], new Vector<>(gm.getAllOfRoll(Person.Role.LEADER))
        );

        DebugMethods.log("Generated groups: ", DebugMethods.LogType.DEBUG);
        DebugMethods.log(current, DebugMethods.LogType.DEBUG);

        sdp.reset();
        sdp.displaySubgroup(current, gm);
    }

    @Override
    public void repaint()
    {
        if (sdp != null)
            sdp.displaySubgroup(current, gm);

        super.repaint();
    }
}
