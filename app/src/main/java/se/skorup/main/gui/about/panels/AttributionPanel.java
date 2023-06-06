package se.skorup.main.gui.about.panels;

import se.skorup.gui.components.Button;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.components.ScrollPane;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

/**
 * The attribution panel.
 * */
public class AttributionPanel extends Panel
{
    private static IconInfo[] icons;

    /**
     * Creates a AttributionPanel
     */
    public AttributionPanel()
    {
        super(new GridLayout(icons.length, 1));

        for (var icon : icons)
            this.add(new IconAttributionPanel(icon));
    }

    /**
     * Loads the icons that need attribution -
     * i.e., all the icons I haven't made myself.
     *
     * @param icons the icons that need attribution.
     * */
    public static void loadAttributionIcons(IconInfo... icons)
    {
        AttributionPanel.icons = icons;
    }

    private static class IconAttributionPanel extends Panel
    {
        /**
         * Creates a new panel IconAttributionPanel.
         *
         * @param icon The layout of the panel.
         * */
        public IconAttributionPanel(IconInfo icon)
        {
            super(new FlowLayout(FlowLayout.LEFT));
            var btn = new Button(icon.icon);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setBackground(Utils.BACKGROUND_COLOR);
            btn.addActionListener(e -> {
                var url = Localization.getValue(icon.localizationKey).split("Download:")[1].trim();
                Utils.openWebpage(url);
            });

            this.add(btn);
            this.add(new Label("   "));
            this.add(new Label(icon.localizationKey, true));
        }
    }

    /**
     * Information about the icon and the icon itself.
     *
     * @param icon the icon itself.
     * @param localizationKey the localization key used for the attribution.
     * */
    public record IconInfo(ImageIcon icon, String localizationKey) {}
}
