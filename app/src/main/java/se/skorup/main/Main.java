package se.skorup.main;

import se.skorup.gui.components.Button;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.MathTextField;
import se.skorup.gui.dialog.Dialog;
import se.skorup.gui.dialog.FileDialog;
import se.skorup.gui.dialog.InputDialog;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.util.Log;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;
import se.skorup.util.resource.ResourceLoader;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Set;

public class Main
{
    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.opengl", "true");
        loadResources();
        Log.debugf("Localization: %s", Localization.getLanguageMap());
//        var data = InputDialog.create()
//                              .setLocalizedTitle("ui.title.test")
//                              .setLocalizedInformation("ui.info.test")
//                              .setLocalizedButtonText("ui.button.dialog.close")
//                              .setLocalizedOkButtonText("ui.button.dialog.ok")
//                              .show();
//        Log.debug(data);
//        MessageDialog.create()
//                     .setLocalizedTitle("ui.title.test")
//                     .setLocalizedInformation("ui.info.test")
//                     .setLocalizedButtonText("ui.button.dialog.close")
//                     .show(Dialog.ERROR_MESSAGE);
//        MessageDialog.create()
//                .setLocalizedTitle("ui.title.test")
//                .setLocalizedInformation("ui.info.test")
//                .setLocalizedButtonText("ui.button.dialog.close")
//                .show(Dialog.INFORMATION_MESSAGE);
//        MessageDialog.create()
//                .setLocalizedTitle("ui.title.test")
//                .setLocalizedInformation("ui.info.test")
//                .setLocalizedButtonText("ui.button.dialog.close")
//                .show(Dialog.WARNING_MESSAGE);
        var res = FileDialog.create()
                            .setPath(".")
                            .setAllowedFileExtensions(Set.of("java", "gradle"))
                            .show();
        Log.debugf("Result: %s", res);
//        SwingUtilities.invokeLater(() -> {
//            var frame = new JFrame();
//            frame.getContentPane().setBackground(Utils.BACKGROUND_COLOR);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setVisible(true);
//            frame.add(new Button("ui.button.save"), BorderLayout.PAGE_START);
//            frame.add(new MathTextField(12), BorderLayout.CENTER);
//            frame.add(new Label("ui.info.test", true), BorderLayout.PAGE_END);
//            frame.pack();
//        });
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
