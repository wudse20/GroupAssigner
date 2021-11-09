package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.frames.GroupFrame;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.util.List;

/**
 * The base type for all the SizeSettingsPanels. The
 * name is a bit confusing but well I'll live.
 * */
public sealed abstract class SettingsPanel extends JPanel
    permits MainGroupSizeSettingsPanel, SizeSettingsPanel, SubgroupSettingsPanel
{
    protected final GroupFrame gf;

    public SettingsPanel(GroupFrame gf)
    {
        super();

        this.gf = gf;
    }

    /**
     * Gets the user input from the program,
     * to determine sizes.
     *
     * @return a list containing the sizes of
     *         the groups.
     * */
    public abstract List<Integer> getUserInput();

    /**
     * Optional to implement. Resets the state of
     * the panel.
     * */
    public void reset() {}

    /**
     * The default border used in the Settings.
     *
     * @return the Border with the settings text.
     * */
    protected final Border getSettingsBorder()
    {
        var settingsBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR), "Inställningar"
        );

        settingsBorder.setTitleColor(Utils.FOREGROUND_COLOR);

        return settingsBorder;
    }
}
