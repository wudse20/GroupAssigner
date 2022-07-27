package se.skorup.main.gui.frames;

import se.skorup.API.util.CSVParser;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.CSVLabel;
import se.skorup.main.gui.interfaces.ActionCallbackWithParam;
import se.skorup.main.manager.GroupManager;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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
    private CSVLabel[][] labels;

    private final String[][] data;

    private final List<ActionCallbackWithParam<GroupManager>> callbacks = new ArrayList<>();

    private final Container cp = this.getContentPane();

    private final JPanel pCSV = new JPanel();
    private final JScrollPane scrCSV = new JScrollPane(pCSV);

    /**
     * Creates a new CSVFrame.
     * */
    public CSVFrame()
    {
        super("Skapa grupper utifrån CSV");

        this.data = loadFile();

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

        this.labels = new CSVLabel[data.length][data[0].length];
        for (var i = 0; i < data.length; i++)
        {
            for (var ii = 0; ii < data[i].length; ii++)
            {
                var label = new CSVLabel(data[i][ii], i, ii, Color.WHITE, Color.BLACK);
                pCSV.add(label);
                labels[i][ii] = label;
            }
        }
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        cp.add(scrCSV, BorderLayout.CENTER);
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
}
