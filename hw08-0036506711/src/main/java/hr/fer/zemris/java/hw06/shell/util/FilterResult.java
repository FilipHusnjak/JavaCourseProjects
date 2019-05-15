package hr.fer.zemris.java.hw06.shell.util;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * Represents the result of of the filtering.<br> 
 * It has two parameters: 
 * <ul>
 * <li> {@code Matcher} m - used to return group count and each matched group
 * <li> {@code Path} selectedFile - used to return which file name is being matched
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class FilterResult {

	/**
	 * File whose name is matched
	 */
	private final Path selectedFile;
	
	/**
	 * Matcher used for grouping
	 */
	private final Matcher m;

	/**
	 * Constructs new {@code FilterResult} object with given file and matcher.
	 * 
	 * @param selectedFile
	 *        file which is matched
	 * @param m
	 *        matcher used for grouping
	 * @throws NullPointerException if the given file or matcher are {@code null}
	 */
	public FilterResult(Path selectedFile, Matcher m) {
		this.selectedFile = Objects.requireNonNull(selectedFile, "Given file cannot be null!");
		this.m = Objects.requireNonNull(m, "Given matcher cannot be null!");
		m.matches();
	}
	
	/**
	 * Returns number of groups of the used matcher.
	 * 
	 * @return number of groups of the used matcher
	 */
	public int numberOfGroups() {
		return m.groupCount();
	}
	
	/**
	 * Returns the input subsequence captured by the given group.
	 * If {@code ZERO} is passed as an argument, whole matched sequence is returned.
	 * 
	 * @param index
	 *        which group is to be returned
	 * @return the input subsequence captured by the given group
	 * @throws IndexOutOfBoundsException if the given group is less than {@code ZERO} or greater
	 *         than numberOfGroups
	 */
	public String group(int index) {
		Objects.checkIndex(index, numberOfGroups() + 1);
		return m.group(index);
	}
	
	/**
	 * {@inheritDoc}
	 * Returns filename that is being matched.
	 */
	@Override
	public String toString() {
		return selectedFile.getFileName().toString();
	}
	
}
