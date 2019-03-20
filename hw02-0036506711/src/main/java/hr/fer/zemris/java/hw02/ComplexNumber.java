package hr.fer.zemris.java.hw02;

import java.util.Objects;

/**
 * The class represents {@code ComplexNumber} and provides some useful methods
 * for working with such. Objects of this class are immutable, so you cannot
 * change its fields after creation.
 * 
 * @author Filip Husnjak
 */
public class ComplexNumber {

	/**
	 * Real part of this {@code ComplexNumber}
	 */
	private final double real;

	/**
	 * Imaginary part of this {@code ComplexNumber}
	 */
	private final double imaginary;
	
	/**
	 * Magnitude of this {@code ComplexNumber}
	 */
	private final double magnitude;
	
	/**
	 * Phase of this {@code ComplexNumber}
	 */
	private final double angle;
	
	/**
	 * Tells if this {@code ComplexNumber} has NaN values in either field
	 */
    private final boolean isNaN;
	
    /**
	 * Tells if this {@code ComplexNumber} has only finite values
	 */
	private final boolean isFinite;
	
	/**
	 * Represents {@code ComplexNumber} that has all fields set to {@code ZERO}
	 */
	public static final ComplexNumber ZERO = new ComplexNumber(0, 0);
	
	/**
	 * Represents {@code ComplexNumber} that has both real and imaginary part set to {@code Infinity}
	 */
	public static final ComplexNumber INFINITE = new ComplexNumber(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	
	/**
	 * Represents {@code ComplexNumber} that has both real and imaginary part set to {@code NaN}
	 */
	public static final ComplexNumber NaN = new ComplexNumber(Double.NaN, Double.NaN);

	/**
	 * Represents generic IllegalArgumentException message when parsing {@code String} to {@code ComplexNumber}
	 */
	private static final String GENERIC_PARSE_EXCEPTION = "Given string cannot be interpreted as complex number! Reason: ";

	/**
	 * Represents generic NullPointerException message when null is passed instead of {@code ComplexNumber}
	 */
	private static final String GENERIC_COMPLEX_NULLPOINTER_EXCEPTION = "Given ComplexNumber cannot be null!";
	
	/**
	 * Represents precision of the equals method when comparing double fields, its calculated by formula: <br>
	 * {@code Math.abs(this.field - other.field) < Math.pow(10, PRECISION_POWER)}, if it returns {@code true} fields are treated as same
	 */
	private static final int PRECISION_POWER = -6;
	
	/**
	 * Constructs {@code ComplexNumber} with given real and imaginary parths.
	 * 
	 * @param real      real part of {@code ComplexNumber}
	 * @param imaginary imaginary part of {@code ComplexNumber}
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
		
		isNaN = Double.isNaN(real) || Double.isNaN(imaginary);
		isFinite = Double.isFinite(real) && Double.isFinite(imaginary);
		
		this.magnitude = Math.sqrt(real * real + imaginary * imaginary);
		double angle = Math.atan2(imaginary, real);
		this.angle = (angle < 0 ? 2 * Math.PI + angle : angle);
	}

	/**
	 * Returns {@code ComplexNumber} by initializing its real part with given value
	 * and imaginary part with {@code 0}.
	 * 
	 * @param real real part of {@code ComplexNumber}
	 * @return {@code ComplexNumber} with specified real part and imaginary part set
	 *         to {@code 0}
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0.0f);
	}

	/**
	 * Returns {@code ComplexNumber} by initializing its imaginary part with given
	 * value and real part with {@code 0}.
	 * 
	 * @param imaginary imaginary part of {@code ComplexNumber}
	 * @return {@code ComplexNumber} with specified imaginary part and real part set
	 *         to {@code 0}
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0.0f, imaginary);
	}
	
	/**
	 * Returns {@code ComplexNumber} with specified {@code magnitude} and phase
	 * {@code angle}.
	 * 
	 * @param magnitude
	 *        magnitude of a new {@code ComplexNumber}
	 * @param angle
	 *        phase angle of a new {@code ComplexNumber}
	 * @return new {@code ComplexNumber} with specified magnitude and phase angle
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	/**
	 * Returns new {@code ComplexNumber} by converting given {@code String}.
	 * <br>
	 * Blanks in the expression are not allowed.
	 * 
	 * @param s {@code String} to be converted to {@code ComplexNumber}
	 * @return {@code ComplexNumber} converted from given {@code String}
	 * @throws IllegalArgumentException if the given {@code String} cannot be converted
	 *         to {@code ComplexNumber}
	 * @throws NullPointerException if given {@code String} is {@code null}
	 */
	public static ComplexNumber parse(String s) {
		Objects.requireNonNull(s, "Given String cannot be null!");
		s = s.trim();
		// Splits given string by either '+' or '-' sign into parts. Each part keeps the sign by using positive look ahead method.
		String[] parts = s.split("(?=(\\+|\\-))");
		if (parts.length == 0 || parts.length > 2) {
			throw new IllegalArgumentException(GENERIC_PARSE_EXCEPTION + 
					"Multiple math operators without proper meaning!");
		}
		if (parts.length == 1) {
			if (parts[0].trim().endsWith("i")) {
				return fromImaginary(checkImaginary(parts[0]));
			} else {
				return fromReal(checkReal(parts[0]));
			}
		}
		return new ComplexNumber(checkReal(parts[0]), checkImaginary(parts[1]));
	}
	
	/**
	 * Checks if given {@code String} can be interpreted as {@code RealPart} of 
	 * a {@code ComplexNumber} object and returns {@code RealPart} as double
	 * if it exits or throws exception.
	 * 
	 * @param s
	 *        {@code String} to be converted to {@code RealPart} as double
	 * @return double representation of a given real part
	 * @throws IllegalArgumentException if the given {@code String} cannot be
	 *         converted to {@code RealPart} of a {@code ComplexNumber}
	 */
	private static double checkReal(String s) {
		s = s.trim();
		if (s.isEmpty()) {
			throw new IllegalArgumentException(GENERIC_PARSE_EXCEPTION + 
					"Real part of a complex number cannot be empty String!");
		}
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(GENERIC_PARSE_EXCEPTION + 
					"Real part of a given complex number cannot be interpreted as a number!");
		}
	}
	
