package se.skorup.main.gui.panels;

import se.skorup.API.expression_evalutator.parser.Parser;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.TerminalInput;
import se.skorup.main.gui.components.TerminalOutput;
import se.skorup.main.gui.components.TerminalPane;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The panel that holds the calculator.
 * */
public class CalculatorPanel extends JPanel implements KeyListener
{
    /** The keycode for the enter key. */
    private static final int ENTER = 10;

    private TerminalPane input;
    private TerminalPane output;
    private JScrollPane scrOutput;

    /**
     * Creates a new Calculator panel.
     * */
    public CalculatorPanel()
    {
        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties of the frame.
     * */
    private void setProperties()
    {
        this.setLayout(new BorderLayout());
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);

        this.input = new TerminalInput();
        this.output = new TerminalOutput(new Dimension(380, 450));

        this.scrOutput = new JScrollPane(output);
        this.scrOutput.setBorder(BorderFactory.createEmptyBorder());

        this.input.addKeyListener(this);
        this.output.setFontSize(12);
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        var cont = new JPanel();
        cont.setBackground(Utils.BACKGROUND_COLOR);
        cont.setLayout(new BorderLayout());

        var cont2 = new JPanel();
        cont2.setBackground(Utils.BACKGROUND_COLOR);
        cont2.setLayout(new BoxLayout(cont2, BoxLayout.Y_AXIS));

        cont2.add(new JLabel(" "));
        cont2.add(input);

        cont.add(scrOutput, BorderLayout.CENTER);
        cont.add(cont2, BorderLayout.PAGE_END);

        this.add(new JLabel("    "), BorderLayout.PAGE_START);
        this.add(new JLabel("    "), BorderLayout.LINE_START);
        this.add(cont, BorderLayout.CENTER);
        this.add(new JLabel("    "), BorderLayout.LINE_END);
        this.add(new JLabel("    "), BorderLayout.PAGE_END);
    }

    /**
     * Calculates the entered expression.
     * */
    private void calculate()
    {
        var parser = new Parser(input.getText());
        var res = parser.parse();

        if (parser.getDiagnostics().size() == 0)
        {
            output.appendColoredString("<green>%f</green>".formatted(res.getValue()));
        }
        else
        {
            var errors = parser.getDiagnostics().mkString("\n");
            DebugMethods.log(errors, DebugMethods.LogType.ERROR);
            output.appendColoredString("<light_red>%s</light_red>".formatted(errors));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == ENTER)
            calculate();
    }
}
