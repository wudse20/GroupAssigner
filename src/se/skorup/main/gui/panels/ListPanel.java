package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class ListPanel extends JPanel implements ActionListener
{
    /** The key for the added set in {@link ListPanel#getLists()} */
    public static final String ADDED_KEY = "added";

    /** The key for the not added set in {@link ListPanel#getLists()} */
    public static final String NOT_ADDED_KEY = "notAdded";

    /** The enum used for button action commands. */
    private enum Buttons
    {
        /** The add button. */
        ADD,
        /** The remove button. */
        REMOVE
    }

    /** The persons that aren't added. */
    private final Set<Person> notAdded;

    /** The persons that are added. */
    private final Set<Person> added;

    /** All the action callbacks. */
    private final List<ActionCallback> callbacks = new Vector<>();

    /** The panel holding the buttons. */
    private final JPanel btnPanel = new JPanel();

    /** The panel for the btnPanel. */
    private final JPanel buttonContainer = new JPanel();

    /** The container holding the main items. */
    private final JPanel container = new JPanel();

    /** The button for removing. */
    private final JButton btnRemove = new JButton("<html>&larr;</html>");

    /** The button for adding. */
    private final JButton btnAdd = new JButton("<html>&rarr;</html>");

    /** The spacer label. */
    private final JLabel lblSpacer1 = new JLabel(" ");

    /** The spacer label. */
    private final JLabel lblSpacer2 = new JLabel(" ");

    /** The spacer label. */
    private final JLabel lblSpacer3 = new JLabel("        ");

    /** The spacer label. */
    private final JLabel lblSpacer4 = new JLabel("        ");

    /** The info label. */
    private final JLabel lblInfo;

    /** The layout of the panel. */
    private final BorderLayout layout = new BorderLayout();

    /** The layout of the container. */
    private final GridLayout containerLayout = new GridLayout(1, 3);

    /** The layout of the btnPanel. */
    private final BoxLayout btnPanelLayout = new BoxLayout(btnPanel, BoxLayout.Y_AXIS);

    /** The layout of the button container.*/
    private final FlowLayout buttonContainerLayout = new FlowLayout(FlowLayout.CENTER);

    /** The list of the added persons. */
    private final JList<Person> listAdded = new JList<>();

    /** The list of the not added persons. */
    private final JList<Person> listNotAdded = new JList<>();

    /** The scroller for the added persons. */
    private final JScrollPane scrAdded = new JScrollPane(listAdded);

    /** The scroller for the not added persons. */
    private final JScrollPane scrNotAdded = new JScrollPane(listNotAdded);

    /** The list model of the added persons. */
    private final PersonListModel modelAdded = new PersonListModel(null);

    /** The list model of the not added persons. */
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
        btnPanel.add(lblSpacer1);
        btnPanel.add(btnAdd);
        btnPanel.add(lblSpacer2);
        btnPanel.add(btnRemove);

        buttonContainer.add(lblSpacer3);
        buttonContainer.add(btnPanel);
        buttonContainer.add(lblSpacer4);

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
        container.setLayout(containerLayout);

        btnPanel.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnPanel.setLayout(btnPanelLayout);

        buttonContainer.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        buttonContainer.setLayout(buttonContainerLayout);

        btnAdd.setForeground(Utils.FOREGROUND_COLOR);
        btnAdd.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnAdd.setActionCommand(Buttons.ADD.toString());
        btnAdd.addActionListener(this);
        btnAdd.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createEmptyBorder(3, 15, 5, 15)
            )
        );

        btnRemove.setForeground(Utils.FOREGROUND_COLOR);
        btnRemove.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnRemove.setActionCommand(Buttons.REMOVE.toString());
        btnRemove.addActionListener(this);
        btnRemove.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createEmptyBorder(3, 15, 5, 15)
            )
        );

        listAdded.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        listAdded.setForeground(Utils.FOREGROUND_COLOR);
        listAdded.setModel(modelAdded);

        scrAdded.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        scrAdded.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        listNotAdded.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        listNotAdded.setForeground(Utils.FOREGROUND_COLOR);
        listNotAdded.setModel(modelNotAdded);

        scrNotAdded.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        scrNotAdded.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        lblInfo.setForeground(Utils.FOREGROUND_COLOR);
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
        res.put("added", added);
        res.put("notAdded", notAdded);
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

    @Override
    public void actionPerformed(ActionEvent e)
    {
        var cmd = e.getActionCommand();
        DebugMethods.log(
            "Getting action command: %s".formatted(cmd),
            DebugMethods.LogType.DEBUG
        );

        if (cmd.equals(Buttons.ADD.toString()))
        {
            var index = listNotAdded.getSelectedIndex();
            DebugMethods.log("ADD, index: %d".formatted(index), DebugMethods.LogType.DEBUG);

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

                DebugMethods.log("Adding person: %s".formatted(p), DebugMethods.LogType.DEBUG);
            }
        }
        else
        {
            var index = listAdded.getSelectedIndex();
            DebugMethods.log("REMOVE, index: %d".formatted(index), DebugMethods.LogType.DEBUG);

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

                DebugMethods.log("Removing person: %s".formatted(p), DebugMethods.LogType.DEBUG);
            }
        }
    }
}
