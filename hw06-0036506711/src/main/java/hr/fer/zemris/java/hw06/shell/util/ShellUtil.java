package hr.fer.zemris.java.hw06.shell.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Helper class that provides some useful methods for ShellCommands.
 * 
 * @author Filip Husnjak
 */
public class ShellUtil {
	
	/**
	 * Prevents users from creating instances of this class
	 */
	private ShellUtil() {}

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
		List<String> result = new ArrayList<>();
		if (!(arg = arg.trim()).startsWith("\"")) {
			String[] parts = arg.split("\\s+");
			if (parts.length < 1 || parts[0].isBlank()) {
				return result;
			}
			result.add(parts[0]);
			result.addAll(getArgs(arg.replaceFirst("^" + Pattern.quote(parts[0]), "").trim()));
			return result;
		}
		char[] data = arg.replaceFirst("^\"", "").trim().toCharArray();
		int index; StringBuilder sb = new StringBuilder();
		for (index = 0; index < data.length && data[index] != '"'; ++index) {
			if (data[index] == '\\' && index < data.length - 1 && (data[index + 1] == '\"' || data[index + 1] == '\\')) index++;
			sb.append(data[index]);
		}
		if (index == data.length) {
			throw new IllegalArgumentException("Double quotes not closed!");
		}
		if (index < data.length - 1 && data[index + 1] != ' ') {
			throw new IllegalArgumentException("Quoted strings have to be separated by spaces!");
		}
		result.add(sb.toString());
		if (index == data.length - 1) return result;
		result.addAll(getArgs(arg.substring(index + 2).trim()));
		return result;
	}
	
}
