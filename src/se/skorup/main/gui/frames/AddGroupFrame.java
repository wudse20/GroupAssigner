package se.skorup.main.gui.frames;

import se.skorup.API.DebugMethods;
import se.skorup.API.ImmutableArray;
import se.skorup.API.Utils;
import se.skorup.main.gui.events.AddEvent;
import se.skorup.main.gui.interfaces.AddListener;
import se.skorup.main.gui.models.NameListModel;
import se.skorup.main.gui.panels.InputPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * The frame used to add groups.
 * */
public class AddGroupFrame extends JFrame implements KeyListener
{
    /** The resulting group. */
    private GroupManager result;

    /** The add listeners of the frame. */
    private final List<AddListener> addListeners = new ArrayList<>();

    /** The container of the frame. */
    private final Container cp = this.getContentPane();

    /** The container panel. */
    private final JPanel pContainer = new JPanel();

    /** The container panel for the inputs.*/
    private final JPanel pInputContainer = new JPanel();

    /** The container for the JList. */
    private final JPanel pListContainer = new JPanel();

    /** The input panel for the group name. */
    private final InputPanel pName = new InputPanel("Grupp: ", 24);

    /** The input panel for the group members. */
    private final InputPanel pInputGroupMember = new InputPanel("Namn: ", 24);

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
        names.setForeground(Utils.FOREGROUND_COLOR);
        names.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        names.setModel(nameModel);

        pContainer.setBackground(Utils.BACKGROUND_COLOR);
        pContainer.setLayout(pContainerLayout);

        pButtons.setBackground(Utils.BACKGROUND_COLOR);
        pButtons.setLayout(pButtonsLayout);

        btnApply.setForeground(Utils.FOREGROUND_COLOR);
        btnApply.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnApply.addActionListener((e) -> createGroup());

        btnCancel.setForeground(Utils.FOREGROUND_COLOR);
        btnCancel.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCancel.addActionListener((e) -> this.dispose());

        btnImport.setForeground(Utils.FOREGROUND_COLOR);
        btnImport.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnImport.addActionListener((e) -> importFromDocs());

        pListContainer.setLayout(pListContainerLayout);
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

        result = new GroupManager(pName.getText());
        nameModel.getItems().forEach(x -> result.registerPerson(x, Person.Role.CANDIDATE));
        DebugMethods.log("Created: %s".formatted(result), DebugMethods.LogType.DEBUG);
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
    private void invokeAddListeners()
    {
        var ae = new AddEvent(this, result);

        for (var al : addListeners)
            al.groupCreated(ae);
    }

    /**
     * Reads a file and return its content.
     *
     * @param f the file to be read.
     * @return the content of the file,
     *         if it cannot be read then
     *         it will return an empty
     *         String.
     * */
    private String readFile(File f)
    {
        if (!f.exists())
            return "";

        try
        {
            var fr = new FileReader(f, StandardCharsets.UTF_8);
            var br = new BufferedReader(fr);

            var lines = new StringBuilder();

            String tmp;

            while ((tmp = br.readLine()) != null)
                lines.append(tmp).append("\n");

            fr.close();
            br.close();

            return lines.toString();
        }
        catch (IOException e)
        {
            return "";
        }
    }

    /**
     * Imports data and creates a group manager from the docs.
     * */
    private void importFromDocs()
    {
        var fc = new JFileChooser(".");
        var selection = fc.showDialog(this, "Välj");

        if (selection == JFileChooser.APPROVE_OPTION)
        {
            var data =
                readFile(fc.getSelectedFile()).trim().toUpperCase();

            data = ImmutableArray.fromArray(data.split("")).dropMatching("\"").mkString("");

            if (data.equals(""))
            {
                JOptionPane.showMessageDialog(
                this, "The content of the file %s cannot be read.".formatted(fc.getSelectedFile()),
                "Error", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            var lines = data.split("\n");
            var processedData =
                ImmutableArray.fromArray(lines)
                              .drop(1) // Drops the header.
                              .map(x -> x.split(",")) // Splits at ','.
                              .map(ImmutableArray::fromArray) // Converts to immutable arrays.
                              .map(x -> x.drop(1)) // drops the time info.
                              .map(x -> x.map(String::trim)); // Trims the strings to shape.

            DebugMethods.log(processedData.toString(), DebugMethods.LogType.DEBUG);

            result = new GroupManager(fc.getSelectedFile().getName());
            for (int i = 0; i < processedData.size(); i++)
            {
                var curArr = processedData.get(i);
                Person p = null;

                for (int ii = 0; ii < curArr.size(); ii++)
                {
                    var s = curArr.get(ii);
                    var names = result.getNames();

                    if (!names.contains(s))
                    {
                        var person = result.registerPerson(s, Person.Role.CANDIDATE);

                        if (ii == 0)
                        {
                            p = person;
                            continue;
                        }

                        assert p != null;
                        p.addWishlistId(person.getId());
                    }
                    else
                    {
                        var person = result.getPersonFromName(s).get(0);

                        if (ii == 0)
                        {
                            p = person;
                            continue;
                        }

                        assert p != null;
                        p.addWishlistId(person.getId());
                    }
                }
            }

            invokeAddListeners();
        }
    }

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
}
