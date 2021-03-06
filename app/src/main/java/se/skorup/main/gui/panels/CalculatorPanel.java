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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The panel that holds the calculator.
 * */
public class CalculatorPanel extends JPanel implements Environment, CommandEnvironment, DocumentListener, KeyListener
{
    /** The button border. */
    private static final CompoundBorder BUTTON_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
        BorderFactory.createLineBorder(Utils.COMPONENT_BACKGROUND_COLOR, 4)
    );

    /** The font for the buttons. */
    private static final Font BUTTON_FONT = new Font(Font.DIALOG, Font.BOLD, 16);

    private boolean lastVarCallValid = true;
    private String lastConstantError = "";

    private final Map<String, Number> vars;
    private final Map<String, Command> cmds;

    private final HistoryStructure<String> history = new HistoryList<>();

    private final CalculatorButtonPanel cbp = new CalculatorButtonPanel();
    private final CalculatorConstantPanel ccp;
    private final JPanel ctrButtons = new JPanel();

    private final JButton btnDel = new JButton("Delete");
    private final JButton btnCalc = new JButton("Ber??kna");
    private final JButton btnNewConst = new JButton("Ny konstant");
    private final JButton btnUp = new JButton("<html>&uarr;</html>");
    private final JButton btnDown = new JButton("<html>&darr;</html>");
    private final JButton btnClear = new JButton("Rensa");

    private final TerminalInput input;
    private final TerminalPane output;

    private final JScrollPane scrCCP;
    private final JScrollPane scrOutput;

    /**
     * Creates a new Calculator panel.
     *
     * @param manager the group manager in use.
     * */
    public CalculatorPanel(GroupManager manager)
    {
        this.vars = new HashMap<>();
        this.cmds = new HashMap<>();
        this.ccp = new CalculatorConstantPanel(vars.keySet());
        this.input = new TerminalInput('!', new ExpressionSyntaxHighlighting(vars.keySet()));
        this.output = new TerminalOutput(new Dimension(200, 300));
        this.scrCCP = new JScrollPane(ccp);
        this.scrOutput = new JScrollPane(output);

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

        scrCCP.setBorder(BorderFactory.createEmptyBorder());
        scrCCP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        scrOutput.setBorder(BorderFactory.createEmptyBorder());

        ccp.addCallback(s -> {
            var data = "%s %s ".formatted(input.getText(), s);
            input.setText(data);
            input.syntaxHighlighting();
            cbp.setData(data);
        });

        cbp.addActionCallback(() -> {
            input.setText(cbp.getData());
            input.syntaxHighlighting();
        });

        input.addKeyListener(this);
        input.getDocument().addDocumentListener(this);

        ctrButtons.setBackground(Utils.BACKGROUND_COLOR);
        ctrButtons.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnDel.setForeground(Utils.FOREGROUND_COLOR);
        btnDel.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnDel.setFont(BUTTON_FONT);
        btnDel.setBorder(BUTTON_BORDER);

        btnDel.addActionListener(e -> {
            if (cbp.getData().trim().isEmpty())
                return;

            DebugMethods.log("Before: %s".formatted(cbp.getData()), DebugMethods.LogType.DEBUG);
            var newData = cbp.getData().substring(0, cbp.getData().length() - 1);
            DebugMethods.log("After: %s".formatted(newData), DebugMethods.LogType.DEBUG);
            cbp.setData(newData);
            input.setText(newData);
        });

        btnCalc.setForeground(Utils.FOREGROUND_COLOR);
        btnCalc.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCalc.setFont(BUTTON_FONT);
        btnCalc.addActionListener(e -> doCalculation(input.getText()));
        btnCalc.setBorder(BUTTON_BORDER);

        btnNewConst.setForeground(Utils.FOREGROUND_COLOR);
        btnNewConst.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnNewConst.setFont(BUTTON_FONT);
        btnNewConst.setBorder(BUTTON_BORDER);

        btnNewConst.addActionListener(e -> {
            var identifier = JOptionPane.showInputDialog(
                this, "Vad ska konstanten heta?",
                "Konstantens namn", JOptionPane.INFORMATION_MESSAGE
            );

            if (identifier == null || identifier.trim().isEmpty())
                return;

            var data = "let %s = ".formatted(identifier);
            input.setText(data);
            input.syntaxHighlighting();
            cbp.setData(data);
        });

        btnUp.setForeground(Utils.FOREGROUND_COLOR);
        btnUp.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnUp.setFont(BUTTON_FONT);
        btnUp.setBorder(BUTTON_BORDER);
        btnUp.addActionListener(e -> swapInput(true));

        btnDown.setForeground(Utils.FOREGROUND_COLOR);
        btnDown.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnDown.setFont(BUTTON_FONT);
        btnDown.setBorder(BUTTON_BORDER);
        btnDown.addActionListener(e -> swapInput(false));

        btnClear.setForeground(Utils.FOREGROUND_COLOR);
        btnClear.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnClear.setFont(BUTTON_FONT);
        btnClear.setBorder(BUTTON_BORDER);
        btnClear.addActionListener(e -> executeCommand("clear"));

        output.setBackground(Utils.BACKGROUND_COLOR);
        output.setFontSize(14);
        output.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        var cont = new JPanel();
        cont.setBackground(Utils.BACKGROUND_COLOR);
        cont.setLayout(new BorderLayout());

        var buttons = new JPanel();
        buttons.setBackground(Utils.BACKGROUND_COLOR);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

        var cont2 = new JPanel();
        cont2.setBackground(Utils.BACKGROUND_COLOR);
        cont2.setLayout(new FlowLayout(FlowLayout.LEFT));

        ctrButtons.add(btnCalc);
        ctrButtons.add(btnDel);
        ctrButtons.add(btnClear);
        ctrButtons.add(new JLabel(" "));
        ctrButtons.add(btnNewConst);
        ctrButtons.add(new JLabel(" "));
        ctrButtons.add(btnUp);
        ctrButtons.add(btnDown);

        buttons.add(input);
        buttons.add(new JLabel(" "));
        buttons.add(ctrButtons);
        buttons.add(cbp);

        cont.add(scrCCP, BorderLayout.PAGE_START);
        cont.add(scrOutput, BorderLayout.CENTER);
        cont.add(buttons, BorderLayout.PAGE_END);

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
        vars.put("medl??mmar", manager.getMemberCount());
        vars.put("ledare", manager.getMemberCountOfRole(Person.Role.LEADER));
        vars.put("deltagare", manager.getMemberCountOfRole(Person.Role.CANDIDATE));
        vars.put("huvudgrupp1", manager.getMembersOfMainGroup(Person.MainGroup.MAIN_GROUP_1));
        vars.put("huvudgrupp2", manager.getMembersOfMainGroup(Person.MainGroup.MAIN_GROUP_2));

        updateConstantButtons();
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
     * Does a calculation based on the input text.
     *
     * @param text the input to the code.
     * */
    private void doCalculation(String text)
    {
        if (text.trim().length() > 0)
            history.add(text);
        else
            return;

        if (text.charAt(0) == '!')
        {
            var cmd = executeCommand(text.substring(1));

            if (cmd.isSuccessful())
            {
                input.setText("");

                if (!
                        cmd.result().trim().isEmpty())
                    output.appendColoredString(cmd.result());

                cbp.resetData();
            }
            else
            {
                output.appendColoredString("<IDEA_PURPLE>%s</IDEA_PURPLE>".formatted(text));
                output.appendColoredString("<RED>Error executing command: <WHITE>%s</WHITE></RED>".formatted(text.substring(1)));
                output.appendColoredString(cmd.result());
                output.appendColoredString("");
            }


            return;
        }

        var p = new Parser(text);
        var expr = p.parse();

        if (p.getDiagnostics().size() == 0)
        {
            var res = expr.getValue(this);
            var highlighted = new ExpressionSyntaxHighlighting(vars.keySet()).syntaxHighlight(expr.toString());
            output.appendColoredString(highlighted);

            if (!lastVarCallValid)
            {
                output.appendColoredString("<RED>The constant: %s doesn't exist</RED>%n".formatted(lastConstantError));
                lastVarCallValid = true;
                return;
            }

            output.appendColoredString("<LIGHT_GREEN>%s</LIGHT_GREEN>%n".formatted(res));
            input.setText("");
            cbp.resetData();
        }
        else
        {
            var highlighted = new ExpressionSyntaxHighlighting(vars.keySet()).syntaxHighlight(input.getText());
            output.appendColoredString(highlighted);
            output.appendColoredString("<RED>%s</RED>%n".formatted(p.getDiagnostics().mkString("\n")));
        }
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
    }

    /**
     * Updates the constant buttons.
     * */
    public void updateConstantButtons()
    {
        ccp.setUpButtons(vars.keySet());
    }

    @Override
    public Number getValue(String key)
    {
        lastVarCallValid = vars.containsKey(key);

        if (!lastVarCallValid)
            lastConstantError = key;

        return vars.getOrDefault(key, 0);
    }

    @Override
    public void registerValue(String key, Number value)
    {
        vars.put(key, value);
        updateConstantButtons();
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

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e)
    {
        final var ENTER = 10;
        final var ARROW_DOWN = 40;
        final var ARROW_UP = 38;

        switch (e.getKeyCode())
        {
            case ENTER -> doCalculation(input.getText());
            case ARROW_DOWN -> swapInput(false);
            case ARROW_UP -> swapInput(true);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        cbp.setData(input.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        cbp.setData(input.getText());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {}
}
