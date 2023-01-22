package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.manager.Group;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.Optional;

import static se.skorup.API.util.Utils.padString;

/**
 * Formats the data of the group manager to
 * a panel.
 * */
public final class GroupStatisticsPanel extends AbstractStatisticsPanel
{
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
    public GroupStatisticsPanel(Group gm)
    {
        super(gm);
        this.init(Optional.empty());
    }

    @Override
    protected void setProperties()
    {
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
        this.removeAll();
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

        basicLayout(cont, gm.getName());
    }

    @Override
    protected void updateData(Optional<Subgroups> sg, Group gm)
    {
        var total = gm.getMemberCount();
        var leaders = gm.getMemberCountOfRole(Person.Role.LEADER);
        var candidates = gm.getMemberCountOfRole(Person.Role.CANDIDATE);
        var mg1 = gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_1).size();
        var mg2 = gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_2).size();
        var denies = gm.getDenyGraph().size();
        var wishes = gm.getWishGraph().size();

        lblPersons.setText("%s%d".formatted(padString("Antal personer:", ' ', len), total));
        lblLeaders.setText("%s%d".formatted(padString("Antal ledare:", ' ', len), leaders));
        lblCandidates.setText("%s%d".formatted(padString("Antal deltagare:", ' ', len), candidates));
        lblMainGroup1.setText("%s%d".formatted(padString("Antal deltagare i huvudgrupp 1:", ' ', len), mg1));
        lblMainGroup2.setText("%s%d".formatted(padString("Antal deltagare i huvudgrupp 2:", ' ', len), mg2));
        lblWishItems.setText("%s%d".formatted(padString("Total antal önskningar:", ' ', len), wishes));
        lblDenyItems.setText("%s%d".formatted(padString("Total antal denylist placeringar:", ' ', len), denies));
    }
}
