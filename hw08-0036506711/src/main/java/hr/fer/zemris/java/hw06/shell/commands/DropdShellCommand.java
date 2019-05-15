package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Implementation of interface {@code ShellCommand}.<br>
 * Removes the directory on top of the internal stack. Current directory stays the same.<br>
 * Takes no arguments.<br>
 * 
 * @author Filip Husnjak
 */
public class DropdShellCommand implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String NAME = "dropd";
	
	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the given Environment or String object is {@code null}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment cannot be null!");
		Objects.requireNonNull(arguments, "Given arguments cannot be null!");
		if (!arguments.strip().isBlank()) {
			env.writeln("Dropd command expects no arguments!");
			return ShellStatus.CONTINUE;
		}
		Deque<Path> paths = (Deque<Path>) env.getSharedData("cdstack");
		if (paths == null || paths.isEmpty()) {
			env.writeln("Internal stack is empty!");
			return ShellStatus.CONTINUE;
		}
		paths.pop();
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
				"| Usage: dropd",
				"| Removes the directory on top of the internal stack. "
				+ "Current directory stays the same.",
				"| Takes no arguments.\n"));
	}

}
