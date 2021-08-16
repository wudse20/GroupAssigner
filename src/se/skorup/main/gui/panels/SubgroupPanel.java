package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.groups.AlternateWishlistGroupCreator;
import se.skorup.main.groups.GroupCreator;
import se.skorup.main.groups.RandomGroupCreator;
import se.skorup.main.groups.WishlistGroupCreator;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.gui.interfaces.GroupGenerator;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * The panel that draws the SubGroups.
 * */
public class SubgroupPanel extends JPanel
{
    private final GroupFrame gf;

    private final GroupManager gm;

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

        this.setProperties();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);

        gf.addActionListener(e -> gf.waitCursorAction(this::generateGroups), GroupButtonPanel.Buttons.CREATE);
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

        List<Set<Integer>> groups = null;
        try
        {
            groups = tryGenerateGroups(gg);
        }
        catch (NoGroupAvailableException e)
        {
            JOptionPane.showMessageDialog(
                this,
                "Misslyckades att generera grupper.\nFelmeddeleande: %s".formatted(e.getLocalizedMessage()),
                "Gruppskapande misslyckades", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        current = new Subgroups(
            null, groups, gf.getSizeState().equals(GroupFrame.State.PAIR_WITH_LEADERS),
            gc instanceof WishlistGroupCreator, new Vector<>()
        );

        DebugMethods.log("Generated groups: ", DebugMethods.LogType.DEBUG);
        DebugMethods.log(current, DebugMethods.LogType.DEBUG);
    }

    /**
     * Calculates the height of the panel. TODO: IMPLEMENT
     *
     * @return the calculated height of the panel.
     * */
    private int height()
    {
        return 0;
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
}
