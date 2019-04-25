package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Lists names of supported charsets for this Java platform.<br>
 * Takes no arguments.<br>
 * A single charset name is written per line.<br>
 * 
 * @author Filip Husnjak
 */
public class CharsetsShellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "charsets";

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the given Environment or String object is {@code null}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment cannot be null!");
		Objects.requireNonNull(arguments, "Given arguments cannot be null!");
		if (arguments.isEmpty()) {
			Charset.availableCharsets().entrySet().stream()
					.map(Map.Entry::getValue).map(Charset::toString).forEach(env::writeln);
		} else {
			env.writeln("Charsets command takes no arguments!");
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
				"| Usage: charsets",
				"| Lists names of supported charsets for this Java platform.",
				"| Takes no arguments.",
				"| A single charset name is written per line.\n"));
	}

}
