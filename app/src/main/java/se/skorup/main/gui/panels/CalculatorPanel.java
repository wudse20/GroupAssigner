package se.skorup.main.gui.panels;

import se.skorup.API.collections.mutable_collections.HistoryList;
import se.skorup.API.collections.mutable_collections.HistoryStructure;
import se.skorup.API.expression_evalutator.Environment;
import se.skorup.API.expression_evalutator.parser.Parser;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.command.ClearCommand;
import se.skorup.main.gui.command.Command;
import se.skorup.main.gui.command.CommandEnvironment;
import se.skorup.main.gui.command.CommandResult;
import se.skorup.main.gui.command.ErrorCommand;
import se.skorup.main.gui.command.HelpCommand;
import se.skorup.main.gui.command.ListCommand;
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
import java.util.List;
import java.util.Map;

/**
 * The panel that holds the calculator.
 * */
public class CalculatorPanel extends JPanel implements KeyListener, Environment, CommandEnvironment
{
    /** The keycode for the enter key. */
    private static final int ENTER = 10;

    private boolean lastVarCallValid = true;
    private String lastConstantError = "";

    private final Map<String, Double> vars;
    private final Map<String, Command> cmds;

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
        this.cmds = new HashMap<>();
        this.history = new HistoryList<>();

        this.setProperties();
        this.addComponents();
        this.setUpConstants(manager);
        this.setUpCommands();
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
        this.output.setFontSize(14);

        output.appendColoredString(
            "<dark_green>Skriv <dark_blue>!help</dark_blue> för att få hjälp.</dark_green>\n"
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
     * Setup for all the commands.
     * */
    private void setUpCommands()
    {
        cmds.put("help", new HelpCommand());
        cmds.put("list", new ListCommand());
        cmds.put("clear", new ClearCommand(output));
    }

    /**
     * Processes a command.
     *
     * @param cmd the command to be processed.
     * */
    private void processCommand(String cmd)
    {
        var res = executeCommand(cmd);

        if (res.isSuccessful())
        {
            output.appendColoredString(res.result());
        }
        else
        {
            output.appendColoredString(res.result());
            output.appendColoredString(
                "<green>Skriv <white>'<dark_blue>!help</dark_blue>'</white> för att lista alla kommandon.</green>"
            );
        }
    }

    /**
     * Calculates the entered expression.
     * */
    private void calculate()
    {
        var userInput = input.getText();

        if (userInput.trim().length() == 0)
            return;

        history.add(userInput);

        if (userInput.charAt(0) == '!')
        {
            processCommand(userInput.substring(1));
            input.setText("");
            return;
        }

        var parser = new Parser(userInput);
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

    @Override
    public void registerCommand(String name, Command cmd)
    {
        cmds.put(name, cmd);
    }

    @Override
    public CommandResult executeCommand(String name)
    {
        return cmds.getOrDefault(name, new ErrorCommand()).execute(this);
    }

    @Override
    public List<String> getCommands()
    {
        return cmds.entrySet()
                   .stream()
                   .map(e ->
                       "<blue>!%s<white> => </white><green>%s</green></blue>".formatted(
                           e.getKey(), e.getValue().getDescription()
                       )
                   ).toList();
    }
}
