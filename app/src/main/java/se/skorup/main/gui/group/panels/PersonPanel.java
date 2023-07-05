package se.skorup.main.gui.group.panels;

import se.skorup.group.Group;
import se.skorup.group.Person;
import se.skorup.gui.callbacks.DeleteCallback;
import se.skorup.gui.components.Button;
import se.skorup.gui.components.CheckBox;
import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.components.ScrollPane;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The panel used to control a person's wishes and
 * */
public class PersonPanel extends Panel
{
    private Person p;
    private Group g;

    private final List<DeleteCallback> callbacks;

    private final Button btnDelete = new Button("ui.button.delete");

    /**
     * Creates a new person panel.
     * */
    public PersonPanel()
    {
        super(null);
        this.callbacks = new ArrayList<>();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        btnDelete.addActionListener(e -> callbacks.forEach(c -> c.onDelete(g, p)));
    }

    /**
     * Sets up a panel with all the things to display either wishes
     * or denylist items.
     *
     * @param p the panel affected
     * @param titleKey the key for the title label.
     * @param id the id of the person that is selected in the GUI.
     * @param all all the ids.
     * @param selectedIds the selected ids.
     * @param update the function to register an update.
     * */
    private static void setupPanel(
        Panel p, String titleKey, int id, List<Person> all,
        Set<Integer> selectedIds, RegisterUpdate update
    )
    {
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        var lbl = new Label(titleKey, true);
        lbl.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        p.add(new ComponentContainer(lbl));

        var cbPanel = new Panel(null);
        cbPanel.setLayout(new BoxLayout(cbPanel, BoxLayout.Y_AXIS));

        for (var per : all)
        {
            if (per.id() == id)
                continue;

            var cb = new CheckBox(per.name());
            cb.setSelected(selectedIds.contains(per.id()));
            cb.addActionListener(e -> update.onUpdate(id, per.id(), cb.isSelected()));

            var cont = new Panel(new FlowLayout(FlowLayout.LEFT));
            cont.add(cb);
            cbPanel.add(cont);
        }

        var scr = new ScrollPane(cbPanel);
        scr.setBorder(BorderFactory.createEmptyBorder());
        p.add(scr);
    }

    /**
     * Displays a person.
     * */
    public void displayPerson(Person p, Group g)
    {
        this.removeAll();
        var wishes = g.getWishedIds(p.id());
        var denies = g.getDeniedIds(p.id());
        var all = new ArrayList<>(g.getPersons());
        Collections.sort(all);

        var wish = new Panel(null);
        setupPanel(wish, "ui.label.wish", p.id(), all, wishes, (id1, id2, selected) -> {
            if (selected)
                g.addWishItem(id1, id2);
            else
                g.removeWishItem(id1, id2);
        });

        var deny = new Panel(null);
        setupPanel(deny, "ui.label.deny", p.id(), all, denies, (id1, id2, selected) -> {
            if (selected)
                g.addDenyItem(id1, id2);
            else
                g.removeDenyItem(id1, id2);
        });

        this.add(wish);
        this.add(deny);
        this.add(new ComponentContainer(btnDelete));
        this.revalidate();

        this.p = p;
        this.g = g;
    }

    /**
     * Adds a callback that will be invoked when
     * a person is deleted from a group.
     *
     * @param callback the callback to be added.
     * */
    public void addCallback(DeleteCallback callback)
    {
        if (callback == null)
            return;

        callbacks.add(callback);
    }

    @FunctionalInterface
    private interface RegisterUpdate
    {
        /**
         * This is called on an update.
         *
         * @param id1 the id of the person selected in the PersonPanel.
         * @param id2 the id of the person selected with the checkbox.
         * @param selected if {@code true} then it will add,
         *                 if {@code false} then it will remove.
         * */
        void onUpdate(int id1, int id2, boolean selected);
    }
}
