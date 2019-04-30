package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

/**
 * Produces hex-output formatted like this:<br>
 * [row number]: [sixteen bytes of source file as hex numbers] | [chars]<br>
 * Field [chars] will show only a standard subset of characters, others will be replaced by '.'<br>
 * Expects one argument that represents the file to be read.<br>
 * 
 * @author Filip Husnjak
 */
public class HexDumpShellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "hexdump";

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
			env.writeln("Shell dump command expects 1 argument that represents directory!");
			return ShellStatus.CONTINUE;
		}
		try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(env.getCurrentDirectory().resolve(list.get(0))))) {
			int counter = 0;
			while (true) {
				byte[] buffer = bis.readNBytes(16);
				if (buffer.length <= 0) break;
				Pattern p1 = Pattern.compile("(.{2})", Pattern.DOTALL);
				Matcher m1 = p1.matcher(ShellUtil.byteToHex(buffer).toUpperCase());
				Pattern p2 = Pattern.compile("(.{24})", Pattern.DOTALL);
				String hexPart = p2.matcher(String.format("%-48s", m1.replaceAll("$1 "))).replaceAll("$1\\|");
				char[] array = new char[buffer.length];
				for (int i = 0; i < array.length; ++i) {
					array[i] = buffer[i] < 32 || buffer[i] > 127 ? '.' : (char) buffer[i];
				}
				String charPart = new String(array);
				env.writeln(String.format("%08X: %s %s", counter, hexPart, charPart));
				counter += 16;
			}
		} catch (IOException e) {
			env.writeln("Given file cannot be opened or does not exist!");
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
				"| Usage: hexdump <file name>",
				"| Produces hex-output formatted like this:",
				"| <row number>: <sixteen bytes of source file as hex numbers> | <chars>",
				"| Field <chars> will show only a standard subset of characters, others will be replaced by '.'",
				"| Expects one argument that represents the file to be read.\n"));
	}

}
