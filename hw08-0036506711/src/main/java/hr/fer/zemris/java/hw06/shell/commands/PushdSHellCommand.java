package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

public class PushdSHellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "pushd";

	@SuppressWarnings("unchecked")
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
			env.writeln("Command pushd expects one argument!");
			return ShellStatus.CONTINUE;
		}
		Path currentDir = env.getCurrentDirectory();
		try {
			env.setCurrentDirectory(currentDir.resolve(list.get(0)));
			Deque<Path> paths = (Deque<Path>) env.getSharedData("cdstack");
			if (paths == null) {
				env.setSharedData("cdstack", (paths = new ArrayDeque<>()));
			}
			paths.push(currentDir);
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
		return Collections.unmodifiableList(Arrays.asList(
				"| Usage: pushd <dir>",
				"| Pushes the current directory onto the internal stack and changes current directory to the given one.",
				"| Takes single argument which represents directory path.\n"));
	}

}
