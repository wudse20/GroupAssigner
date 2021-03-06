package se.skorup.main.gui.panels;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.groups.creators.WishlistGroupCreator;
import se.skorup.main.groups.creators.GroupCreator;
import se.skorup.main.groups.creators.MultiWishlistCreator;
import se.skorup.main.groups.creators.RandomGroupCreator;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
            var path = gf.BASE_GROUP_PATH;
            var dir = new File(path).list();

            if (dir == null || dir.length == 0)
            {
                JOptionPane.showMessageDialog(
                    this, "Det finns inga sparade grupper",
                    "Inga sparade grupper", JOptionPane.ERROR_MESSAGE
                );

                DebugMethods.log("No saved groups", DebugMethods.LogType.ERROR);
                return;
            }
            List<Subgroups> groups;

            try
            {
                groups = Arrays.stream(Objects.requireNonNull(dir))
                               .map(x -> "%s%s".formatted(path, x))
                               .map(File::new) // Creates files.
                               .map(f -> {
                                   try
                                   {
                                       return (Subgroups) SerializationManager.deserializeObject(f.getAbsolutePath());
                                   }
                                   catch (IOException | ClassNotFoundException e)
                                   {
                                       DebugMethods.log(e, DebugMethods.LogType.ERROR);
                                       throw new RuntimeException(e);
                                   }
                               }).toList();
            }
            catch (RuntimeException e)
            {
                JOptionPane.showMessageDialog(
                    this, "ERROR: %s".formatted(e.getLocalizedMessage()),
                    "ERROR", JOptionPane.ERROR_MESSAGE
                );

                DebugMethods.log(e, DebugMethods.LogType.ERROR);
                return;
            }

            var frame = new SubgroupListFrame(groups, gm, "Ladda");

            frame.addActionCallback(sg -> {
                frame.dispose();
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
                this, "Namnet m??ste vara minst tre tecken l??ngt.",
                "F??r kort namn!", JOptionPane.ERROR_MESSAGE
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
            this, "Nu finns varje persons gruppmeddl??mmar p?? denylistan.",
            "Denylistor uppdaterad", JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Prints the groups to a file.
     * */
    private void toFile()
    {
        var fc = new JFileChooser(".");
        var selection = fc.showDialog(this, "V??lj");

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
    private GroupCreatorResult getGroupCreator(boolean shouldUseOneMainGroup, Person.MainGroup mg)
    {
        var gc = gf.getGroupSelectedGroupCreator();

        if (shouldUseOneMainGroup)
        {
            var persons = gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, mg);
            var gm = new GroupManager(mg.toString());
            persons.forEach(gm::registerPerson);

            GroupCreator res;

            if (gc instanceof RandomGroupCreator)
                res = new RandomGroupCreator(gm);
            else if (gc instanceof MultiWishlistCreator)
                res = new MultiWishlistCreator(gm);
            else if (gc instanceof WishlistGroupCreator w)
                res = new WishlistGroupCreator(gm);
            else
                res = new WishlistGroupCreator(gm);

            return new GroupCreatorResult(res, gm);
        }

        return new GroupCreatorResult(gc, this.gm);
    }

    /**
     * Tries to generate groups. It will try to generate the group 1000 times,
     * if it fails then NoGroupAvailableException will be thrown.
     *
     * @param gg the generator that generates the groups.
     * @return the generated groups.
     * @throws NoGroupAvailableException iff the group creation failed.
     */
    private List<List<Set<Integer>>> tryGenerateGroups(GroupGenerator gg) throws NoGroupAvailableException
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
     * Calculates the optimal size for overflow to make it
     * the best groups possible.
     *
     * @param size the size of the group
     * @param gm the group manager in use.
     * */
    private int calculateOptimalSize(int size, GroupManager gm)
    {
        var total = gm.getMemberCountOfRole(Person.Role.CANDIDATE);

        // If match then do nothing!
        if (total % size == 0)
            return size;

        // Check biggest delta with simple overflow.
        // i.e. just adding the overflow to its own
        // group. Delta = size - (total % size)
        var delta1 = size - (total % size);
        var delta2 = (size + 1) - (total % (size + 1));
        var delta3 = (size - 1) - (total % (size - 1));

        if (delta1 < delta2)
        {
            if (delta1 < delta3)
                return size;
            else
                return size - 1 < 1 ? size : size - 1;
        }
        else
        {
            if (delta2 < delta3)
                return size + 1;
            else
                return size - 1 < 1 ? size : size - 1;
        }
    }

    /**
     * Figures out which group generator to use.
     *
     * @param gc the group creator in use.
     * @param sizes the sizes of the groups.
     * @param gm the group manager in use.
     * @return the correct GroupGenerator.
     * */
    private GroupGenerator getGroupGenerator(GroupCreator gc, List<Integer> sizes, GroupManager gm)
    {
        if (!gf.shouldOverflow())
        {
            return switch (gf.getSizeState()) {
                case NUMBER_GROUPS -> () -> gc.generateGroupNbrGroups(sizes.get(0), false, gm);
                case NUMBER_PERSONS -> () -> gc.generateGroupNbrPeople(sizes.get(0), false);
                case PAIR_WITH_LEADERS -> {
                    var shouldOverflow = gm.getMemberCountOfRole(Person.Role.CANDIDATE) % sizes.get(0) != 0;
                    yield () -> gc.generateGroupNbrGroups(sizes.get(0), shouldOverflow, gm);
                }
                case DIFFERENT_GROUP_SIZES -> () -> gc.generateGroupNbrGroups(sizes);
            };
        }

        return switch (gf.getSizeState()) {
            case NUMBER_GROUPS -> {
                var nbrPersons = (int) Math.ceil(gm.getMemberCountOfRole(Person.Role.CANDIDATE) / (double) sizes.get(0));
                yield () -> gc.generateGroupNbrGroups(calculateOptimalSize(nbrPersons, gm), true, gm);
            }
            case NUMBER_PERSONS -> () -> gc.generateGroupNbrPeople(calculateOptimalSize(sizes.get(0), gm), true);
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
     * @param gm the group manager in use.
     * @return the List of Lists of Sets consisting of
     *         the newly created subgroups.
     */
    private List<List<Set<Integer>>> generateSingleSubgroup(GroupCreator gc, GroupManager gm)
    {
        final var sizes = gf.getUserInput();

        if (sizes.size() == 0)
            return List.of();

        try
        {
            return tryGenerateGroups(getGroupGenerator(gc, sizes, gm));
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
     */
    private List<List<Set<Integer>>> generateMultipleSubgroup()
    {
        final var sizes = gf.getUserInput();

        if (sizes.size() == 0)
            return List.of();

        var splitIndex = sizes.indexOf(Integer.MIN_VALUE);

        // Create using one MainGroup
        if (splitIndex == -1)
        {
            var gc = getGroupCreator(true, gf.getMainGroup());
            var gg = getGroupGenerator(gc.gc, sizes, gc.gm);
            return gg.generate();
        }

        var mg1Sizes = sizes.subList(0, splitIndex);
        var mg2Sizes = sizes.subList(splitIndex + 1, sizes.size());

        try
        {
            var gc1 = getGroupCreator(true, Person.MainGroup.MAIN_GROUP_1);
            var gg1 = getGroupGenerator(gc1.gc, mg1Sizes, gc1.gm);
            var groups = tryGenerateGroups(gg1);

            var gc2 = getGroupCreator(true, Person.MainGroup.MAIN_GROUP_2);
            var gg2 = getGroupGenerator(gc2.gc, mg2Sizes, gc2.gm);
            var groups2 = tryGenerateGroups(gg2);
            groups.addAll(groups2);

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
        final var gc = getGroupCreator(gf.shouldUseOneMainGroup(), gf.getMainGroup()).gc;
        var leaders = gm.getMemberCountOfRole(Person.Role.LEADER);
        var candidates = gm.getMemberCountOfRole(Person.Role.CANDIDATE);

        // If leaders cannot be properly divided between groups
        // display warning to user.
        if (
            gf.getSizeState().equals(GroupFrame.State.PAIR_WITH_LEADERS) &&
            candidates / leaders <= 1
        )
        {
            JOptionPane.showMessageDialog(
                this, "Kan inte skapa grupper till alla ledare! Hoppar ??ver vissa ledare",
                "F??r m??nga ledare", JOptionPane.WARNING_MESSAGE
            );
        }

        var groups =
            gf.shouldUseMainGroups()   ?
            generateMultipleSubgroup() :
            generateSingleSubgroup(gc, gm);

        if (groups.size() == 0)
            return;

        if (groups.size() == 1)
        {
            current = new Subgroups(
               null, groups.get(0), gf.getSizeState().equals(GroupFrame.State.PAIR_WITH_LEADERS),
                gc instanceof WishlistGroupCreator || gc instanceof MultiWishlistCreator,
                new String[groups.get(0).size()], new Vector<>(gm.getAllOfRoll(Person.Role.LEADER))
            );

            DebugMethods.log("Generated groups: ", DebugMethods.LogType.DEBUG);
            DebugMethods.log(current, DebugMethods.LogType.DEBUG);

            sdp.reset();
            sdp.displaySubgroup(current, gm);

            return;
        }

        SwingUtilities.invokeLater(() -> {
            var sgs = new ArrayList<Subgroups>();

            int i = 1;
            for (var g : groups)
            {
                sgs.add(new Subgroups(
                    "F??rslag: %d".formatted(i++), g, gf.getSizeState().equals(GroupFrame.State.PAIR_WITH_LEADERS),
                    gc instanceof WishlistGroupCreator || gc instanceof MultiWishlistCreator,
                    new String[g.size()], new Vector<>(gm.getAllOfRoll(Person.Role.LEADER))
                ));
            }

            DebugMethods.log("Generated groups:", DebugMethods.LogType.DEBUG);
            DebugMethods.log(groups, DebugMethods.LogType.DEBUG);
            var frame = new SubgroupListFrame(sgs, gm, "V??lj");
            gf.setVisible(false);

            frame.addActionCallback(sg -> {
                DebugMethods.log("Chosen groups:", DebugMethods.LogType.DEBUG);
                DebugMethods.log(sg, DebugMethods.LogType.DEBUG);

                sdp.reset();
                sdp.displaySubgroup(sg, gm);
                current = sg;

                frame.dispose();
                gf.setVisible(true);
            });
        });
    }

    /**
     * Shows and hides the colors of the names from the main groups.
     *
     * @param shouldDisplayMainGroups if {@code true} it will display the colors,
     *                               else if {@code false} it will display the standard forground color.
     * */
    public void setMainGroupDisplay(boolean shouldDisplayMainGroups)
    {
        sdp.setMainGroupDisplay(shouldDisplayMainGroups);
    }

    @Override
    public void repaint()
    {
        if (sdp != null)
            sdp.displaySubgroup(current, gm);

        super.repaint();
    }

    private record GroupCreatorResult(GroupCreator gc, GroupManager gm) {}
}
