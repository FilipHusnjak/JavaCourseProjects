package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

/**
 * Creates the appropriate directory structure. All required parent directories are created.<br>
 * Takes single argument which represents directory name.
 * 
 * @author Filip Husnjak
 */
public class MkdirShellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "mkdir";

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the given Environment or String object is {@code null}
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
			env.writeln("Mkdir command expects 1 argument!");
			return ShellStatus.CONTINUE;
		}
		try {
			Path dir = Paths.get(list.get(0));
			if (Files.isDirectory(dir)) {
				env.writeln("Directory " + dir.getFileName() + " already exists!");
			} else {
				Files.createDirectories(dir);
				env.writeln("Directory created!");
			}
		} catch (FileAlreadyExistsException e) {
			env.writeln("File with the same name already exists!");
		} catch (IOException e) {
			env.writeln("Error while reading given file!");
		} catch (InvalidPathException e) {
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
				"| Usage: mkdir <dir>",
				"| Creates the appropriate directory structure. All required parent directories are created.",
				"| Takes single argument which represents directory name.\n"));
	}

}