	/**
	 * Checks if given {@code String} can be interpreted as {@code ImaginaryPart} of 
	 * a {@code ComplexNumber} object and returns {@code ImaginaryPart} as double
	 * if it exits or throws exception. The given {@code String} should end with {@code i}
	 * or this method will throw exception.
	 * 
	 * @param s
	 *        {@code String} to be converted to {@code ImaginaryPart} as double
	 * @return double representation of a given imaginary part
	 * @throws IllegalArgumentException if the given {@code String} cannot be
	 *         converted to {@code ImaginaryPart} of a {@code ComplexNumber}
	 */
	private static double checkImaginary(String s) {
		s = s.trim();
		if (s.isEmpty()) {
			throw new IllegalArgumentException(GENERIC_PARSE_EXCEPTION + 
					"Imaginary part of a complex number cannot be empty String!");
		}
		if (!s.endsWith("i")) {
			throw new IllegalArgumentException(GENERIC_PARSE_EXCEPTION + 
					"Imaginary part of a complex number has to end with 'i'!");
		}
		try {
			String number = s.substring(0, s.length() - 1);
			// Triggers when only {@code i} is given.
			if (number.isEmpty()) {
				return 1.0f;
			}
			if (number.length() == 1) {
				if (number.startsWith("+")) {
					return 1.0f;
				} else if (number.startsWith("-")) {
					return -1.0f;
				}
			}
			return Double.parseDouble(number);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(GENERIC_PARSE_EXCEPTION +  
					"Imaginary part of a given complex number cannot be interpreted as a number!" + s);
		}
	}
	
	/**
	 * Returns double representation of real part of this {@code ComplexNumber}.
	 * 
	 * @return double representation of real part of this {@code ComplexNumber}
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Returns double representation of imaginary part of this {@code ComplexNumber}.
	 * 
	 * @return double representation of imaginary part of this {@code ComplexNumber}
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * Returns magnitude of this {@code ComplexNumber}.
	 * 
	 * @return magnitude of this {@code ComplexNumber}.
	 */
    public double getMagnitude() {
    	return magnitude;
    }
    
    /**
     * Returns phase angle of this {@code ComplexNumber}, angle is in the range of [0, 2PI].
     * 
     * @return phase angle of this {@code ComplexNumber} thats in the range of [0, 2PI]
     */
    public double getAngle() {
    	return angle;
    }

    /**
     * Returns the sum of the given {@code ComplexNumber} and this {@code ComplexNumber} object
     * as a new {@code ComplexNumber}.
     * <ul>
     *  <li>If either {@code this} or {@code c} has a {@code NaN} value
     *   in either part, {@link #NaN} is returned.
     *  </li>
     * </ul>
     * 
     * @param c
     *        the {@code ComplexNumber} object to be added
     * @return the result of addition of the given and this {@code ComplexNumber} objects
     * @throws NullPointerException if given {@code ComplexNumber} is {@code null}
     */
    public ComplexNumber add(ComplexNumber c) {
    	Objects.requireNonNull(c, GENERIC_COMPLEX_NULLPOINTER_EXCEPTION);
    	if (this.isNaN || c.isNaN) {
    		return NaN;
    	}
    	return new ComplexNumber(this.getReal() + c.getReal(), this.getImaginary() + c.getImaginary());
    }
    
