package se.skorup.main.gui.group.frames;

import se.skorup.group.Group;
import se.skorup.group.Person;
import se.skorup.gui.callbacks.ActionCallback;
import se.skorup.gui.components.Button;
import se.skorup.gui.components.CSVLabel;
import se.skorup.gui.components.Frame;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.components.RadioButton;
import se.skorup.gui.components.ScrollPane;
import se.skorup.gui.dialog.Dialog;
import se.skorup.gui.dialog.InputDialog;
import se.skorup.gui.dialog.MessageDialog;
import se.skorup.gui.helper.State;
import se.skorup.main.gui.group.frames.helper.Template;
import se.skorup.main.gui.group.frames.helper.TemplateItem;
import se.skorup.util.Log;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static se.skorup.gui.components.CSVLabel.PERSON_COLOR;
import static se.skorup.gui.components.CSVLabel.SKIP_COLOR;
import static se.skorup.gui.components.CSVLabel.UNSELECTED_COLOR;
import static se.skorup.gui.components.CSVLabel.WISH_COLOR;

/**
 * The frame responsible for parsing CSV-files.
 * */
public class ImportFrame extends Frame implements KeyListener
{
    private enum FrameState
    {
        NORMAL, FILL_ROW, FILL_COLUMN,
        FILL_TEMPLATE_CREATING, FILL_TEMPLATE_CREATED
    }

    private State state = State.PERSON;
    private FrameState fs = FrameState.NORMAL;
    private PersonLabelRecord wishPerson;
    private Template template;

    private boolean isCtrlDown = false;
    private boolean flashing = false;
    private boolean hasClickedTemplate = false;

    private CSVLabel[] selected = new CSVLabel[0];

    private final PersonLabelRecord[][] labels;
    private final String[][] data;

    private final List<ActionCallback<Group>> callbacks = new ArrayList<>();
    private final Set<Person> persons = new HashSet<>();
    private final Map<Person, Set<PersonLabelRecord>> wishes = new HashMap<>();
    private final Map<Person, Integer> personCount = new HashMap<>();

    private final Group g = new Group("");

    private final Container cp = this.getContentPane();

    private final Button btnCancel = new Button("ui.button.cancel");
    private final Button btnAdd = new Button("ui.button.add.text");
    private final Button btnCreateTemplate = new Button("ui.button.create-template");
    private final Button btnFinishTemplate = new Button("ui.button.finish-template");

    private final RadioButton radioPerson = new RadioButton("ui.choose.person");
    private final RadioButton radioWish = new RadioButton("ui.choose.wish");
    private final RadioButton radioSkip = new RadioButton("ui.choose.skip");
    private final RadioButton radioNormal = new RadioButton("ui.choose.normal");
    private final RadioButton radioRow = new RadioButton("ui.choose.row");
    private final RadioButton radioColumn = new RadioButton("ui.choose.column");
    private final RadioButton radioTemplate = new RadioButton("ui.choose.template");

    private final ButtonGroup bgMode = new ButtonGroup();
    private final ButtonGroup bgEditMode = new ButtonGroup();

    private final Label lblInfo = new Label("ui.info.person");

    private final Panel pCSV = new Panel(null);
    private final Panel pButtons = new Panel(new FlowLayout(FlowLayout.RIGHT));
    private final Panel pSelector = new Panel(null);
    private final Panel pInfo = new Panel(null);
    private final Panel pEditMode = new Panel(null);
    private final Panel pTemplateButtons = new Panel(new FlowLayout(FlowLayout.CENTER));

    private final ScrollPane scrCSV = new ScrollPane(pCSV);

