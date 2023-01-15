package se.skorup.games.base;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.manager.helper.SerializationManager;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;

public class GamePanel extends JPanel
{
    protected void drawStart(Graphics2D g, GameFrame gf)
    {
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        g.setColor(Utils.SELECTED_COLOR);
        var width = g.getFontMetrics().stringWidth("Press 'Enter' to start!");
        g.drawString(
                "Press 'Enter' to start!",
                (gf.width() - 7) / 2 - width / 2, gf.height() / 2 - 25
        );
    }

    /**
     * Loads the highscore.
     *
     * @param path the path to the file.
     * @return the current highscore.
     * */
    protected Score loadHighscore(String path)
    {
        Score hs = null;

        try
        {
            hs = (Score) SerializationManager.deserializeObject(path);
        }
        catch (IOException | ClassNotFoundException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.ERROR);
        }

        return hs == null ? new Score(0) : hs;
    }
}
