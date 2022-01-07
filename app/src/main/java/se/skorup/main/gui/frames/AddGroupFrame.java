package se.skorup.main.gui.frames;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.collections.immutable_collections.ImmutableArray;
import se.skorup.API.util.Utils;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The frame used to add groups.
 * */
public class AddGroupFrame extends JFrame implements KeyListener, ListSelectionListener
{
    private GroupManager result;

    private final List<AddListener> addListeners = new ArrayList<>();

    private final Set<String> removed = new HashSet<>();

    private final Container cp = this.getContentPane();

    private final JPanel pContainer = new JPanel();
    private final JPanel pInputContainer = new JPanel();
    private final JPanel pListContainer = new JPanel();
    private final JPanel pButtons = new JPanel();

    private final InputPanel pName = new InputPanel("Grupp: ", 24);
    private final InputPanel pInputGroupMember = new InputPanel("Namn: ", 24);

    private final JList<String> names = new JList<>();

    /** The layout of the frame. */
    private final BorderLayout layout = new BorderLayout();

    /** The model of the list. */
    private final NameListModel nameModel = new NameListModel(new HashSet<>());

    /** The label for the list. */
    private final JLabel lblList = new JLabel("  Medlämmar:");


    private final JButton btnApply = new JButton("Lägg till");
    private final JButton btnCancel = new JButton("Avbryt");
    private final JButton btnImport = new JButton("Importera från google forms");
    private final JButton btnRemove = new JButton("Tabort");

    private final JScrollPane scrList = new JScrollPane(names);

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
     * Creates an AddGroupFrame with
     * a group manager.
     *
     * @param gm the group manager.
     * */
    public AddGroupFrame(GroupManager gm)
    {
        super("Ändra en grupp!");
        this.result = gm;

        gm.getAllPersons()
          .stream()
          .map(Person::getName)
          .forEach(nameModel::addItem);

        pName.setText(gm.getName());
        btnApply.setText("Uppdatera gruppen");

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
        pInputContainer.add(new JLabel(" "));

        pButtons.add(btnRemove);

        if (result == null)
            pButtons.add(btnImport);

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
    private void setProperties()
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
        names.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
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

        btnImport.setForeground(Utils.FOREGROUND_COLOR);
        btnImport.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnImport.addActionListener((e) -> importFromDocs());

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
        btnRemove.addActionListener((e) -> {
            if (result != null)
                removed.add(names.getSelectedValue());

            nameModel.removeItem(names.getSelectedValue());
            btnRemove.setEnabled(names.getSelectedIndex() - 1 != -1);
        });

        scrList.setBorder(BorderFactory.createEmptyBorder());
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

        if (result == null)
        {
            result = new GroupManager(pName.getText());

            nameModel.getItems().forEach(x -> result.registerPerson(x, Person.Role.CANDIDATE));
            DebugMethods.log("Created: %s".formatted(result), DebugMethods.LogType.DEBUG);
        }
        else
        {
            result.setName(pName.getText());
            var names = new HashSet<>(result.getNames());

            for (var n : nameModel.getItems())
            {
                if (!names.contains(n))
                    result.registerPerson(n, Person.Role.CANDIDATE);
            }

            for (var n : removed)
                for (var p : result.getPersonFromName(n))
                    result.removePerson(p.getId());
        }

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
     * @param files the files to be read.
     * @return the contents of the files
     *         in an array.
     * */
    private ImmutableArray<String> readFiles(File[] files)
    {
        var res = new ArrayList<String>();

        for (var f : files)
        {
            if (!f.exists())
                continue;

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

                res.add(lines.toString());
            }
            catch (IOException e)
            {
                DebugMethods.log(e, DebugMethods.LogType.ERROR);
                JOptionPane.showMessageDialog(
                    this,
                    "Kunde inte läsa fil: %s\nFelmeddelande%s".formatted(f.getName(), e.getLocalizedMessage()),
                    "Kunde inte läsa fil", JOptionPane.ERROR_MESSAGE
                );
            }
        }

        return ImmutableArray.fromList(res);
    }

    /**
     * Imports data and creates a group manager from the docs.
     * */
    private void importFromDocs()
    {
        var fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(true);
        var selection = fc.showDialog(this, "Välj");

        if (selection == JFileChooser.APPROVE_OPTION)
        {
            var sb = new StringBuilder();
            for (var f : fc.getSelectedFiles())
                sb.append(f.getName()).append(" + ");

            result = new GroupManager(sb.substring(0, sb.length() - 3));
            for (var str : readFiles(fc.getSelectedFiles()))
            {
                var data = str.trim();
                data = ImmutableArray.fromArray(data.split("")).dropMatching("\"").mkString("");

                if (data.equals(""))
                    return;


                var lines = data.split("\n");
                var processedData =
                    ImmutableArray.fromArray(lines)
                                  .drop(1) // Drops the header.
                                  .map(x -> x.split(",")) // Splits at ','.
                                  .map(ImmutableArray::fromArray) // Converts to immutable arrays.
                                  .map(x -> x.drop(1)) // drops the time info.
                                  .map(x -> x.map(String::trim))
                                  .map(x -> x.map(Utils::toNameCase)); // Trims the strings to shape.

                DebugMethods.log(processedData.toString(), DebugMethods.LogType.DEBUG);

                for (int i = 0; i < processedData.size(); i++)
                {
                    var curArr = processedData.get(i);
                    Person p = null;
                    for (int ii = 0; ii < curArr.size(); ii++)
                    {
                        var s = curArr.get(ii);
                        var names = result.getNames();

                        var person = !names.contains(s) ?
                            result.registerPerson(s, Person.Role.CANDIDATE) :
                            result.getPersonFromName(s).get(0);

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

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        // If selected item enable remove button.
        btnRemove.setEnabled(names.getSelectedIndex() != -1);
    }
}
