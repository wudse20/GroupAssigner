
package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.objects.Candidate;
import se.skorup.main.objects.Leader;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * The panel responsible for listing a person for edit.
 * */
public class PersonPanel extends JPanel implements ActionListener, WindowStateListener, ComponentListener
{
    private final MainFrame mf;

    private Person p;

    private final JPanel pName = new JPanel();
    private final JPanel pContainer = new JPanel();
    private final JPanel pCheckBox = new JPanel();

    private final BorderLayout layout = new BorderLayout();

    private final JLabel lblName = new JLabel();

    private final ListPanel wishlist;
    private final ListPanel denylist;

    private final JRadioButton radioMG1 = new JRadioButton("Huvudgrupp 1");
    private final JRadioButton radioMG2 = new JRadioButton("Huvudgrupp 2");

    private final ButtonGroup bgMainGroup = new ButtonGroup();

    private final JLabel lblSpacer1 = new JLabel(" ");
    private final JLabel lblSpacer2 = new JLabel("   ");
    private final JLabel lblSpacer3 = new JLabel("   ");
    private final JLabel lblSpacer4 = new JLabel(" ");
    private final JLabel lblSpacer5 = new JLabel(" ");
    private final JLabel lblSpacer6 = new JLabel(" ");
    private final JLabel lblSpacer7 = new JLabel(" ");

    private final JCheckBox cbShowOnlySameRole = new JCheckBox("Visa endast deltagare av samma roll");
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
        // Stops the panel from adding the components if p is null.
        if (p == null)
            return;

        pName.removeAll();
        pCheckBox.removeAll();
        pContainer.removeAll();

        pName.add(lblName);

        pCheckBox.add(cbShowOnlySameRole);

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
        this.setLayout(layout);

        lblName.setForeground(Utils.FOREGROUND_COLOR);
        lblName.setFont(new Font(Font.DIALOG, Font.BOLD, 24));

        pName.setLayout(new FlowLayout(FlowLayout.LEFT));
        pName.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        cbShowOnlySameRole.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbShowOnlySameRole.setForeground(Utils.FOREGROUND_COLOR);
        cbShowOnlySameRole.addActionListener(this);

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
    }

    /**
     * The method that initializes a person
     * and draws it to the GUI.
     * */
    private void initPerson()
    {
        this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

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
        // If there's no person, then remove everything.
        if (p == null)
        {
            this.setBorder(BorderFactory.createEmptyBorder());
            this.setBackground(Utils.BACKGROUND_COLOR);
            this.removeAll();

            lblName.setText("");
        }
        else // Initialize the panel with a person.
        {
            initPerson();
        }

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

        var candidatesOnly = cbShowOnlySameRole.isSelected();

        var addedDeny = candidatesOnly ? group.getAllOfRollBut(p) : group.getAllBut(p);
        var addedWish = candidatesOnly ? group.getAllOfRollBut(p) : group.getAllBut(p);

        var notAddedDeny = candidatesOnly ? group.getAllOfRollBut(p) : group.getAllBut(p);
        var notAddedWish = candidatesOnly ? group.getAllOfRollBut(p) : group.getAllBut(p);

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
        var d = new Dimension(mf.getWidth() / 5, mf.getHeight() / 5);

        wishlist.setPreferredListSize(d);
        denylist.setPreferredListSize(d);
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
        var d = new Dimension(mf.getWidth() / 5, mf.getHeight() / 5);

        wishlist.setPreferredListSize(d);
        denylist.setPreferredListSize(d);
    }
}