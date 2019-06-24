package hr.fer.zemris.java.hw17.shell.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Util {

	private Util() {}
	
	/**
	 * Parses the given arguments and returns them as list of String objects.
	 * Given string is splitted by spaces but if argument is inside quotation marks
	 * it will not be separated.
	 * 
	 * @param arg
	 *        String representation of arguments
	 * @return parsed arguments as list of String objects
	 * @throws NullPointerException if the given String is {@code null}
	 */
	public static List<String> getArgs(String arg) {
		Objects.requireNonNull(arg, "Given string argument cannot be null!");
		List<String> result = new LinkedList<>();
		String[] parts = arg.split("\\s+");
		for (int i = 0; i < parts.length && !parts[0].isBlank(); ++i) {
			result.add(parts[i]);
		}
		return result;
	}
	
}
