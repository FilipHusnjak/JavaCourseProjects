package hr.fer.zemris.math;

import java.util.Objects;

/**
 * This class models complex polynomials in rooted form (i.e. z0 * (z - z1) * (z - z2) 
 * is represented as an array of roots [z1, z2]). Roots are complex numbers
 * modeled by a {@link #Complex} class.
 * 
 * @author Filip Husnjak
 */
public class ComplexRootedPolynomial {

	/**
	 * Roots of this polynomial
	 */
	private Complex[] roots;
	
	/**
	 * Constant of this polynomial
	 */
	private Complex constant;
	
	/**
	 * Constructs new {@code ComplexRootedPolynomial} with specified constant
	 * and roots.
	 * 
	 * @param constant
	 *        constant of {@code this} polynomial
	 * @param roots
	 *        roots of {@code this} polynomial
	 * @throws NullPointerException if the given constant or roots are {@code null}
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.roots = Objects.requireNonNull(roots, "Given roots cannot be null!");
		this.constant = Objects.requireNonNull(constant, "Given constant cannot be null!");
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
		Complex result = constant;
		for (Complex c: roots) {
			result = result.mul(z.sub(c));
		}
		return result;
	}
	
	/**
	 * Transforms {@code this} polynomial to the normal form and returns new
	 * {@code ComplexPolynomial} as the result.
	 * 
	 * @return new {@code ComplexPolynomial} as the result of transformation to standard form
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(constant);
		for (int i = 0; i < roots.length; ++i) {
			result = result.multiply(new ComplexPolynomial(roots[i].mul(Complex.ONE_NEG),
					Complex.ONE));
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append("(" + constant.toString() + ")");
		for (int i = 0; i < roots.length; ++i) {
			sb.append("*(z-(" + roots[i].toString() + "))");
		}
		return sb.toString();
	}
	
	/**
	 * Finds and returns the closes root to the given complex number. If there are
	 * no roots whose distance from the given complex number is less than given
	 * threshold, {@code -1} is returned.
	 * 
	 * @param z
	 *        complex number to which the closest root is to be determined
	 * @param treshold
	 *        minimum distance between root and complex number
	 * @return the closes root to the given complex number
	 * @throws NullPointerException if the given complex number is {@code null}
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		Objects.requireNonNull(z, "Given complex number cannot be null!");
		int index = -1;
		double minTreshhold = treshold;
		for (int i = 0; i < roots.length; ++i) {
			double dif = Math.abs(z.sub(roots[i]).getMagnitude());
			if (dif < minTreshhold) {
				index = i;
				minTreshhold = dif;
			}
		}
		return index;
	}
	
}
