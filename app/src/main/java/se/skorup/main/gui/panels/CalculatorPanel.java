package se.skorup.main.gui.panels;

import se.skorup.API.collections.mutable_collections.HistoryList;
import se.skorup.API.collections.mutable_collections.HistoryStructure;
import se.skorup.API.expression_evalutator.Environment;
import se.skorup.API.expression_evalutator.parser.Parser;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.ExpressionSyntaxHighlighting;
import se.skorup.main.gui.components.TerminalInput;
import se.skorup.main.gui.components.TerminalOutput;
import se.skorup.main.gui.components.TerminalPane;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * The panel that holds the calculator.
 * */
public class CalculatorPanel extends JPanel implements KeyListener, Environment
{
    /** The keycode for the enter key. */
    private static final int ENTER = 10;

    private boolean lastVarCallValid = true;
    private String lastConstantError = "";

    private final Map<String, Double> vars;
    private final HistoryStructure<String> history;

    private TerminalPane input;
    private TerminalPane output;
    private JScrollPane scrOutput;

    /**
     * Creates a new Calculator panel.
     *
     * @param manager the group manager in use.
     * */
    public CalculatorPanel(GroupManager manager)
    {
        this.vars = new HashMap<>();
        this.history = new HistoryList<>();

        this.setUpConstants(manager);
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

        this.input = new TerminalInput('!', new ExpressionSyntaxHighlighting(vars.keySet()));
        this.output = new TerminalOutput(new Dimension(380, 450));

        this.scrOutput = new JScrollPane(output);
        this.scrOutput.setBorder(BorderFactory.createEmptyBorder());

        this.input.addKeyListener(this);
        this.output.setFontSize(12);

        output.appendColoredString(
            "<dark_green>Skriv <dark_blue>!list</dark_blue> för att se alla konstanter</dark_green>\n"
        );
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
     * Setup for all the constants.
     *
     * @param manager the manager that's the source of
     *                the data.
     * */
    private void setUpConstants(GroupManager manager)
    {
        vars.put("members", (double) manager.getMemberCount());
        vars.put("leaders", (double) manager.getMemberCountOfRole(Person.Role.LEADER));
        vars.put("candidates", (double) manager.getMemberCountOfRole(Person.Role.CANDIDATE));
        vars.put("mgOne", (double) manager.getMembersOfMainGroup(Person.MainGroup.MAIN_GROUP_1));
        vars.put("mgTwo", (double) manager.getMembersOfMainGroup(Person.MainGroup.MAIN_GROUP_2));
    }

    /**
     * Processes a command.
     * */
    private void processCommand()
    {
        var cmd = input.getText().substring(1);

        if (cmd.equals("list"))
        {
            var arrow = "<dark_blue> => </dark_blue>";
            var sb = new StringBuilder();

            sb.append("<dark_green>members").append(arrow)
              .append("Antalet medlämmar i denna grupp\n")
              .append("leaders").append(arrow)
              .append("Antalet ledare i denna grupp\n")
              .append("candidates").append(arrow)
              .append("Antalet deltagare i denna grupp\n")
              .append("mgOne").append(arrow)
              .append("Antalet medlämmar i huvudgrupp 1\n")
              .append("mgTwo").append(arrow)
              .append("Antalet medlämmar i huvudgrupp 2\n</dark_green>");

            output.appendColoredString(sb.toString());
        }
        else
        {
            output.appendColoredString(
                "<light_red>Kommandot: %s finns inte.\nEndast kommandot list är tillåtet!</light_red>".formatted(cmd)
            );
            DebugMethods.log("Command not found: %s".formatted(cmd), DebugMethods.LogType.ERROR);
        }
    }

    /**
     * Calculates the entered expression.
     * */
    private void calculate()
    {
        if (input.getText().trim().length() == 0)
            return;

        history.add(input.getText());

        if (input.getText().charAt(0) == '!')
        {
            processCommand();
            return;
        }

        var parser = new Parser(input.getText());
        var res = parser.parse();

        if (parser.getDiagnostics().size() == 0)
        {
            var val = res.getValue(this);

            if (lastVarCallValid)
            {
                output.appendColoredString("<green>%f</green>".formatted(val));
            }
            else
            {
                output.appendColoredString(
                    "<light_red>Konstanten: %s hittades inte!<light_red>".formatted(lastConstantError)
                );

                DebugMethods.log(
                    "Constant %s wasn't found!".formatted(lastConstantError),
                    DebugMethods.LogType.ERROR
                );
            }
        }
        else
        {
            var errors = parser.getDiagnostics().mkString("\n");
            DebugMethods.log(errors, DebugMethods.LogType.ERROR);
            output.appendColoredString("<light_red>%s</light_red>".formatted(errors));
        }

        input.clear();
        history.reset();
    }

    /**
     * Swaps the input in the text box going through
     * the history. If there are no valid entry then
     * we do nothing.
     *
     * @param isGoingUp if {@code true} then we go backwards in
     *                  history, else we go forwards in history.
     * */
    private void swapInput(boolean isGoingUp)
    {
        if (isGoingUp)
        {
            history.peek().ifPresent(input::setText);
            history.forward();
        }
        else
        {
            history.backward().ifPresentOrElse(input::setText, input::clear);
        }

        ((TerminalInput) input).syntaxHighlighting();
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
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            swapInput(false);
        else if (e.getKeyCode() == KeyEvent.VK_UP)
            swapInput(true);
        else
            e.consume();
    }

    @Override
    public double getValue(String key)
    {
        lastVarCallValid = vars.containsKey(key);

        if (!lastVarCallValid)
            lastConstantError = key;

        return vars.getOrDefault(key, 0D);
    }

    @Override
    public void registerValue(String key, double value)
    {
        vars.put(key, value);
    }
}
