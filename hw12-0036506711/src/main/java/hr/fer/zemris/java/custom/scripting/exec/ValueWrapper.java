package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * Represents wrapper that keeps value of any type including null references.
 * It also provides useful methods for math operations if the certain conditions
 * are met, otherwise those methods will throw exceptions. In order to be able to
 * perform mathematical operation upon two Object values they both should be instances
 * of String, Integer, Double or null (then its treated as {@code ZERO}). If its
 * instance of String it have to be convertible to Double or Integer.
 * 
 * @author Filip Husnjak
 */
public class ValueWrapper {

	/**
	 * Value of this {@code ValueWrapper}, it can be object of any type or null
	 */
	private Object value;

	/**
	 * Constructs new {@code ValueWrapper} object with specified value which can
	 * also be {@code null};
	 * 
	 * @param value
	 *        value of the new {@code ValueWrapper} object
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}
	
	/**
	 * If the given value or the value of this {@code ValueWrapper} are not instance of 
	 * Integer, Double, String or {@code null} this method will throw exception.
	 * If the given value or the value of this {@code ValueWrapper} are instances
	 * of String but not convertible to Integer or Double this method will also
	 * throw exception.
	 * In all other cases this method will add the given value to the value of
	 * this {@code ValueWrapper}.
	 * 
	 * @param incValue
	 *        value to be added to the value of this {@code ValueWrapper}
	 * @throws RuntimeException if conditions specified in the description of the method
	 *         are not met
	 */
	public void add(Object incValue) {
		value = execute(incValue, Double::sum, Integer::sum);
	}
	
	/**
	 * If the given value or the value of this {@code ValueWrapper} are not instance of 
	 * Integer, Double, String or {@code null} this method will throw exception.
	 * If the given value or the value of this {@code ValueWrapper} are instances
	 * of String but not convertible to Integer or Double this method will also
	 * throw exception.
	 * In all other cases this method will subtract the given value from the value of
	 * this {@code ValueWrapper}.
	 * 
	 * @param decValue
	 *        value to be subtracted from the value of this {@code ValueWrapper}
	 * @throws RuntimeException if conditions specified in the description of the method
	 *         are not met
	 */
	public void subtract(Object decValue) {
		value = execute(decValue, (d1, d2) -> d1 - d2, (i1, i2) -> i1 - i2);
	}
	
	/**
	 * If the given value or the value of this {@code ValueWrapper} are not instance of 
	 * Integer, Double, String or {@code null} this method will throw exception.
	 * If the given value or the value of this {@code ValueWrapper} are instances
	 * of String but not convertible to Integer or Double this method will also
	 * throw exception.
	 * In all other cases this method will multiply the value of this {@code ValueWrapper}
	 * with the given value.
	 * 
	 * @param mulValue
	 *        value by which value of this {@code ValueWrapper} will be multiplied
	 * @throws RuntimeException if conditions specified in the description of the method
	 *         are not met
	 */
	public void multiply(Object mulValue) {
		value = execute(mulValue, (d1, d2) -> d1 * d2, (i1, i2) -> i1 * i2);
	}
	
	/**
	 * If the given value or the value of this {@code ValueWrapper} are not instance of 
	 * Integer, Double, String or {@code null} this method will throw exception.
	 * If the given value or the value of this {@code ValueWrapper} are instances
	 * of String but not convertible to Integer or Double this method will also
	 * throw exception.
	 * In all other cases this method will divide the value of this {@code ValueWrapper}
	 * with the given value.
	 * 
	 * @param divValue
	 *        value by which value of this {@code ValueWrapper} will be divided
	 * @throws RuntimeException if conditions specified in the description of the method
	 *         are not met
	 */
	public void divide(Object divValue) {
		value = execute(divValue, (d1, d2) -> d1 / d2, (i1, i2) -> i1 / i2);
	}
	
	/**
	 * If the given value or the value of this {@code ValueWrapper} are not instance of 
	 * Integer, Double, String or {@code null} this method will throw exception.
	 * If the given value or the value of this {@code ValueWrapper} are instances
	 * of String but not convertible to Integer or Double this method will also
	 * throw exception.
	 * In all other cases this method will compare the given value with the value
	 * of this {@code ValueWrapper} and return:
	 * <ul>
	 * <li> {@code -1} - if the value of this {@code ValueWrapper} object is less than the given value
	 * <li> {@code  0} - if the value of this {@code ValueWrapper} object is equal to the given value
	 * <li> {@code  1} - if the value of this {@code ValueWrapper} object is greater than the given value
	 * </ul>
	 * 
	 * @param withValue
	 *        Value to be added to the value of this {@code ValueWrapper}
	 * @return the result of the comparison specified in method description
	 * @throws RuntimeException if conditions specified in description of the method
	 *         are not met
	 */
	public int numCompare(Object withValue) {
		return execute(withValue, Double::compare, Integer::compare);
	}
	
	/**
	 * Method that determines whether arithmetic operations upon given value and
	 * value of this {@code ValueWrapper} are legal. In other words this method
	 * checks the conditions specified by each method for arithmetic operations.
	 * If the conditions are met this method will use proper {@code BiFunction}
	 * and return the result.
	 * 
	 * @param givenValue
	 *        given value used for arithmetic operation
	 * @param doubles
	 *        {@code BiFunction} used for Double values
	 * @param integers
	 *        {@code BiFunction} used for Integer values
	 * @return the result of the {@code BiFunction}, which {@code BiFunction} is
	 *         used is determined in this method
	 * @throws RuntimeException if the conditions specified for arithmetic operations
	 *         are not met
	 */
	private <T extends Number> T execute(Object givenValue, BiFunction<Double, Double, T> doubles, 
			BiFunction<Integer, Integer, T> integers) {
		value = getValue(value);
		givenValue = getValue(givenValue);
		if (value instanceof Double || givenValue instanceof Double) {
			return doubles.apply(((Number) value).doubleValue(), ((Number) givenValue).doubleValue());
		}
		return integers.apply((Integer) value, (Integer) givenValue);
	}
	
	private Object getValue(Object value) {
		if (value == null) value = 0;
		if (value instanceof String) value = parseNumber((String)value);
		if (!checkDoubleOrInteger(value)) {
			throw new RuntimeException("One of the given numbers is not instance of Double or Integer!");
		}
		return value;
	}
	
	/**
	 * Returns {@code true} if the given object is instance of Double or Integer.
	 * 
	 * @param o
	 *        object to be checked
	 * @return {@code true} if the given object is instance of Double or Integer
	 */
	private boolean checkDoubleOrInteger(Object o) {
		return o instanceof Double || o instanceof Integer;
	}
	
	public double doubleValue() {
		return ((Number) getValue(value)).doubleValue();
	}

	/**
	 * Converts the given string to Double or Integer, if its not possible it
	 * throws exception.
	 * 
	 * @param number
	 *        String to be converted
	 * @return Integer or Double representation of the given String
	 * @throws RuntimeException if the given String is not convertible to Double or Integer
	 */
	private Object parseNumber(String number) {
		try {
			if (number.contains(".") || number.contains("E")) {
				return Double.parseDouble(number);
			}
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Given String cannot be interpreted as real number: " + number);
		}
	}
	
	/**
	 * Returns the value of this {@code ValueWrapper}.
	 * 
	 * @return the value of this {@code ValueWrapper}
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Sets the value of this {@code ValueWrapper} to the specified one.
	 * 
	 * @param value
	 *        new value of the {@code ValueWrapper}
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value == null ? "0" : value.toString();
	}
	
}
