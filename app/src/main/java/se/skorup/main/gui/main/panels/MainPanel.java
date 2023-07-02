package se.skorup.main.gui.main.panels;

import se.skorup.gui.components.Button;
import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.main.gui.about.frames.AboutFrame;
import se.skorup.main.gui.calculator.frames.CalculatorFrame;
import se.skorup.main.gui.group.frames.GroupFrame;
import se.skorup.main.gui.main.frames.MainFrame;

import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * The panel responsible for the main menu.
 * */
public class MainPanel extends Panel
{
    private static final Font FONT = new Font(Font.DIALOG, Font.BOLD, 24);

    private final MainFrame mf;

    private final Label lblTitle = new Label("ui.title.main", true);

    private final Button btnMyGroups = new Button("ui.button.my-groups");
    private final Button btnCalculator = new Button("ui.button.calculator");
    private final Button btnAbout = new Button("ui.button.about");
    private final Button btnQuit = new Button("ui.button.quit");

    /**
     * Creates a new MainPanel.
     *
     * @param mf the instance of the MainFrame in use.
     * */
    public MainPanel(MainFrame mf)
    {
        super(new GridLayout(5, 1));

        this.mf = mf;
        this.setupPanel();
        this.addListeners();
    }

    /**
     * Adds the listeners to the frame.
     * */
    private void addListeners()
    {
        btnMyGroups.addActionListener(e -> {
            mf.setVisible(false);
            SwingUtilities.invokeLater(() -> new GroupFrame(mf));
        });
        btnCalculator.addActionListener(e -> SwingUtilities.invokeLater(CalculatorFrame::new));
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

        this.add(new ComponentContainer(titleCont));
        this.add(new ComponentContainer(btnMyGroups));
        this.add(new ComponentContainer(btnCalculator));
        this.add(new ComponentContainer(btnAbout));
        this.add(new ComponentContainer(btnQuit));
    }
}
