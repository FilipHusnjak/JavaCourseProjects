package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class ComparisonOperatorsTest {

	@Test
	public void testLessTrue() {
		assertTrue(ComparisonOperators.LESS.satisfied("aaa", "bbb"));
		assertTrue(ComparisonOperators.LESS.satisfied("1bb", "2aa"));
		assertTrue(ComparisonOperators.LESS.satisfied("a", "aaa"));
	}
	
	@Test
	public void testLessFalse() {
		assertFalse(ComparisonOperators.LESS.satisfied("111", "11"));
		assertFalse(ComparisonOperators.LESS.satisfied("1a", "1a"));
		assertFalse(ComparisonOperators.LESS.satisfied("1b", "1a"));
	}
	
	@Test
	public void testLessOrEqualsTrue() {
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("aaa", "bbb"));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("1bb", "2aa"));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("aaa", "aaa"));
	}
	
	@Test
	public void testLessOrEqualsFalse() {
		assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied("111", "11"));
		assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied("1a1", "1a"));
		assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied("1b", "1a"));
	}
	
	@Test
	public void testGreaterTrue() {
		assertTrue(ComparisonOperators.GREATER.satisfied("bbb", "aaa"));
		assertTrue(ComparisonOperators.GREATER.satisfied("2bb", "1aa"));
		assertTrue(ComparisonOperators.GREATER.satisfied("aaa", "a"));
	}
	
	@Test
	public void testGreaterFfalse() {
		assertFalse(ComparisonOperators.GREATER.satisfied("11", "111"));
		assertFalse(ComparisonOperators.GREATER.satisfied("1a", "1a"));
		assertFalse(ComparisonOperators.GREATER.satisfied("1a", "1b"));
	}
	
	@Test
	public void testGreaterOrEqualsTrue() {
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("bbb", "aaa"));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("2aa", "1bb"));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("aaa", "aaa"));
	}
	
	@Test
	public void testGreaterOrEqualsFalse() {
		assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied("11", "111"));
		assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied("1a", "1a1"));
		assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied("1a", "1b"));
	}
	
	@Test
	public void testEqualsTrue() {
		assertTrue(ComparisonOperators.EQUALS.satisfied("aaa", "aaa"));
		assertTrue(ComparisonOperators.EQUALS.satisfied("2a2", "2a2"));
		assertTrue(ComparisonOperators.EQUALS.satisfied("bab", "bab"));
	}
	
	@Test
	public void testEqualsFalse() {
		assertFalse(ComparisonOperators.EQUALS.satisfied("11", "111"));
		assertFalse(ComparisonOperators.EQUALS.satisfied("1a", "1a1"));
		assertFalse(ComparisonOperators.EQUALS.satisfied("1a", "1b"));
	}
	
	@Test
	public void testLikeTrue() {
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("AABAA", "AA*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("ABCDD", "AB*"));
	}
	
	@Test
	public void testLikeFalse() {
		assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
		assertFalse(ComparisonOperators.LIKE.satisfied("ABCC", "ABC*CC"));
		assertFalse(ComparisonOperators.LIKE.satisfied("AAAB", "AA*AAB"));
	}
	
	@Test
	public void testLikeMultipleWildcards() {
		assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("AAA", "AA*A*A"));
		assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("ABCC", "ABC*CC*"));
		assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("AAAB", "AA**AAB"));
	}
	
	@Test
	public void testLikeWithoutWildcards() {
		assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AAAA"));
		assertFalse(ComparisonOperators.LIKE.satisfied("ABCC", "ABC"));
		assertFalse(ComparisonOperators.LIKE.satisfied("AAAB", "AAB"));
		
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AAAA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("AABAA", "AABAA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("ABCDD", "ABCDD"));
	}
	
}
