package se.skorup.main.gui.frames;

import se.skorup.API.util.CSVParser;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.CSVLabel;
import se.skorup.main.gui.components.enums.State;
import se.skorup.main.gui.interfaces.ActionCallbackWithParam;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static se.skorup.main.gui.components.CSVLabel.PERSON_COLOR;
import static se.skorup.main.gui.components.CSVLabel.SKIP_COLOR;
import static se.skorup.main.gui.components.CSVLabel.UNSELECTED_COLOR;
import static se.skorup.main.gui.components.CSVLabel.WISH_COLOR;

/**
 * The frame that houses the custom CSV parsing
 * of groups.
 * */
public class CSVFrame extends JFrame implements KeyListener
{
    private static final String PERSON_INFO =
        "Välj en person, genom att klicka på den rutan som personer motsvarar!";

    private static final String WISH_INFO =
        "Välj en person - genom att klicka på rätt ruta, att tilldela önskningar till och sedan klicka på önskningarna.";

    private static final String SKIP_INFO =
        "Välj en ruta som kommer att hoppas över. Detta är valfritt och behöver inte göras.";

    private State state = State.PERSON;
    private PersonLabelRecord wishPerson;

    private boolean isCtrlDown = false;

    private final PersonLabelRecord[][] labels;
    private final String[][] data;

    private final List<ActionCallbackWithParam<GroupManager>> callbacks = new ArrayList<>();
    private final Set<Person> persons = new HashSet<>();
    private final Map<Person, Set<PersonLabelRecord>> wishes = new HashMap<>();

    private final GroupManager gm = new GroupManager("");

    private final Container cp = this.getContentPane();

    private final JButton btnCancel = new JButton("Avbryt");
    private final JButton btnAdd = new JButton("Lägg till");

    private final JRadioButton radioPerson = new JRadioButton("   Välj en person           ");
    private final JRadioButton radioWish = new JRadioButton("   Välj en önskning         ");
    private final JRadioButton radioSkip = new JRadioButton("   Välj en att hoppa över   ");

    private final ButtonGroup btnGroup = new ButtonGroup();

    private final JLabel lblInfo = new JLabel(WISH_INFO);

    private final JPanel pCSV = new JPanel();
    private final JPanel pButtons = new JPanel();
    private final JPanel pSelector = new JPanel();
    private final JPanel pInfo = new JPanel();

    private final JScrollPane scrCSV = new JScrollPane(pCSV);

    /**
     * Creates a new CSVFrame.
     * */
    public CSVFrame()
    {
        super("Skapa grupper utifrån CSV");

        this.data = loadFile();
        this.labels = new PersonLabelRecord[data.length][data.length];

        this.setProperties();
        this.addComponents();
        this.setVisible(true);
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        if (Arrays.deepEquals(data, new String[0][0]))
            return;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(1600, 900));
        this.addKeyListener(this);

        cp.setLayout(new BorderLayout());
        cp.setBackground(Utils.BACKGROUND_COLOR);

        pCSV.setBackground(Utils.BACKGROUND_COLOR);
        pCSV.setLayout(new GridLayout(data.length, data[0].length));

        scrCSV.setBorder(BorderFactory.createEmptyBorder());

        for (var i = 0; i < data.length; i++)
        {
            for (var ii = 0; ii < data[i].length; ii++)
            {
                var label = new CSVLabel(data[i][ii], i, ii, Color.WHITE, Color.BLACK);
                label.addEnterEffect(c -> {
                    c.setSavedBackground(c.getBackground());
                    c.setBackground(Utils.SELECTED_COLOR);

                    if (isCtrlDown)
                        clicked(c);

                    this.requestFocus();
                });
                label.addExitEffect(c -> {
                    c.setBackground(c.getSavedBackground());
                    this.requestFocus();
                });
                label.addActionCallback(this::clicked);
                pCSV.add(label);
                labels[i][ii] = new PersonLabelRecord(label, null);
            }
        }

