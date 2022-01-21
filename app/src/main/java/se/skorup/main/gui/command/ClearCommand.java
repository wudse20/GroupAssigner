package se.skorup.main.gui.command;

import se.skorup.main.gui.components.TerminalPane;

/**
 * The command used to clear a terminal pane.
 *
 * @param p the terminal pane to be cleared;
 * */
public record ClearCommand(TerminalPane p) implements Command
{
    @Override
    public CommandResult execute(CommandEnvironment cmdEnv)
    {
        p.clear();
        return new CommandResult("", true);
    }

    @Override
    public String getDescription()
    {
        return "Clears the <white>output</white> of the screen.";
    }
}
