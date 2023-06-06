package se.skorup.main.gui.main.panels;

import se.skorup.gui.components.Button;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.dialog.Dialog;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.main.gui.about.frames.AboutFrame;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * The panel responsible for the main menu.
 * */
public class MainPanel extends Panel
{
    private static final Font FONT = new Font(Font.DIALOG, Font.BOLD, 24);

    private final Label lblTitle = new Label("ui.title.main", true);

    private final Button btnMyGroups = new Button("ui.button.my-groups");
    private final Button btnCalculator = new Button("ui.button.calculator");
    private final Button btnAbout = new Button("ui.button.about");
    private final Button btnQuit = new Button("ui.button.quit");

    /**
     * Creates a new MainPanel.
     * */
    public MainPanel()
    {
        super(new GridLayout(5, 1));

        this.setupPanel();
        this.addListeners();
    }

    /**
     * Adds the listeners to the frame.
     * */
    private void addListeners()
    {
        btnMyGroups.addActionListener(e -> {
            new Thread(() -> { // To not halt the EDT, since listeners are run on the EDT.
                MessageDialog.create()
                             .setLocalizedTitle("ui.title.NYI")
                             .setLocalizedInformation("ui.title.NYI")
                             .setLocalizedButtonText("ui.button.dialog.close")
                             .show(Dialog.ERROR_MESSAGE);
            }).start();
        });
        btnCalculator.addActionListener(e -> {
            new Thread(() -> { // To not halt the EDT, since listeners are run on the EDT.
                MessageDialog.create()
                             .setLocalizedTitle("ui.title.NYI")
                             .setLocalizedInformation("ui.title.NYI")
                             .setLocalizedButtonText("ui.button.dialog.close")
                             .show(Dialog.ERROR_MESSAGE);
            }).start();
        });
        btnAbout.addActionListener(e -> SwingUtilities.invokeLater(AboutFrame::new));
        btnQuit.addActionListener(e -> System.exit(0));
    }

    /**
     * Setup for the panel.
     * */
    private void setupPanel()
    {
        lblTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 32));
        btnMyGroups.setFont(FONT);
        btnCalculator.setFont(FONT);
        btnAbout.setFont(FONT);
        btnQuit.setFont(FONT);

        var titleCont = new Panel(new FlowLayout(FlowLayout.CENTER));
        titleCont.add(lblTitle);

        this.add(new Container(titleCont));
        this.add(new Container(btnMyGroups));
        this.add(new Container(btnCalculator));
        this.add(new Container(btnAbout));
        this.add(new Container(btnQuit));
    }

    /** A container for the button. */
    private static final class Container extends Panel
    {
        /**
         * Creates a new panel.
         *
         * @param btn The layout of the panel.
         * */
        private Container(Component btn)
        {
            super(new BorderLayout());

            this.add(new Label(" "), BorderLayout.PAGE_START);
            this.add(new Label("     "), BorderLayout.LINE_START);
            this.add(btn, BorderLayout.CENTER);
            this.add(new Label("     "), BorderLayout.LINE_END);
            this.add(new Label(" "), BorderLayout.PAGE_END);
        }
    }
}
