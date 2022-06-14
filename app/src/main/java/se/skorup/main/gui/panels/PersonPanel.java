package se.skorup.main.gui.panels;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.gui.models.PersonListModel;
import se.skorup.main.objects.Candidate;
import se.skorup.main.objects.Leader;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The panel responsible for listing a person for edit.
 * */
public class PersonPanel extends JPanel implements ActionListener, WindowStateListener, ComponentListener, KeyListener
{
    private final MainFrame mf;

    private Person p;

    private final JPanel pName = new JPanel();
    private final JPanel pContainer = new JPanel();
    private final JPanel pCheckBox = new JPanel();
    private final JPanel pMainGroupOverview = new JPanel();
    private final JPanel pButtons = new JPanel();
    private final JPanel pBottom = new JPanel();
    private final JPanel pBottomContainer = new JPanel();
    private final JPanel pSearch = new JPanel();

    private final BorderLayout layout = new BorderLayout();

    private final JLabel lblName = new JLabel();
    private final JLabel lblSearch = new JLabel("   Sök: ");

    private final JTextField txfSearch = new JTextField(12);

    private final ListPanel wishlist;
    private final ListPanel denylist;

    private final PersonListModel mainGroup1Model = new PersonListModel(new ArrayList<>());
    private final PersonListModel mainGroup2Model = new PersonListModel(new ArrayList<>());

    private final JList<Person> mainGroup1 = new JList<>();
    private final JList<Person> mainGroup2 = new JList<>();

    private final JScrollPane scrMainGroup1 = new JScrollPane(mainGroup1);
    private final JScrollPane scrMainGroup2 = new JScrollPane(mainGroup2);

    private final JRadioButton radioMG1 = new JRadioButton("Huvudgrupp 1");
    private final JRadioButton radioMG2 = new JRadioButton("Huvudgrupp 2");

    private final JButton btnAddMg1 = new JButton("<html>&larr;</html>");
    private final JButton btnAddMg2 = new JButton("<html>&rarr;</html>");

    private final ButtonGroup bgMainGroup = new ButtonGroup();

    private final JLabel lblSpacer1 = new JLabel(" ");
    private final JLabel lblSpacer2 = new JLabel("   ");
    private final JLabel lblSpacer3 = new JLabel("   ");
    private final JLabel lblSpacer4 = new JLabel(" ");
    private final JLabel lblSpacer5 = new JLabel(" ");
    private final JLabel lblSpacer6 = new JLabel(" ");
    private final JLabel lblSpacer7 = new JLabel(" ");

    private final JCheckBox cbSameMainGroup = new JCheckBox("Visa endast deltagare av huvudgrupp");
    private final JButton btnChangeRole = new JButton("Byt roll");

    /**
     * Creates a new person panel.
     *
     * @param mf the instance of the MainFrame in use.
     * */
    public PersonPanel(MainFrame mf)
    {
        this.mf = mf;
        this.wishlist = new ListPanel(new HashSet<>(), new HashSet<>(), "Önskelista:");
        this.denylist = new ListPanel(new HashSet<>(), new HashSet<>(), "Denylist:");

        this.setProperties();
        this.setup();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        if (p == null)
            addAllDisplayComponents();
        else
            addPersonComponents();
    }

    /**
     * Adds the components when not displaying a person.
     * */
    private void addAllDisplayComponents()
    {
        pButtons.removeAll();
        pMainGroupOverview.removeAll();
        pBottom.removeAll();
        pBottomContainer.removeAll();

        pButtons.add(btnAddMg2);
        pButtons.add(new JLabel("<html><br></html>")); // Love HTML hacks :)
        pButtons.add(btnAddMg1);

        pMainGroupOverview.add(new JLabel("   "));
        pMainGroupOverview.add(scrMainGroup1);
        pMainGroupOverview.add(new JLabel(" ".repeat(25)));
        pMainGroupOverview.add(pButtons);
        pMainGroupOverview.add(new JLabel(" ".repeat(25)));
        pMainGroupOverview.add(scrMainGroup2);
        pMainGroupOverview.add(new JLabel("   "));

        lblName.setForeground(Utils.FOREGROUND_COLOR);
        lblName.setText("<html><br>&nbsp;&nbsp;&nbsp;Överblick<br><br></html>"); // Gotta love HTMl Hax :)

        pSearch.add(lblSearch);
        pSearch.add(txfSearch);

        pBottom.add(new JLabel("<html><br><br></html>"));
        pBottom.add(pSearch);
        pBottom.add(new JLabel("<html><br><br></html>"));

        pBottomContainer.add(new JLabel("   "));
        pBottomContainer.add(pBottom);

        this.add(lblName, BorderLayout.PAGE_START);
        this.add(pMainGroupOverview, BorderLayout.CENTER);
        this.add(pBottomContainer, BorderLayout.PAGE_END);
    }

