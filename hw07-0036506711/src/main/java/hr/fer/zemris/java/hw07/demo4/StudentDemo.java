package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple Demo program for filtering and formating data using stream API.
 * 
 * @author Filip Husnjak
 */
public class StudentDemo {

	/**
	 * Reads given file and prints solutions to 8 tasks.
	 * 
	 * @param args
	 *        not used
	 */
	public static void main(String[] args) {
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get("./studenti.txt"));
		} catch (IOException e1) {
			System.out.println("Cannot open given file!");
			return;
		}
		List<StudentRecord> records;
		try {
			records = convert(lines);
		} catch (NumberFormatException e) {
			System.out.println("Wrong file format, numbers expected but not given!");
			return;
		}
		
		System.out.println("Zadatak 1");
		System.out.println("=========");
		System.out.println(vratiBodovaViseOd25(records));
		
		System.out.println("\nZadatak 2");
		System.out.println("=========");
		System.out.println(vratiBrojOdlikasa(records));
		
		System.out.println("\nZadatak 3");
		System.out.println("=========");
		vratiListuOdlikasa(records).forEach(System.out::println);
		
		System.out.println("\nZadatak 4");
		System.out.println("=========");
		vratiSortiranuListuOdlikasa(records).forEach(System.out::println);
		
		System.out.println("\nZadatak 5");
		System.out.println("=========");
		vratiPopisNepolozenih(records).forEach(System.out::println);
		
		System.out.println("\nZadatak 6");
		System.out.println("=========");
		rezvrstajSudentePoOcjenama(records).entrySet().forEach(e -> {
			System.out.println("\n" + e.getKey());
			e.getValue().forEach(System.out::println);
		});
		
		System.out.println("\nZadatak 7");
		System.out.println("=========");
		razvrstajBrojStudenataPoOcjenama(records).entrySet().forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
	

		System.out.println("\nZadatak 8");
		System.out.println("=========");
		razvrstajProlazPad(records).entrySet().forEach(e -> {
			System.out.println("\n" + e.getKey());
			e.getValue().forEach(System.out::println);
		});
	}

	/**
	 * Converts given String lines to list of StudentRecords.
	 * 
	 * @param lines
	 *        lines to be converted
	 * @return list of StudentRecords
	 * @throws NumberFormatException if the expected number cannot be parsed
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		return lines.stream().filter(line -> line.split("\\t").length == 7).map(line -> {
			String[] parts = line.split("\\t");
			return new StudentRecord(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), 
					Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), Integer.parseInt(parts[6]));
		}).collect(Collectors.toList());
	}
	
	/**
	 * Returns number of student records with sum of points greater than 25.
	 * 
	 * @param records
	 *        list of student records used for data
	 * @return number of student records with sum of points greater than 25
	 */
	private static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream().map(StudentDemo::sumaBodova)
				.filter(b -> b > 25.0).count();
	}
	
	/**
	 * Returns number of student records with grade equal to 5.
	 * 
	 * @param records
	 *        list of student records used for data
	 * @return number of student records with grade equal to 5
	 */
	private static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return vratiListuOdlikasa(records).size();
	}
	
	/**
	 * Returns list of student records containing only student records with grade
	 * equal to 5.
	 * 
	 * @param records
	 *        list of student records used for data
	 * @return list of student records containing only student records with grade
	 *         equal to 5
	 */
	private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(r -> r.getOcjena() == 5).collect(Collectors.toList());
	}
	
	/**
	 * Returns list of student records containing only student records with grade
	 * equal to 5 sorted by number of points in descending order.
	 * 
	 * @param records
	 *        list of student records used for data
	 * @return list of student records containing only student records with grade
	 *         equal to 5 sorted by number of points in descending order
	 */
	private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(r -> r.getOcjena() == 5)
				.sorted((r1, r2) -> sumaBodova(r2).compareTo(sumaBodova(r1))).collect(Collectors.toList());
	}
	
	/**
	 * Returns list of StudentRecord#jmbag of students with grade equal to 1.
	 * 
	 * @param records
	 *        list of student records used for data
	 * @return list of StudentRecord#jmbag of students with grade equal to 1
	 */
	private static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream().filter(r -> r.getOcjena() == 1).map(StudentRecord::getJmbag).sorted().collect(Collectors.toList());
	}
	
	/**
	 * Returns map that maps student records with grades. Keys are grades while
	 * values are lists of student records with that grade.
	 * 
	 * @param records
	 *        list of student records used for data
	 * @return map that maps student records with grades. Keys are grades while
	 *         values are lists of student records with that grade.
	 */
	private static Map<Integer, List<StudentRecord>> rezvrstajSudentePoOcjenama(List<StudentRecord> records) {
		return records.stream().collect(Collectors.groupingBy(StudentRecord::getOcjena));	
	}
	
	/**
	 * Returns map that maps grades with number of students that achieved that grade.
	 * 
	 * @param records
	 *        list of student records used for data
	 * @return map that maps grades with number of students that achieved that grade
	 */
	private static Map<Integer, Integer> razvrstajBrojStudenataPoOcjenama(List<StudentRecord> records) {
		return records.stream().collect(Collectors.toMap(StudentRecord::getOcjena, r -> 1, Integer::sum));
	}
	
	/**
	 * Returns map that maps student grades that have grade greater than 1 with {@code true}
	 * and student records with grade equal to 1 with {@code false}.
	 * 
	 * @param records
	 *        list of student records used for data
	 * @return map that maps student grades that have grade greater than 1 with {@code true}
	 *         and student records with grade equal to 1 with {@code false}
	 */
	private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		return records.stream().collect(Collectors.partitioningBy(r -> r.getOcjena() > 1));
	}
	
	/**
	 * Returns sum of student record points: {@code pointsMI + pointsZI + pointsLAB}.
	 * 
	 * @param r
	 *        student record whose sum is to be calculated
	 * @return sum of student record points: {@code pointsMI + pointsZI + pointsLAB}
	 */
	private static Double sumaBodova(StudentRecord r) {
		return r.getBrojBodovaLab() + r.getBrojBodovaMI() + r.getBrojBodovaZI();
	}

}
