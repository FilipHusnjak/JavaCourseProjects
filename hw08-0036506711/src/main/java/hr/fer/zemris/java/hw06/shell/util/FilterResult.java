package hr.fer.zemris.java.hw06.shell.util;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;

public class FilterResult {

	private final Path selectedFile;
	
	private final Matcher m;

	public FilterResult(Path selectedFile, Matcher m) {
		this.selectedFile = Objects.requireNonNull(selectedFile, "Given file cannot be null!");
		this.m = Objects.requireNonNull(m, "Given matcher cannot be null!");
		m.matches();
	}
	
	public int numberOfGroups() {
		return m.groupCount();
	}
	
	public String group(int index) {
		Objects.checkIndex(index, numberOfGroups() + 1);
		return m.group(index);
	}
	
	@Override
	public String toString() {
		return selectedFile.getFileName().toString();
	}
	
}
