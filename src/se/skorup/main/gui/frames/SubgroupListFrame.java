package se.skorup.main.gui.frames;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.interfaces.ActionCallback;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The SubGroupListFrame used in seeing
 * the saved SubGroups.
 * */
public class SubgroupListFrame extends JFrame implements ActionListener
{
    /** The path of this frame. */
    private final String path;

    /** The list of ActionCallbacks. */
    private final List<ActionCallback> callbacks = new Vector<>();

    /** The found files. */
    private List<File> files;

    /** The content pane of the frame. */
    private final Container cp = this.getContentPane();

    /** The ComboBox for displaying the saves. */
    private final JComboBox<String> cbSaves = new JComboBox<>();

    /** The button used to load a save. */
    private final JButton btnLoad = new JButton("Ladda");

    /** A spacer. */
    private final JLabel lblSpacer1 = new JLabel(" ");

    /** A spacer. */
    private final JLabel lblSpacer2 = new JLabel("   ");

    /** A spacer. */
    private final JLabel lblSpacer3 = new JLabel("   ");

    /** A spacer. */
    private final JLabel lblSpacer4 = new JLabel(" ");

    /** The container panel. */
    private final JPanel pContainer = new JPanel();

    /** The layout of the frame. */
    private final BorderLayout layout = new BorderLayout();

    /** The layout for the container. */
    private final FlowLayout pContainerLayout = new FlowLayout(FlowLayout.CENTER);

    /**
     * Creates a new SubGroupListFrame.
     *
     * @param path the path of the frame.
     * */
    public SubgroupListFrame(String path)
    {
        super("Undergrupper!");
        this.path = path;

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
        pContainer.add(cbSaves);
        pContainer.add(btnLoad);

        cp.add(lblSpacer1, BorderLayout.PAGE_START);
        cp.add(lblSpacer2, BorderLayout.LINE_START);
        cp.add(pContainer, BorderLayout.CENTER);
        cp.add(lblSpacer3, BorderLayout.LINE_END);
        cp.add(lblSpacer4, BorderLayout.PAGE_END);
    }

    /**
     * Sets all the properties.
     * */
    private void setProperties()
    {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(400, 110));
        this.setVisible(true);
        this.refreshList();

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setForeground(Utils.FOREGROUND_COLOR);
        cp.setLayout(layout);

        pContainer.setBackground(Utils.BACKGROUND_COLOR);
        pContainer.setForeground(Utils.FOREGROUND_COLOR);
        pContainer.setLayout(pContainerLayout);

        btnLoad.setForeground(Utils.FOREGROUND_COLOR);
        btnLoad.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnLoad.addActionListener(this);

        cbSaves.setForeground(Utils.FOREGROUND_COLOR);
        cbSaves.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
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
}
