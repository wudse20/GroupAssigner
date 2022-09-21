package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.objects.Subgroups;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.util.Optional;

/**
 * Formats the data of a Subgroups objet to a panel.
 * */
public final class SubgroupStatisticsPanel extends AbstractStatisticsPanel
{
    /** The error message for no groups. */
    private static final String NO_GROUPS = "Det finns inga skapade undergrupper :(";

    private boolean isEmpty = true;
    private String name;

    private final JLabel lblNoGroup = new JLabel(NO_GROUPS);

    public SubgroupStatisticsPanel()
    {
        this.init(Optional.empty());
    }

    @Override
    protected void addComponents()
    {
        if (isEmpty)
        {
            this.setBorder(getBorder("Undergrupper"));
            this.add(lblNoGroup, BorderLayout.CENTER);
        }

        this.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setLayout(new BorderLayout());
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

        this.isEmpty = false;
        this.name = sg.get().name();
    }
}
