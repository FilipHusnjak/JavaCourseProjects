package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * 
 * Program for calculating an area and extent of a rectangle.
 * 
 * @author Filip Husnjak
 *
 */

public class Rectangle {
	
	public static void main(String[] args) {
		
		if (args.length != 2 && args.length != 0) {
			System.out.println("Nedozvoljen broj ulaznih argumenata!");
			return;
		}
	
		if (args.length == 2) {
			try {
				double width = check(args[0]);
				double height = check(args[1]);
				
				printAreaAndExtent(width, height);
				
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			}
			
			return;
		}
		
		Scanner sc = new Scanner(System.in);

		double width = entry(sc, "širinu");

		double height = entry(sc, "visinu");
		
		printAreaAndExtent(width, height);
		
		sc.close();
	}
	
	
	/**
	 * 
	 * Reads from InputStream wrapped with Scanner object.
	 * 
	 * @param sc 
	 *        Scanner object used for reading entries from InputStream 
	 * 
	 * @param s
	 *        word to be printed before each entry
	 * 
	 * @return first entry that is positive number
	 * 
	 * @throws IllegalArgumentException if the given number is negative
	 * @throws NumberFormatException if the given word can't be interpreted as a number
	 * @throws NullPointerException if one of the given parameters is <code>null</code>
	 */
	
	private static double entry(Scanner sc, String word) {
		
		if (sc.equals(null)) {
			throw new NullPointerException("Scanner objekt ne može biti null");
		}
		
		if (word.equals(null)) {
			throw new NullPointerException("String objekt ne može biti null");
		}
			
		while (true) {
			System.out.printf("Unesite %s > ", word);
			try {
				return check(sc.next());
			} catch(IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	
	/**
	 * 
	 * Prints an area and extent of a given rectangle.
	 * 
	 * @param width 
	 *        width of a rectangle
	 *        
	 * @param height
	 *        height of a rectangle
	 */
	
	public static void printAreaAndExtent(double width, double height) {
		System.out.printf("Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.", width, height, width * height, 2 * (width + height));
	}
	
	
	/**
	 * 
	 * Checks if given value is positive number.
	 *  	
	 * @param value 
	 *        value to check
	 * 
	 * @return value as double
	 */
	
	private static double check(String value) {
		try {
			double number = Double.parseDouble(value);
			
			if (number < 0) {
				throw new IllegalArgumentException("Unijeli ste negativnu vrijednost.");
			}
			
			return number;
			
		} catch(NumberFormatException ex) {
			throw new NumberFormatException(String.format("\'%s\' se ne može protumačiti kao broj.", value));
		}
	}
	
	
}
