package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Command line application which accepts a single command-line argument.
 * Argument should be expression in postfix representation which is then evaluated.
 * 
 * @author Filip Husnjak
 *
 */
public class StackDemo {

	/**
	 * Reads 1 parameter given through console and evaluates that expression.
	 * 
	 * @param args
	 *        parameter given through console
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Wrong number of command line arguments! " + "accepts = 1, provided = " + args.length + "!");
			return;
		}
		
		String[] params = args[0].split("\\s+");
		ObjectStack stack = new ObjectStack();
		
		for (int i = 0; i < params.length; ++i) {
			process(stack, params[i]);
		}
		
		if (stack.size() != 1) {
			invalidExpression("Stack size at the end is not one!");
			return;
		}
		
		System.out.println("Expression evaluates to " + stack.pop() + ".");
	}
	
	/**
	 * Processes outputs or terminates the program if expression is invalid, 
	 * based on given parameters.
	 * 
	 * @param stack
	 *        stack whose elements are used in calculation
	 * @param character
	 *        character of an expression
	 */
	private static void process(ObjectStack stack, String character) {
		try {
			stack.push(Integer.parseInt(character));
		} catch (NumberFormatException ex1) {
			try {
				stack.push(calculate((Integer)stack.pop(), (Integer)stack.pop(), character));
			} catch (EmptyStackException ex2) {
				invalidExpression("Popping from empty stack!");
				System.exit(1);
			} catch (ClassCastException | ArithmeticException | IllegalArgumentException ex2) {
				invalidExpression(ex2.getMessage());
				System.exit(1);
			}
		}
	}
	
	/**
	 * Writes the given message to {@code System.err} with appropriate explanation.
	 * 
	 * @param msg
	 *        message to be written
	 */
	private static void invalidExpression(String msg) {
		System.err.println("Expression is invalid! Reason: " + msg);
	}
	
	/**
	 * Returns the result of the calculation of expression given through parameters.
	 * 
	 * @param num2
	 *        second number used in calculation
	 * @param num1
	 *        first number used in calculation
	 * @param operator
	 *        operator used in calculation, must be a member of this set: {+, -, /, *, %}
	 * @return result of the calculation
	 * @throws ArithmeticException if there was division by 0
	 * @throws IllegalArgumentException if operator in not member of a 
	 *         defined set of operators: {+, -, /, *, %}
	 */
	private static int calculate(Integer num2, Integer num1, String operator) {
		if (operator.equals("+")) {
			return num1 + num2;
		} else if (operator.equals("-")) {
			return num1 - num2;
		} else if (operator.equals("/")) {
			try {
				return num1 / num2;
			} catch (ArithmeticException ex) {
				throw new ArithmeticException("Division by zero!");
			}
			
		} else if (operator.equals("*")) {
			return num1 * num2;
		} else if (operator.equals("%")) {
			return num1 % num2;
		}
		
		throw new IllegalArgumentException("Wrong operator provided \"" + operator + "\"!");
	}

}
