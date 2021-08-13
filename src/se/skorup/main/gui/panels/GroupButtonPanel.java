package se.skorup.main.gui.panels;

import se.skorup.API.Utils;

import javax.swing.JPanel;

import java.awt.FlowLayout;

/**
 * The button panel used in the group generation GUI.
 * */
public class GroupButtonPanel extends JPanel
{
    /**
     * Creates a new GroupButtonPanel.
     * */
    public GroupButtonPanel()
    {
        this.setProperties();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
    }

    /**
     * Populates the panel with the buttons, based
     * on the panel.
     *
     * @param panel the panel in use.
     * @throws IllegalArgumentException iff panel isn't instance of {@link SubgroupPanel SubgroupPanel}
     *                                  or {@link SubgroupSettingsPanel SubgroupSettingsPanel}.
     * */
    public void populateButtons(JPanel panel) throws IllegalArgumentException
    {
        if (panel instanceof SubgroupSettingsPanel p)
        {

        }
        else if (panel instanceof SubgroupPanel p)
        {

        }
        else
        {
            throw new IllegalArgumentException(
                "%s isn't an accepted class, for the panel. Only SubgroupPanel and SubgroupSettingsPanel are accepted."
                .formatted(panel.getClass())
            );
        }
    }
}
