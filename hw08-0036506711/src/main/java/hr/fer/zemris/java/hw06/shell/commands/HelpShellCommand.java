package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

/**
 * Lists all possible commands or writes more information for specified command.<br>
 * Expects one or no arguments.<br>
 * If no arguments were given list of possible commands will be written.<br>
 * If one argument is provided it will print name and description of selected command.<br>
 * 
 * @author Filip Husnjak
 */
public class HelpShellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "help";

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the given Environment or String object is {@code null}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment cannot be null!");
		Objects.requireNonNull(arguments, "Given arguments cannot be null!");
		if (arguments.isBlank()) {
			env.writeln("List of commands:");
			env.commands().entrySet().stream().map(Map.Entry::getKey).forEach(env::writeln);
		} else {
			List<String> list;
			try {
				list = ShellUtil.getArgs(arguments);
			} catch (IllegalArgumentException e) {
				env.writeln(e.getMessage());
				return ShellStatus.CONTINUE;
			}
			if (list.size() != 1) {
				env.writeln("Help command expects 1 argument");
				return ShellStatus.CONTINUE;
			}
			env.writeln("Help for command " + list.get(0) + ":\n");
			if (env.commands().get(list.get(0)) == null) {
				env.writeln("Given command does not exist!");
			} else {
				env.commands().get(list.get(0)).getCommandDescription().forEach(env::writeln);
			}
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
				"| Usage: help <command name>",
				"| Lists all possible commands or writes more information for specified command.",
				"| Expects one or no arguments.",
				"| If no arguments were given list of possible commands will be written.",
				"| If one argument is provided it will print name and description of selected command.\n"));
	}

}
