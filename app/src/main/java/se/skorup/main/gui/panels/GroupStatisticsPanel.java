package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import static se.skorup.API.util.Utils.padString;

/**
 * Formats the data of the group manager to
 * a panel.
 * */
public final class GroupStatisticsPanel extends AbstractStatisticsPanel
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
        super();
        this.gm = gm;
    }

    @Override
    protected void setProperties()
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

    @Override
    protected void addComponents()
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
        contCont.setBorder(getBorder(gm.getName()));
        contCont.add(cont);

        this.add(new JLabel(" "), BorderLayout.PAGE_START);
        this.add(new JLabel("   "), BorderLayout.LINE_START);
        this.add(contCont, BorderLayout.CENTER);
        this.add(new JLabel("   "), BorderLayout.LINE_END);
        this.add(new JLabel(" "), BorderLayout.PAGE_END);
    }

    @Override
    protected void updateData()
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
