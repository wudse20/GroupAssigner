package se.skorup.games.base;

import se.skorup.API.util.Utils;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Graphics2D;

public class GamePanel extends JPanel
{
    protected void drawStart(Graphics2D g, GameFrame gf) {
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        g.setColor(Utils.SELECTED_COLOR);
        var width = g.getFontMetrics().stringWidth("Press 'Enter' to start!");
        g.drawString(
                "Press 'Enter' to start!",
                (gf.width() - 7) / 2 - width / 2, gf.height() / 2 - 25
        );
    }
}
