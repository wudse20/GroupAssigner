package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.ImmutableArray;
import se.skorup.API.ImmutableHashSet;
import se.skorup.API.Utils;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.gui.objects.PersonBox;
import se.skorup.main.gui.objects.TextBox;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;
import se.skorup.main.objects.Tuple;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The panel that draws the SubGroups.
 * */
public class SubgroupPanel extends JPanel implements Scrollable, MouseListener
{
    /** The vertical spacer. */
    private static final int VERTICAL_SPACER = 50;

    /** The instance of the GroupFrame. */
    private final GroupFrame gf;

    /** The GroupManager in use. */
    private final GroupManager gm;

    /** The font metrics of the font. */
    private FontMetrics fm;

    /** The current groups. */
    private Subgroups currentGroups;

    /** The text boxes in the GUI. */
    private ImmutableArray<TextBox> textBoxes;

    /** The timer used for flashing the boxes. */
    private Timer t;

    /** The last tuple generated from the list. */
    private Tuple lastTuple;

    /**
     * Creates a new SubGroupPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * @param gm the instance of the GroupManager that
     *           holds the persons with the IDs.
     * */
    public SubgroupPanel(GroupFrame gf, GroupManager gm)
    {
        this.gf = gf;
        this.gm = gm;
        this.currentGroups = gf.getCurrentGroups();
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
    }

    /**
     * Prepares the drawing of the Groups.
     *
     * @param g the Graphics2D instance drawing.
     * */
    private void initGroups(Graphics2D g)
    {
        if (textBoxes != null)
            return;

        var tb = new Vector<TextBox>();

        var groups =
            currentGroups.groups()
                         .stream()
                         .map(x -> x.stream().map(gm::getPersonFromId))
                         .map(x -> x.collect(Collectors.toList()))
                         .collect(Collectors.toList());

        var max = Collections.max(currentGroups.groups().stream().map(Set::size).collect(Collectors.toList()));
        var leaders = new ArrayList<>(gm.getAllOfRoll(Person.Role.LEADER));

        for (var i = 0; i < groups.size(); i++)
        {
            var x = (i % 2 == 0) ? this.getWidth() / 4 : 3 * (this.getWidth() / 4);
            var y = VERTICAL_SPACER + VERTICAL_SPACER * (i % groups.size() / 2) * (max + 2);

            var groupName =
                currentGroups.isLeaderMode() ?
                leaders.remove(0).getName() :
                "Grupp %d:".formatted(i + 1);

            if (!currentGroups.isLeaderMode())
                x -= fm.stringWidth(groupName) / 2;

            tb.add(new TextBox(groupName, x, y, Utils.GROUP_NAME_COLOR));

            var gr = groups.get(i);
            for (var p : gr)
            {
                var wishes =
                    new ImmutableHashSet<>(
                        currentGroups.groups().get(i)).intersection(
                            Arrays.stream(p.getWishlist()).boxed().collect(Collectors.toSet())
                    ).size();

                var name =
                    currentGroups.isWishListMode() ?
                    "%s (Önskningar: %d)".formatted(p.getName(), wishes) :
                    p.getName();

                y += VERTICAL_SPACER / 5 + fm.getHeight();
                tb.add(new PersonBox(name, x, y, Utils.FOREGROUND_COLOR, p.getId()));
            }
        }

        textBoxes = ImmutableArray.fromList(tb);
    }

    /**
     * Calculates the height of the panel.
     *
     * @return the height of the panel.
     * */
    private int height()
    {
        if (currentGroups == null)
            return this.getHeight();

        var groups =
                currentGroups.groups()
                        .stream()
                        .map(x -> x.stream().map(gm::getPersonFromId))
                        .map(x -> x.collect(Collectors.toList()))
                        .collect(Collectors.toList());

        var max = Collections.max(currentGroups.groups().stream().map(Set::size).collect(Collectors.toList()));
        return VERTICAL_SPACER + VERTICAL_SPACER * ((groups.size() - 1 % groups.size()) / 2) * (max + 4);
    }

