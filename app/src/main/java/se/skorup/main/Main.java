package se.skorup.main;

import se.skorup.main.gui.main.frames.MainFrame;
import se.skorup.util.Log;
import se.skorup.util.resource.ResourceLoader;

import javax.swing.SwingUtilities;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.opengl", "true");
        loadResources();
        SwingUtilities.invokeLater(MainFrame::new);
    }

    private static void loadResources()
    {
        var rl = ResourceLoader.getBuilder()
                               .initLangFile("SV_se.lang")
                               .loadIcons()
                               .build();

        try
        {
            rl.loadResources();
        }
        catch (IOException e)
        {
            Log.error(e);
        }
    }
}
