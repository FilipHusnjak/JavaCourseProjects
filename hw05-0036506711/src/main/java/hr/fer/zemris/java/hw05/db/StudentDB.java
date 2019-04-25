package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Simulation of query filtering upon dummy database.
 * 
 * @author Filip Husnjak
 */
public class StudentDB {

	/**
	 * Loads the database from ./src/main/resources/database.txt and performs given
	 * queries. Queries are given through console until "exit" is sent. The results
	 * will be present immediately sorted by JMBAG.
	 */
	public static void main(String[] args) {
		try {
			StudentDatabase database = new StudentDatabase(
					Files.readAllLines(Paths.get("./src/main/resources/database.txt"), StandardCharsets.UTF_8));
			Scanner sc = new Scanner(System.in);
			System.out.print("> ");
			while (sc.hasNext()) {
				String line = sc.nextLine();
				if (line.equals("exit")) break;
				try {
					if (!line.trim().split(" ", 2)[0].trim().equals("query")) {
						System.out.println("Query has to start with query keyword!");
						System.out.print("\n> ");
						continue;
					}
					QueryParser parser = new QueryParser(line.trim().split(" ", 2)[1]);
					if (parser.isDirectQuery()) {
						formatQueriedResults(database.forJMBAG(parser.getQueriedJMBAG()) == null ?
								new ArrayList<>() : Arrays.asList(database.forJMBAG(parser.getQueriedJMBAG()))).forEach(System.out::println);
					} else {
						formatQueriedResults(database.filter(new QueryFilter(parser.getQuery()))).forEach(System.out::println);
					}
				} catch (QueryParserException | IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
				System.out.print("\n> ");
			}
			System.out.println("Goodbye!");
			sc.close();
			
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Text file cannot be read or opened!");
		}
	}
	
	/**
	 * Formats the list of selected records as list of Strings to be printed. First
	 * and last String represent borders of a table.
	 * 
	 * @param selectedRecords
	 *        records to be formatted
	 * @return List of Strings to be printed
	 * @throws NullPointerException if the given list is {@code null}
	 */
	private static List<String> formatQueriedResults(List<StudentRecord> selectedRecords) {
		Objects.requireNonNull(selectedRecords, "Selected records list cannot be null!");
		if (selectedRecords.size() == 0) {
			return Arrays.asList("Records selected: " + selectedRecords.size());
		}
		List<String> formattedData = new ArrayList<>();
		int maxLenJmbag = 0, maxLenLastName = 0, maxLenFirstName = 0;
		for (StudentRecord r : selectedRecords) {
			if (r.getJmbag().length() > maxLenJmbag) {
				maxLenJmbag = r.getJmbag().length();
			}
			if (r.getLastName().length() > maxLenLastName) {
				maxLenLastName = r.getLastName().length();
			}
			if (r.getFirstName().length() > maxLenFirstName) {
				maxLenFirstName = r.getFirstName().length();
			}
		}
		formattedData.add(formatBorder(maxLenJmbag, maxLenLastName, maxLenFirstName));
		for (StudentRecord r : selectedRecords) {
			formattedData.add(String.format("| %" + -maxLenJmbag + "s | %" + -maxLenLastName + "s | %" + -maxLenFirstName + "s | %s |",
					r.getJmbag(), r.getLastName(), r.getFirstName(), r.getGrade()));
		}
		formattedData.add(formatBorder(maxLenJmbag, maxLenLastName, maxLenFirstName));
		formattedData.add("Records selected: " + selectedRecords.size());
		return formattedData;
	}
	
	/**
	 * Formats the border of a table from given arguments.
	 * 
	 * @param maxLenJmbag
	 *        max length of JMBAG attribute in the records list
	 * @param maxLenLastName
	 *        max length of lastName attribute in the records list
	 * @param maxLenFirstName
	 *        max length of firstName attribute in the records list
	 * @return formatted border from given arguments
	 */
	private static String formatBorder(int maxLenJmbag, int maxLenLastName, int maxLenFirstName) {
		return new StringBuilder().append("+").append("=".repeat(maxLenJmbag + 2)).append("+")
									.append("=".repeat(maxLenLastName + 2)).append("+")
									.append("=".repeat(maxLenFirstName + 2)).append("+" + "===+").toString();
	}

}
