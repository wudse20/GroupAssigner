package se.skorup.games.base;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.API.util.SerializationUtil;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
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
            hs = (Score) SerializationUtil.deserializeObject(path);
        }
        catch (IOException | ClassNotFoundException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.ERROR);
        }

        return hs == null ? new Score(0) : hs;
    }

    /**
     * Saves the highscore to a path.
     *
     * @param path the path of the score.
     * @param score the new highscore.
     * */
    protected void saveHighscore(String path, Score score)
    {
        try
        {
            SerializationUtil.createFileIfNotExists(new File(path));
            SerializationUtil.serializeObject(path, score);
        }
        catch (IOException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.ERROR);
        }
    }
}
