package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Represents conditional expression that consists of 3 parts:
 * <ul>
 *  <li>
 *  IFieldValueGetter fieldGetter - field getter used to get value from the specified filed in StudentRecord
 *  </li>
 *  <li>
 *  String stringLiteral - stringLiteral to which the value of the extracted field is compared
 *  </li>
 *  <li>
 *  IComparisonOperator operator - used to compare value of a field with provided string literal
 *  </li>
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class ConditionalExpression {

	/**
	 * Field getter used to get value from the specified filed in StudentRecord
	 */
	private final IFieldValueGetter fieldGetter;
	
	/**
	 * StringLiteral to which the value of the extracted field is compared
	 */
	private final String stringLiteral;
	
	/**
	 * Used to compare value of a field with provided string literal
	 */
	private final IComparisonOperator operator;

	/**
	 * Constructs {@code ConditionalExpression} with specified parameters.
	 * 
	 * @param fieldGetter
	 * 		  field getter used to get value from the specified filed in StudentRecord
	 * @param stringLiteral
	 *        stringLiteral to which the value of the extracted field is compared
	 * @param operator
	 *        used to compare value of a field with provided string literal
	 * @throws NullPointerException if one of the given arguments is {@code null}
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator operator) {
		this.fieldGetter = Objects.requireNonNull(fieldGetter, "Given field getter cannot be null!");
		this.stringLiteral = Objects.requireNonNull(stringLiteral, "Given string literal cannot be null!");
		this.operator = Objects.requireNonNull(operator, "Given operator cannot be null!");
	}

	/**
	 * Returns the IFieldValueGetter of {@code this} ConditionalExpression.
	 * 
	 * @return the IFieldValueGetter of {@code this} ConditionalExpression
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Returns the String literal of {@code this} ConditionalExpression.
	 * 
	 * @return the String literal of {@code this} ConditionalExpression
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Returns the IComparisonOperator of {@code this} ConditionalExpression.
	 * 
	 * @return the IComparisonOperator of {@code this} ConditionalExpression
	 */
	public IComparisonOperator getComparisonOperator() {
		return operator;
	}
	
}
