package hr.fer.zemris.java.hw06.crypto;

import java.util.Objects;

/**
 * Helper class that provides useful static methods for conversions from hexadecimal
 * numbers to byte array and vice versa.
 * 
 * @author Filip Husnjak
 */
public class Util {
	
	/**
	 * Prevents users from creating instances of this class
	 */
	private Util() {}

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