    /**
     * Adds the components for displaying a person.
     * */
    private void addPersonComponents()
    {
        pName.removeAll();
        pCheckBox.removeAll();
        pContainer.removeAll();

        pName.add(lblName);

        pCheckBox.add(cbSameMainGroup);

        // Don't want MainGroups for leaders.
        if (p instanceof Candidate)
        {
            pCheckBox.add(radioMG1);
            pCheckBox.add(radioMG2);
        }

        pCheckBox.add(btnChangeRole);

        pContainer.add(pName);
        pContainer.add(lblSpacer5);
        pContainer.add(wishlist);
        pContainer.add(lblSpacer6);
        pContainer.add(denylist);
        pContainer.add(lblSpacer7);
        pContainer.add(pCheckBox);

        this.add(lblSpacer1, BorderLayout.PAGE_START);
        this.add(lblSpacer2, BorderLayout.LINE_START);
        this.add(pContainer, BorderLayout.CENTER);
        this.add(lblSpacer3, BorderLayout.LINE_END);
        this.add(lblSpacer4, BorderLayout.PAGE_END);

        pContainer.revalidate();
        pName.revalidate();
        pCheckBox.revalidate();
    }

    /**
     * Sets the properties of the panel and
     * its components.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        this.setLayout(layout);

        lblName.setForeground(Utils.FOREGROUND_COLOR);
        lblName.setFont(new Font(Font.DIALOG, Font.BOLD, 24));

        pName.setLayout(new FlowLayout(FlowLayout.LEFT));
        pName.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        cbSameMainGroup.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbSameMainGroup.setForeground(Utils.FOREGROUND_COLOR);
        cbSameMainGroup.addActionListener(this);

        pCheckBox.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        pCheckBox.setLayout(new FlowLayout(FlowLayout.LEFT));

        pContainer.setLayout(new BoxLayout(pContainer, BoxLayout.Y_AXIS));
        pContainer.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnChangeRole.setForeground(Utils.FOREGROUND_COLOR);
        btnChangeRole.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnChangeRole.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createEmptyBorder(3, 15, 5, 15)
            )
        );

        radioMG1.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        radioMG1.setForeground(Utils.MAIN_GROUP_1_COLOR);
        radioMG1.addActionListener(e ->{
            p.setMainGroup(Person.MainGroup.MAIN_GROUP_1);
            lblName.setForeground(Utils.MAIN_GROUP_1_COLOR);
        });

        radioMG2.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        radioMG2.setForeground(Utils.MAIN_GROUP_2_COLOR);
        radioMG2.addActionListener(e -> {
            p.setMainGroup(Person.MainGroup.MAIN_GROUP_2);
            lblName.setForeground(Utils.MAIN_GROUP_2_COLOR);
        });

        bgMainGroup.add(radioMG1);
        bgMainGroup.add(radioMG2);

        pMainGroupOverview.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        pMainGroupOverview.setLayout(new FlowLayout(FlowLayout.CENTER));

        mainGroup1.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        mainGroup1.setForeground(Utils.MAIN_GROUP_1_COLOR);
        mainGroup1.setModel(mainGroup1Model);

        mainGroup2.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        mainGroup2.setForeground(Utils.MAIN_GROUP_2_COLOR);
        mainGroup2.setModel(mainGroup2Model);

        var b1 =
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                "Huvudgrupp 1"
            );
        b1.setTitleColor(Utils.FOREGROUND_COLOR);

        var b2 =
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                "Huvudgrupp 2"
            );
        b2.setTitleColor(Utils.FOREGROUND_COLOR);

        scrMainGroup1.setForeground(Utils.FOREGROUND_COLOR);
        scrMainGroup1.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        scrMainGroup1.setBorder(b1);
        scrMainGroup1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrMainGroup1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrMainGroup2.setForeground(Utils.FOREGROUND_COLOR);
        scrMainGroup2.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        scrMainGroup2.setBorder(b2);
        scrMainGroup2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrMainGroup2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        btnAddMg1.setForeground(Utils.FOREGROUND_COLOR);
        btnAddMg1.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnAddMg1.addActionListener(e -> setMainGroupOfSelected(mainGroup2, Person.MainGroup.MAIN_GROUP_1));
        btnAddMg1.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createEmptyBorder(3, 15, 5, 15)
            )
        );

        btnAddMg2.setForeground(Utils.FOREGROUND_COLOR);
        btnAddMg2.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnAddMg2.addActionListener(e -> setMainGroupOfSelected(mainGroup1, Person.MainGroup.MAIN_GROUP_2));
        btnAddMg2.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createEmptyBorder(3, 15, 5, 15)
            )
        );

        pButtons.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        pButtons.setLayout(new BoxLayout(pButtons, BoxLayout.Y_AXIS));

        pBottom.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));

        pBottomContainer.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        pBottomContainer.setLayout(new FlowLayout(FlowLayout.LEFT));

        pSearch.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        pSearch.setLayout(new FlowLayout(FlowLayout.RIGHT));

        lblSearch.setForeground(Utils.FOREGROUND_COLOR);

        txfSearch.setForeground(Utils.FOREGROUND_COLOR);
        txfSearch.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        txfSearch.addKeyListener(this);
    }

    /**
     * Resets a JList.
     *
     * @param list the panel to be reset.
     * */
    private void resetList(JList<Person> list)
    {
        if (mf.getCurrentGroup() == null)
        {
            list.setSelectedIndices(new int[0]);
            list.setListData(new Person[0]);
        }
    }

