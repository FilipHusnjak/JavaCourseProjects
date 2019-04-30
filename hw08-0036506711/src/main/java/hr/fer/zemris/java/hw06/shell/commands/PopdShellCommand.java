package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

public class PopdShellCommand implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String NAME = "popd";
	
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment cannot be null!");
		Objects.requireNonNull(arguments, "Given arguments cannot be null!");
		if (!arguments.strip().isBlank()) {
			env.writeln("Popd command expects no arguments!");
			return ShellStatus.CONTINUE;
		}
		Deque<Path> paths = (Deque<Path>) env.getSharedData("cdstack");
		if (paths == null || paths.isEmpty()) {
			env.writeln("Internal stack is empty!");
			return ShellStatus.CONTINUE;
		}
		try {
			env.setCurrentDirectory(paths.pop());
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
