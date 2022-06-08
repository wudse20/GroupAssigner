package se.skorup.main.gui.frames;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.gui.panels.SubgroupDisplayPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.manager.helper.SerializationManager;
import se.skorup.main.objects.Subgroups;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The SubGroupListFrame used in seeing
 * the saved SubGroups.
 * */
public class SubgroupListFrame extends JFrame implements ActionListener, ComponentListener
{
    private List<File> files;

    private final String path;

    private final GroupManager gm;

    private final List<ActionCallback> callbacks = new Vector<>();

    private final Container cp = this.getContentPane();

    private final JComboBox<String> cbSaves = new JComboBox<>();

    private final JButton btnLoad = new JButton("Ladda");

    private final JLabel lblInfo = new JLabel("Välj den önskade gruppen: ");

    private final JPanel pContainer = new JPanel();
    private final JPanel pPreview = new JPanel();
    private final JPanel pButtons = new JPanel();

    private final SubgroupDisplayPanel sgdp;
    private final JScrollPane scrSGDP;

    /**
     * Creates a new SubGroupListFrame.
     *
     * @param path the path of the frame.
     * */
    public SubgroupListFrame(String path, GroupManager gm)
    {
        super("Undergrupper!");
        this.path = path;
        this.gm = gm;
        this.scrSGDP = new JScrollPane(pPreview);
        this.sgdp = new SubgroupDisplayPanel(scrSGDP, true);

        this.setProperties();
        this.addComponents();
    }

    /**
     * Refreshes the list data.
     * */
    private void refreshList()
    {
        cbSaves.removeAllItems();
        var dir = new File(path).list();

        if (dir == null || dir.length == 0)
        {
            this.setVisible(false);

            JOptionPane.showMessageDialog(
                this, "Det finns inga sparade grupper.",
                "Inga sparade grupper", JOptionPane.ERROR_MESSAGE
            );

            DebugMethods.log("No saved groups!", DebugMethods.LogType.ERROR);

            this.dispose();
            return;
        }

        var files =
            Arrays.stream(Objects.requireNonNull(dir))
                  .map(x -> "%s%s".formatted(path, x))
                  .map(File::new) // Creates files.
                  .collect(Collectors.toCollection(Vector::new)); // Converts to collection, java.util.Vector.

        this.files = files;

        files.stream()
             .map(File::getName) // Gets the names of the file.
             .map(x -> x.substring(0, x.indexOf('.'))) // Removes the file extension.
             .forEach(cbSaves::addItem); // Adds the items.
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        pPreview.add(sgdp, BorderLayout.CENTER);

        pButtons.add(btnLoad);

        pContainer.add(lblInfo);
        pContainer.add(cbSaves);

        cp.add(pContainer, BorderLayout.PAGE_START);
        cp.add(new JLabel("   "), BorderLayout.LINE_START);
        cp.add(scrSGDP, BorderLayout.CENTER);
        cp.add(new JLabel("   "), BorderLayout.LINE_END);
        cp.add(pButtons, BorderLayout.PAGE_END);
    }

    /**
     * Sets all the properties.
     * */
    private void setProperties()
    {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(1200, 900));
        this.setVisible(true);
        this.addComponentListener(this);
        this.refreshList();

        previewCurrentSubgroup("Loading default item!");

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setForeground(Utils.FOREGROUND_COLOR);
        cp.setLayout(new BorderLayout());

        pContainer.setBackground(Utils.BACKGROUND_COLOR);
        pContainer.setForeground(Utils.FOREGROUND_COLOR);
        pContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnLoad.setForeground(Utils.FOREGROUND_COLOR);
        btnLoad.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnLoad.addActionListener(this);

        cbSaves.setForeground(Utils.FOREGROUND_COLOR);
        cbSaves.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbSaves.addActionListener(e -> previewCurrentSubgroup("Selected an item!"));

        var b1 = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR), "Förhandsgranskning"
        );
        b1.setTitleColor(Utils.FOREGROUND_COLOR);
        b1.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        scrSGDP.getVerticalScrollBar().setUnitIncrement(16);
        scrSGDP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrSGDP.setBorder(b1);
        scrSGDP.setBackground(Utils.BACKGROUND_COLOR);

        pPreview.setLayout(new BorderLayout());

        pButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        pButtons.setBackground(Utils.BACKGROUND_COLOR);

        lblInfo.setForeground(Utils.FOREGROUND_COLOR);
    }

    /**
     * Loads the currently selected subgroup into the preview window.
     *
     * @param message The log message that will be logged.
     * */
    private void previewCurrentSubgroup(String message)
    {
        DebugMethods.log(message, DebugMethods.LogType.DEBUG);
        try
        {
            sgdp.displaySubgroup(
                (Subgroups) SerializationManager.deserializeObject(getSelectedFile().getAbsolutePath()), gm
            );
        }
        catch (IOException | ClassNotFoundException ex)
        {
            DebugMethods.log(ex, DebugMethods.LogType.ERROR);
            JOptionPane.showMessageDialog(
                pPreview, "Kunde inte ladda gruppen: %s".formatted(ex),
                "Fel vid läsning :(", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Adds an action callback
     *
     * @param a the action callback to be added.
     *          If {@code null} then it will return
     *          without doing anything.
     * */
    public void addActionCallback(ActionCallback a)
    {
        if (a == null)
            return;

        callbacks.add(a);
    }

    /**
     * Gets the currently selected file.
     *
     * @return the currently selected file.
     * */
    public File getSelectedFile()
    {
        var index = cbSaves.getSelectedIndex();
        DebugMethods.log(files.get(index).getAbsolutePath(), DebugMethods.LogType.DEBUG);
        return files.get(index);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.callbacks.forEach(ActionCallback::callback);
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        previewCurrentSubgroup("Showing component due to resize.");
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
        previewCurrentSubgroup("Showing component due to showing window.");
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}
