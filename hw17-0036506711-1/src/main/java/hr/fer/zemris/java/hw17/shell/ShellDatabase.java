package hr.fer.zemris.java.hw17.shell;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.FileVisitResult;
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

import hr.fer.zemris.java.hw17.math.VectorN;
import hr.fer.zemris.java.hw17.parser.DocParser;

public class ShellDatabase {

	private static Set<String> dictionary = new LinkedHashSet<>();
	
	private static Set<String> stopWords = new HashSet<>();
	
	private static Map<Path, VectorN> docs = new HashMap<>();
	
	private static Map<Path, Double> lastQuery;
	
	private static int docCount;
	
	private static VectorN idfVec;
	
	public static void loadDatabase(Path path) throws IOException {
		fillStopWords();
		fillDictionary(path);
		fillVectors(path);
	}

	private static void fillStopWords() throws IOException {
		Path path = Paths.get("src/main/resources/stopWords.txt");
		List<String> lines = Files.readAllLines(path);
		for (String line: lines) {
			stopWords.add(line.strip().toLowerCase());
		}
	}
	
	private static void fillDictionary(Path path) throws IOException {
		docCount = 0;
		DictionaryVisitor dicVisit = new DictionaryVisitor();
		Files.walkFileTree(path, dicVisit);
	}
	
	private static void fillVectors(Path path) throws IOException {
		VectorVisitor vecVisit = new VectorVisitor();
		Files.walkFileTree(path, vecVisit);
		vecVisit.idf.replaceAll((k, v) -> Math.log(docCount / v));
		idfVec = createVectorN(vecVisit.idf);
		docs.replaceAll((k, v) -> v.multiply(idfVec));
	}
	
	public static Map<Path, Double> executeQuery(String[] args) {
		Map<String, Double> tf = new HashMap<>();
		for (String word: args) {
			tf.merge(word.toLowerCase(), 1.0, Double::sum);
		}
		return findBestMatches(createVectorN(tf));
	}
	
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
	
	private static double round4(double d) {
		BigDecimal bd = new BigDecimal(d);
	    bd = bd.setScale(4, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
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
	
	private static class DictionaryVisitor extends SimpleFileVisitor<Path> {
				
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			String data = Files.readString(file);
			DocParser parser = new DocParser(data);
			String word;
			while ((word = parser.extract()) != null) {
				if (stopWords.contains(word.strip().toLowerCase())) continue;
				dictionary.add(word);
			}
			docCount++;
			return super.visitFile(file, attrs);
		}
		
	}
	
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
	
	public static String[] filterQuery(String args) {
		Set<String> filtered = new LinkedHashSet<>();
		DocParser parser = new DocParser(args);
		String word;
		while ((word = parser.extract()) != null) {
			if (!dictionary.contains(word.toLowerCase())) continue;
			filtered.add(word);
		}
		return filtered.toArray(String[]::new);
	}
	
	private static Map<String, Double> getFrequency(String data) {
		Map<String, Double> tf = new HashMap<>();
		DocParser parser = new DocParser(data);
		String word;
		while ((word = parser.extract()) != null) {
			if (stopWords.contains(word.toLowerCase())) continue;
			tf.merge(word.toLowerCase(), 1.0, Double::sum);
		}
		return tf;
	}
	
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
	
	public static Map<Path, Double> getLastQuery() {
		return lastQuery;
	}
	
	public static int getDictionarySize() {
		return dictionary.size();
	}
	
}
