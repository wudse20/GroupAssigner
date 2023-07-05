package se.skorup.main.gui.group.panels;

import se.skorup.group.Group;
import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.dialog.ConfirmDialog;
import se.skorup.gui.dialog.Dialog;
import se.skorup.gui.dialog.FileDialog;
import se.skorup.gui.dialog.InputDialog;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.main.gui.group.frames.GroupFrame;
import se.skorup.main.gui.group.frames.ImportFrame;
import se.skorup.util.Log;
import se.skorup.util.io.CSVReader;

import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * The panel responsible for displaying the groups.
 * */
public class GroupPanel extends Panel
{
    enum State
    {
        NO_SELECTED, SELECTED
    }

    private State state = State.NO_SELECTED;

    private List<Group> groups;

    private final Label lblTitle = new Label("ui.label.my-groups", true);

    private final GroupDisplayPanel gdp = new GroupDisplayPanel();
    private final PersonPanel pp = new PersonPanel();

    private final GroupFrame gf;

    /**
     * Creates a new GroupPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public GroupPanel(GroupFrame gf)
    {
        super(new BorderLayout());
        this.gf = gf;

        setProperties();
        addComponents();
    }

    /**
     * Sets the properties of the components.
     * */
    private void setProperties()
    {
        lblTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 36));

        gdp.addSelectionCallback((p, g) -> {
            if (p == null || g == null)
            {
                state = State.NO_SELECTED;
                addComponents();
                return;
            }

            state = State.SELECTED;
            pp.displayPerson(p, g);
            addComponents();
        });

        gdp.addCreateGroupCallback(unused -> {
            gf.setVisible(false);
            Log.debugf("Is EDT? %s", SwingUtilities.isEventDispatchThread() ? "yes" : "no");

            // New thread since it's the EDT.
            new Thread(() -> {
                // true = from scratch
                // false = CSV-parsing
                var option = ConfirmDialog.create()
                                          .setLocalizedQuestion("ui.question.how-create")
                                          .setLocalizedApproveButtonText("ui.button.empty-group")
                                          .setLocalizedDisapproveButtonText("ui.button.csv-group")
                                          .setLocalizedTitle("ui.title.create-group")
                                          .show(Dialog.NO_ICON);

                if (option == null)
                {
                    gf.setVisible(true);
                    return;
                }

                if (option) // Group from scratch.
                {
                    var name = InputDialog.create()
                                          .setLocalizedTitle("ui.title.create-group")
                                          .setLocalizedInformation("ui.label.create-group")
                                          .setLocalizedCancelButtonText("ui.button.dialog.cancel")
                                          .setLocalizedOkButtonText("ui.button.dialog.ok")
                                          .show(InputDialog.NO_ICON);

                    if (name == null)
                    {
                        SwingUtilities.invokeLater(() -> gf.setVisible(true));
                        return;
                    }

                    SwingUtilities.invokeLater(() -> {
                        var g = new Group(name);
                        groups.add(g);
                        setGroups(groups);
                        gf.setVisible(true);
                    });
                }
                else // CSV-group.
                {
                    try
                    {
                        var file = FileDialog.create()
                                .setPath(".")
                                .setAllowedFileExtensions(Set.of("csv"))
                                .show();

                        if (file == null)
                        {
                            SwingUtilities.invokeLater(() -> gf.setVisible(true));
                            return;
                        }

                        final var data = CSVReader.readCSV(file.getAbsolutePath());

                        SwingUtilities.invokeLater(() -> {
                            var frame = new ImportFrame(data);
                            frame.addActionCallback(g -> {
                                SwingUtilities.invokeLater(() -> {
                                    if (g != null)
                                    {
                                        groups.add(g);
                                        setGroups(groups);
                                    }

                                    gf.setVisible(true);
                                });
                            });
                        });
                    }
                    catch (IOException e)
                    {
                        MessageDialog.create()
                                     .setLocalizedTitlef("ui.title.dialog.error", e.getLocalizedMessage())
                                     .setLocalizedInformation(e.getLocalizedMessage()).setLocalizedButtonText("ui.button.close")
                                     .show(Dialog.ERROR_MESSAGE);

                        SwingUtilities.invokeLater(() -> gf.setVisible(true));
                    }
                }
            }, "Creation-dialog thread").start();
        });

        gdp.addDeleteGroupCallback(g -> {
            new Thread(() -> {
                var ans = ConfirmDialog.create()
                                       .setLocalizedQuestionf("ui.question.delete-group", g)
                                       .setLocalizedApproveButtonText("ui.button.dialog.approve")
                                       .setLocalizedDisapproveButtonText("ui.button.dialog.disapprove")
                                       .setLocalizedTitle("ui.title.delete")
                                       .show(Dialog.WARNING_MESSAGE);

                if (!ans)
                    return;

                SwingUtilities.invokeLater(() -> { // Since the EDT owns the groups. Thread Confinement :)
                    groups.remove(g);
                    setGroups(groups);
                });
            }, "Group deletion thread").start();
        });

        pp.addCallback((g, p) -> {
            g.removePerson(p.id());
            gdp.setGroups(groups);
            gdp.clearSelection();
        });
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.removeAll();

        var cont = new Panel(null);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));

        var titleCont = new Panel(new FlowLayout(FlowLayout.LEFT));
        titleCont.add(lblTitle);

        cont.add(titleCont);
        cont.add(gdp);

        this.add(cont, BorderLayout.CENTER);

        if (state.equals(State.SELECTED))
            this.add(new ComponentContainer(pp), BorderLayout.LINE_END);

        this.revalidate();
    }

    /**
     * Sets the groups to be displayed.
     *
     * @param groups the groups that are supposed to be displayed.
     * */
    public void setGroups(List<Group> groups)
    {
        gdp.setGroups(groups);
        this.groups = groups;
    }
}
