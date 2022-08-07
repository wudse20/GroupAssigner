package se.skorup.main.gui.frames;

import se.skorup.API.util.CSVParser;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.FormsParser;
import se.skorup.API.util.MyFileReader;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.panels.ControlPanel;
import se.skorup.main.manager.GroupManager;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.Arrays;

/**
 * The frame that's used to display the different CSV-options.
 * */
public class ImportFrame extends JFrame
{
    private final MainFrame mf;
    private final ControlPanel cp;

    private static final String INFO_TEXT =
        "<html><br>Välj det alterantivet som matchar den filen du har. <br>Är du osäker? Välj CSV-fil.<br><br></html>";

    private final Container cont = this.getContentPane();

    private final JButton btnCSVParsing = new JButton("Importera en CSV-fil");
    private final JButton btnGoogleForms = new JButton("Importera från Google Forms");

    private final JPanel pButtons = new JPanel();

    private final JLabel lblInfo = new JLabel(INFO_TEXT);

    /**
     * Creates a new ImportFrame.
     *
     * @param mf the instance of the MainFrame in use.
     * @param cp the instance of the ControlPanel in use.
     * */
    public ImportFrame(MainFrame mf, ControlPanel cp)
    {
        super("Importera");

        this.mf = mf;
        this.cp = cp;
        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets properties.
     * */
    private void setProperties()
    {
        this.setSize(new Dimension(450, 200));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        cont.setBackground(Utils.BACKGROUND_COLOR);
        cont.setLayout(new BorderLayout());

        pButtons.setBackground(Utils.BACKGROUND_COLOR);
        pButtons.setLayout(new FlowLayout(FlowLayout.CENTER));

        lblInfo.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        lblInfo.setForeground(Utils.FOREGROUND_COLOR);

        btnCSVParsing.setForeground(Utils.FOREGROUND_COLOR);
        btnCSVParsing.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCSVParsing.addActionListener(e -> parseCSV());

        btnGoogleForms.setForeground(Utils.FOREGROUND_COLOR);
        btnGoogleForms.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnGoogleForms.addActionListener(e -> importGoogleForms());
    }

    /**
     * Adds components.
     * */
    private void addComponents()
    {
        var container = new JPanel();
        container.setBackground(Utils.BACKGROUND_COLOR);
        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        container.add(lblInfo);

        pButtons.add(btnGoogleForms);
        pButtons.add(btnCSVParsing);

        cont.add(container, BorderLayout.PAGE_START);
        cont.add(pButtons, BorderLayout.CENTER);
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
        var selection = fc.showDialog(mf, "Välj");

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
     * The action to parse a CSV-file.
     * */
    private void parseCSV()
    {
        var data = loadFile();

        if (Arrays.deepEquals(data, new String[0][0]))
        {
            this.dispose();
            return;
        }

        var frame = new CSVFrame(data);
        frame.addActionCallback(gm -> {
            mf.addGroupManager(gm);
            cp.updateManagers();
            frame.dispose();
        });

        this.dispose();
    }

    /**
     * Imports and parses a CSV file from the GoogleForms
     * format described in the help pages.
     * */
    private void importGoogleForms()
    {
        var fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(true);
        var selection = fc.showDialog(mf, "Välj");

        if (selection == JFileChooser.APPROVE_OPTION)
        {
            var sb = new StringBuilder();
            for (var f : fc.getSelectedFiles())
                sb.append(f.getName()).append(" + ");

            var result = new GroupManager(sb.substring(0, sb.length() - 3));

            try
            {
                for (var str : MyFileReader.readFiles(fc.getSelectedFiles()))
                    FormsParser.parseFormData(str, result);
            }
            catch (IOException ex)
            {
                DebugMethods.log(ex, DebugMethods.LogType.ERROR);
                JOptionPane.showMessageDialog(
                    this,
                    "Kunde inte läsa fil!\nFelmeddelande%s".formatted(ex.getLocalizedMessage()),
                    "Kunde inte läsa fil", JOptionPane.ERROR_MESSAGE
                );

                this.dispose();
                return;
            }

            this.dispose();
            mf.addGroupManager(result);
            cp.updateManagers();
        }
    }
}