        pButtons.setBackground(Utils.BACKGROUND_COLOR);
        pButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));

        btnAdd.setForeground(Utils.FOREGROUND_COLOR);
        btnAdd.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnAdd.addActionListener(e -> addGroup());

        btnCancel.setForeground(Utils.FOREGROUND_COLOR);
        btnCancel.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCancel.addActionListener(e -> this.dispose());

        pSelector.setBackground(Utils.BACKGROUND_COLOR);
        pSelector.setLayout(new BoxLayout(pSelector, BoxLayout.Y_AXIS));

        var font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
        radioPerson.addActionListener(e -> lblInfo.setText(PERSON_INFO));
        radioPerson.addActionListener(e -> state = State.PERSON);
        radioPerson.setSelected(true);
        radioPerson.setForeground(Color.BLACK);
        radioPerson.setBackground(PERSON_COLOR);
        radioPerson.setFont(font);

        radioWish.addActionListener(e -> lblInfo.setText(WISH_INFO));
        radioWish.addActionListener(e -> state = State.WISH);
        radioWish.setSelected(false);
        radioWish.setForeground(Color.BLACK);
        radioWish.setBackground(WISH_COLOR);
        radioWish.setFont(font);

        radioSkip.addActionListener(e -> lblInfo.setText(SKIP_INFO));
        radioSkip.addActionListener(e -> state = State.SKIP);
        radioSkip.setSelected(false);
        radioSkip.setForeground(Color.BLACK);
        radioSkip.setBackground(SKIP_COLOR);
        radioSkip.setFont(font);

        btnGroup.add(radioPerson);
        btnGroup.add(radioWish);
        btnGroup.add(radioSkip);

        lblInfo.setForeground(Utils.FOREGROUND_COLOR);
        lblInfo.setFont(new Font(Font.DIALOG, Font.BOLD, 16));

        pInfo.setBackground(Utils.BACKGROUND_COLOR);
        pInfo.setLayout(new BoxLayout(pInfo, BoxLayout.Y_AXIS));
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        var cont = new JPanel();
        cont.setBackground(Utils.BACKGROUND_COLOR);
        cont.setLayout(new FlowLayout(FlowLayout.LEFT));
        cont.add(new JLabel("   "));
        cont.add(pInfo);

        var cont2 = new JPanel();
        cont2.setBackground(Utils.BACKGROUND_COLOR);
        cont2.setLayout(new FlowLayout(FlowLayout.LEFT));
        cont2.add(new JLabel("   "));
        cont2.add(pSelector);
        cont2.add(new JLabel("   "));

        pButtons.add(btnCancel);
        pButtons.add(btnAdd);
        pButtons.add(new JLabel(" "));

        pSelector.add(radioPerson);
        pSelector.add(new JLabel(" "));
        pSelector.add(radioWish);
        pSelector.add(new JLabel(" "));
        pSelector.add(radioSkip);

        pInfo.add(new JLabel(" "));
        pInfo.add(lblInfo);
        pInfo.add(new JLabel(" "));

        cp.add(cont, BorderLayout.PAGE_START);
        cp.add(cont2, BorderLayout.LINE_START);
        cp.add(scrCSV, BorderLayout.CENTER);
        cp.add(new JLabel("   "), BorderLayout.LINE_END);
        cp.add(pButtons, BorderLayout.PAGE_END);
    }

    /**
     * The code to add a created group.
     * */
    private void addGroup()
    {
        // Fixing group manager.
        for (var p : persons)
        {
            for (var w : wishes.getOrDefault(p, new HashSet<>()))
            {
                var id = w.p.getId();
                p.addWishlistId(id);
            }
        }

        this.setVisible(false);

        // Opening edit GUI.
        var agf = new EditGroupFrame(gm);
        agf.addAddListener(event -> {
            var res = event.result();
            event.frame().dispose();
            this.invokeCallbacks(res);
            this.dispose();
        });
    }

    /**
     * Loads the file from the system and returns
     * the CSV-file as a 2D-matrix. It will also
     * allow the user to select the file.
     *
     * @return the loaded file from the data.
     * */
    private String[][] loadFile()
    {
        var fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(false);
        var selection = fc.showDialog(this, "Välj");

        if (selection == JFileChooser.APPROVE_OPTION)
        {
            var f = fc.getSelectedFile();
            return CSVParser.parseCSV(f.getAbsolutePath());
        }
        else
        {
            return new String[0][0];
        }
    }

    /**
     * Invokes the callbacks.
     *
     * @param gm the created group manager.
     * */
    private void invokeCallbacks(GroupManager gm)
    {
        callbacks.forEach(ac -> ac.action(gm));
    }

    /**
     * Handles the person at coord x, y and
     * updates everything accordingly.
     *
     * @param x the x-coord.
     * @param y the y-coord.
     * */
    private void handlePerson(int x, int y)
    {
        var pr = labels[x][y];
        var p = pr.p();
        var l = pr.label();

        if (l.getState().equals(State.PERSON)) // Deselection.
        {
            persons.remove(p);
            gm.removePerson(p.getId());
            l.setSavedBackground(UNSELECTED_COLOR);
            l.setBackground(UNSELECTED_COLOR);
            var wishes =
                Optional.ofNullable(this.wishes.remove(p))
                        .orElse(new HashSet<>());

            for (var plr : wishes)
            {
                plr.label().setSavedBackground(Color.WHITE);
                plr.label().setBackground(Color.WHITE);
                plr.label().setState(State.UNSELECTED);
            }

            l.setState(State.UNSELECTED);
            labels[x][y] = labels[x][y].swapPerson(null);
        }
        else // Selection
        {
            handleAlreadyExistingPerson(x, y, p, l);
            l.setState(State.PERSON);
        }

        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Persons: %s", persons);
    }

    /**
     * Handles an already existing person.
     *
     * @param x the x-coord of the existing person.
     * @param y the y-coord of the existing person.
     * @param p the already existing person
     * @param l the label that's affected.
     * */
    private void handleAlreadyExistingPerson(int x, int y, Person p, CSVLabel l)
    {
        if (p == null)
        {
            if (gm.getPersonFromName(l.getText()).isEmpty())
            {
                p = gm.registerPerson(l.getText(), Person.Role.CANDIDATE);
                persons.add(p);
            }
            else
            {
                p = gm.getPersonFromName(l.getText()).get(0);
            }
        }

        labels[x][y] = labels[x][y].swapPerson(p);
    }

    /**
     * Handles a wish click.
     *
     * @param x the x-coord of the label.
     * @param y the y-coord of the label.
     * */
    private void handleWish(int x, int y)
    {
            var pr = labels[x][y];
        var p = pr.p();
        var l = pr.label();
        var s = l.getState();

        if (s.equals(State.PERSON) && p != null && wishPerson == null) // First wish person
        {
            wishPerson = labels[x][y];
            wishes.put(p, wishes.getOrDefault(p, new HashSet<>()));
            l.startFlashing(500, PERSON_COLOR, WISH_COLOR);
            DebugMethods.log("First wish person", DebugMethods.LogType.DEBUG);
            l.setState(State.WISH);
        }
        else if (s.equals(State.UNSELECTED) && wishPerson == null && !persons.contains(p)) // First wish person and someone isn't a person
        {
            l.setSavedBackground(PERSON_COLOR);
            l.setBackground(PERSON_COLOR);
            handleAlreadyExistingPerson(x, y, p, l);
            wishPerson = labels[x][y];
            wishes.put(p, wishes.getOrDefault(p, new HashSet<>()));
            l.startFlashing(500, PERSON_COLOR, WISH_COLOR);
            DebugMethods.log("First wish person and someone isn't a person", DebugMethods.LogType.DEBUG);
            l.setState(State.WISH);
        }
        else if (s.equals(State.WISH) && wishPerson != null && wishPerson == labels[x][y]) // Deselection of wish person
        {
            l.stopFlashing();
            l.setSavedBackground(PERSON_COLOR); // Always no matter what revert to PERSON_COLOR
            l.setBackground(PERSON_COLOR);
            wishPerson = null;
            DebugMethods.log("Deselection of wish person", DebugMethods.LogType.DEBUG);
            l.setState(State.PERSON);
        }
        else if (s.equals(State.UNSELECTED) && wishPerson != null && (p == null || persons.contains(p))) // Adding wish.
        {
            var set = wishes.getOrDefault(wishPerson.p(), new HashSet<>());

            handleAlreadyExistingPerson(x, y, p, l);
            set.add(labels[x][y]);
            wishes.put(wishPerson.p(), set);
            l.setState(State.WISH);
        }
        else if (s.equals(State.WISH) && wishPerson != null && wishPerson != pr) // Removing wish.
        {
            var set = wishes.getOrDefault(wishPerson.p(), new HashSet<>());
            set.remove(pr);
            wishes.put(wishPerson.p(), set);
            l.setState(State.UNSELECTED);
            labels[x][y] = labels[x][y].swapPerson(null);
        }
        else
        {
            DebugMethods.error("Unknown state");
        }

        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Persons: %s", persons);
        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Wishes: %s", wishes);
    }

    /**
     * Handles skip action.
     *
     * @param x the x-coord.
     * @param y the y-coord.
     * */
    private void handleSkip(int x, int y)
    {
        if (labels[x][y].label().getState().equals(State.SKIP))
            labels[x][y].label().setState(State.UNSELECTED);
        else
            labels[x][y].label().setState(State.SKIP);
    }

    /**
     * The code for handling clicks.
     *
     * @param label the label that is clicked.
     * */
    private void clicked(CSVLabel label)
    {
        this.requestFocus();

        switch (state)
        {
            case PERSON -> handlePerson(
                label.getXCoordinate(),
                label.getYCoordinate()
            );
            case WISH -> handleWish(
                label.getXCoordinate(),
                label.getYCoordinate()
            );
            case SKIP -> handleSkip(
                label.getXCoordinate(),
                label.getYCoordinate()
            );
            default -> label.setState(state);
        }
    }

    /**
     * Adds an action callback.
     *
     * @param ac the action callback to be added.
     * */
    public void addActionCallback(ActionCallbackWithParam<GroupManager> ac)
    {
        if (ac != null)
            callbacks.add(ac);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        this.isCtrlDown = e.isControlDown();

        if (isCtrlDown)
            DebugMethods.log("Control is down!", DebugMethods.LogType.DEBUG);
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        this.isCtrlDown = e.isControlDown();
    }

    /** The record for keeping tack on label and person relationship. */
    private record PersonLabelRecord(CSVLabel label, Person p)
    {
        /**
         * Swaps the person of this record.
         *
         * @param p the new person.
         * */
        public PersonLabelRecord swapPerson(Person p)
        {
            return new PersonLabelRecord(label, p);
        }

        @Override
        public String toString()
        {
            return p != null ? p.toString() : "null";
        }
    }
}
