package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
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
 * Implementation of interface {@code ShellCommand}.<br>
 * Opens given file and writes its content to console.<br>
 * Takes one or two arguments.<br>
 * The first argument is path to some file and is mandatory.<br>
 * The second argument is charset name that should be used to interpret chars from bytes.<br>
 * If not provided default charset of this JVM will be used.<br>
 * 
 * @author Filip Husnjak
 */
public class CatShellCommand implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String NAME = "cat";
	
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
		if (list.size() < 1 || list.size() > 2) {
			env.writeln("Cat command expects 1 or 2 arguments!");
			return ShellStatus.CONTINUE;
		}
		try (InputStreamReader is = new InputStreamReader(Files
				.newInputStream(Paths.get(list.get(0))), list.size() == 2 ? 
						Charset.forName(list.get(1)) : Charset.defaultCharset())){
			char[] buffer = new char[1024];
			while (true) {
				int n = is.read(buffer);
				if (n <= 0) break;
				env.write(new String(buffer, 0, n));
			}
			env.writeln("");
		} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
			env.writeln("Given charset name does not exist: " + list.get(1));
		} catch (IOException e) {
			env.writeln("Error while reading given file. Either it does not exist or it cannot be opened!");
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
				"| Usage: cat <filepath> <charset>",
				"| Opens given file and writes its content to console.",
				"| Takes one or two arguments.",
				"| The first argument is path to some file and is mandatory.",
				"| The second argument is charset name that should be used to interpret chars from bytes.",
				"| If not provided default charset of this JVM will be used.\n"));
	}

}
