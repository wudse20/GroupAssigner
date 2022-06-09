package se.skorup.main.gui.frames;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.events.AddEvent;
import se.skorup.main.gui.interfaces.AddListener;
import se.skorup.main.gui.models.NameListModel;
import se.skorup.main.gui.panels.InputPanel;
import se.skorup.main.manager.GroupManager;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The abstract window for adding and editing a group.
 * */
public abstract sealed class AbstractGroupFrame extends JFrame implements KeyListener, ListSelectionListener
       permits EditGroupFrame, AddGroupFrame
{

    protected final JButton btnImport = new JButton("Importera från google forms");
    protected final JButton btnApply = new JButton("Lägg till");

    private final JButton btnRemove = new JButton("Ta bort");

    protected final JList<String> names = new JList<>();
    protected final InputPanel pName = new InputPanel("Grupp: ", 24);
    protected final NameListModel nameModel = new NameListModel(new ArrayList<>());
    protected final List<String> removed = new ArrayList<>();

    private GroupManager result;
    private final List<AddListener> addListeners = new ArrayList<>();
    private final Container cp = this.getContentPane();

    private final JPanel pContainer = new JPanel();
    private final JPanel pInputContainer = new JPanel();
    private final JPanel pListContainer = new JPanel();
    private final JPanel pButtons = new JPanel();

    private final InputPanel pInputGroupMember = new InputPanel("Namn: ", 24);
    private final BorderLayout layout = new BorderLayout();
    private final JLabel lblList = new JLabel("  Medlämmar:");
    private final JButton btnCancel = new JButton("Avbryt");
    private final JScrollPane scrList = new JScrollPane(names);

    /**
     * Creates a new AbstractGroupFrame.
     *
     * @param title the title of the frame.
     * @param shouldAddImport If {@code true} the import button will be added,
     *                        else it won't.
     * */
    protected AbstractGroupFrame(String title, boolean shouldAddImport)
    {
        super(title);
        this.setProperties();
        this.addComponents(shouldAddImport);
    }

    /**
     * Creates a new AbstractGroupFrame with a title and
     * a GroupManager.
     *
     * @param title the title of the frame.
     * @param gm the GroupManager that it should be initialized with.
     * @param shouldAddImport If {@code true} the import button will be added,
     *                        else it won't.
     * */
    protected AbstractGroupFrame(String title, GroupManager gm, boolean shouldAddImport)
    {
        this(title, shouldAddImport);
        this.result = gm;
    }

    /**
     * Creates the group from the inputted data.
     * */
    private void createGroup()
    {
        // Check if the name of the groups meet
        // the standard.
        if (pName.getText().trim().length() < 5)
        {
            pName.setTextFieldBackground(Color.RED);
            JOptionPane.showMessageDialog(
                this, "För kort namn! Måste var minst fem bokstäver långt.",
                "För kort namn!", JOptionPane.ERROR_MESSAGE
            );

            return;
        }
        else if (nameModel.getItems().size() == 0)
        {
            JOptionPane.showMessageDialog(
                this, "Du måste lägga till minst en person i gruppen.",
                "För kort namn!", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        this.result = this.groupAction(result);
        this.invokeAddListeners();
    }

    /**
     * Adds a name to the list.
     * */
    private void addName()
    {
        var input = pInputGroupMember.getText();

        // If the input is accepted.
        if (input.trim().length() >= 2)
        {
            if (input.indexOf(',') != -1)
            {
                var inputs =
                        Arrays.stream(input.split(","))
                              .map(String::trim)
                              .toArray(String[]::new);

                for (var s : inputs)
                {
                    if (s.length() >= 2)
                    {
                        nameModel.addItem(s);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(
                            this, "Namnet %s är för kort, minst två bokstäver.".formatted(s),
                            "För kort namn!", JOptionPane.ERROR_MESSAGE
                        );
                    }
                }

                pInputGroupMember.clear();
                return;
            }

            nameModel.addItem(input);
            pInputGroupMember.clear();
        }
        else
        {
            pInputGroupMember.setTextFieldBackground(Color.RED.darker());
            JOptionPane.showMessageDialog(
                this, "För kort namn! Måste var minst två bokstäver långt.",
                "För kort namn!", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Invokes the add listeners.
     * */
    protected void invokeAddListeners()
    {
        var ae = new AddEvent(this, result);

        for (var al : addListeners)
            al.groupCreated(ae);
    }

    /**
     * Adds the components.
     *
     * @param shouldAddImport If {@code true} the import button will be added,
     *                        else it won't.
     * */
    protected void addComponents(boolean shouldAddImport)
    {
        pInputContainer.add(pName);
        pInputContainer.add(pInputGroupMember);
        pInputContainer.add(new JLabel(" "));

        if (shouldAddImport)
            pButtons.add(btnImport);

        pButtons.add(btnRemove);
        pButtons.add(btnCancel);
        pButtons.add(btnApply);

        pListContainer.add(lblList, BorderLayout.PAGE_START);
        pListContainer.add(new JLabel("   "), BorderLayout.LINE_START);
        pListContainer.add(scrList, BorderLayout.CENTER);
        pListContainer.add(new JLabel("   "), BorderLayout.LINE_END);

        pContainer.add(pInputContainer, BorderLayout.PAGE_START);
        pContainer.add(pListContainer, BorderLayout.CENTER);
        pContainer.add(new JLabel(" "), BorderLayout.PAGE_END);

        cp.add(new JLabel(" "), BorderLayout.PAGE_START);
        cp.add(new JLabel("   "), BorderLayout.LINE_START);
        cp.add(pContainer, BorderLayout.CENTER);
        cp.add(new JLabel("   "), BorderLayout.LINE_END);
        cp.add(pButtons, BorderLayout.PAGE_END);
    }

    /**
     * Sets the properties fo the frame and its
     * components.
     * */
    protected void setProperties()
    {
        this.setVisible(true);
        this.setSize(new Dimension(675, 600));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(layout);

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);

        pInputContainer.setBackground(Utils.BACKGROUND_COLOR);
        pInputContainer.setLayout(new BoxLayout(pInputContainer, BoxLayout.Y_AXIS));

        names.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        names.setForeground(Utils.FOREGROUND_COLOR);
        names.setModel(nameModel);
        names.addListSelectionListener(this);

        pContainer.setBackground(Utils.BACKGROUND_COLOR);
        pContainer.setLayout(new BorderLayout());

        pButtons.setBackground(Utils.BACKGROUND_COLOR);
        pButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));

        btnApply.setForeground(Utils.FOREGROUND_COLOR);
        btnApply.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnApply.addActionListener((e) -> createGroup());

        btnCancel.setForeground(Utils.FOREGROUND_COLOR);
        btnCancel.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCancel.addActionListener((e) -> this.dispose());

        pListContainer.setLayout(new BorderLayout());
        pListContainer.setBackground(Utils.BACKGROUND_COLOR);

        lblList.setForeground(Utils.FOREGROUND_COLOR);
        lblList.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));

        pInputGroupMember.addKeyListener(this);
        pInputGroupMember.addActionCallback(
            () -> pInputGroupMember.setTextFieldBackground(Utils.COMPONENT_BACKGROUND_COLOR)
        );

        pName.addActionCallback(
            () -> pName.setTextFieldBackground(Utils.COMPONENT_BACKGROUND_COLOR)
        );

        btnRemove.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnRemove.setForeground(Utils.FOREGROUND_COLOR);
        btnRemove.setEnabled(false);
        btnRemove.addActionListener(e -> removeAction());

        scrList.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
    }

    /**
     * Sets the result GroupManager. If {@code null},
     * then it will just return.
     *
     * @param gm the group manager to be the result.
     * */
    protected void setResult(GroupManager gm)
    {
        if (gm == null)
            return;

        this.result = gm;
    }

    /**
     * The default behaviour for: btnRemove.
     * */
    protected void removeAction()
    {
        nameModel.removeItem(names.getSelectedValue());
        btnRemove.setEnabled(names.getSelectedIndex() - 1 != -1);
    }

    /**
     * The action used when interacting with the GroupManager.
     *
     * @param gm the current GroupManager.
     * */
    protected abstract GroupManager groupAction(GroupManager gm);

    /**
     * Adds an AddListener to the frame.
     *
     * @param l the AddListener to be added,
     *          if {@code null} then it will
     *          return without doing anything.
     * */
    public void addAddListener(AddListener l)
    {
        if (l == null)
            return;

        addListeners.add(l);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e)
    {
        // If enter key was released
        if (e.getKeyCode() == 10)
            addName();
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        // If selected item enable remove button.
        btnRemove.setEnabled(names.getSelectedIndex() != -1);
    }
}
