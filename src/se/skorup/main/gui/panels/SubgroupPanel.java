package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.ImmutableArray;
import se.skorup.API.ImmutableHashSet;
import se.skorup.API.Utils;
import se.skorup.main.groups.AlternateWishlistGroupCreator;
import se.skorup.main.groups.GroupCreator;
import se.skorup.main.groups.RandomGroupCreator;
import se.skorup.main.groups.WishlistGroupCreator;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.gui.frames.SubgroupListFrame;
import se.skorup.main.gui.interfaces.GroupGenerator;
import se.skorup.main.gui.objects.PersonBox;
import se.skorup.main.gui.objects.TextBox;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.manager.helper.SerializationManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;
import se.skorup.main.objects.Tuple;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * The panel that draws the SubGroups.
 * */
public class SubgroupPanel extends JPanel implements MouseListener
{
    private static final int SPACER = 50;

    private final GroupFrame gf;

    private final GroupManager gm;

    private Subgroups current;

    private ImmutableArray<TextBox> textBoxes;

    private FontMetrics fm;

    private Tuple lastTuple;

    private Timer flashingTimer;

    /**
     * Creates a new SubgroupSettingsPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public SubgroupPanel(GroupFrame gf)
    {
        this.gf = gf;
        this.gm = gf.getManager();

        this.setProperties();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.addMouseListener(this);

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
                drawGroups();
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
     * Prepares the drawing of the Groups.
     *  */
    private void initGroups()
    {
        if (textBoxes != null)
            return;

        lastTuple = null;

        var tb = new Vector<TextBox>();

        var groups =
                current.groups()
                       .stream()
                       .map(x -> x.stream().map(gm::getPersonFromId))
                       .map(x -> x.collect(Collectors.toList()))
                       .collect(Collectors.toList());

        var max = Collections.max(current.groups().stream().map(Set::size).collect(Collectors.toList()));

        for (var i = 0; i < groups.size(); i++)
        {
            var x = (i % 2 == 0) ? this.getWidth() / 10 : 3 * (this.getWidth() / 4);
            var y = 3 * SPACER + SPACER * (i % groups.size() / 2) * (max + 2);

            tb.add(new TextBox(current.getLabel(i), x, y, Utils.GROUP_NAME_COLOR));

            var gr = groups.get(i);
            for (var p : gr)
            {
                var wishes = Arrays.stream(p.getWishlist()).boxed().collect(Collectors.toSet());
                var nbrWishes = new ImmutableHashSet<>(current.groups().get(i)).intersection(wishes).size();

                var name =
                        current.isWishListMode() ?
                                "%s (Önskningar: %d)".formatted(p.getName(), nbrWishes) :
                                p.getName();

                y += SPACER / 5 + fm.getHeight();
                tb.add(new PersonBox(name, x, y, Utils.FOREGROUND_COLOR, p.getId()));
            }
        }

        textBoxes = ImmutableArray.fromList(tb);
    }

    /**
     * Flashes the group boxed the person can
     * be swapped into.
     * */
    private void flashGroupBoxes()
    {
        if (flashingTimer != null)
            flashingTimer.stop();

        final var tbs =
                textBoxes.toList()
                        .stream()
                        .filter(x -> !(x instanceof PersonBox))
                        .collect(Collectors.toList());

        final var counter = new AtomicInteger(0);
        flashingTimer = new Timer(500, (e) -> {
            tbs.forEach(x -> x.setColor(counter.get() % 2 == 0 ? Utils.FLASH_COLOR : Utils.GROUP_NAME_COLOR));
            counter.incrementAndGet();
            repaint();
        });

        flashingTimer.start();
    }

