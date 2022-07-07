package se.skorup.main.gui.frames;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.interfaces.ActionCallbackWithParam;
import se.skorup.main.gui.panels.SubgroupDisplayPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Subgroups;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import java.util.List;
import java.util.Vector;

/**
 * The SubGroupListFrame used in seeing
 * the saved SubGroups.
 * */
public class SubgroupListFrame extends JFrame implements ActionListener, ComponentListener
{
    private final List<Subgroups> groups;

    private final GroupManager gm;

    private final List<ActionCallbackWithParam<Subgroups>> callbacks = new Vector<>();

    private final Container cp = this.getContentPane();

    private final JComboBox<CBEntry> cbGroups = new JComboBox<>();

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
     * @param groups the groups to be displayed.
     * @param gm the GroupManager used to create
     *           the subgroups.
     * @param buttonLabel The label of the button.
     * */
    public SubgroupListFrame(List<Subgroups> groups, GroupManager gm, String buttonLabel)
    {
        super("Undergrupper!");
        this.groups = groups;
        this.gm = gm;
        this.scrSGDP = new JScrollPane(pPreview);
        this.sgdp = new SubgroupDisplayPanel(scrSGDP, true);
        this.btnLoad.setText(buttonLabel);

        this.setProperties();
        this.addComponents();
    }

    private void refreshList()
    {
        for (var g : groups)
            cbGroups.addItem(new CBEntry(g));
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        pPreview.add(sgdp, BorderLayout.CENTER);

        pButtons.add(btnLoad);

        pContainer.add(lblInfo);
        pContainer.add(cbGroups);

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

        cbGroups.setForeground(Utils.FOREGROUND_COLOR);
        cbGroups.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbGroups.addActionListener(e -> previewCurrentSubgroup("Selected an item!"));

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
        sgdp.displaySubgroup(cbGroups.getItemAt(cbGroups.getSelectedIndex()).sg, gm);
    }

    /**
     * Adds an action callback
     *
     * @param a the action callback to be added.
     *          If {@code null} then it will return
     *          without doing anything.
     * */
    public void addActionCallback(ActionCallbackWithParam<Subgroups> a)
    {
        if (a == null)
            return;

        callbacks.add(a);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.callbacks.forEach(cb -> cb.action(cbGroups.getItemAt(cbGroups.getSelectedIndex()).sg));
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

    /**
     * Wrapper class to change the toString for
     * displaying in the GUI.
     * */
    private record CBEntry(Subgroups sg)
    {
        @Override
        public String toString()
        {
            return sg.name();
        }
    }
}
