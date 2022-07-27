package se.skorup.main.gui.frames;

import se.skorup.API.util.CSVParser;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.CSVLabel;
import se.skorup.main.gui.interfaces.ActionCallbackWithParam;
import se.skorup.main.manager.GroupManager;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The frame that houses the custom CSV parsing
 * of groups.
 * */
public class CSVFrame extends JFrame
{
    private static final String PERSON_INFO =
        "Välj en person, genom att klicka på den rutan som personer motsvarar!";

    private static final String WISH_INFO =
        "Välj en person - genom att klicka på rätt ruta, att tilldela önskningar till och sedan klicka på önskningarna.";

    private static final String SKIP_INFO =
        "Välj en ruta som kommer att hoppas över. Detta är valfritt och behöver inte göras.";

    private State state = State.PERSON;

    private final CSVLabel[][] labels;
    private final String[][] data;

    private final List<ActionCallbackWithParam<GroupManager>> callbacks = new ArrayList<>();

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
        this.labels = new CSVLabel[data.length][data.length];

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
                });
                label.addExitEffect(c -> c.setBackground(c.getSavedBackground()));
                label.addActionCallback(l -> {
                    if (l.isSelected())
                    {
                        l.setSavedBackground(Color.WHITE);
                        l.setBackground(Color.WHITE);
                    }
                    else
                    {
                        l.setSavedBackground(state.c);
                        l.setBackground(state.c);
                    }
                });
                pCSV.add(label);
                labels[i][ii] = label;
            }
        }

        pButtons.setBackground(Utils.BACKGROUND_COLOR);
        pButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));

        btnAdd.setForeground(Utils.FOREGROUND_COLOR);
        btnAdd.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

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
        radioPerson.setBackground(Utils.LIGHT_GREEN);
        radioPerson.setFont(font);

        radioWish.addActionListener(e -> lblInfo.setText(WISH_INFO));
        radioWish.addActionListener(e -> state = State.WISH);
        radioWish.setSelected(false);
        radioWish.setForeground(Color.BLACK);
        radioWish.setBackground(Utils.LIGHT_BLUE);
        radioWish.setFont(font);

        radioSkip.addActionListener(e -> lblInfo.setText(SKIP_INFO));
        radioSkip.addActionListener(e -> state = State.SKIP);
        radioSkip.setSelected(false);
        radioSkip.setForeground(Color.BLACK);
        radioSkip.setBackground(Utils.LIGHT_RED);
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
     * Adds an action callback.
     *
     * @param ac the action callback to be added.
     * */
    public void addActionCallback(ActionCallbackWithParam<GroupManager> ac)
    {
        if (ac != null)
            callbacks.add(ac);
    }

    private enum State
    {
        PERSON(Utils.LIGHT_GREEN),
        WISH(Utils.LIGHT_BLUE),
        SKIP(Utils.LIGHT_RED);

        public final Color c;

        State(Color c)
        {
            this.c = c;
        }
    }
}
