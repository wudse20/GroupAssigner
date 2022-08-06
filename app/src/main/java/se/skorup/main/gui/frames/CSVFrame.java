package se.skorup.main.gui.frames;

import se.skorup.API.util.CSVParser;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.CSVLabel;
import se.skorup.main.gui.components.enums.State;
import se.skorup.main.gui.components.objects.Template;
import se.skorup.main.gui.components.objects.TemplateItem;
import se.skorup.main.gui.interfaces.ActionCallbackWithParam;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
    private FrameState fs = FrameState.NORMAL;
    private PersonLabelRecord wishPerson;
    private  Template template;

    private boolean isCtrlDown = false;
    private boolean flashing = false;
    private boolean hasClickedTemplate = false;

    private CSVLabel[] selected = new CSVLabel[0];

    private final PersonLabelRecord[][] labels;
    private final String[][] data;

    private final List<ActionCallbackWithParam<GroupManager>> callbacks = new ArrayList<>();
    private final Set<Person> persons = new HashSet<>();
    private final Map<Person, Set<PersonLabelRecord>> wishes = new HashMap<>();

    private final GroupManager gm = new GroupManager("");

    private final Container cp = this.getContentPane();

    private final JButton btnCancel = new JButton("Avbryt");
    private final JButton btnAdd = new JButton("Lägg till");
    private final JButton btnHelp = new JButton("Hjälp!");
    private final JButton btnCreateTemplate = new JButton("Skapa mall");
    private final JButton btnFinishTemplate = new JButton("Avsluta mall");

    private final JRadioButton radioPerson = new JRadioButton("   Välj en person           ");
    private final JRadioButton radioWish = new JRadioButton("   Välj en önskning         ");
    private final JRadioButton radioSkip = new JRadioButton("   Välj en att hoppa över   ");
    private final JRadioButton radioNormal = new JRadioButton("   Normal ifyllnad          ");
    private final JRadioButton radioRow = new JRadioButton("   Fyll i rad               ");
    private final JRadioButton radioColumn = new JRadioButton("   Fyll i kolumn            ");
    private final JRadioButton radioTemplate = new JRadioButton("   Fyll i med mall          ");

    private final ButtonGroup bgMode = new ButtonGroup();
    private final ButtonGroup bgEditMode = new ButtonGroup();

    private final JLabel lblInfo = new JLabel(WISH_INFO);

    private final JPanel pCSV = new JPanel();
    private final JPanel pButtons = new JPanel();
    private final JPanel pSelector = new JPanel();
    private final JPanel pInfo = new JPanel();
    private final JPanel pEditMode = new JPanel();
    private final JPanel pTemplateButtons = new JPanel();

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
                var fi = i;
                var fii = ii;
                label.addEnterEffect(c -> hoverEnter(c, fi, fii));
                label.addExitEffect(c -> hoverExit());
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

        btnHelp.setForeground(Utils.FOREGROUND_COLOR);
        btnHelp.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnHelp.addActionListener(e -> Utils.openHelpPages());

        pSelector.setBackground(Utils.BACKGROUND_COLOR);
        pSelector.setLayout(new BoxLayout(pSelector, BoxLayout.Y_AXIS));

        var font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
        radioPerson.addActionListener(e -> setLabelInfoText());
        radioPerson.addActionListener(e -> state = State.PERSON);
        radioPerson.setSelected(true);
        radioPerson.setForeground(Color.BLACK);
        radioPerson.setBackground(PERSON_COLOR);
        radioPerson.setFont(font);

        radioWish.addActionListener(e -> setLabelInfoText());
        radioWish.addActionListener(e -> state = State.WISH);
        radioWish.setSelected(false);
        radioWish.setForeground(Color.BLACK);
        radioWish.setBackground(WISH_COLOR);
        radioWish.setFont(font);

        radioSkip.addActionListener(e -> setLabelInfoText());
        radioSkip.addActionListener(e -> state = State.SKIP);
        radioSkip.setSelected(false);
        radioSkip.setForeground(Color.BLACK);
        radioSkip.setBackground(SKIP_COLOR);
        radioSkip.setFont(font);

        bgMode.add(radioPerson);
        bgMode.add(radioWish);
        bgMode.add(radioSkip);

        radioNormal.setSelected(true);
        radioNormal.setBackground(Utils.BACKGROUND_COLOR);
        radioNormal.setForeground(Utils.FOREGROUND_COLOR);
        radioNormal.setSelected(true);
        radioNormal.setFont(font);
        radioNormal.addActionListener(e -> fs = FrameState.NORMAL);
        radioNormal.addActionListener(e -> radioWish.setEnabled(true));
        radioNormal.addActionListener(e -> resetTemplate());

        radioColumn.setSelected(true);
        radioColumn.setBackground(Utils.BACKGROUND_COLOR);
        radioColumn.setForeground(Utils.FOREGROUND_COLOR);
        radioColumn.setSelected(false);
        radioColumn.setFont(font);
        radioColumn.addActionListener(e -> fs = FrameState.FILL_COLUMN);
        radioColumn.addActionListener(e -> wishDeactivation());
        radioColumn.addActionListener(e -> resetTemplate());

        radioRow.setSelected(true);
        radioRow.setBackground(Utils.BACKGROUND_COLOR);
        radioRow.setForeground(Utils.FOREGROUND_COLOR);
        radioRow.setSelected(false);
        radioRow.setFont(font);
        radioRow.addActionListener(e -> fs = FrameState.FILL_ROW);
        radioRow.addActionListener(e -> wishDeactivation());
        radioRow.addActionListener(e -> resetTemplate());

        radioTemplate.setSelected(true);
        radioTemplate.setBackground(Utils.BACKGROUND_COLOR);
        radioTemplate.setForeground(Utils.FOREGROUND_COLOR);
        radioTemplate.setSelected(true);
        radioTemplate.setFont(font);
        radioTemplate.addActionListener(e -> fs = FrameState.FILL_TEMPLATE_CREATING);
        radioTemplate.addActionListener(e -> radioWish.setEnabled(true));
        radioTemplate.addActionListener(e -> setLabelInfoText());
        radioTemplate.addActionListener(e -> resetTemplate());

        bgEditMode.add(radioNormal);
        bgEditMode.add(radioColumn);
        bgEditMode.add(radioRow);
        bgEditMode.add(radioTemplate);

        lblInfo.setForeground(Utils.FOREGROUND_COLOR);
        lblInfo.setFont(new Font(Font.DIALOG, Font.BOLD, 16));

        pInfo.setBackground(Utils.BACKGROUND_COLOR);
        pInfo.setLayout(new BoxLayout(pInfo, BoxLayout.Y_AXIS));

        pEditMode.setBackground(Utils.BACKGROUND_COLOR);
        pEditMode.setLayout(new BoxLayout(pEditMode, BoxLayout.Y_AXIS));

        pTemplateButtons.setBackground(Utils.BACKGROUND_COLOR);
        pTemplateButtons.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnCreateTemplate.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCreateTemplate.setForeground(Utils.FOREGROUND_COLOR);
        btnCreateTemplate.setEnabled(false);
        btnCreateTemplate.addActionListener(e -> resetTemplate());
        btnCreateTemplate.addActionListener(e -> fs = FrameState.FILL_TEMPLATE_CREATING);

        btnFinishTemplate.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnFinishTemplate.setForeground(Utils.FOREGROUND_COLOR);
        btnFinishTemplate.setEnabled(false);
        btnFinishTemplate.addActionListener(e -> {
            if (template.isEmpty())
            {
                JOptionPane.showMessageDialog(
                    this, "Du måste ha minst en handling i mallen!",
                    "För få handlingar", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            fs = FrameState.FILL_TEMPLATE_CREATED;
            btnCreateTemplate.setEnabled(true);
            btnFinishTemplate.setEnabled(false);

            for (var i : template)
            {
                var label = labels[template.getY()][i.x()].label;
                label.stopFlashing();
                label.setState(State.UNSELECTED);
            }
        });
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

        var cont3 = new JPanel();
        cont3.setBackground(Utils.BACKGROUND_COLOR);
        cont3.setLayout(new BoxLayout(cont3, BoxLayout.Y_AXIS));
        cont3.add(new JLabel(" "));
        cont3.add(new JLabel(" "));
        cont3.add(new JLabel(" "));
        cont3.add(pTemplateButtons);

        var innerCont2 = new JPanel();
        innerCont2.setLayout(new GridLayout(3, 1));
        innerCont2.setBackground(Utils.BACKGROUND_COLOR);
        innerCont2.add(pSelector);
        innerCont2.add(pEditMode);
        innerCont2.add(cont3);

        var cont2 = new JPanel();
        cont2.setBackground(Utils.BACKGROUND_COLOR);
        cont2.setLayout(new FlowLayout(FlowLayout.LEFT));
        cont2.add(new JLabel("   "));
        cont2.add(innerCont2);
        cont2.add(new JLabel("   "));

        pTemplateButtons.add(btnCreateTemplate);
        pTemplateButtons.add(new JLabel(" ".repeat(10)));
        pTemplateButtons.add(btnFinishTemplate);

        pButtons.add(btnHelp);
        pButtons.add(btnCancel);
        pButtons.add(btnAdd);
        pButtons.add(new JLabel(" "));

        pSelector.add(radioPerson);
        pSelector.add(new JLabel(" "));
        pSelector.add(radioWish);
        pSelector.add(new JLabel(" "));
        pSelector.add(radioSkip);

        pEditMode.add(radioNormal);
        pEditMode.add(new JLabel(" "));
        pEditMode.add(radioRow);
        pEditMode.add(new JLabel(" "));
        pEditMode.add(radioColumn);
        pEditMode.add(new JLabel(" "));
        pEditMode.add(radioTemplate);

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
     * Resets the template and clears its selection.
     * */
    private void resetTemplate()
    {
        if (template == null)
            return;

        if (!hasClickedTemplate)
        {
            for (var i : template)
            {
                labels[template.getY()][i.x()].label()
                                              .setState(State.UNSELECTED);
            }
        }

        template = null;

        btnCreateTemplate.setEnabled(false);
        btnFinishTemplate.setEnabled(false);
    }

    /**
     * Sets the correct text for the info label.
     * */
    private void setLabelInfoText()
    {
        lblInfo.setText(
            switch (state)
            {
                case PERSON -> PERSON_INFO;
                case WISH -> WISH_INFO;
                case SKIP -> SKIP_INFO;
                default -> "";
            }
        );
    }

    /**
     * The code for deactivating wishes.
     * */
    private void wishDeactivation()
    {
        radioWish.setEnabled(false);

        if (state.equals(State.WISH))
        {
            radioPerson.setSelected(true);
            state = State.PERSON;
        }
    }

    /**
     * Handles hover enter foreach CSVLabel.
     *
     * @param c the label itself.
     * @param x the x-coord of the label.
     * @param y the y-coord of the label.
     * */
    private void hoverEnter(CSVLabel c, int x, int y)
    {
        if (fs.equals(FrameState.NORMAL) || fs.equals(FrameState.FILL_TEMPLATE_CREATING))
        {
            c.setSavedBackground(c.getBackground());
            c.setBackground(Utils.SELECTED_COLOR);
            selected = new CSVLabel[1];
            selected[0] = c;

            if (isCtrlDown)
                clicked(c);
        }
        else if (fs.equals(FrameState.FILL_ROW))
        {
            var rows = data[0].length;
            selected = new CSVLabel[rows];

            for (var i = 0; i < selected.length; i++)
            {
                var l = labels[x][i].label();
                l.setSavedBackground(l.getBackground());
                l.setBackground(Utils.SELECTED_COLOR);
                selected[i] = l;
            }
        }
        else if (fs.equals(FrameState.FILL_COLUMN))
        {
            var cols = data.length;
            selected = new CSVLabel[cols];

            for (var i = 0; i < selected.length; i++)
            {
                var l = labels[i][y].label();
                l.setSavedBackground(l.getBackground());
                l.setBackground(Utils.SELECTED_COLOR);
                selected[i] = l;
            }
        }
        else if (fs.equals(FrameState.FILL_TEMPLATE_CREATED))
        {
            selected = new CSVLabel[Math.min(data[0].length, template.size())];
            var set = new HashSet<CSVLabel>();
            var count = 0;

            for (var i : template)
            {
                var l = labels[x][i.x()].label();

                if (set.contains(l))
                    continue;

                set.add(l);
                l.setSavedBackground(l.getState().color);
                l.setBackground(Utils.SELECTED_COLOR);
                selected[count++] = l;
            }
        }

        this.requestFocus();
    }

    /**
     * The code that runs on hover exit.
     * */
    private void hoverExit()
    {
        for (var l : selected)
        {
            if (l != null)
                l.setBackground(l.getSavedBackground());
        }

        this.requestFocus();
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

        if (fs.equals(FrameState.NORMAL)) // Normal selection.
        {
            handlePersonSelectionLogic(x, y, p, l);
        }
        else if (fs.equals(FrameState.FILL_ROW)) // Fill row.
        {
            for (var i = 0; i < selected.length; i++)
            {
                var label = labels[x][i].label();
                var person = labels[x][i].p();
                handlePersonSelectionLogic(x, i, person, label);
            }
        }
        else if (fs.equals(FrameState.FILL_COLUMN)) // Fill column
        {
            for (var i = 0; i < selected.length; i++)
            {
                var label = labels[i][y].label();
                var person = labels[i][y].p();
                handlePersonSelectionLogic(i, y, person, label);
            }
        }

        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Persons: %s", persons);
        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Wishes:  %s", wishes);
    }

    /**
     * The logic for handling selection and deselection of persons.
     *
     * @param x the current x-coord.
     * @param y the current y-coord.
     * @param p the current person of the position.
     * @param label the label of position (x, y).
     * */
    private void handlePersonSelectionLogic(int x, int y, Person p, CSVLabel label)
    {
        if (label.getState().equals(State.PERSON)) // Deselection.
        {
            persons.remove(p);
            gm.removePerson(p.getId());
            label.setSavedBackground(UNSELECTED_COLOR);
            label.setBackground(UNSELECTED_COLOR);
            var wishes =
                Optional.ofNullable(this.wishes.remove(p))
                        .orElse(new HashSet<>());

            for (var plr : wishes)
            {
                plr.label().setSavedBackground(Color.WHITE);
                plr.label().setBackground(Color.WHITE);
                plr.label().setState(State.UNSELECTED);
            }

            label.setState(State.UNSELECTED);
            labels[x][y] = labels[x][y].swapPerson(null);
        }
        else // Selection
        {
            handleAlreadyExistingPerson(x, y, p, label);
            label.setState(State.PERSON);
        }
    }

    /**
     * Handles an already existing person.
     *
     * @param x the x-coord of the existing person.
     * @param y the y-coord of the existing person.
     * @param p the already existing person
     * @param l the label that's affected.
     * @return the person that has been found or created.
     * */
    private Person handleAlreadyExistingPerson(int x, int y, Person p, CSVLabel l)
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

        return p;
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
            p = handleAlreadyExistingPerson(x, y, p, l);
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
        else if (
            s.equals(State.WISH) && wishPerson != null && wishPerson != pr &&
            wishes.getOrDefault(wishPerson.p, new HashSet<>()).contains(pr)
        ) // Removing wish when selected wish person.
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
        if (fs.equals(FrameState.NORMAL)) // Normal selection.
        {
            skipDeselectionLogic(x, y);
        }
        else if (fs.equals(FrameState.FILL_ROW)) // Filling row
        {
            for (int i = 0; i < selected.length; i++)
            {
                skipDeselectionLogic(x, i);
            }
        }
        else if (fs.equals(FrameState.FILL_COLUMN)) // Filling column
        {
            for (int i = 0; i < selected.length; i++)
            {
                skipDeselectionLogic(i, y);
            }
        }

        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Persons: %s", persons);
        DebugMethods.logF(DebugMethods.LogType.DEBUG, "Wishes:  %s", wishes);
    }

    /**
     * The logic for deselecting when using the skip
     * state.
     *
     * @param x the x-coord of the label.
     * @param y the y-coord of the label.
     * */
    private void skipDeselectionLogic(int x, int y)
    {
        var l = labels[x][y].label();
        var p = labels[x][y].p();

        if (l.getState().equals(State.SKIP))
        {
            l.setState(State.UNSELECTED);
        }
        else
        {
            if (l.getState().equals(State.PERSON))
                handlePersonSelectionLogic(x, y, p, l);
            else if (l.getState().equals(State.WISH) && wishPerson != null)
                handleWish(x, y);
            else if (l.getState().equals(State.WISH))
                return;

            l.setState(State.SKIP);
        }
    }

    /**
     * The code for handling clicks.
     *
     * @param label the label that is clicked.
     * */
    private void clicked(CSVLabel label)
    {
        if (fs.equals(FrameState.FILL_TEMPLATE_CREATING))
        {
            if (template != null && label.getXCoordinate() != template.getY())
                return;

            if (template == null)
            {
                template = new Template(label.getXCoordinate());
                flashing = false;
                hasClickedTemplate = false;
            }

            template.addTemplateItem(new TemplateItem(state, label.getYCoordinate()));
            btnFinishTemplate.setEnabled(true);
            DebugMethods.log(template, DebugMethods.LogType.DEBUG);
            DebugMethods.logF(DebugMethods.LogType.DEBUG, "State: %s, Label: %s", label.getState(), label);

            if (
                state.equals(State.WISH) && !flashing &&
                (label.getSavedBackground().equals(UNSELECTED_COLOR) ||
                 label.getSavedBackground().equals(PERSON_COLOR))
            )
            {
                flashing = true;
                label.startFlashing(500, WISH_COLOR, PERSON_COLOR);
            }
            else if (label.isFlashing())
            {
                flashing = false;
                label.stopFlashing();
                label.setSavedBackground(PERSON_COLOR);
                label.setBackground(PERSON_COLOR);
            }
            else if (!label.getSavedBackground().equals(UNSELECTED_COLOR))
            {
                label.setBackground(UNSELECTED_COLOR);
                label.setSavedBackground(UNSELECTED_COLOR);
            }
            else
            {
                label.setBackground(state.color);
                label.setSavedBackground(state.color);
            }
        }
        else if (fs.equals(FrameState.FILL_TEMPLATE_CREATED))
        {
            // Save org. state.
            var orgState = state;

            // Templates will be added as if they are entered in normal mode.
            fs = FrameState.NORMAL;

            hasClickedTemplate = true;

            for (var i : template)
            {
                DebugMethods.logF(DebugMethods.LogType.DEBUG, "Handling item: %s", i);

                state = i.state();

                var label1 = labels[label.getXCoordinate()][i.x()].label;
                clicked(label1);

                DebugMethods.logF(DebugMethods.LogType.DEBUG, "Label just handled: %s", label1);
            }

            // Reset state.
            fs = FrameState.FILL_TEMPLATE_CREATED;
            state = orgState;
        }
        else
        {
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

        this.requestFocus();
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

    private enum FrameState
    {
        NORMAL, FILL_ROW, FILL_COLUMN, FILL_TEMPLATE_CREATING, FILL_TEMPLATE_CREATED
    }
}
