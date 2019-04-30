package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

public class PwdShellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "pwd";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment cannot be null!");
		Objects.requireNonNull(arguments, "Given arguments cannot be null!");
		if (!arguments.strip().isBlank()) {
			env.writeln("Pwd command expects no arguments!");
			return ShellStatus.CONTINUE;
		}
		env.writeln(env.getCurrentDirectory().toString());
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(Arrays.asList(
				"| Usage: pwd",
				"| Writes current directory.",
				"| Expects no arguments.\n"));
	}

}
