package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw05.db.lexer.QueryToken;
import hr.fer.zemris.java.hw05.db.lexer.QueryTokenType;

/**
 * Representation of a parser that parses simple queries using {@code QueryLexer}.
 * 
 * @author Filip Husnjak
 */
public class QueryParser {
	
	/**
	 * {@code QueryLexer} used in this parser
	 */
	private QueryLexer lexer;
	
	/**
	 * Maps IFieldValueGetter implementations with attribute names
	 */
	private static Map<String, IFieldValueGetter> attributeNames = new HashMap<>();
	
	/**
	 * Maps IComparisonOperators with their String representation
	 */
	private static Map<String, IComparisonOperator> operators = new HashMap<>();
	
	/**
	 * List of parsed expressions
	 */
	private List<ConditionalExpression> expressions = new ArrayList<>();
	
	/**
	 * Tells whether the query is direct
	 */
	private boolean directQuery;
	
	/**
	 * If the query is direct this variable holds the queried JMBAG
	 */
	private String queriedJMBAG;
	
	static {
		attributeNames.put("jmbag", FieldValueGetters.JMBAG);
		attributeNames.put("firstName", FieldValueGetters.FIRST_NAME);
		attributeNames.put("lastName", FieldValueGetters.LAST_NAME);
		
		operators.put("LIKE", ComparisonOperators.LIKE);
		operators.put("=", ComparisonOperators.EQUALS);
		operators.put("<", ComparisonOperators.LESS);
		operators.put(">", ComparisonOperators.GREATER);
		operators.put("<=", ComparisonOperators.LESS_OR_EQUALS);
		operators.put(">=", ComparisonOperators.GREATER_OR_EQUALS);
		operators.put("!=", ComparisonOperators.NOT_EQUALS);
	}

	/**
	 * Constructs {@code QueryParser} object with the given String representation
	 * of a query.
	 * 
	 * @param query
	 *        query to be parsed
	 * @throws NullPointerException if the given query is {@code null}
	 * @throws QueryParserException if the given query cannot be parsed
	 */
	public QueryParser(String query) {
		Objects.requireNonNull(query, "Given query cannot be null!");
		lexer = new QueryLexer(query);
		try {
			parse();
		} catch (QueryLexerException e) {
			throw new QueryParserException(e.getMessage());
		}
	}
	
	/**
	 * Parses the given query using {@link #lexer}.
	 * 
	 * @throws QueryParserException if the given query cannot be parsed
	 */
	public void parse() {
		directQuery = false;
		expressions.add(parseAttribWithValue());
		for (QueryToken token = lexer.extractNextToken(); token.getType() != QueryTokenType.EOF; token = lexer.extractNextToken()) {
			if (token.getType() != QueryTokenType.WORD || token.getValue() == null) {
				throw new QueryParserException("Logical operator expected but not given!");
			}
			if (!token.getValue().toUpperCase().equals("AND")) {
				throw new QueryParserException("Unexpected logical operator: " + token.getValue());
			}
			expressions.add(parseAttribWithValue());
		}
	
		if (expressions.size() == 1 && expressions.get(0).getFieldGetter() == FieldValueGetters.JMBAG && expressions.get(0).getComparisonOperator() == ComparisonOperators.EQUALS) {
			directQuery = true;
			queriedJMBAG = expressions.get(0).getStringLiteral();
		}
	}
	
	/**
	 * Parses attribute with operator and proper string literal and returns
	 * created ConditionalExpression.
	 * 
	 * @return ConditionalExpression created from one part of the query
	 * @throws QueryParserException if the query cannot be parsed
	 */
	private ConditionalExpression parseAttribWithValue() {
		QueryToken attrib = lexer.extractNextToken();
		if (attrib.getType() == QueryTokenType.EOF) {
			throw new QueryParserException("Query cannot be empty or end with logical operator!");
		}
		if (attrib.getType() != QueryTokenType.WORD) {
			throw new QueryParserException("Attribute expexted but not given! Given: " + attrib.getValue());
		}
		
		QueryToken operator = lexer.extractNextToken();
		if (operator.getType() == QueryTokenType.EOF) {
			throw new QueryParserException("Missing operator and string literal!");
		}
		if (operator.getType() != QueryTokenType.SYMBOLS && operator.getType() != QueryTokenType.WORD) {
			throw new QueryParserException("Operator expexted but not given! Given: " + operator.getValue());
		}
		
		QueryToken string = lexer.extractNextToken();
		if (string.getType() == QueryTokenType.EOF) {
			throw new QueryParserException("Missing string literal!");
		}
		
		IFieldValueGetter fieldGetter = attributeNames.get(attrib.getValue());
		if (fieldGetter == null) {
			throw new QueryParserException("Given attribute does not exist! Given: " + attrib.getValue());
		}
		if (operator.getType() != QueryTokenType.WORD && operator.getType() != QueryTokenType.SYMBOLS) {
			throw new QueryParserException("Operator expexted but not given! Given: " + string.getValue());
		}
		
		IComparisonOperator compOperator = operators.get(operator.getValue());
		if (compOperator == null) {
			throw new QueryParserException("Unrecognized operator given: " + operator.getValue());
		}
		if (string.getType() != QueryTokenType.STRING) {
			throw new QueryParserException("String literal expexted but not given! Given: " + string.getValue());
		}
		
		return new ConditionalExpression(fieldGetter, string.getValue(), compOperator);
	}
	
	/**
	 * Returns the list of {@code ConditionalExpression} created from the given query.
	 * 
	 * @return the list of {@code ConditionalExpression} created from the given query
	 */
	public List<ConditionalExpression> getQuery() {
		return expressions;
	}

	/**
	 * Returns {@code true} if the query was direct.
	 * 
	 * @return {@code true} if the query was direct
	 */
	public boolean isDirectQuery() {
		return directQuery;
	}
	
	/**
	 * Returns queried JMBAG if the query was direct, otherwise an exception is thrown.
	 * 
	 * @return queried JMBAG if the query was direct
	 * @throws IllegalStateException if the query was not direct
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("Query is not direct!");
		}
		return queriedJMBAG;
	}
	
}
