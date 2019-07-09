package hr.fer.zemris.java.hw17.trazilica;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw17.trazilica.math.VectorN;
import hr.fer.zemris.java.hw17.trazilica.parser.DocParser;

/**
 * Represents database used to perform certain queries required by the shell.
 * 
 * @author Filip Husnjak
 */
public class ShellDatabase {

	/** Set of words in all documents */
	private static Set<String> dictionary = new LinkedHashSet<>();
	
	/** Set of stop words */
	private static Set<String> stopWords = new HashSet<>();
	
	/** Maps document to its calculated {@link VectorN} */
	private static Map<Path, VectorN> docs = new HashMap<>();
	
	/** Maps documents to the last result */
	private static Map<Path, Double> lastQuery;
	
	/** Number of documents */
	private static int docCount;
	
	/** Represents idf vector used when calculating tfidf vector of each document */
	private static VectorN idfVec;
	
	/**
	 * Loads database filling proper sets with proper data.
	 * 
	 * @param path
	 *        path of the directory which contains documents
	 * @throws IOException if an I/O error occurs
	 */
	public static void loadDatabase(Path path) throws IOException {
		fillStopWords();
		fillDictionary(path);
		fillVectors(path);
	}

	/**
	 * Fills the stopWords set from the proper file.
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	private static void fillStopWords() throws IOException {
		Path path = Paths.get("src/main/resources/stopWords.txt");
		List<String> lines = Files.readAllLines(path);
		for (String line: lines) {
			stopWords.add(line.strip().toLowerCase());
		}
	}
	
	/**
	 * Fills the dictionary set with proper words contained in each document in
	 * the given directory.
	 * 
	 * @param path
	 *        directory path which contains documents
	 * @throws IOException if an I/O error occurs
	 */
	private static void fillDictionary(Path path) throws IOException {
		docCount = 0;
		DictionaryVisitor dicVisit = new DictionaryVisitor();
		Files.walkFileTree(path, dicVisit);
	}
	
	/**
	 * Calculates tfidf vectors for each document.
	 * 
	 * @param path
	 *        directory path which contains documents
	 * @throws IOException if an I/O error occurs
	 */
	private static void fillVectors(Path path) throws IOException {
		VectorVisitor vecVisit = new VectorVisitor();
		Files.walkFileTree(path, vecVisit);
		vecVisit.idf.replaceAll((k, v) -> Math.log(docCount / v));
		idfVec = createVectorN(vecVisit.idf);
		docs.replaceAll((k, v) -> v.multiply(idfVec));
	}
	
	/**
	 * Returns map of best matches for the given array of words.
	 * 
	 * @param args
	 *        array of words upon which the query is performed
	 * @return map of best matches for the given array of words
	 */
	public static Map<Path, Double> executeQuery(String[] args) {
		Map<String, Double> tf = new HashMap<>();
		for (String word: args) {
			tf.merge(word.toLowerCase(), 1.0, Double::sum);
		}
		return findBestMatches(createVectorN(tf).multiply(idfVec));
	}
	
	/**
	 * Finds and returns best matches for the given {@link VectorN}.
	 * 
	 * @param tfidf
	 *        {@link VectorN} object used to calculate similarities
	 * @return best matches for the given {@link VectorN}
	 */
	private static Map<Path, Double> findBestMatches(VectorN tfidf) {
		Map<Path, Double> similarity = new HashMap<>();
		docs.forEach((k, v) -> {
			double rounded = round4(v.cos(tfidf));
			if (rounded > 0) {
				similarity.put(k, rounded);
			}
		});
		return lastQuery = similarity.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(10)
				.collect(Collectors.toMap(Map.Entry::getKey, 
						Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
	}
	
	/**
	 * Rounds the double value to 4 decimal places.
	 * 
	 * @param d
	 *        double value to be rounded
	 * @return rounded double value
	 */
	private static double round4(double d) {
		BigDecimal bd = new BigDecimal(d);
	    bd = bd.setScale(4, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/**
	 * Returns result at the given index or {@code null} if such does not exist.
	 * 
	 * @param index
	 *        index of the result to be returned
	 * @return result at the given index
	 */
	public static Path getEntryAt(int index) {
		if (lastQuery == null) {
			return null;
		}
		Objects.checkIndex(index, lastQuery.size());
		for (Map.Entry<Path, Double> entry: lastQuery.entrySet()) {
			if (index-- == 0) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	/**
	 * Implementation of {@link FileVisitor} which fills the dictionary map.
	 * 
	 * @author Filip Husnjak
	 */
	private static class DictionaryVisitor extends SimpleFileVisitor<Path> {
				
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			String data = Files.readString(file);
			DocParser parser = new DocParser(data);
			String word;
			while ((word = parser.extract()) != null) {
				word = word.toLowerCase();
				if (stopWords.contains(word)) continue;
				dictionary.add(word);
			}
			docCount++;
			return super.visitFile(file, attrs);
		}
		
	}
	
	/**
	 * Implementation of {@link FileVisitor} which calculates {@link VectorN} for
	 * each document.
	 * 
	 * @author Filip Husnjak
	 */
	private static class VectorVisitor extends SimpleFileVisitor<Path> {
		
		private Map<String, Double> idf = new HashMap<>();
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			String data = Files.readString(file);
			Map<String, Double> tf = getFrequency(data);
			docs.put(file, createVectorN(tf));
			tf.entrySet().stream().forEach(e -> idf.merge(e.getKey(), 1.0, Double::sum));
			return super.visitFile(file, attrs);
		}
		
	}
	
	/**
	 * Filters the given words removing all stop words and words that are not contained
	 * in dictionary.
	 * 
	 * @param args
	 *        array to be filtered
	 * @return filtered array of words
	 */
	public static String[] filterQuery(String args) {
		Set<String> filtered = new LinkedHashSet<>();
		DocParser parser = new DocParser(args);
		String word;
		while ((word = parser.extract()) != null) {
			word = word.toLowerCase();
			if (!dictionary.contains(word)) continue;
			filtered.add(word);
		}
		return filtered.toArray(String[]::new);
	}
	
	/**
	 * Returns frequencies for each word in dictionary in the given data string.
	 * 
	 * @param data
	 *        data which frequencies are to be returned
	 * @return map which maps word to its frequency in the given string
	 */
	private static Map<String, Double> getFrequency(String data) {
		Map<String, Double> tf = new HashMap<>();
		DocParser parser = new DocParser(data);
		String word;
		while ((word = parser.extract()) != null) {
			word = word.toLowerCase();
			if (stopWords.contains(word) || !dictionary.contains(word)) continue;
			tf.merge(word, 1.0, Double::sum);
		}
		return tf;
	}
	
	/**
	 * Creates and returns {@link VectorN} object for the given map.
	 * 
	 * @param map
	 *        map from which the {@link VectorN} object is to be created
	 * @return {@link VectorN} object created from the given map
	 */
	public static VectorN createVectorN(Map<String, Double> map) {
		double[] components = new double[dictionary.size()];
		int i = 0;
		for (String word: dictionary) {
			Double value = map.get(word);
			components[i] = value == null ? 0.0 : value;
			i++;
		}
		return new VectorN(components);
	}
	
	/**
	 * Returns map representing results of the last query.
	 * 
	 * @return map representing results of the last query
	 */
	public static Map<Path, Double> getLastQuery() {
		return lastQuery;
	}
	
	/**
	 * Returns size of the dictionary.
	 * 
	 * @return size of the dictionary
	 */
	public static int getDictionarySize() {
		return dictionary.size();
	}
	
}
