package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Terminates the shell.<br>
 * Expects no arguments.<br>
 * 
 * @author Filip Husnjak
 */
public class ExitShellCommand implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String NAME = "exit";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return ShellStatus.TERMINATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCommandName() {
		return NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(Arrays.asList(
				"| Usage: exit",
				"| Terminates the shell.",
				"| Expects no arguments."));
	}

}
