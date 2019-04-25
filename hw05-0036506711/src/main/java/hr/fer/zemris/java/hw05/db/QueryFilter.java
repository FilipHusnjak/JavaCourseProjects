package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

/**
 * Represents implementation of {@code IFilter} interface which stores {@code ConditionalExpressions}
 * in the list. Method accepts returns {@code true} if every condition in the list
 * is satisfied.
 * 
 * @author Filip Husnjak
 */
public class QueryFilter implements IFilter {

	/**
	 * List of {@code ConditionalExpressions} used in method {@link #accepts(StudentRecord)}
	 */
	private List<ConditionalExpression> expressions;
	
	/**
	 * Constructs a {@code QueryFilter} object with given list of {@code ConditionalExpressions}.
	 * 
	 * @param expressions
	 *        list of {@code ConditionalExpressions}
	 * @throws NullPointerException if the given list is {@code null}
	 */
	public QueryFilter(List<ConditionalExpression> expressions) {
		this.expressions = Objects.requireNonNull(expressions, "Given list cannot be null!");
	}

	/**
	 * Returns {@code true} if Every condition in list {@link #expressions} is satisfied.
	 * 
	 * @return {@code true} if Every condition in list {@link #expressions} is satisfied
	 * @throws NullPointerException if the given record is {@code null}
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		Objects.requireNonNull(record, "Given record cannot be null!");
		for (ConditionalExpression expression: expressions) {
			if (!expression.getComparisonOperator().satisfied(expression.getFieldGetter().get(record),
					expression.getStringLiteral())) {
				return false;
			}
		}
		return true;
	}

}
