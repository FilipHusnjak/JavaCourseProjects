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
 * Writes all paths currently stored on the internal stack.<br>
 * Takes no arguments.<br>
 * 
 * @author Filip Husnjak
 */
public class ListdShellCommand implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String NAME = "listd";
	
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
			env.writeln("Listd command expects no arguments!");
			return ShellStatus.CONTINUE;
		}
		Deque<Path> paths = (Deque<Path>) env.getSharedData("cdstack");
		if (paths == null || paths.isEmpty()) {
			env.writeln("There are no saved directories!");
			return ShellStatus.CONTINUE;
		}
		paths.stream().map(Path::toString).forEach(env::writeln);
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
				"| Usage: listd",
				"| Writes all paths currently stored on the internal stack.",
				"| Takes no arguments.\n"));
	}

}
