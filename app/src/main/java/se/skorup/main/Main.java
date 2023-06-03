package se.skorup.main;

import se.skorup.gui.components.Button;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.MathTextField;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.util.Log;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;
import se.skorup.util.resource.ResourceLoader;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        loadResources();
        Log.debugf("Localization: %s", Localization.getLanguageMap());
        MessageDialog.create()
                     .setLocalizedTitle("ui.title.test")
                     .setLocalizedInformation("ui.info.test")
                     .setLocalizedButtonText("ui.button.dialog.close")
                     .show();
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame();
            frame.getContentPane().setBackground(Utils.BACKGROUND_COLOR);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.add(new Button("ui.button.save"), BorderLayout.PAGE_START);
            frame.add(new MathTextField(12), BorderLayout.CENTER);
            frame.add(new Label("ui.info.test", true), BorderLayout.PAGE_END);
            frame.pack();
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