    /**
     * Creates a new Frame.
     *
     * @param data the data of the frame.
     * */
    public ImportFrame(String[][] data)
    {
        super("ui.title.csv");

        this.data = data;
        this.labels = new PersonLabelRecord[data.length][data.length];
        init();
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
                labels[template.getY()][i.x()].label().setState(State.UNSELECTED);
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
        var sb = new StringBuilder("<html>");

        sb.append(
            switch (state)
            {
                case PERSON -> Localization.getValue("ui.info.person");
                case WISH -> Localization.getValue("ui.info.wish");
                case SKIP -> Localization.getValue("ui.info.skip");
                default -> "";
            }
        );

        sb.append("<br>");
        sb.append(
            switch (fs)
            {
                case FILL_TEMPLATE_CREATING -> Localization.getValue("ui.info.creating-template");
                case FILL_TEMPLATE_CREATED -> Localization.getValue("ui.info.created-template");
                default -> "";
            }
        );

        sb.append("</html>");

        lblInfo.setText(sb.toString());
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
                var id = w.p.id();
                g.addWishItem(p.id(), id);
            }
        }

        this.setVisible(false);

        new Thread(() -> {
            var name = InputDialog.create()
                                  .setLocalizedTitle("ui.title.create-group")
                                  .setLocalizedInformation("ui.label.create-group")
                                  .setLocalizedCancelButtonText("ui.button.dialog.cancel")
                                  .setLocalizedOkButtonText("ui.button.dialog.ok")
                                  .show(InputDialog.NO_ICON);

            SwingUtilities.invokeLater(() -> { // Just to be safe :)
                g.setName(name);
                this.invokeCallbacks(g);
                this.dispose();
            });
        }).start();
    }

    /**
     * Invokes the callbacks.
     *
     * @param gm the created group manager.
     * */
    private void invokeCallbacks(Group gm)
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

        Log.debugf("Persons: %s", persons);
        Log.debugf("Wishes:  %s", wishes);
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
            safeRemovePerson(p);
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
                personCount.put(plr.p, personCount.getOrDefault(plr.p, 1) - 1);
            }

            label.setState(State.UNSELECTED);
            labels[x][y] = labels[x][y].swapPerson(null);
        }
        else // Selection
        {
            p = handleAlreadyExistingPerson(x, y, p, label);

            if (p != null)
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
            if (g.getPersonFromName(l.getText()).isEmpty())
            {
                p = new Person(l.getText(), g.registerPerson(l.getText()));
                persons.add(p);
                personCount.put(p, personCount.getOrDefault(p, 0) + 1);
            }
            else
            {
                p = g.getPersonFromName(l.getText()).get(0);
                personCount.put(p, personCount.getOrDefault(p, 0) + 1);
            }
        }

        labels[x][y] = labels[x][y].swapPerson(p);

        return p;
    }

    /**
     * Safely removes a person.
     *
     * @param p the person to be removed.
     * */
    private void safeRemovePerson(Person p)
    {
        var count = personCount.getOrDefault(p, 0) - 1;

        if (count <= 0)
        {
            persons.remove(p);
            personCount.remove(p);
            g.removePerson(p.id());
        }
        else
        {
            personCount.put(p, count);
        }
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
            Log.debug("First wish person");
            l.setState(State.WISH);
        }
        else if (s.equals(State.UNSELECTED) && wishPerson == null && !persons.contains(p)) // First wish person and someone isn't a person
        {
            p = handleAlreadyExistingPerson(x, y, p, l);

            if (p == null)
                return;

            l.setSavedBackground(PERSON_COLOR);
            l.setBackground(PERSON_COLOR);
            wishPerson = labels[x][y];
            wishes.put(p, wishes.getOrDefault(p, new HashSet<>()));
            l.startFlashing(500, PERSON_COLOR, WISH_COLOR);
            Log.debug("First wish person and someone isn't a person");
            l.setState(State.WISH);
        }
        else if (s.equals(State.WISH) && wishPerson != null && wishPerson == labels[x][y]) // Deselection of wish person
        {
            l.stopFlashing();
            l.setSavedBackground(PERSON_COLOR); // Always no matter what revert to PERSON_COLOR
            l.setBackground(PERSON_COLOR);
            wishPerson = null;
            Log.debug("Deselection of wish person");
            l.setState(State.PERSON);
        }
        else if (s.equals(State.UNSELECTED) && wishPerson != null && (p == null || persons.contains(p))) // Adding wish.
        {
            var set = wishes.getOrDefault(wishPerson.p(), new HashSet<>());

            p = handleAlreadyExistingPerson(x, y, p, l);

            if (p == null)
                return;

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
            Log.error("Unknown state");
        }

        Log.debugf("Persons: %s", persons);
        Log.debugf("Wishes: %s", wishes);
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

        Log.debugf("Persons: %s", persons);
        Log.debugf("Wishes:  %s", wishes);
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
            Log.debug(template);
            Log.debugf("State: %s, Label: %s", label.getState(), label);

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
                Log.debugf("Handling item: %s", i);

                state = i.state();

                var label1 = labels[label.getXCoordinate()][i.x()].label;
                clicked(label1);

                Log.debugf("Label just handled: %s", label1);
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
    public void addActionCallback(ActionCallback<Group> ac)
    {
        if (ac != null)
            callbacks.add(ac);
    }

    @Override
    public void dispose()
    {
        this.invokeCallbacks(null);
        super.dispose();
    }

    @Override
    protected void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(1600, 900));
        this.addKeyListener(this);

        cp.setLayout(new BorderLayout());
        pCSV.setLayout(new GridLayout(data.length, data[0].length));
        pSelector.setLayout(new BoxLayout(pSelector, BoxLayout.Y_AXIS));
        pInfo.setLayout(new BoxLayout(pInfo, BoxLayout.Y_AXIS));
        pEditMode.setLayout(new BoxLayout(pEditMode, BoxLayout.Y_AXIS));

        scrCSV.setBorder(BorderFactory.createEmptyBorder());

        for (var i = 0; i < data.length; i++)
        {
            for (var ii = 0; ii < data[i].length; ii++)
            {
                var label = new CSVLabel(data[i][ii], i, ii, Color.WHITE, Color.DARK_GRAY);
                var fi = i;
                var fii = ii;
                label.addEnterEffect(c -> hoverEnter(c, fi, fii));
                label.addExitEffect(c -> hoverExit());
                label.addActionCallback(this::clicked);
                pCSV.add(label);
                labels[i][ii] = new PersonLabelRecord(label, null);
            }
        }

        btnAdd.addActionListener(e -> addGroup());
        btnCancel.addActionListener(e -> this.dispose());

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
        radioNormal.setFont(font);
        radioNormal.addActionListener(e -> {
            fs = FrameState.NORMAL;
            radioWish.setEnabled(true);
            resetTemplate();
            setLabelInfoText();
        });

        radioColumn.setSelected(false);
        radioColumn.setBackground(Utils.BACKGROUND_COLOR);
        radioColumn.setForeground(Utils.FOREGROUND_COLOR);
        radioColumn.setFont(font);
        radioColumn.addActionListener(e -> {
            fs = FrameState.FILL_COLUMN;
            wishDeactivation();
            resetTemplate();
            setLabelInfoText();
        });

        radioRow.setSelected(false);
        radioRow.setBackground(Utils.BACKGROUND_COLOR);
        radioRow.setForeground(Utils.FOREGROUND_COLOR);
        radioRow.setFont(font);
        radioRow.addActionListener(e -> {
            fs = FrameState.FILL_ROW;
            wishDeactivation();
            resetTemplate();
            setLabelInfoText();
        });

        radioTemplate.setSelected(false);
        radioTemplate.setBackground(Utils.BACKGROUND_COLOR);
        radioTemplate.setForeground(Utils.FOREGROUND_COLOR);
        radioTemplate.setFont(font);
        radioTemplate.addActionListener(e -> {
            fs = FrameState.FILL_TEMPLATE_CREATING;
            radioWish.setEnabled(true);
            resetTemplate();
            setLabelInfoText();
        });

        bgEditMode.add(radioNormal);
        bgEditMode.add(radioColumn);
        bgEditMode.add(radioRow);
        bgEditMode.add(radioTemplate);

        lblInfo.setFont(new Font(Font.DIALOG, Font.BOLD, 16));

        btnCreateTemplate.setEnabled(false);
        btnCreateTemplate.addActionListener(e -> resetTemplate());
        btnCreateTemplate.addActionListener(e -> fs = FrameState.FILL_TEMPLATE_CREATING);

        btnFinishTemplate.setEnabled(false);
        btnFinishTemplate.addActionListener(e -> {
            if (template.isEmpty())
            {
                try // TODO: KEEP AN EYE!
                {
                    var t1 = new Thread(() -> {
                        MessageDialog.create()
                                     .setLocalizedTitle("")
                                     .setLocalizedInformation("ui.error.at-least-one-action")
                                     .setLocalizedButtonText("ui.button.dialog.close")
                                     .show(Dialog.ERROR_MESSAGE);
                    });
                    t1.start();
                    t1.join();
                }
                catch (InterruptedException unexpected)
                {
                    throw new RuntimeException(unexpected);
                }

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

            setLabelInfoText();
        });
    }

    @Override
    protected void addComponents()
    {
        var cont = new Panel(new FlowLayout(FlowLayout.LEFT));
        cont.add(new Label("   "));
        cont.add(pInfo);

        var cont3 = new Panel(null);
        cont3.setLayout(new BoxLayout(cont3, BoxLayout.Y_AXIS));
        cont3.add(new Label(" "));
        cont3.add(new Label(" "));
        cont3.add(new Label(" "));
        cont3.add(pTemplateButtons);

        var innerCont2 = new Panel(new GridLayout(3, 1));
        innerCont2.add(pSelector);
        innerCont2.add(pEditMode);
        innerCont2.add(cont3);

        var cont2 = new Panel(new FlowLayout(FlowLayout.LEFT));
        cont2.add(new Label("   "));
        cont2.add(innerCont2);
        cont2.add(new Label("   "));

        pTemplateButtons.add(btnCreateTemplate);
        pTemplateButtons.add(new Label(" ".repeat(10)));
        pTemplateButtons.add(btnFinishTemplate);

        pButtons.add(btnCancel);
        pButtons.add(btnAdd);
        pButtons.add(new Label(" "));

        pSelector.add(radioPerson);
        pSelector.add(new Label(" "));
        pSelector.add(radioWish);
        pSelector.add(new Label(" "));
        pSelector.add(radioSkip);

        pEditMode.add(radioNormal);
        pEditMode.add(new Label(" "));
        pEditMode.add(radioRow);
        pEditMode.add(new Label(" "));
        pEditMode.add(radioColumn);
        pEditMode.add(new Label(" "));
        pEditMode.add(radioTemplate);

        pInfo.add(new Label(" "));
        pInfo.add(lblInfo);
        pInfo.add(new Label(" "));

        cp.add(cont, BorderLayout.PAGE_START);
        cp.add(cont2, BorderLayout.LINE_START);
        cp.add(scrCSV, BorderLayout.CENTER);
        cp.add(new Label("   "), BorderLayout.LINE_END);
        cp.add(pButtons, BorderLayout.PAGE_END);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        this.isCtrlDown = e.isControlDown();

        if (isCtrlDown)
            Log.debug("Control is down!");
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
