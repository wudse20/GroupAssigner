package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.ImmutableHashSet;
import se.skorup.API.Utils;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The panel that draws the SubGroups.
 * */
public class SubgroupPanel extends JPanel implements Scrollable
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
    }

    /**
     * Draws the groups, if they're
     * created.
     *
     * @param g the Graphics2D instance drawing.
     * */
    private void drawGroups(Graphics2D g)
    {
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

            g.setColor(Utils.GROUP_NAME_COLOR);
            g.drawString(groupName, x, y);
            g.setColor(Utils.FOREGROUND_COLOR);

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
                g.drawString(name, x, y);
            }
        }
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
     * Draws the current group.
     * */
    public void drawGroups()
    {
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
    public void paintComponent(Graphics gOld)
    {
        super.paintComponent(gOld);
        var g = (Graphics2D) gOld;
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        fm = g.getFontMetrics();

        if (currentGroups != null)
            drawGroups(g);
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
}