    /**
     * The method that initializes the stuff for
     * when a person isn't selected.
     * */
    private void initNoPersonSelected()
    {
        var gm = mf.getCurrentGroup();

        if (gm == null)
            return;

        mainGroup1.setListData(
            new Vector<>(gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_1))
        );

        mainGroup2.setListData(
            new Vector<>(gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_2))
        );

        search();
    }

    /**
     * The method that initializes a person
     * and draws it to the GUI.
     * */
    private void initPerson()
    {
        lblName.setText(p.getName());
        if (p instanceof Candidate)
            lblName.setForeground(
                p.getMainGroup().equals(Person.MainGroup.MAIN_GROUP_1) ?
                Utils.MAIN_GROUP_1_COLOR : Utils.MAIN_GROUP_2_COLOR
            );
        else
            lblName.setForeground(Utils.FOREGROUND_COLOR);

        this.updateListData();

        denylist.removeAllActionCallbacks();
        wishlist.removeAllActionCallbacks();

        denylist.addActionCallback(() -> {
            // Sets the new list data for the denylist.
            var res = denylist.getLists();
            p.setDenylist(res.get(ListPanel.ADDED_KEY));
            updateListData();
        });

        wishlist.addActionCallback(() -> {
            var res = wishlist.getLists();
            p.setWishlist(res.get(ListPanel.ADDED_KEY));
            updateListData();
        });

        btnChangeRole.setText((p instanceof Leader) ? "Gör till deltagare" : "Gör till ledare");
        Arrays.stream(btnChangeRole.getActionListeners()).forEach(btnChangeRole::removeActionListener);

        btnChangeRole.addActionListener((e) -> {
            mf.getCurrentGroup().removePerson(p.getId());
            mf.getCurrentGroup().registerPerson(
                p.getName(), (p instanceof Leader) ? Person.Role.CANDIDATE : Person.Role.LEADER
            );
            mf.refreshSidePanel();

            p = null;
            setup();
        });

        if (p.getMainGroup().equals(Person.MainGroup.MAIN_GROUP_1))
            radioMG1.setSelected(true);
        else
            radioMG2.setSelected(true);
    }

    /**
     * Setup for the panel
     * */
    private void setup()
    {
        resetList(mainGroup1);
        resetList(mainGroup2);
        setSizes();

        if (p == null) // If there's no person, then display overview.
            initNoPersonSelected();
        else // Initialize the panel with a person.
            initPerson();

        this.removeAll();
        this.addComponents();
        this.revalidate();
        this.repaint();
    }

    /**
     * Updates the list data.
     * */
    private void updateListData()
    {
        var group = mf.getCurrentGroup();
        var deny = Arrays.stream(p.getDenylist()).boxed().collect(Collectors.toList());
        var wish = Arrays.stream(p.getWishlist()).boxed().collect(Collectors.toList());

        var sameMainGroup = cbSameMainGroup.isSelected();

        var addedDeny =
            sameMainGroup ?
            group.getAllOfMainGroupAndRollBut(p, p.getRole(), p.getMainGroup()) :
            group.getAllBut(p);

        var addedWish =
            sameMainGroup ?
            group.getAllOfMainGroupAndRollBut(p, p.getRole(), p.getMainGroup()) :
            group.getAllBut(p);

        var notAddedDeny =
            sameMainGroup ?
            group.getAllOfMainGroupAndRollBut(p, p.getRole(), p.getMainGroup()) :
            group.getAllBut(p);

        var notAddedWish =
            sameMainGroup ?
            group.getAllOfMainGroupAndRollBut(p, p.getRole(), p.getMainGroup()) :
            group.getAllBut(p);

        // All persons intersect list = added persons to list.
        var denies = addedDeny.stream().map(Person::getId).collect(Collectors.toSet());
        var wishes = addedWish.stream().map(Person::getId).collect(Collectors.toSet());

        denies.retainAll(deny);
        wishes.retainAll(wish);

        // All persons \ added = persons not added.
        notAddedDeny.removeAll(denies.stream().map(group::getPersonFromId).collect(Collectors.toSet()));
        notAddedWish.removeAll(wishes.stream().map(group::getPersonFromId).collect(Collectors.toSet()));

        // Removes all persons that shouldn't be in the wish- & denylist.
        // It remove the items that are already added in the other list.
        notAddedDeny.removeAll(wishes.stream().map(group::getPersonFromId).collect(Collectors.toSet()));
        notAddedWish.removeAll(denies.stream().map(group::getPersonFromId).collect(Collectors.toSet()));

        denylist.setListData(
            denies.stream().map(group::getPersonFromId).collect(Collectors.toSet()),
            notAddedDeny
        );

        wishlist.setListData(
            wishes.stream().map(group::getPersonFromId).collect(Collectors.toSet()),
            notAddedWish
        );
    }

    /**
     * Sets the sizes of the lists in the GUI.
     * */
    private void setSizes()
    {
        DebugMethods.log("Setting Sizes", DebugMethods.LogType.DEBUG);
        var d = new Dimension((mf.getWidth() / 5), mf.getHeight() / 5); // Base
        var d2 = new Dimension((int) (d.width * 1.23), d.height); // Denylist / wishlist
        var d3 = new Dimension((int) (d.width * 1.365), (int) (d.height * 1.85)); // MainGroup lists

        wishlist.setPreferredListSize(d2);
        denylist.setPreferredListSize(d2);
        scrMainGroup1.setPreferredSize(d3);
        scrMainGroup2.setPreferredSize(d3);
    }

    /**
     * Sets the main group of all persons selected in list persons to
     * the main group mg.
     *
     * @param persons the list of selected persons to be affected.
     * @param mg the new main group.
     * */
    private void setMainGroupOfSelected(JList<Person> persons, Person.MainGroup mg)
    {
        var selected = persons.getSelectedValuesList();

        for (var p : selected)
            p.setMainGroup(mg);

        this.setup();
    }

    /**
     * Given an input it will give all matching results of a given
     * role.
     *
     * @param persons the persons to be searched through.
     * @param input the input to the search.
     * */
    private List<Person> getSearchResultOfRole(Set<Person> persons, String input)
    {
        DebugMethods.log("Search input: '%s'".formatted(input), DebugMethods.LogType.DEBUG);
        DebugMethods.log("Trimmed input: '%s'".formatted(input.trim()), DebugMethods.LogType.DEBUG);

        if (input.trim().equalsIgnoreCase("id"))
            return List.of(); // Don't want any results for id.

        if (input.trim().isEmpty()) // Don't want spaces to give no result.
            return new ArrayList<>(persons);

        return persons.parallelStream()
                      .filter(p -> p.toString().toLowerCase().contains(input.trim().toLowerCase()))
                      .toList();
    }

    /**
     * Uses the text in the search field and based on that
     * updates the GUI with the correct data.
     * */
    private void search()
    {
        var input = txfSearch.getText();
        var gm = mf.getCurrentGroup();
        var candidates = getSearchResultOfRole(gm.getAllOfRoll(Person.Role.CANDIDATE), input);
        var leaders = getSearchResultOfRole(gm.getAllOfRoll(Person.Role.LEADER), input);
        var mg1 = getSearchResultOfRole(
                gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_1), input
        );

        var mg2 = getSearchResultOfRole(
                gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_2), input
        );

        DebugMethods.log("Search result: Candidates: %s".formatted(candidates), DebugMethods.LogType.DEBUG);
        DebugMethods.log("Search result: Leaders: %s".formatted(leaders), DebugMethods.LogType.DEBUG);

        mf.setSideListData(new HashSet<>(candidates), new HashSet<>(leaders));
        mainGroup1.setListData(new Vector<>(mg1));
        mainGroup2.setListData(new Vector<>(mg2));
    }

    /**
     * Setter for: person.
     *
     * @param p the person to be set to this panel.
     * */
    public void setPerson(Person p)
    {
        this.p = p;
        this.setup();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.setup();
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        setSizes();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    @Override
    public void windowStateChanged(WindowEvent e)
    {
        setSizes();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e)
    {
        search();
    }
}
