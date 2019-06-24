package hr.fer.zemris.java.hw17.parser;

public class DocParser {
	
	private int currentIndex;
	
	private char[] data;

	public DocParser(String data) {
		this.data = data.toCharArray();
	}
	
	public String extract() {
		ignoreIrrelevantChars();
		if (currentIndex >= data.length) {
			return null;
		}
		return extractNext();
	}

	private void ignoreIrrelevantChars() {
		for (; currentIndex < data.length && 
				!Character.isAlphabetic(data[currentIndex]); currentIndex++);
	}

	private String extractNext() {
		StringBuilder sb = new StringBuilder();
		while (currentIndex < data.length && Character.isAlphabetic(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		}
		return sb.toString();
	}
	
}
