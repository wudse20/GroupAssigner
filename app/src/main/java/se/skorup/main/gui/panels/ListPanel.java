package se.skorup.main.gui.panels;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.gui.models.PersonListModel;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The panel used for the lists in the person
 * panel.
 * */
public class ListPanel extends JPanel
{
    /** The key for the added set in {@link ListPanel#getLists()} */
    public static final String ADDED_KEY = "added";

    /** The key for the not added set in {@link ListPanel#getLists()} */
    public static final String NOT_ADDED_KEY = "notAdded";

    private final Set<Person> notAdded;
    private final Set<Person> added;

    private final List<ActionCallback> callbacks = new Vector<>();

    private final JPanel btnPanel = new JPanel();
    private final JPanel buttonContainer = new JPanel();
    private final JPanel container = new JPanel();

    private final JButton btnRemove = new JButton("<html>&larr;</html>");
    private final JButton btnAdd = new JButton("<html>&rarr;</html>");

    private final JLabel lblInfo;

    private final BorderLayout layout = new BorderLayout();

    private final JList<Person> listAdded = new JList<>();
    private final JList<Person> listNotAdded = new JList<>();

    private final JScrollPane scrAdded = new JScrollPane(listAdded);
    private final JScrollPane scrNotAdded = new JScrollPane(listNotAdded);

    private final PersonListModel modelAdded = new PersonListModel(null);
    private final PersonListModel modelNotAdded = new PersonListModel(null);

    /**
     * Creates a new ListPanel. The sets passed to
     * the constructor will be cloned, so there's
     * no need to clone them before passing the
     * sets to this constructor.
     *
     * @param added the persons added.
     * @param notAdded the persons not added.
     * @param label the text of the label above the panel.
     * */
    public ListPanel(Set<Person> notAdded, Set<Person> added, String label)
    {
        this.notAdded = notAdded.stream().map(Person::clone).collect(Collectors.toSet());
        this.added = added.stream().map(Person::clone).collect(Collectors.toSet());
        this.lblInfo = new JLabel(label);

        this.setProperties();
        this.refreshListData();
        this.addComponents();
    }

    /**
     * Adds components.
     * */
    private void addComponents()
    {
        btnPanel.add(new JLabel(" "));
        btnPanel.add(btnAdd);
        btnPanel.add(new JLabel(" "));
        btnPanel.add(btnRemove);

        buttonContainer.add(new JLabel("        "));
        buttonContainer.add(btnPanel);
        buttonContainer.add(new JLabel("        "));

        container.add(scrNotAdded);
        container.add(buttonContainer);
        container.add(scrAdded);

        this.add(lblInfo, BorderLayout.PAGE_START);
        this.add(container, BorderLayout.CENTER);
    }

    /**
     * Refreshes the list data.
     * */
    private void refreshListData()
    {
        modelAdded.removeAll();
        modelAdded.addItems(added);

        modelNotAdded.removeAll();
        modelNotAdded.addItems(notAdded);
    }