    /**
     * Converts a TextBox to a tuple,
     * where the first index is the
     * group index and the second index
     * is the index in the group. <br><br>
     *
     * If there are no corresponding index then
     * it will return (-1; -1). <br><br>
     *
     * Format: (group number, index in group)
     *
     * @param tb the text box to be converted.
     * @return a tuple, where the first index is the
     *         group index and the second index
     *         is the index in the the group. If there
     *         are no corresponding index then it will
     *         return (-1; -1).
     * */
    private Tuple textBoxToPerson(TextBox tb)
    {
        var index = textBoxes.indexOf(tb);

        // If there are no such text box it
        // will return (-1; -1).
        if (index == -1)
            return new Tuple(-1, -1);

        var group = 0;
        var i = 0;
        while (index >= 0)
        {
            var text = textBoxes.get(i++);
            if (!(text instanceof PersonBox))
                group++;

            index--;
        }

        return new Tuple(
                group - 1,
                current.groups()
                        .stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toList())
                        .get(group - 1)
                        .indexOf(((PersonBox) tb).getId())
        );
    }

    /**
     * Gets the selected index of the group of
     * the provided text box. If the text box
     * cannot be found it will return -1.
     *
     * @param tb the text box to convert the group
     *           number to.
     * @return the index of the group number. -1 iff
     *         the text box cannot be found.
     * */
    private int getSelectedGroup(TextBox tb)
    {
        var index = textBoxes.indexOf(tb);

        // If there are no such text box it
        // will return (-1; -1).
        if (index == -1)
            return -1;

        var group = 0;
        var i = 0;
        while (index >= 0)
        {
            var text = textBoxes.get(i++);
            if (!(text instanceof PersonBox))
                group++;

            index--;
        }

        return group - 1;
    }

    /**
     * Updates the provided group with the
     * member from the lastTuple.
     *
     * @param groupIndex The group index of the current person.
     * */
    private void updateGroups(int groupIndex)
    {
        // If null => do nothing.
        if (lastTuple == null)
            return;

        // No need to do anything if the group
        // isn't changed.
        if (lastTuple.a() == groupIndex)
            return;

        var groups = current.groups().stream().map(ArrayList::new).collect(Collectors.toList());
        int id = groups.get(lastTuple.a()).remove(lastTuple.b());
        groups.get(groupIndex).add(id);

        current = new Subgroups(
            current.name(), groups.stream().map(HashSet::new).collect(Collectors.toList()),
            current.isLeaderMode(), current.isWishListMode(), current.labels(), current.leaders()
        );
    }

    /**
     * Changes the label of the TextBox,
     * i.e. the text of the TextBox.
     *
     * @param tb the text box to be affected.
     * */
    private void changeLabel(TextBox tb)
    {
        var input = JOptionPane.showInputDialog(
                gf, "Vilken är den nya ettiketten?",
                "Ny ettiket!", JOptionPane.INFORMATION_MESSAGE
        );

        if (input == null)
            return;

        while (input.trim().length() < 3)
        {
            JOptionPane.showMessageDialog(
                    gf, "Ettiekten måste vara minst tre tecken lång.",
                    "För kort!", JOptionPane.ERROR_MESSAGE
            );

            input = JOptionPane.showInputDialog(
                    gf, "Vilken är den nya ettiketten?",
                    "Ny ettiket!", JOptionPane.INFORMATION_MESSAGE
            );

            if (input == null)
                return;
        }

        // Updates the groups.
        var groupIndex = getSelectedGroup(tb);
        current.labels()[groupIndex] = input;

        // Updates the labels in the GUI.s
        tb.setText(input);
        repaint();
    }

    /**
     * Gets the correct group generator,
     * based on all inputs.
     * */
    private GroupCreator getGroupGenerator()
    {
        var gc = gf.getGroupSelectedGroupCreator();

        if (gf.shouldUseMainGroups())
        {
            var persons = gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, gf.getMainGroup());
            var gm = new GroupManager(gf.getMainGroup().toString());
            persons.forEach(gm::registerPerson);

            return gc instanceof RandomGroupCreator   ?
                   new RandomGroupCreator(gm)         :
                   gc instanceof WishlistGroupCreator ?
                   new WishlistGroupCreator(gm)       :
                   new AlternateWishlistGroupCreator(gm);
        }

        return gf.getGroupSelectedGroupCreator();
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
     * Generates the group.
     * */
    private void generateGroups()
    {
        final var gc = getGroupGenerator();
        final var sizes = gf.getUserInput();

        if (sizes == null)
            return;

        GroupGenerator gg = switch (gf.getSizeState()) {
            case NUMBER_GROUPS -> () -> gc.generateGroup((short) ((int) sizes.get(0)), gf.shouldOverflow(), gm);
            case NUMBER_PERSONS -> () -> gc.generateGroup((byte) ((int) sizes.get(0)), gf.shouldOverflow());
            case PAIR_WITH_LEADERS -> () -> gc.generateGroup((short) ((int) sizes.get(0)), gf.shouldOverflow());
            case DIFFERENT_GROUP_SIZES -> () -> gc.generateGroup(sizes);
        };

        List<Set<Integer>> groups;
        try
        {
            groups = tryGenerateGroups(gg);
        }
        catch (NoGroupAvailableException | IllegalArgumentException e)
        {
            JOptionPane.showMessageDialog(
                this,
                "Misslyckades att generera grupper.\nFelmeddeleande: %s".formatted(e.getLocalizedMessage()),
                "Gruppgeneration misslyckades", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        current = new Subgroups(
            null, groups, gf.getSizeState().equals(GroupFrame.State.PAIR_WITH_LEADERS),
            gc instanceof WishlistGroupCreator, new String[groups.size()],
            new Vector<>(gm.getAllOfRoll(Person.Role.LEADER))
        );

        DebugMethods.log("Generated groups: ", DebugMethods.LogType.DEBUG);
        DebugMethods.log(current, DebugMethods.LogType.DEBUG);

        textBoxes = null;
        this.repaint();
    }

    /**
     * Draws the current group.
     * */
    public void drawGroups()
    {
        textBoxes = null;

        this.revalidate();
        this.repaint();
        gf.repaint();
        gf.revalidate();
    }

    /**
     * Calculates the height of the panel.
     *
     * @return the calculated height of the panel.
     * */
    private int height()
    {
        if (current == null || fm == null)
            return 0;

        int max = Collections.max(current.groups().stream().map(Set::size).collect(Collectors.toList()));
        return (int) ((3 * SPACER + SPACER * current.groups().size() + max * (SPACER + fm.getHeight())) * 1.5F);
    }

    @Override
    public void paintComponent(final Graphics gOld)
    {
        super.paintComponent(gOld);

        var g = (Graphics2D) gOld;
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        fm = g.getFontMetrics();

        g.setColor(Utils.BACKGROUND_COLOR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);

        if (current != null)
        {
            initGroups();
            textBoxes.forEach(tb -> tb.draw(g));
        }

        this.setSize(this.getMaximumSize());
    }

    @Override
    public Dimension getPreferredSize()
    {
        var dim = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) dim.getWidth(), Math.max((int) dim.getHeight(), height()));
    }

    @Override
    public Dimension getMinimumSize()
    {
        var dim = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) dim.getWidth(), Math.max((int) dim.getHeight(), height()));
    }

    @Override
    public Dimension getMaximumSize()
    {
        var dim = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) dim.getWidth(), Math.max((int) dim.getHeight(), height()));
    }


    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e)
    {
        DebugMethods.log(
                "Mouse clicked at: %d, %d".formatted(e.getX(), e.getY()),
                DebugMethods.LogType.DEBUG
        );

        if (textBoxes != null)
        {
            var text = textBoxes.getFirstMatch(tb -> tb.isCollision(e.getX(), e.getY()));

            if (text instanceof PersonBox && !text.getColor().equals(Utils.SELECTED_COLOR)) // Name selected.
            {
                // Resets previous selection.
                textBoxes.forEach(tb -> {
                    if (tb instanceof PersonBox)
                        tb.setColor(Utils.FOREGROUND_COLOR);
                    else
                        tb.setColor(Utils.GROUP_NAME_COLOR);

                    repaint();
                });

                lastTuple = textBoxToPerson(text);
                text.setColor(Utils.SELECTED_COLOR);
                flashGroupBoxes();
                repaint();
            }
            else if (text != null && text.getColor().equals(Utils.SELECTED_COLOR)) // Deselection
            {
                // Resets previous selection.
                textBoxes.forEach(tb -> {
                    if (tb instanceof PersonBox)
                        tb.setColor(Utils.FOREGROUND_COLOR);
                    else
                        tb.setColor(Utils.GROUP_NAME_COLOR);

                    repaint();
                });

                if (flashingTimer != null)
                    flashingTimer.stop();

                text.setColor(Utils.FOREGROUND_COLOR);
                lastTuple = null;
                repaint();
            }
            else if (text != null) // Name selected.
            {
                // To change the label.
                if (flashingTimer == null || !flashingTimer.isRunning())
                {
                    changeLabel(text);
                    return;
                }

                // Resets previous selection.
                textBoxes.forEach(tb -> {
                    if (tb instanceof PersonBox)
                        tb.setColor(Utils.FOREGROUND_COLOR);
                    else
                        tb.setColor(Utils.GROUP_NAME_COLOR);

                    repaint();
                });

                if (flashingTimer != null)
                    flashingTimer.stop();

                var groupIndex = getSelectedGroup(text);

                DebugMethods.log(
                        "Detected click on group index %d".formatted(groupIndex),
                        DebugMethods.LogType.DEBUG
                );

                updateGroups(groupIndex);
                lastTuple = null;

                drawGroups();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
