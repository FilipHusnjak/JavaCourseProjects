package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

/**
 * Implementation of interface {@code ShellCommand}.<br>
 * Changes current directory to the specified one.<br>
 * Takes one argument that represents new current directory.<br>
 * 
 * @author Filip Husnjak
 */
public class CdShellCommand implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String NAME = "cd";
	
	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the given Environment or String object are {@code null}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment cannot be null!");
		Objects.requireNonNull(arguments, "Given arguments cannot be null!");
		List<String> list;
		try {
			list = ShellUtil.getArgs(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (list.size() != 1) {
			env.writeln("Command cd expects 1 argument!");
			return ShellStatus.CONTINUE;
		}
		try {
			env.setCurrentDirectory(env.getCurrentDirectory().resolve(list.get(0)));
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
		}
		return ShellStatus.CONTINUE;
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
				"| Usage: cd <dir>",
				"| Changes current directory.",
				"| Takes one argument that represents new current directory\n"));
	}

}
