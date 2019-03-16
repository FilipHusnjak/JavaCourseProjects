package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program to find factorial of a Number.
 * 
 * @author Filip Husnjak
 *
 */

public class Factorial {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.print("Unesite broj > ");
			String entry = sc.next();
			if (entry.equals("kraj")) {
				break;
			}
			
			try {
				int number = Integer.parseInt(entry);
				
				if (number < 3 || number > 20) {
					System.out.printf("\'%d\' nije broj u dozvoljenom rasponu.%n", number);
					continue;
				}
				
				System.out.printf("%d! = %d%n", number, factorialCalc(number));
				
			} catch(NumberFormatException ex) {
				System.out.printf("\'%s\' nije cijeli broj.%n", entry);
				
			}
		}
		
		sc.close();
		
		System.out.println("Doviđenja.");
	}

	/**
	 * 
	 * Calculates factorial of a given number.
	 * 
	 * @param number
	 * 		  the argument whose factorial value is to be determined
	 * 
	 * @return factorial value of the argument
	 * 
	 * @throws IllegalArgumentException if the number is less than <code>0</code> or greater than <code>20</code>
	 */
	
	public static long factorialCalc(int number) {
		
		// Factorial values of numbers greater than 20 can't be stored as long.
		if (number < 0 || number > 20) {
			throw new IllegalArgumentException("Number " + number + " is out of range.");
		}
		
		long result = 1;
		for (int i = 2; i <= number; ++i) {
			result *= i;
		}
		
		return result;
	}
	
}
