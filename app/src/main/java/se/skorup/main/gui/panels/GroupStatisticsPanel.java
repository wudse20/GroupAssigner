package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import static se.skorup.API.util.Utils.padString;

/**
 * Formats the data of the group manager to
 * a panel.
 * */
public class GroupStatisticsPanel extends JPanel
{
    private final GroupManager gm;
    private final JLabel lblPersons = new JLabel();
    private final JLabel lblLeaders = new JLabel();
    private final JLabel lblCandidates = new JLabel();
    private final JLabel lblMainGroup1 = new JLabel();
    private final JLabel lblMainGroup2 = new JLabel();
    private final JLabel lblDenyItems = new JLabel();
    private final JLabel lblWishItems = new JLabel();

    /**
     * Creates a new GroupStatisticsPanel.
     *
     * @param gm the instance of the GroupManager in use.
     * */
    public GroupStatisticsPanel(GroupManager gm)
    {
        this.gm = gm;

        this.setProperties();
        this.updateData();
        this.addComponents();
    }

    /**
     * Sets the properties of everything and anything.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setLayout(new BorderLayout());

        setUpLabel(lblPersons, Utils.FOREGROUND_COLOR);
        setUpLabel(lblLeaders, Utils.FOREGROUND_COLOR);
        setUpLabel(lblCandidates, Utils.FOREGROUND_COLOR);
        setUpLabel(lblMainGroup1, Utils.MAIN_GROUP_1_COLOR);
        setUpLabel(lblMainGroup2, Utils.MAIN_GROUP_2_COLOR);
        setUpLabel(lblWishItems, Utils.FOREGROUND_COLOR);
        setUpLabel(lblDenyItems, Utils.LIGHT_RED);
    }

    /**
     * Setup for the labels to all look and feel the same.
     *
     * @param label the label that is being affected.
     * @param color the color that is the color of the ground of the label.
     * @return it will return itself. So when done it is will invoke {@code return label;}.
     * */
    private JLabel setUpLabel(JLabel label, Color color)
    {
        label.setForeground(color);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));

        return label;
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        var cont = new JPanel();
        cont.setBackground(Utils.BACKGROUND_COLOR);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));

        cont.add(lblPersons);
        cont.add(lblLeaders);
        cont.add(lblCandidates);
        cont.add(setUpLabel(new JLabel(" "), Utils.FOREGROUND_COLOR));
        cont.add(lblMainGroup1);
        cont.add(lblMainGroup2);
        cont.add(setUpLabel(new JLabel(" "), Utils.FOREGROUND_COLOR));
        cont.add(lblWishItems);
        cont.add(lblDenyItems);
        cont.add(new JLabel(" "));

        var contCont = new JPanel(); // Nice name; or not.
        contCont.setBackground(Utils.BACKGROUND_COLOR);
        contCont.setForeground(Utils.FOREGROUND_COLOR);
        contCont.setLayout(new FlowLayout(FlowLayout.CENTER));
        var border = BorderFactory.createTitledBorder(
            BorderFactory.createDashedBorder(null, 1.25f, 6, 4, true),
            gm.getName()
        );

        border.setTitleColor(Utils.FOREGROUND_COLOR);
        contCont.setBorder(border);
        contCont.add(cont);

        this.add(new JLabel(" "), BorderLayout.PAGE_START);
        this.add(new JLabel("   "), BorderLayout.LINE_START);
        this.add(contCont, BorderLayout.CENTER);
        this.add(new JLabel("   "), BorderLayout.LINE_END);
        this.add(new JLabel(" "), BorderLayout.PAGE_END);
    }

    /**
     * Updates the data.
     * */
    private void updateData()
    {
        var total = gm.getMemberCount();
        var leaders = gm.getMemberCountOfRole(Person.Role.LEADER);
        var candidates = gm.getMemberCountOfRole(Person.Role.CANDIDATE);
        var mg1 = gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_1).size();
        var mg2 = gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_2).size();
        var denies = gm.getDenyGraph().size();
        var wishes = gm.getWishGraph().size();
        var len = "Total antal denylist placeringar: ".length();

        lblPersons.setText("%s%d".formatted(padString("Antal personer:", ' ', len), total));
        lblLeaders.setText("%s%d".formatted(padString("Antal ledare:", ' ', len), leaders));
        lblCandidates.setText("%s%d".formatted(padString("Antal deltagare:", ' ', len), candidates));
        lblMainGroup1.setText("%s%d".formatted(padString("Antal deltagare i huvudgrupp 1:", ' ', len), mg1));
        lblMainGroup2.setText("%s%d".formatted(padString("Antal deltagare i huvudgrupp 2:", ' ', len), mg2));
        lblWishItems.setText("%s%d".formatted(padString("Total antal önskningar:", ' ', len), wishes));
        lblDenyItems.setText("%s%d".formatted(padString("Total antal denylist placeringar:", ' ', len), denies));
    }
}
