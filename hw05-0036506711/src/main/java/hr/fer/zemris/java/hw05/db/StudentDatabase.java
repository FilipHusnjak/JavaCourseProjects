package hr.fer.zemris.java.hw05.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Stores StudentRecords in a hash map. Each StudentRecord has to have unique jmbag
 * and grade between 1 and 5.
 * 
 * @author Filip Husnjak
 *
 */
public class StudentDatabase {
	
	private Map<String, StudentRecord> records = new HashMap<>();
	
	/**
	 * Constructs {@code StudentDatabase} object from given lines. Each line should
	 * represent one record. Each attribute of the student record should be separated
	 * by tab. Student with the same jmbag or grade less than 0 or greater than 5
	 * are not allowed. If the grade cannot be interpreted as Integer exception will be thrown.
	 * 
	 * @param lines
	 *        list of lines that represent student records
	 * @throws NullPointerException if the given array is {@code null}
	 * @throws IllegalArgumentException if lines cannot be parsed, the more appropriate
	 *         message is written
	 */
	public StudentDatabase(List<String> lines) {
		Objects.requireNonNull(lines, "Given list cannot be null!");
		for (String line: lines) {
			String[] parts = line.split("\\t+");
			if (parts.length != 4) {
				throw new IllegalArgumentException("Wrong line format!");
			}
			if (records.containsKey(parts[0])) {
				throw new IllegalArgumentException("Students with same jmbag are not allowed!");
			}
			try {
				int grade = Integer.parseInt(parts[3]);
				if (grade < 1 || grade > 5) {
					throw new IllegalArgumentException("Only grades between 1 and 5 are allowed!");
				}
				records.put(parts[0], new StudentRecord(parts[0], parts[1], parts[2], grade));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Given grade cannot be interpreted as integer!");
			}
		}
	}
	
	/**
	 * Returns the {@code StudentRecord} with specified jmbag. If no student records
	 * with given jmbag were found, {@code null} is returned.
	 * 
	 * @param jmbag
	 *        jmbag of the student record to be returned
	 * @return the {@code StudentRecord} with specified jmbag or null if no student records
	 *         were found
	 * @throws NullPointerException if the given jmbag is null
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return records.get(Objects.requireNonNull(jmbag, "Given jmbag cannot be null!"));
	}
	
	/**
	 * Filters this database with specified {@code IFilter} object. Each StudentRecord, for 
	 * which the filter method {@code accept} returns {@code true}, will be returned.
	 * 
	 * @param filter
	 *        {@code IFilter} instance used to filter this database
	 * @return List of filtered student records
	 */
	public List<StudentRecord> filter(IFilter filter) {
		Objects.requireNonNull(filter, "Given filter cannot be null!");
		return records.entrySet().stream().map(Map.Entry::getValue).filter(filter::accepts)
						.sorted((v1, v2) -> v1.getJmbag().compareTo(v2.getJmbag())).collect(Collectors.toList());
	}
	
}
