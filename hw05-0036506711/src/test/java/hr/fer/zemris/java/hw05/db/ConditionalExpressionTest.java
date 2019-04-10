package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ConditionalExpressionTest {

	@Test
	public void testNullParameters() {
		assertThrows(NullPointerException.class, () -> new ConditionalExpression(null, "a", ComparisonOperators.EQUALS));
		assertThrows(NullPointerException.class, () -> new ConditionalExpression(FieldValueGetters.FIRST_NAME, null, ComparisonOperators.EQUALS));
		assertThrows(NullPointerException.class, () -> new ConditionalExpression(FieldValueGetters.FIRST_NAME, "a", null));
	}
	
	@Test
	public void testNormalInput() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bos*", ComparisonOperators.LIKE);
		StudentRecord record = new StudentRecord("0000000003", "Bosnić", "Andrea", 4);
		assertTrue(expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record), // returns lastName from given record
				expr.getStringLiteral() // returns "Bos*"
		));
	}
	
}
