package se.skorup.main.gui.command;

/**
 * The result of a command execution.
 *
 * @param result the result of the command.
 * @param isSuccessful {@code true} iff the command was
 *                     successfully executed, else it
 *                     will return {@code false}.
 * */
public record CommandResult(String result, boolean isSuccessful) {}
