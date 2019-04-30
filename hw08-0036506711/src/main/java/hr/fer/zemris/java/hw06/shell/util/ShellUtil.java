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
	
	/**
	 * Returns byte array as the result of the conversion of the given string.
	 * Each 2 characters in the given string should represent one byte.
	 * 
	 * @param keyText
	 *        given string to be converted
	 * @return byte array as the result of the conversion of the given string
	 * @throws IllegalArgumentException if the given string cannot be converted to
	 *         byte array. This will thow if the size of the given string is not
	 *         even or if illegal characters are used (ones that do not represent
	 *         hexadecimal digits)
	 * @throws NullPointerException if the given string is {@code null}
	 */
	public static byte[] hextobyte(String keyText) {
		Objects.requireNonNull(keyText, "Given text cannot be null!");
		if (keyText.length() % 2 == 1) {
			throw new IllegalArgumentException("Given text has to have even size!");
		}
		if ((keyText = keyText.trim()).indexOf(" ") != -1) {
			throw new IllegalArgumentException("Spaces are not allowed in keys!");
		}
		byte[] result = new byte[keyText.length() / 2];
		String[] parts = keyText.toUpperCase().split("(?<=\\G..)");
		for (int i = 0; i < result.length; ++i) {
			try {
				result[i] = (byte)(Integer.parseInt(parts[i], 16) & 0xff);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid characters used: " + parts[i]);
			}
		}
		return result;
	}
	
	/**
	 * Converts the given byte array into sequence of hexadecimal digits represented
	 * as string object which is then returned. Each byte will be converted into 2
	 * digits so the result String will have a length of {@code bytes.length * 2}.
	 * 
	 * @param bytes
	 *        byte array to be converted
	 * @return String representation of a result of a conversion
	 * @throws NullPointerException if the given array is {@code null}
	 */
	public static String byteToHex(byte[] bytes) {
		Objects.requireNonNull(bytes, "Given array cannot be null!");
		StringBuilder result = new StringBuilder();
		for (byte b: bytes) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
	}
	
}
