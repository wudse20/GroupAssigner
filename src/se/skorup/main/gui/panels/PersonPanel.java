package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * The panel responsible for listing a person for edit.
 * */
public class PersonPanel extends JPanel
{
    /** The instance of the MainFrame in use. */
    private final MainFrame mf;

    /** The person that is being shown at the moment. */
    private Person p;

    /** The panel for the name. */
    private final JPanel pName = new JPanel();

    /** The container panel. */
    private final JPanel pContainer = new JPanel();

    /** The layout of the name panel. */
    private final FlowLayout pNameLayout = new FlowLayout(FlowLayout.LEFT);

    /** The layout of the container. */
    private final BoxLayout pContainerLayout = new BoxLayout(pContainer, BoxLayout.Y_AXIS);

    /** The layout of the panel. */
    private final BorderLayout layout = new BorderLayout();

    /** The label with the persons name. */
    private final JLabel lblName = new JLabel();

    /** The ListPanel for the wishlist. */
    private final ListPanel wishlist = new ListPanel(new HashSet<>(), new HashSet<>(), "Önskelista:");

    /** The ListPanel for the denylist. */
    private final ListPanel denylist = new ListPanel(new HashSet<>(), new HashSet<>(), "Denylist:");

    /** Spacer */
    private final JLabel lblSpacer1 = new JLabel(" ");

    /** Spacer */
    private final JLabel lblSpacer2 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer3 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer4 = new JLabel(" ");

    /** Spacer */
    private final JLabel lblSpacer5 = new JLabel(" ");

    /** Spacer */
    private final JLabel lblSpacer6 = new JLabel(" ");

    /**
     * Creates a new person panel.
     *
     * @param mf the instance of the MainFrame in use.
     * */
    public PersonPanel(MainFrame mf)
    {
        this.mf = mf;

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

        pName.add(lblName);

        pContainer.add(pName);
        pContainer.add(lblSpacer5);
        pContainer.add(wishlist);
        pContainer.add(lblSpacer6);
        pContainer.add(denylist);

        this.add(lblSpacer1, BorderLayout.PAGE_START);
        this.add(lblSpacer2, BorderLayout.LINE_START);
        this.add(pContainer, BorderLayout.CENTER);
        this.add(lblSpacer3, BorderLayout.LINE_END);
        this.add(lblSpacer4, BorderLayout.PAGE_END);
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

        pName.setLayout(pNameLayout);
        pName.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        pContainer.setLayout(pContainerLayout);
        pContainer.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
    }

    /**
     * Setup for the panel
     * */
    private void setup()
    {
        // If there's no person,
        // then remove everything.
        if (p == null)
        {
            this.setBorder(BorderFactory.createEmptyBorder());
            this.setBackground(Utils.BACKGROUND_COLOR);
            this.removeAll();

            lblName.setText("");
        }
        else
        {
            this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
            this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

            lblName.setText(p.getName());
            var group = mf.getCurrentGroup();
            var deny = Arrays.stream(p.getDenylist()).boxed().collect(Collectors.toList());
            var wish = Arrays.stream(p.getWishlist()).boxed().collect(Collectors.toList());

            var addedDeny = group.getAllBut(p);
            var addedWish = group.getAllBut(p);

            // All persons intersect list = added persons to list.
            var denies = addedDeny.stream().map(Person::getId).collect(Collectors.toSet());
            var wishes = addedWish.stream().map(Person::getId).collect(Collectors.toSet());

            denies.retainAll(deny);
            wishes.retainAll(wish);

            var notAddedDeny = group.getAllBut(p);
            var notAddedWish = group.getAllBut(p);

            // All persons \ added = persons not added.
            notAddedDeny.removeAll(denies.stream().map(group::getPersonFromId).collect(Collectors.toSet()));
            notAddedWish.removeAll(wishes.stream().map(group::getPersonFromId).collect(Collectors.toSet()));

            denylist.setListData(
                denies.stream().map(group::getPersonFromId).collect(Collectors.toSet()),
                notAddedDeny
            );

            wishlist.setListData(
                wishes.stream().map(group::getPersonFromId).collect(Collectors.toSet()),
                notAddedWish
            );
        }

        this.addComponents();
        this.revalidate();
        this.repaint();
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
}
