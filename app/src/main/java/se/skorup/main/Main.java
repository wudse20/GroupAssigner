package se.skorup.main;

import se.skorup.gui.components.CheckBox;
import se.skorup.gui.components.Label;
import se.skorup.util.Log;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;
import se.skorup.util.resource.ResourceLoader;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.opengl", "true");
        loadResources();
        SwingUtilities.invokeLater(() -> {
            var cb = new CheckBox("ui.button.save");
            cb.setEnabled(false);
            var frame = new JFrame(Localization.getValue("ui.title.test"));
            frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(Utils.BACKGROUND_COLOR);
            frame.getContentPane().add(new Label(" "));
            frame.getContentPane().add(new CheckBox("ui.text.bug"));
            frame.getContentPane().add(new Label(" "));
            frame.getContentPane().add(cb);
            frame.getContentPane().add(new Label(" "));
            frame.pack();
            frame.setVisible(true);
        });
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