    /**
     * Sets the properties of the frame.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setLayout(layout);

        container.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        container.setLayout(new GridLayout(1, 3));

        btnPanel.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));

        buttonContainer.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnAdd.setForeground(Utils.FOREGROUND_COLOR);
        btnAdd.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnAdd.addActionListener(e -> addButton());
        btnAdd.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createEmptyBorder(3, 15, 5, 15)
            )
        );

        btnRemove.setForeground(Utils.FOREGROUND_COLOR);
        btnRemove.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnRemove.addActionListener(e -> removeButton());
        btnRemove.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createEmptyBorder(3, 15, 5, 15)
            )
        );

        listAdded.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        listAdded.setForeground(Utils.FOREGROUND_COLOR);
        listAdded.setModel(modelAdded);
        listAdded.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == 10) // Enter was released
                    removeButton();
            }
        });

        scrAdded.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        scrAdded.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        listNotAdded.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        listNotAdded.setForeground(Utils.FOREGROUND_COLOR);
        listNotAdded.setModel(modelNotAdded);
        listNotAdded.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == 10) // Enter was released
                    addButton();
            }
        });

        scrNotAdded.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        scrNotAdded.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        lblInfo.setForeground(Utils.FOREGROUND_COLOR);
    }

    /**
     * The remove button action.
     * */
    private void removeButton()
    {
        // Needs boxing to be able to sort with custom comparator and not the natural ordering.
        var indices = Arrays.stream(listAdded.getSelectedIndices()).boxed().toArray(Integer[]::new);
        Arrays.sort(indices, (i1, i2) -> Integer.compare(i2, i1));

        for (var index : indices)
        {
            if (index != -1 && modelAdded.getSize() != 0)
            {
                var p = modelAdded.getElementAt(index);

                notAdded.add(p);
                added.remove(p);

                modelNotAdded.addItem(p);
                modelAdded.removeItem(p);

                listAdded.clearSelection();
                listNotAdded.clearSelection();

                // Invokes the callbacks.
                callbacks.forEach(ActionCallback::callback);
            }
        }
    }

    /**
     * The add button action.
     * */
    private void addButton()
    {
        // Needs boxing to be able to sort with custom comparator and not the natural ordering.
        var indices = Arrays.stream(listNotAdded.getSelectedIndices()).boxed().toArray(Integer[]::new);
        Arrays.sort(indices, (i1, i2) -> Integer.compare(i2, i1));

        for (var index : indices)
        {
            if (index != -1 && modelNotAdded.getSize() != 0)
            {
                var p = modelNotAdded.getElementAt(index);

                added.add(p);
                notAdded.remove(p);

                modelAdded.addItem(p);
                modelNotAdded.removeItem(p);

                listAdded.clearSelection();
                listNotAdded.clearSelection();

                // Invokes the callbacks.
                callbacks.forEach(ActionCallback::callback);
            }
        }
    }

    /**
     * Adds an ActionCallback to the panel.
     * If the provided callback is null, then
     * it will do nothing.
     *
     * @param c the callback to be added, if {@code null}
     *          then it will do nothing and just return.
     * */
    public void addActionCallback(ActionCallback c)
    {
        if (c == null)
            return;

        callbacks.add(c);
    }

    /**
     * Removes all the callbacks.
     * */
    public void removeAllActionCallbacks()
    {
        callbacks.clear();
    }

    /**
     * Sets the list data of the panel.
     *
     * @param added the new elements in the added list.
     * @param notAdded the new elements in the not added list.
     * */
    public void setListData(Collection<Person> added, Collection<Person> notAdded)
    {
        this.added.clear();
        this.added.addAll(added);
        this.listAdded.clearSelection();

        this.notAdded.clear();
        this.notAdded.addAll(notAdded);
        this.listNotAdded.clearSelection();

        this.refreshListData();
    }

    /**
     * Get the two different lists.<br> <br>
     *
     * To access the different sets use: <br>
     * <b>Added: {@link ListPanel#ADDED_KEY}</b><br>
     * <b>Not Added: {@link ListPanel#NOT_ADDED_KEY}</b>
     *
     * @return a Map containing the two lists. Where the key, String,
     *         is the key for the set and the value, Set&lt;Person&gt;,
     *         is the set containing the person that corresponds to the
     *         list with the correct key.
     * */
    public Map<String, Set<Person>> getLists()
    {
        var res = new HashMap<String, Set<Person>>();
        res.put(ADDED_KEY, added);
        res.put(NOT_ADDED_KEY, notAdded);
        return res;
    }

    /**
     * Sets the preferred size of the lists.
     *
     * @param d the new size of the lists.
     * */
    public void setPreferredListSize(Dimension d)
    {
        DebugMethods.log(
            "New list size for %s: %s px".formatted(lblInfo.getText(), d),
            DebugMethods.LogType.DEBUG
        );

        scrAdded.setPreferredSize(d);
        scrNotAdded.setPreferredSize(d);
    }
}
