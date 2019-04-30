package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Represents environment of the shell. Its an interface for communication 
 * with the shell that provides some useful methods.
 * 
 * @author Filip Husnjak
 */
public interface Environment {
	
	/**
	 * Reads and returns next line from InputStream this shell is reading from.
	 * 
	 * @return next line from InputStream this shell is reading from
	 * @throws ShellIOException if error occurs while reading next line
	 */
	String readLine() throws ShellIOException;

	/**
	 * Writes the given message to the console without going to the next
	 * line.
	 * 
	 * @param text
	 *        message to be written to the console
	 * @throws ShellIOException if error occurs while writing the message
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Writes the given message to the console and ends it with '\r\n' characters.
	 * 
	 * @param text
	 *        message to be written to the console
	 * @throws ShellIOException if error occurs while writing the message
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Returns an unmodifiable map of commands in this environment. Keys represent
	 * name of the command while value is some implementation of ShellCommand.
	 * 
	 * @return an unmodifiable map of commands in this environment
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Returns multiline symbol of this shell.
	 * 
	 * @return multiline symbol of this shell
	 */
	Character getMultilineSymbol();

	/**
	 * Sets multiline symbol of this shell to the given one.
	 * 
	 * @param symbol 
	 *        new multiline symbol
	 * @throws NullPointerException if the given symbol is {@code null}
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Returns prompt symbol of this shell.
	 * 
	 * @return prompt symbol of this shell
	 */
	Character getPromptSymbol();

	/**
	 * Sets prompt symbol of this shell to the given one.
	 * 
	 * @param symbol 
	 *        new prompt symbol
	 * @throws NullPointerException if the given symbol is {@code null}
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Returns more lines symbol of this shell.
	 * 
	 * @return more lines symbol of this shell
	 */
	Character getMorelinesSymbol();

	/**
	 * Sets more lines symbol of this shell to the given one.
	 * 
	 * @param symbol 
	 *        new more lines symbol
	 * @throws NullPointerException if the given symbol is {@code null}
	 */
	void setMorelinesSymbol(Character symbol);
	
	Path getCurrentDirectory();
	
	void setCurrentDirectory(Path path);
	
	Object getSharedData(String key);
	
	void setSharedData(String key, Object value);
	
}
