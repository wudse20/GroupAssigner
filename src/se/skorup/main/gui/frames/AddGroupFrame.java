package se.skorup.main.gui.frames;

import se.skorup.API.Utils;
import se.skorup.main.gui.models.NameListModel;
import se.skorup.main.gui.panels.ButtonPanel;
import se.skorup.main.gui.panels.InputPanel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashSet;

/**
 * The frame used to add groups.
 * */
public class AddGroupFrame extends JFrame
{
    /** The container of the frame. */
    private final Container cp = this.getContentPane();

    /** The container panel. */
    private final JPanel pContainer = new JPanel();

    /** The container panel for the inputs.*/
    private final JPanel pInputContainer = new JPanel();

    /** The container for the JList. */
    private final JPanel pListContainer = new JPanel();

    /** The input panel for the group name. */
    private final InputPanel pName = new InputPanel("Grupp: ", 36);

    /** The input panel for the group members. */
    private final InputPanel pInputGroupMember = new InputPanel("Namn: ", 36);

    /** The button panel. */
    private final JPanel pButtons = new JPanel();

    /** The layout of the button panel. */
    private final FlowLayout pButtonsLayout = new FlowLayout(FlowLayout.RIGHT);

    /** The JList for the persons. */
    private final JList<String> names = new JList<>();

    /** The layout for the input container. */
    private final BoxLayout pInputContainerLayout =
        new BoxLayout(pInputContainer, BoxLayout.Y_AXIS);

    /** The layout of the frame. */
    /** The layout of the frame. */
    private final BorderLayout layout = new BorderLayout();

    /** The layout of the frame. */
    private final BorderLayout pContainerLayout = new BorderLayout();

    /** The layout of the frame. */
    private final BorderLayout pListContainerLayout = new BorderLayout();

    /** The model of the list. */
    private final NameListModel nameModel = new NameListModel(new HashSet<>());

    /** The label for the list. */
    private final JLabel lblList = new JLabel("  Medlämmar:");

    /** Spacer */
    private final JLabel lblSpacer1 = new JLabel(" ");

    /** Spacer */
    private final JLabel lblSpacer2 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer3 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer4 = new JLabel(" ");

    /** Spacer */
    private final JLabel lblSpacer5 = new JLabel(" ");

    /** Spacer */
    private final JLabel lblSpacer6 = new JLabel("   ");

    /** Spacer */
    private final JLabel lblSpacer7 = new JLabel("   ");

    /** The button for applying. */
    private final JButton btnApply = new JButton("Lägg till");

    /** The button for canceling. */
    private final JButton btnCancel = new JButton("Avbryt");

    /** The button for importing from google forms. */
    private final JButton btnImport = new JButton("Importera från google forms");

    /**
     * Creates and shows a new AddGroupFrame.
     * */
    public AddGroupFrame()
    {
        super("Skapa en grupp!");

        this.setProperties();
        this.addComponents();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        pInputContainer.add(pName);
        pInputContainer.add(pInputGroupMember);
        pInputContainer.add(lblSpacer5);

        pButtons.add(btnImport);
        pButtons.add(btnCancel);
        pButtons.add(btnApply);

        pListContainer.add(lblList, BorderLayout.PAGE_START);
        pListContainer.add(lblSpacer6, BorderLayout.LINE_START);
        pListContainer.add(names, BorderLayout.CENTER);
        pListContainer.add(lblSpacer7, BorderLayout.LINE_END);

        pContainer.add(pInputContainer, BorderLayout.PAGE_START);
        pContainer.add(pListContainer, BorderLayout.CENTER);
        pContainer.add(lblSpacer4, BorderLayout.PAGE_END);

        cp.add(lblSpacer1, BorderLayout.PAGE_START);
        cp.add(lblSpacer2, BorderLayout.LINE_START);
        cp.add(pContainer, BorderLayout.CENTER);
        cp.add(lblSpacer3, BorderLayout.LINE_END);
        cp.add(pButtons, BorderLayout.PAGE_END);
    }

    /**
     * Sets the properties fo the frame and its
     * components.
     * */
    private void setProperties()
    {
        this.setVisible(true);
        this.setSize(new Dimension(675, 600));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(layout);

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);

        pInputContainer.setBackground(Utils.BACKGROUND_COLOR);
        pInputContainer.setLayout(pInputContainerLayout);

        names.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        names.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));

        pContainer.setBackground(Utils.BACKGROUND_COLOR);
        pContainer.setLayout(pContainerLayout);

        pButtons.setBackground(Utils.BACKGROUND_COLOR);
        pButtons.setLayout(pButtonsLayout);

        btnApply.setForeground(Utils.FOREGROUND_COLOR);
        btnApply.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnCancel.setForeground(Utils.FOREGROUND_COLOR);
        btnCancel.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnImport.setForeground(Utils.FOREGROUND_COLOR);
        btnImport.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        pListContainer.setLayout(pListContainerLayout);
        pListContainer.setBackground(Utils.BACKGROUND_COLOR);

        lblList.setForeground(Utils.FOREGROUND_COLOR);
        lblList.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
    }
}
