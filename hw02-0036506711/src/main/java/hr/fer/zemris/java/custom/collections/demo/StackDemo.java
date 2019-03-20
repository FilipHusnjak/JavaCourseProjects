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
	 * Reads 1 parameter given as program argument and evaluates that expression.
	 * 
	 * @param args
	 *        parameter given as argument that represents expression
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Wrong number of command line arguments! " + "accepts = 1, provided = " + args.length + "!");
			return;
		}
		
		try {
			System.out.println("Expression evaluates to " + evaluate(args[0]) + ".");
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Evaluates given expression and returns the result as {@code integer}.
	 * 
	 * @param expression
	 *        expression to be evaluated
	 * @return {@code integer} representation of the result of an expression
	 * @throws IllegalArgumentException if the expression is invalid
	 */
	public static int evaluate(String expression) {
		String[] params = expression.split("\\s+");
		ObjectStack stack = new ObjectStack();
		
		for (int i = 0; i < params.length; ++i) {
			process(stack, params[i]);
		}
		
		if (stack.size() != 1) {
			invalidExpression("Stack size at the end is not one!");
		}
		
		return (Integer) stack.pop();
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
			} catch (ClassCastException | ArithmeticException | IllegalArgumentException ex2) {
				invalidExpression(ex2.getMessage());
			}
		}
	}
	
	/**
	 * Throws {@code IllegalArgumentException} with appropriate explanation.
	 * 
	 * @param msg
	 *        message to be sent to {@code IllegalArgumentException} constructor
	 * @throws IllegalArgumentException always
	 */
	private static void invalidExpression(String msg) {
		throw new IllegalArgumentException("Expression is invalid! Reason: " + msg);
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
