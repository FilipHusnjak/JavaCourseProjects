package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class models complex polynomials in their standard form (i.e. x^3 + 2x + 3 is
 * represented as an array of factors [1, 0, 2, 3]). Factors are complex numbers
 * modeled by a {@link #Complex} class.
 * 
 * @author Filip Husnjak
 */
public class ComplexPolynomial {

	/**
	 * Factors of the polynomial in its standard form. Lower indexes represent lower
	 * potencies.
	 */
	private Complex[] factors;
		
	/**
	 * Constructs new {@code ComplexPolynomial} object with the given factors.
	 * 
	 * @param factors
	 *        factors of the polynomial
	 * @throws NullPointerException if the given array is {@code null}
	 */
	public ComplexPolynomial(Complex ... factors) {
		this.factors = Objects.requireNonNull(factors, "Given factors cannot be null!");
	}
	
	/**
	 * Returns an order of this polynomial.
	 * 
	 * @return an order of this polynomial
	 */
	public short order() {
		return (short) (factors.length - 1);
	}
	
	/**
	 * Multiplies {@code this} polynomial by the given one and returns new
	 * {@code ComplexPolynomial} as the result.
	 * 
	 * @param p
	 *        polynomial used in multiplication
	 * @return new {@code ComplexPolynomial} as the result of the multiplication
	 * @throws NullPointerException if the given polynomial is {@code null}
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Objects.requireNonNull(p, "Given polynomial cannot be null!");
		Complex[] result = new Complex[order() + p.order() + 1];
		Arrays.fill(result, Complex.ZERO);
		for (int i = 0; i < factors.length; ++i) {
			for (int j = 0; j < p.factors.length; ++j) {
				result[i + j] = result[i + j].add(factors[i].mul(p.factors[j]));
			}
		}
		return new ComplexPolynomial(result);
	}
	
	/**
	 * Returns the derivation of {@code this} polynomial as the new {@code ComplexPolynomial}
	 * object.
	 * 
	 * @return the derivation of {@code this} polynomial as the new {@code ComplexPolynomial}
	 *         object
	 */
	public ComplexPolynomial derive() {
		if (factors.length <= 1) return new ComplexPolynomial();
		Complex[] newFactors = new Complex[factors.length - 1];
		for (int i = 1; i < factors.length; ++i) {
			newFactors[i - 1] = factors[i].scale(i);
		}
		return new ComplexPolynomial(newFactors);
	}
	
	/**
	 * Calculates and returns the polynomial value at the given complex point.
	 * 
	 * @param z
	 *        complex point at which the value is to be calculated
	 * @return the polynomial value at the given complex point
	 * @throws NullPointerException if the given complex number is {@code null}
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z, "Given complex number cannot be null!");
		Complex result = factors[factors.length - 1];
		for (int i = factors.length - 2; i >= 0; --i) {
			result = result.mul(z).add(factors[i]);
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = factors.length - 1; i >= 0; --i) {
			sb.append("(" + factors[i].toString() + ")" + (i > 0 ? "*z^" + i : "") + "+");
		}
		return sb.toString().replaceAll("\\+$", "");
	}
	
}
