package se.skorup.main.gui.panels;

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
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The panel that holds the calculator.
 * */
public class CalculatorPanel extends JPanel implements Environment, CommandEnvironment
{
    /** The keycode for the enter key. */
    private static final int ENTER = 10;

    private boolean lastVarCallValid = true;
    private String lastConstantError = "";

    private final Map<String, Double> vars;
    private final Map<String, Command> cmds;

    private final CalculatorButtonPanel cbp = new CalculatorButtonPanel();
    private final CalculatorIOPanel ciop;
    private final CalculatorConstantPanel ccp;
    private final JPanel ctrButtons = new JPanel();

    private final JButton btnDel = new JButton("Delete");
    private final JButton btnCalc = new JButton("Beräkna");
    private final JButton btnNewConst = new JButton("Ny konstant");
    private final JButton btnUp = new JButton("<html>&uarr;</html>");
    private final JButton btnDown = new JButton("<html>&darr;</html>");

    private final JScrollPane scrCCP;

    /**
     * Creates a new Calculator panel.
     *
     * @param manager the group manager in use.
     * */
    public CalculatorPanel(GroupManager manager)
    {
        this.vars = new HashMap<>();
        this.cmds = new HashMap<>();
        this.ciop = new CalculatorIOPanel(vars.keySet());
        this.ccp = new CalculatorConstantPanel(vars.keySet());
        this.scrCCP = new JScrollPane(ccp);

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

        cbp.addActionCallback(() -> ciop.setInputText(cbp.getData()));

        ctrButtons.setBackground(Utils.BACKGROUND_COLOR);
        ctrButtons.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnDel.setForeground(Utils.FOREGROUND_COLOR);
        btnDel.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnDel.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        btnDel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createLineBorder(Utils.COMPONENT_BACKGROUND_COLOR, 4)
            )
        );

        btnDel.addActionListener(e -> {
            if (cbp.getData().trim().isEmpty())
                return;

            DebugMethods.log("Before: %s".formatted(cbp.getData()), DebugMethods.LogType.DEBUG);
            var newData = cbp.getData().substring(0, cbp.getData().length() - 1);
            DebugMethods.log("After: %s".formatted(newData), DebugMethods.LogType.DEBUG);
            cbp.setData(newData);
            ciop.setInputText(newData);
        });

        btnCalc.setForeground(Utils.FOREGROUND_COLOR);
        btnCalc.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCalc.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        btnCalc.addActionListener(e -> doCalculation(ciop.getInputText()));
        btnCalc.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createLineBorder(Utils.COMPONENT_BACKGROUND_COLOR, 4)
            )
        );

        btnNewConst.setForeground(Utils.FOREGROUND_COLOR);
        btnNewConst.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnNewConst.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        btnNewConst.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createLineBorder(Utils.COMPONENT_BACKGROUND_COLOR, 4)
            )
        );

        btnNewConst.addActionListener(e -> {
            var identifier = JOptionPane.showInputDialog(
                this, "Vad ska konstanten heta?",
                "Konstantens namn", JOptionPane.INFORMATION_MESSAGE
            );

            if (identifier == null || identifier.trim().isEmpty())
                return;

            var data = "let %s = ".formatted(identifier);
            ciop.setInputText(data);
            cbp.setData(data);
        });

        btnUp.setForeground(Utils.FOREGROUND_COLOR);
        btnUp.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnUp.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        btnUp.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createLineBorder(Utils.COMPONENT_BACKGROUND_COLOR, 4)
            )
        );

        btnDown.setForeground(Utils.FOREGROUND_COLOR);
        btnDown.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnDown.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        btnDown.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
                BorderFactory.createLineBorder(Utils.COMPONENT_BACKGROUND_COLOR, 4)
            )
        );
    }

    /**
     * Does a calculation based on the input text.
     *
     * @param text the input to the code.
     * */
    private void doCalculation(String text)
    {
        if (text.length() > 0 && text.charAt(0) == '!')
        {
            executeCommand(text.substring(1));
            ciop.setInputText("");
            cbp.resetData();
            return;
        }

        var p = new Parser(text);
        var expr = p.parse();

        if (p.getDiagnostics().size() == 0)
        {
            var res = expr.getValue(this);
            var highlighted = new ExpressionSyntaxHighlighting(vars.keySet()).syntaxHighlight(expr.toString());

            ciop.appendOutputText(highlighted);
            ciop.appendOutputText("<LIGHT_GREEN>%f</LIGHT_GREEN>%n".formatted(res));
            ciop.setInputText("");
            cbp.resetData();
        }
        else
        {
            ciop.appendOutputText("<RED>%s</RED>".formatted(p.getDiagnostics().mkString("\n")));
        }
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

        cont2.add(new JLabel("   "));
        cont2.add(ciop);

        ctrButtons.add(btnCalc);
        ctrButtons.add(btnDel);
        ctrButtons.add(btnNewConst);
        ctrButtons.add(btnUp);
        ctrButtons.add(btnDown);

        buttons.add(ctrButtons);
        buttons.add(cbp);

        cont.add(scrCCP, BorderLayout.PAGE_START);
        cont.add(cont2, BorderLayout.CENTER);
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
        vars.put("medlämmar", (double) manager.getMemberCount());
        vars.put("ledare", (double) manager.getMemberCountOfRole(Person.Role.LEADER));
        vars.put("deltagare", (double) manager.getMemberCountOfRole(Person.Role.CANDIDATE));
        vars.put("huvudgrupp 1", (double) manager.getMembersOfMainGroup(Person.MainGroup.MAIN_GROUP_1));
        vars.put("huvudgrupp 2", (double) manager.getMembersOfMainGroup(Person.MainGroup.MAIN_GROUP_2));

        updateConstantButtons();
    }

    /**
     * Setup for all the commands.
     * */
    private void setUpCommands()
    {
        cmds.put("help", new HelpCommand());
        cmds.put("list", new ListCommand());
        cmds.put("clear", new ClearCommand(ciop.getOutput()));
    }

    /**
     * Updates the constant buttons.
     * */
    public void updateConstantButtons()
    {
        ccp.setUpButtons(vars.keySet());
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
}
