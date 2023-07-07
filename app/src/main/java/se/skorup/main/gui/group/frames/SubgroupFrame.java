package se.skorup.main.gui.group.frames;

import se.skorup.group.Group;
import se.skorup.group.generation.GroupCreator;
import se.skorup.group.generation.RandomGroupCreator;
import se.skorup.group.generation.WishesGroupCreator;
import se.skorup.group.progress.Progress;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.containers.Frame;
import se.skorup.gui.components.progress.ProgressBar;
import se.skorup.main.gui.group.helper.Creator;
import se.skorup.main.gui.group.helper.ProgressReport;
import se.skorup.main.gui.group.panels.CreatorPanel;
import se.skorup.main.gui.group.panels.SizePanel;
import se.skorup.main.gui.group.panels.SubgroupDisplayPanel;
import se.skorup.util.Log;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import java.util.Set;

/**
 * The frame responsible for generating subgroups.
 * */
public class SubgroupFrame extends Frame implements ComponentListener
{
    enum State
    {
        CREATOR, SIZE, PROGRESS, DISPLAY
    }

    private State state = State.CREATOR;

    private final Group g;
    private GroupCreator creator;
    private Progress p;
    private List<Set<Integer>> current;

    private final GroupFrame gf;

    private final CreatorPanel creatorPanel = new CreatorPanel();
    private final SizePanel sizePanel = new SizePanel();
    private final SubgroupDisplayPanel subgroupDisplayPanel = new SubgroupDisplayPanel((JComponent) cp);

    private final ProgressBar progress = new ProgressBar();

    /**
     * Creates a new Frame. With a localization key
     * that will be used for the title.
     *
     * @param gf the instance of the GroupFrame in use.
     * @param g the group that will be used to create subgroups.
     **/
    public SubgroupFrame(GroupFrame gf, Group g)
    {
        super("ui.title.subgroups");
        this.gf = gf;
        this.g = g;

        init();
    }

    @Override
    protected void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addComponentListener(this);

        creatorPanel.addCallback(c -> {
            p = new ProgressReport(progress);

            if (c.equals(Creator.RANDOM))
                creator = new RandomGroupCreator(p);
            else
                creator = new WishesGroupCreator(p);

            Log.debugf("Chosen creator: %s", creator);
            state = State.SIZE;
            addComponents();
        });

        sizePanel.addBackCallback(unused -> {
            state = State.CREATOR;
            addComponents();
        });

        sizePanel.addNextCallback(this::generateAction);
    }

    /**
     * The action that will be performed when next is pressed
     * in the sizePanel.
     *
     * @param sizes the list of sizes that is gotten from
     *              the size panel.
     * */
    private void generateAction(List<Integer> sizes)
    {
        new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                state = State.PROGRESS;
                addComponents();
            });

            final var res = sizes.size() == 1 ?
                            creator.generate(g, sizes.get(0), false) :
                            creator.generate(g, sizes);

            Log.debugf("Generated Groups: %s", res);

            SwingUtilities.invokeLater(() -> {
                progress.setValue(1_000_000);
                subgroupDisplayPanel.displayGroups(g, res.get(0));
                current = res.get(0);
                state = State.DISPLAY;
                addComponents();
            });
        }, "Geneartion therad :)").start();
    }

    @Override
    protected void addComponents()
    {
        cp.removeAll();

        var p = switch (state) {
            case CREATOR -> creatorPanel;
            case SIZE -> sizePanel;
            case PROGRESS -> new ComponentContainer(progress);
            case DISPLAY -> subgroupDisplayPanel;
        };

        cp.add(p, BorderLayout.CENTER);
        cp.revalidate();
        cp.repaint();

        if (!state.equals(State.DISPLAY))
            this.pack();
        else
            this.setSize(1690, 910);
    }

    @Override
    protected void init()
    {
        gf.setVisible(false);
        super.init();
    }

    @Override
    public void dispose()
    {
        gf.setVisible(true);
        super.dispose();
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        if (current != null)
            subgroupDisplayPanel.displayGroups(g, current);
    }

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}
