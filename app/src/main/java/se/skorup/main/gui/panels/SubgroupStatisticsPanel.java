package se.skorup.main.gui.panels;

import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.API.util.Utils;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;
import se.skorup.main.objects.Tuple;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.Optional;

import static se.skorup.API.util.Utils.padString;

/**
 * Formats the data of a Subgroups objet to a panel.
 * */
public final class SubgroupStatisticsPanel extends AbstractStatisticsPanel
{
    /** The error message for no groups. */
    private static final String NO_GROUPS = "Inga undergrupper i fliken undergrupper";

    private boolean isEmpty = true;
    private boolean isWishListMode = false;
    private String name = "";

    private final JLabel lblNoGroup = new JLabel(NO_GROUPS);
    private final JLabel lblGroups = new JLabel();
    private final JLabel lblWishesGranted = new JLabel();
    private final JLabel lblPersonsWithWishes = new JLabel();

    public SubgroupStatisticsPanel(GroupManager gm)
    {
        super(gm);
        this.init(Optional.empty());
    }

    @Override
    protected void addComponents()
    {
        this.removeAll();
        var cont = new JPanel();
        cont.setBackground(Utils.BACKGROUND_COLOR);

        if (isEmpty)
        {
            cont.setLayout(new FlowLayout(FlowLayout.CENTER));
            cont.add(lblNoGroup);
        }
        else
        {
            cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
            cont.add(lblGroups);

            if (isWishListMode)
            {
                cont.add(lblWishesGranted);
                cont.add(setUpLabel(new JLabel(" "), Utils.FOREGROUND_COLOR));
                cont.add(lblPersonsWithWishes);
            }
        }

        basicLayout(cont, name.trim().isEmpty() ? "Undergrupper" : name);
        this.revalidate();
    }

    @Override
    protected void setProperties()
    {
        this.setLayout(new BorderLayout());
        this.setUpLabel(lblNoGroup, Utils.GROUP_NAME_COLOR);
        this.setUpLabel(lblGroups, Utils.FOREGROUND_COLOR);
        this.setUpLabel(lblWishesGranted, Utils.FOREGROUND_COLOR);
        this.setUpLabel(lblPersonsWithWishes, Utils.FOREGROUND_COLOR);
    }

    @Override
    protected void updateData(Optional<Subgroups> sg)
    {
        if (sg.isEmpty())
        {
            lblNoGroup.setText(NO_GROUPS);
            this.isEmpty = true;
            return;
        }

        var s = sg.get();
        this.isEmpty = false;
        this.name = s.name() == null ? "" : s.name();
        this.isWishListMode = s.isWishListMode();

        var groups = s.getGroupCount();
        lblGroups.setText("%s%d".formatted(padString("Antal grupper:", ' ', len), groups));

        if (s.isWishListMode())
        {
            var candidates = gm.getAllOfRoll(Person.Role.CANDIDATE);
            var n = candidates.size();
            var x = new int[n];

            candidates.forEach(p -> {
                var wishes = new ImmutableHashSet<>(Tuple.imageOf(gm.getWishGraph(), p.getId()));
                ImmutableHashSet<Integer> group = null;
                for (var g : s.groups())
                {
                    if (g.contains(p.getId()))
                    {
                        group = new ImmutableHashSet<>(g);
                        break;
                    }
                }

                assert group != null;
                x[wishes.intersection(group).size()] += 1;
            });

            var w = 0;
            for (var i = 0; i < x.length; i++)
                w += i * x[i];

            lblWishesGranted.setText("%s%d".formatted(padString("Totalt antal uppfyllda önskningar:", ' ', len), w));

            var sb = new StringBuilder().append("<html>");
            var lastIndexUsed = 0;

            for (var i = x.length - 1; i >= 0; i--)
            {
                if (x[i] != 0)
                {
                    lastIndexUsed = Math.min(i + 1, x.length - 1); // Want to keep the last zero.
                    break;
                }
            }

            for (var i = 0; i <= lastIndexUsed; i++) // <= safe, since lastIndexUsed is at most x.length - 1.
            {
                var baseText = "Antal personer med %d önskningar uppfyllda:";
                var text = x[i] < 100 ? baseText + "&nbsp;&nbsp;" : baseText;
                sb.append(padString(text, "&nbsp;", len - text.length()).formatted(i));
                sb.append(x[i]);
                sb.append("<br>");
            }
            sb.append("</html>");

            lblPersonsWithWishes.setText(sb.toString());
        }
    }
}