    /**
     * Calculates the width of the panel.
     *
     * @return the width of the panel.
     * */
    private int width()
    {
        if (currentGroups == null)
            return this.getWidth();

        var groups =
            currentGroups.groups()
                .stream()
                .map(x -> x.stream().map(gm::getPersonFromId))
                .flatMap(x -> x.map(p -> currentGroups.isWishListMode() ? p.getName() + " (Önskningar: 10)" : p.getName()))
                .collect(Collectors.toList());

        groups.sort((s1, s2) -> Integer.compare(s2.length(), s1.length()));

        var width = this.getWidth() / 4;

        if (!currentGroups.isLeaderMode())
            width -= fm.stringWidth("Grupp x") / 2;

        DebugMethods.log(width, DebugMethods.LogType.DEBUG);
        width += fm.stringWidth(groups.get(0)) * 3;

        DebugMethods.log("Longest entry: %s".formatted(groups.get(0)), DebugMethods.LogType.DEBUG);
        DebugMethods.log(width, DebugMethods.LogType.DEBUG);

        return width;
    }

    /**
     * Flashes the group boxed the person can
     * be swapped into.
     * */
    private void flashGroupBoxes()
    {
        if (t != null)
            t.stop();

        final var tbs =
            textBoxes.toList()
                     .stream()
                     .filter(x -> !(x instanceof PersonBox))
                     .collect(Collectors.toList());

        // No lambda since need to keep track of value.
        t = new Timer(500, new ActionListener() {
            /** The counter. */
            private int counter = 0;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (counter++ % 2 == 1)
                    tbs.forEach(x -> x.setColor(Utils.FLASH_COLOR));
                else
                    tbs.forEach(x -> x.setColor(Utils.GROUP_NAME_COLOR));

                repaint();
            }
        });

        t.start();
    }

    /**
     * Converts a TextBox to a tuple,
     * where the first index is the
     * group index and the second index
     * is the index in the the group. <br><br>
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
            currentGroups.groups()
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

        var groups = currentGroups.groups().stream().map(ArrayList::new).collect(Collectors.toList());
        int id = groups.get(lastTuple.a()).remove(lastTuple.b());
        groups.get(groupIndex).add(id);

        var sg = new Subgroups(
            currentGroups.name(), groups.stream().map(HashSet::new).collect(Collectors.toList()),
            currentGroups.isLeaderMode(), currentGroups.isWishListMode()
        );

        currentGroups = sg;
        gf.setCurrentGroups(sg);
        drawGroups();
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
     * Setter for: currentGroups.
     *
     * @param sg the new set of groups.
     * */
    public void setCurrentGroups(Subgroups sg)
    {
        this.currentGroups = sg;
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

        if (currentGroups != null)
        {
            initGroups(g);
            textBoxes.forEach(tb -> tb.draw(g));
//            textBoxes.forEach(x -> x.getHitBox().drawHitBox(g)); // Only for debug purposes.
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(width(), height());
    }

    @Override
    public Dimension getMinimumSize()
    {
        return new Dimension(width(), height());
    }

    @Override
    public Dimension getMaximumSize()
    {
        return new Dimension(width(), height());
    }

    @Override
    public Dimension getPreferredScrollableViewportSize()
    {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        return 16;
    }

    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        return getPreferredSize().width <= getParent().getSize().width;
    }

    @Override
    public boolean getScrollableTracksViewportHeight()
    {
        return getPreferredSize().height <= getParent().getSize().height;
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

                if (t != null)
                    t.stop();

                text.setColor(Utils.FOREGROUND_COLOR);
                lastTuple = null;
                repaint();
            }
            else if (text != null) // Name selected.
            {
                // Resets previous selection.
                textBoxes.forEach(tb -> {
                    if (tb instanceof PersonBox)
                        tb.setColor(Utils.FOREGROUND_COLOR);
                    else
                        tb.setColor(Utils.GROUP_NAME_COLOR);

                    repaint();
                });

                if (t != null)
                    t.stop();

                var groupIndex = getSelectedGroup(text);

                DebugMethods.log(
                    "Detected click on group index %d".formatted(groupIndex),
                    DebugMethods.LogType.DEBUG
                );

                updateGroups(groupIndex);
                lastTuple = null;

                repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
