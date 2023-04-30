package se.skorup.main.gui.command;

public class ErrorCommand implements Command
{
    @Override
    public CommandResult execute(CommandEnvironment env)
    {
        return new CommandResult("", false);
    }

    @Override
    public String getDescription()
    {
        return "";
    }
}
