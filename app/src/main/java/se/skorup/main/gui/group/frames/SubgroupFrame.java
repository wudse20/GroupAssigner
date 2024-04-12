package se.skorup.main.gui.group.frames;

import se.skorup.group.Group;
import se.skorup.group.generation.GroupCreator;
import se.skorup.group.generation.RandomGroupCreator;
import se.skorup.group.generation.WishesGroupCreator;
import se.skorup.group.progress.Progress;
import se.skorup.gui.components.buttons.Button;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.containers.FlowContainer;
import se.skorup.gui.components.containers.Frame;
import se.skorup.gui.components.containers.Panel;
import se.skorup.gui.components.progress.ProgressBar;
import se.skorup.gui.dialog.Dialog;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.main.gui.group.helper.Creator;
import se.skorup.main.gui.group.helper.GenerationSettings;
import se.skorup.main.gui.group.helper.ProgressReport;
import se.skorup.main.gui.group.panels.CreatorPanel;
import se.skorup.main.gui.group.panels.GenerationSettingsPanel;
import se.skorup.main.gui.group.panels.SubgroupDisplayPanel;
import se.skorup.util.Log;
import se.skorup.util.PrinterUtil;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private final GenerationSettingsPanel settingsPanel = new GenerationSettingsPanel(this);
    private final SubgroupDisplayPanel subgroupDisplayPanel;

    private final ProgressBar progress = new ProgressBar();

    private final Button btnPrint = new Button("ui.button.print");

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
        this.subgroupDisplayPanel = new SubgroupDisplayPanel((JComponent) cp, g);

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

        settingsPanel.addBackCallback(unused -> {
            state = State.CREATOR;
            addComponents();
        });

        settingsPanel.addNextCallback(this::generateAction);

        btnPrint.addActionListener(e -> {
            try
            {
                PrinterUtil.print(toPrintString());
            }
            catch (PrinterException ex)
            {
                new Thread(() -> {
                    MessageDialog.create()
                                 .setLocalizedTitle("ui.title.printer-failure")
                                 .setLocalizedInformationf("ui.title.dialog.error", ex.getLocalizedMessage())
                                 .setLocalizedButtonText("ui.button.dialog.ok")
                                 .show(Dialog.ERROR_MESSAGE);
                }, "SubgroupFrame::setProperties-print-failure-dialog").start();

                Log.errorf("Failed to print: %s", ex.getLocalizedMessage());
            }
        });
    }

    /**
     * The action that will be performed when next is pressed
     * in the sizePanel.
     *
     * @param settings the list of sizes that is gotten from
     *              the size panel.
     * */
    private void generateAction(GenerationSettings settings)
    {
        new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                state = State.PROGRESS;
                addComponents();
            });

            final List<List<Set<Integer>>> res;
            if (settings.useMainGroups())
            {
                var mg1 = settings.mg1Sizes();
                var mg2 = settings.mg2Sizes();

                var mg1Res = mg1.size() == 1 ?
                             creator.generate(g.mainGroupOneAsGroup(), mg1.getFirst(), false) :
                             creator.generate(g.mainGroupOneAsGroup(), mg1);

                var mg2Res = mg1.size() == 1 ?
                             creator.generate(g.mainGroupTwoAsGroup(), mg2.getFirst(), false) :
                             creator.generate(g.mainGroupTwoAsGroup(), mg2);

                var groups = new ArrayList<>(mg1Res.get(new Random().nextInt(0, mg1Res.size())));
                groups.addAll(mg2Res.get(new Random().nextInt(0, mg2Res.size())));
                res = List.of(groups);
            }
            else
            {
                var sizes = settings.sizes();

                res = sizes.size() == 1 ?
                      creator.generate(g, sizes.getFirst(), false) :
                      creator.generate(g, sizes);

                Log.debugf("Generated Groups: %s", res);
            }

            SwingUtilities.invokeLater(() -> {
                progress.setValue(1_000_000);
                subgroupDisplayPanel.displayGroups(g, res.getFirst());
                current = res.getFirst();
                state = State.DISPLAY;
                addComponents();
            });
        }, "Generation thread :)").start();
    }

    private String toPrintString()
    {
        var sb = new StringBuilder();

        var cnt = 0;
        for (var group : current)
        {
            cnt++;
            sb.append("<red>")
              .append("Grupp ")
              .append(cnt)
              .append(':')
              .append('\n')
              .append("<black>");

            for (var id : group)
            {
                sb.append('\t')
                  .append(g.getFromId(id))
                  .append('\n');
            }

            sb.append("</black>")
              .append('\n');
        }

        return sb.toString();
    }

    @Override
    protected void addComponents()
    {
        cp.removeAll();

        var p = switch (state) {
            case CREATOR -> creatorPanel;
            case SIZE -> settingsPanel;
            case PROGRESS -> new ComponentContainer(progress);
            case DISPLAY -> {
                var cont = new Panel(new BorderLayout());
                cont.add(subgroupDisplayPanel, BorderLayout.CENTER);
                cont.add(new FlowContainer(btnPrint, FlowLayout.RIGHT), BorderLayout.PAGE_END);
                yield cont;
            }
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
