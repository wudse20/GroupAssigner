package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.gui.models.PersonListModel;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The panel used for the lists in the person
 * panel.
 * */
public class ListPanel extends JPanel implements ActionListener
{
    /** The enum used for button action commands. */
    private enum Buttons
    {
        ADD, REMOVE
    }

    /** The persons that aren't added. */
    private final Set<Person> notAdded;

    /** The persons that are added. */
    private final Set<Person> added;

    /** All the action callbacks. */
    private final List<ActionCallback> callbacks = new Vector<>();

    /** The panel holding the buttons. */
    private final JPanel btnPanel = new JPanel();

    /** The container holding the main items. */
    private final JPanel container = new JPanel();

    /** The button for removing. */
    private final JButton btnRemove = new JButton("<html>&larr;</html>");

    /** The button for adding. */
    private final JButton btnAdd = new JButton("<html>&rarr;</html>");

    /** The spacer label. */
    private final JLabel lblSpacer = new JLabel(" ");

    private final JLabel lblInfo;

    /** The layout of the panel. */
    private final BorderLayout layout = new BorderLayout();

    /** The layout of the container. */
    private final FlowLayout containerLayout = new FlowLayout(FlowLayout.CENTER);

    /** The layout of the btnPanel. */
    private final BoxLayout btnPanelLayout = new BoxLayout(btnPanel, BoxLayout.Y_AXIS);

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
        btnPanel.add(btnAdd);
        btnPanel.add(lblSpacer);
        btnPanel.add(btnRemove);

        container.add(scrNotAdded);
        container.add(btnPanel);
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

        btnAdd.setForeground(Utils.FOREGROUND_COLOR);
        btnAdd.setBackground(Utils.BACKGROUND_COLOR);
        btnAdd.setActionCommand(Buttons.ADD.toString());
        btnAdd.addActionListener(this);

        btnRemove.setForeground(Utils.FOREGROUND_COLOR);
        btnRemove.setBackground(Utils.BACKGROUND_COLOR);
        btnRemove.setActionCommand(Buttons.REMOVE.toString());
        btnRemove.addActionListener(this);

        listAdded.setBackground(Utils.BACKGROUND_COLOR);
        listAdded.setForeground(Utils.FOREGROUND_COLOR);
        listAdded.setModel(modelAdded);

        scrAdded.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        scrAdded.setBackground(Utils.BACKGROUND_COLOR);

        listNotAdded.setBackground(Utils.BACKGROUND_COLOR);
        listNotAdded.setForeground(Utils.FOREGROUND_COLOR);
        listNotAdded.setModel(modelNotAdded);

        scrNotAdded.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        scrNotAdded.setBackground(Utils.BACKGROUND_COLOR);
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

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JOptionPane.showMessageDialog(
                this, "Not Yet Implemented",
                "Not Yet Implemented", JOptionPane.ERROR_MESSAGE
        );
    }
}