    /**
     * Returns the difference of this {@code ComplexNumber} object and the argument 
     * as a new {@code ComplexNumber}.
     * <ul>
     *  <li>If either {@code this} or {@code c} has a {@code NaN} value
     *   in either part, {@link #NaN} is returned.
     *  </li>
     * </ul>
     * 
     * @param c
     *        the second {@code ComplexNumber} object to subtract from this {@code ComplexNumber}
     * @return the result of subtraction of the given {@code ComplexNumber} from this {@code ComplexNumber}
     * @throws NullPointerException if given {@code ComplexNumber} is {@code null}
     */
    public ComplexNumber sub(ComplexNumber c) {
    	Objects.requireNonNull(c, GENERIC_COMPLEX_NULLPOINTER_EXCEPTION);
    	if (this.isNaN || c.isNaN) {
    		return NaN;
    	}
    	return new ComplexNumber(this.getReal() - c.getReal(), this.getImaginary() - c.getImaginary());
    }
    
    /**
     * Returns the product of this {@code ComplexNumber} and given {@code ComplexNumber} object
     * as a new {@code ComplexNumber}.
     * <ul>
     *  <li>If either {@code this} or {@code c} has a {@code NaN} value
     *   in either part, {@link #NaN} is returned.
     *  </li>
     *  <li>If either {@code this} or {@code c} has a {@code Infinite} value
     *   in either part, {@link #INFINITE} is returned.
     *  </li>
     * </ul>
     * 
     * @param c
     *        the factor to be multiplied by {@code this} {@code ComplexNumber}
     * @return the product of this {@code ComplexNumber} and given {@code ComplexNumber}
     * @throws NullPointerException if given {@code ComplexNumber} is {@code null}
     */
    public ComplexNumber mul(ComplexNumber c) {
    	Objects.requireNonNull(c, GENERIC_COMPLEX_NULLPOINTER_EXCEPTION);
    	if (this.isNaN || c.isNaN) {
    		return NaN;
    	}
    	if (!this.isFinite || !c.isFinite) {
    		return INFINITE;
    	}
        double newReal = this.getReal() * c.getReal() - this.getImaginary() * c.getImaginary();
        double newImaginary = this.getReal() * c.getImaginary() + this.getImaginary() * c.getReal();
        return new ComplexNumber(newReal, newImaginary);
    }
    
    /**
     * Returns a {@code ComplexNumber} whose value is reciprocal value of the given {@code ComplexNumber}.
     * <ul>
     *  <li>If {@code c} has a {@code NaN} value in either part, {@link #NaN} is returned.
     *  </li>
     *  <li>If {@code c} equals {@link #ZERO}, {@link #INFINITE} is returned.
     *  </li>
     *  <li>If {@code c} is {@code Infinite} {@link #ZERO} is returned.
     *  </li>
     * </ul>
     * @param c
     *        given {@code ComplexNumber} whose reciprocal value is to be calculated
     * @return reciprocal value of a given {@code ComplexNumber} as a new {@code ComplexNumber}
     * @throws NullPointerException if given {@code ComplexNumber} is {@code null}
     */
    private static ComplexNumber reciprocal(ComplexNumber c) {
    	Objects.requireNonNull(c, GENERIC_COMPLEX_NULLPOINTER_EXCEPTION);
    	if (c.isNaN) {
    		return NaN;
    	}
    	if (!c.isFinite) {
    		return ZERO;
    	}
    	if (c.getReal() == 0 && c.getImaginary() == 0) {
    		return INFINITE;
    	}
    	double squaredMagnitude = c.getMagnitude() * c.getMagnitude();
    	return new ComplexNumber(c.getReal() /squaredMagnitude, -c.getImaginary() / squaredMagnitude);
    }
    
    /**
     * Returns a {@code ComplexNumber} whose value is {@code (this / c)}.
     * <ul>
     *  <li>If either {@code this} or {@code c} has a {@code NaN} value
     *   in either part, {@link #NaN} is returned.
     *  </li>
     *  <li>If {@code c} equals {@link #ZERO}, {@link #NaN} is returned.
     *  </li>
     *  <li>If {@code this} and {@code c} are both infinite,
     *   {@link #NaN} is returned.
     *  </li>
     *  <li>If {@code this} is finite (i.e., has no {@code Infinite} or
     *   {@code NaN} parts) and {@code divisor} is infinite (one or both parts
     *   infinite), {@link #ZERO} is returned.
     *  </li>
     *  <li>If {@code this} is infinite and {@code c} is finite,
     *   {@code NaN} values are returned
     *  </li>
     * </ul>
     *
     * @param  c 
     *         value by which this {@code Complex} is to be divided
     * @return {@code ComplexNumber} whose value is {@code this / c}.
     * @throws NullPointerException if given {@code ComplexNumber} is {@code null}
     */
    public ComplexNumber div(ComplexNumber c) {
    	Objects.requireNonNull(c, GENERIC_COMPLEX_NULLPOINTER_EXCEPTION);
    	if (this.isNaN || c.isNaN) {
    		return NaN;
    	}
    	if (this.isFinite && !c.isFinite) {
    		return ZERO;
    	}
    	if (!this.isFinite || !c.isFinite) {
    		return NaN;
    	}
    	if (c.getReal() == 0 && c.getImaginary() == 0) {
    		return NaN;
    	}
    	return this.mul(reciprocal(c));
    }
    
