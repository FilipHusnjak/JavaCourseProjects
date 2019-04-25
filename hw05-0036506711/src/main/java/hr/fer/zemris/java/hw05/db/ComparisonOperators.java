package hr.fer.zemris.java.hw05.db;

/**
 * Represents the group of static {@code IComparisonOperator} instances. 
 * <br>
 * Each is used for one comparison from the set of {@code (=, !=, >, <, >=, <=, LIKE)}.
 * 
 * @author Filip Husnjak
 */
public class ComparisonOperators {

	/**
	 * Implementation of {@code IComparisonOperator} which compares if the first given value is less than second
	 */
	public static final IComparisonOperator LESS = (v1, v2) -> v1.compareTo(v2) < 0;
	
	/**
	 * Implementation of {@code IComparisonOperator} which compares if the first given value is less or equal to the second
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) <= 0;

	/**
	 * Implementation of {@code IComparisonOperator} which compares if the first given value is greater than second
	 */
	public static final IComparisonOperator GREATER = (v1, v2) -> v1.compareTo(v2) > 0;
	
	/**
	 * Implementation of {@code IComparisonOperator} which compares if the first given value is greater or equal to the second
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) >= 0;
	
	/**
	 * Implementation of {@code IComparisonOperator} which compares if the first given value is equal to the second
	 */
	public static final IComparisonOperator EQUALS = (v1, v2) -> v1.compareTo(v2) == 0;
	
	/**
	 * Implementation of {@code IComparisonOperator} which compares if the first given value is not equal to the second
	 */
	public static final IComparisonOperator NOT_EQUALS = (v1, v2) -> v1.compareTo(v2) != 0;
	
	/**
	 * Implementation of {@code IComparisonOperator} which compares if the first given value matches the expression 
	 * provided with the second parameter.
	 */
	public static final IComparisonOperator LIKE = (v1, v2) -> {
		StringBuilder start = new StringBuilder();
		StringBuilder end = new StringBuilder();
		int i;
		for (i = 0; i < v2.length() && v2.charAt(i) != '*'; ++i) {
			start.append(v2.charAt(i));
		}
		if (i == v2.length()) {
			return v1.equals(start.toString());
		}
		for (i = i + 1; i < v2.length(); ++i) {
			if (v2.charAt(i) == '*') {
				throw new IllegalArgumentException("More than one wildcards used!");
			}
			end.append(v2.charAt(i));
		}
		int size = start.length() + end.length();
		return v1.startsWith(start.toString()) && v1.endsWith(end.toString()) && v1.length() >= size;
	};
	
}
