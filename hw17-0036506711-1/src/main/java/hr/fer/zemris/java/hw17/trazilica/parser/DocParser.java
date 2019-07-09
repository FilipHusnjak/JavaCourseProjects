package hr.fer.zemris.java.hw17.trazilica.parser;

/**
 * Simple parser used to parse documents. All non alphabetic characters are ignored.
 * 
 * @author Filip Husnjak
 */
public class DocParser {
	
	/** Current index in data array */
	private int currentIndex;
	
	/** Data array which is parsed */
	private char[] data;

	/**
	 * Constructs new doc parser with the given data to be parsed.
	 * 
	 * @param data
	 *        data to be parsed
	 */
	public DocParser(String data) {
		this.data = data.toCharArray();
	}
	
	/**
	 * Extracts and returns next word.
	 * 
	 * @return next word or null if there are no more words
	 */
	public String extract() {
		ignoreIrrelevantChars();
		if (currentIndex >= data.length) {
			return null;
		}
		return extractNext();
	}

	/**
	 * Increments currentIndex until it points to the relevant character
	 */
	private void ignoreIrrelevantChars() {
		for (; currentIndex < data.length && 
				!Character.isAlphabetic(data[currentIndex]); currentIndex++);
	}

	/**
	 * Extracts and returns next word.
	 * 
	 * @return next word
	 */
	private String extractNext() {
		StringBuilder sb = new StringBuilder();
		while (currentIndex < data.length && Character.isAlphabetic(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		}
		return sb.toString();
	}
	
}