    /**
     * Returns {@code this} {@code ComplexNumber} raised to the power of {@code n}, 
     * as new {@code ComplexNumber}.
     * <ul>
     *  <li>If {@code this} has a {@code NaN} value in either part, 
     *  {@link #NaN} is returned.
     *  </li>
     *  <li>If {@code this} has a {@code Infinite} value in either part, 
     *  {@link #INFINITE} is returned.
     *  </li>
     * </ul>
     * 
     * @param n
     *        the exponent to which this {@code ComplexNumber} is to be raised
     * @return {@code this} {@code ComplexNumber} raised to the power of {@code n}
     */
    public ComplexNumber power(int n) {
    	if (isNaN) {
    		return NaN;
    	}
    	if (!isFinite) {
    		return INFINITE;
    	}
    	double newPhase = angle * n;
    	double newMagnitude = Math.pow(magnitude, n);
    	return fromMagnitudeAndAngle(newMagnitude, newPhase);
    }
    
    /**
     * Computes the {@code n-th} root of this {@code ComplexNumber}. Degree of the
     * root has to be positive number.
     * <ul>
     *  <li>If {@code this} has a {@code NaN} value in either part, 
     *  {@link #NaN} is returned.
     *  </li>
     *  <li>If {@code this} has a {@code Infinite} value in either part, 
     *  {@link #INFINITE} is returned.
     *  </li>
     * </ul>
     * 
     * @param n
     *        degree of a root, has to be positive or exception will be thrown
     * @return {@code n-th} root of {@code this} {@code ComplexNumber}
     * @throws IllegalArgumentException if the given number is less than or equal to
     *         {@code ZERO}
     */
    public ComplexNumber[] root(int n) {
    	if (n <= 0) {
    		throw new IllegalArgumentException("Given number has to be positive!");
    	}
    	if (isNaN) {
    		return new ComplexNumber[] {NaN};
    	}
    	if (!isFinite) {
    		return new ComplexNumber[] {INFINITE};
    	}
    	ComplexNumber[] roots = new ComplexNumber[n];
    	double newMagnitude = Math.pow(magnitude, 1.0f / n);
    	for (int i = 0; i < roots.length; ++i) {
    		roots[i] = fromMagnitudeAndAngle(newMagnitude, (angle + 2 * i * Math.PI) / n);
    	}
    	return roots;
    }
    
    /**
     * {@inheritDoc}}
     */
    @Override
    public String toString() {
    	if (imaginary < 0) {
    		return String.format("%.4f%.4fi", real, imaginary);
    	}
    	else {
    		return String.format("%.4f+%.4fi", real, imaginary);
    	}
    }

    /**
     * {@inheritDoc}}
     */
	@Override
	public int hashCode() {
		return Objects.hash(angle, imaginary, isFinite, isNaN, magnitude, real);
	}

	/**
     * {@inheritDoc}}
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ComplexNumber))
			return false;
		ComplexNumber other = (ComplexNumber) obj;
		return Math.abs(angle - other.angle) < Math.pow(10, PRECISION_POWER)
				&& Math.abs(imaginary - other.imaginary) < Math.pow(10, PRECISION_POWER)
				&& isFinite == other.isFinite && isNaN == other.isNaN
				&& Math.abs(magnitude - other.magnitude) < Math.pow(10, PRECISION_POWER)
				&& Math.abs(real - other.real) < Math.pow(10, PRECISION_POWER);
	}

	/**
	 * Returns {@code true} if this {@code ComplexNumber} is {@code NaN}.
	 * 
	 * @return {@code true} if this {@code ComplexNumber} is {@code NaN}
	 */
	public boolean isNaN() {
		return isNaN;
	}

	/**
	 * Returns {@code true} if this {@code ComplexNumber} is {@code finite}.
	 * 
	 * @return {@code true} if this {@code ComplexNumber} is {@code finite}
	 */
	public boolean isFinite() {
		return isFinite;
	}
	
}
