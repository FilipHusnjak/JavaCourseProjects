package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class ComplexNumberTest {
	
	/**
	 * Helper class for easier testing.
	 * 
	 * @author Filip Husnjak
	 */
	private static class DataTestClass {
		
		/**
		 * {@code String} to be parsed
		 */
		private String parse;
		
		/**
		 * expected real part
		 */
		private double real;
		
		/**
		 * expected imaginary part
		 */
		private double imaginary;
		
		/**
		 * Constructs object using fields
		 * 
		 * @param parse
		 * @param real
		 * @param imaginary
		 */
		public DataTestClass(String parse, double real, double imaginary) {
			super();
			this.parse = parse;
			this.real = real;
			this.imaginary = imaginary;
		}
		
		/**
		 * Returns true if the {@code ComplexNumber} created by {@code ComplexNumber.parse(parse)}
		 * has the expected real and imaginary part.
		 * 
		 * @return true if the {@code ComplexNumber} created by {@code ComplexNumber.parse(parse)}
		 * has the expected real and imaginary part.
		 */
		public boolean compare() {
			return ComplexNumber.parse(parse).equals(new ComplexNumber(real, imaginary));
		}
		
	}
	
	@Test
	public void testConstructorNormalData() {
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(2, 5 * Math.PI / 3), new ComplexNumber(1, -Math.sqrt(3)));
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(3, 2 * Math.PI / 3), new ComplexNumber(-1.5, 3 * Math.sqrt(3) / 2));
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(4, Math.PI / 4), new ComplexNumber(2 * Math.sqrt(2), 2 * Math.sqrt(2)));
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(5, Math.PI / 2), new ComplexNumber(0, 5));
	}
	
	@Test
	public void testConstructorInfinite() {
		assertFalse(new ComplexNumber(Double.POSITIVE_INFINITY, 1).isFinite());
		assertFalse(new ComplexNumber(1, Double.NEGATIVE_INFINITY).isFinite());
		assertFalse(new ComplexNumber(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).isFinite());
	}
	
	@Test
	public void testConstructorNaN() {
		assertTrue(new ComplexNumber(Double.NaN, 1).isNaN());
		assertTrue(new ComplexNumber(1, Double.NaN).isNaN());
		assertTrue(new ComplexNumber(Double.NaN, Double.NaN).isNaN());
	}
	
	@Test
	public void testParserNormalData() {
		DataTestClass[] testDataOnePart = new DataTestClass[] {
				new DataTestClass("1", 1, 0),
				new DataTestClass("378", 378, 0),
				new DataTestClass("-420", -420, 0),
				new DataTestClass("4.20", 4.2, 0),
				new DataTestClass("+4.20", 4.2, 0),
				
				new DataTestClass("i", 0, 1),
				new DataTestClass("+i", 0, 1),
				new DataTestClass("-i", 0, -1),
				
				new DataTestClass("420i", 0, 420),
				new DataTestClass("+69i", 0, 69),
				new DataTestClass("4.2i", 0, 4.2),
				new DataTestClass("-4.20i", 0, -4.2)
		};
		
		DataTestClass[] testDataTwoParts = new DataTestClass[] {
				new DataTestClass("-4.2-3.14i", -4.2, -3.14),
				new DataTestClass("34+2i", 34, 2),
				new DataTestClass("2.6-3i", 2.6, -3),
				new DataTestClass("-5.65+78i", -5.65, 78),
				
				new DataTestClass("1+i", 1, 1),
				new DataTestClass("1-i", 1, -1),
				new DataTestClass("-1+i", -1, 1),
				new DataTestClass("-1-i", -1, -1)
		};
		
		assertTrue(testDataOnePart[0].compare());
		assertTrue(testDataOnePart[1].compare());
		assertTrue(testDataOnePart[2].compare());
		assertTrue(testDataOnePart[3].compare());
		assertTrue(testDataOnePart[4].compare());
		
		assertTrue(testDataOnePart[5].compare());
		assertTrue(testDataOnePart[6].compare());
		assertTrue(testDataOnePart[7].compare());
		
		assertTrue(testDataOnePart[8].compare());
		assertTrue(testDataOnePart[9].compare());
		assertTrue(testDataOnePart[10].compare());
		assertTrue(testDataOnePart[11].compare());
		
		
		assertTrue(testDataTwoParts[0].compare());
		assertTrue(testDataTwoParts[1].compare());
		assertTrue(testDataTwoParts[2].compare());
		assertTrue(testDataTwoParts[3].compare());
		
		assertTrue(testDataTwoParts[4].compare());
		assertTrue(testDataTwoParts[5].compare());
		assertTrue(testDataTwoParts[6].compare());
		assertTrue(testDataTwoParts[7].compare());
	}
	
	@Test
	public void testParserWrongData() {
		String[] testWrongData = new String[] {
			"i420",
			"-i420",
			"i4.20",
			"-i4.20",
			"+-i420",
			"--i4.20",
			"++i4.20",
			"-24++420i",
			"+2.8-+56i",
			"2.9-i55",
			"28+29",
			"28i-"
		};
		
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[0]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[1]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[2]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[3]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[4]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[5]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[6]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[7]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[8]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[9]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[10]));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(testWrongData[11]));
	}
	
	@Test
	public void testFromReal() {
		ComplexNumber[] testDataReal = new ComplexNumber[] {
				new ComplexNumber(2.0, 0),
				new ComplexNumber(-1.0, 0),
				new ComplexNumber(3.14, 0),
				new ComplexNumber(200, 0),
				new ComplexNumber(4, 0),
		};
		
		assertEquals(testDataReal[0], ComplexNumber.fromReal(2.0));
		assertEquals(testDataReal[1], ComplexNumber.fromReal(-1.0));
		assertEquals(testDataReal[2], ComplexNumber.fromReal(3.14));
		assertEquals(testDataReal[3], ComplexNumber.fromReal(200.0));
		assertEquals(testDataReal[4], ComplexNumber.fromReal(4.0));
	}
	
	@Test
	public void testFromImaginary() {
		ComplexNumber[] testDataReal = new ComplexNumber[] {
				new ComplexNumber(0, 2.0),
				new ComplexNumber(0, -1.0),
				new ComplexNumber(0, 3.14),
				new ComplexNumber(0, 200),
				new ComplexNumber(0, 4),
		};
		
		assertEquals(testDataReal[0], ComplexNumber.fromImaginary(2.0));
		assertEquals(testDataReal[1], ComplexNumber.fromImaginary(-1.0));
		assertEquals(testDataReal[2], ComplexNumber.fromImaginary(3.14));
		assertEquals(testDataReal[3], ComplexNumber.fromImaginary(200.0));
		assertEquals(testDataReal[4], ComplexNumber.fromImaginary(4.0));
	}
	
	public void testFromMagnitudeAndAngle() {
		ComplexNumber[] testDataReal = new ComplexNumber[] {
				new ComplexNumber(Math.sqrt(3), 1),
				new ComplexNumber(Math.sqrt(2), Math.sqrt(2)),
				new ComplexNumber(0, 2),
				new ComplexNumber(-2, 0),
				new ComplexNumber(1, -Math.sqrt(3))
		};
		
		assertEquals(testDataReal[0], ComplexNumber.fromMagnitudeAndAngle(2, Math.PI / 6));
		assertEquals(testDataReal[1], ComplexNumber.fromMagnitudeAndAngle(2, Math.PI / 4));
		assertEquals(testDataReal[2], ComplexNumber.fromMagnitudeAndAngle(2, Math.PI / 2));
		assertEquals(testDataReal[3], ComplexNumber.fromMagnitudeAndAngle(2, Math.PI));
		assertEquals(testDataReal[4], ComplexNumber.fromMagnitudeAndAngle(2, -Math.PI / 3));
	}
	
	@Test
	public void testAddNotNaNData() {
		assertEquals(new ComplexNumber(2, 2), new ComplexNumber(1, 1).add(new ComplexNumber(1, 1)));
		assertEquals(new ComplexNumber(1.14, 2.5), new ComplexNumber(1, 2).add(new ComplexNumber(0.14, 0.5)));
		assertEquals(new ComplexNumber(Math.sqrt(3), 0), new ComplexNumber(0, 0).add(new ComplexNumber(Math.sqrt(3), 0)));
		assertEquals(new ComplexNumber(Math.sqrt(2), 1), new ComplexNumber(Math.sqrt(2) / 2, -1).add(new ComplexNumber(Math.sqrt(2) / 2, 2)));
		assertEquals(new ComplexNumber(Math.sqrt(3), Math.sqrt(2)), new ComplexNumber(Math.sqrt(3) / 3, Math.sqrt(2)).add(new ComplexNumber(2 * Math.sqrt(3) / 3, 0)));
	}
	
	@Test
	public void testAddNaNData() {
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, 1).add(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, Double.NaN).add(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).add(new ComplexNumber(Double.NaN, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).add(new ComplexNumber(1, Double.NaN)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, Double.NaN).add(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).add(new ComplexNumber(Double.NaN, Double.NaN)));
	}
	
	@Test
	public void testAddNull() {
		assertThrows(NullPointerException.class, () -> new ComplexNumber(1, 1).add(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(Double.NaN, 1).add(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(1, Double.NaN).add(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(Double.NaN, Double.NaN).add(null));
	}
	
	@Test
	public void testSubNotNaNData() {
		assertEquals(new ComplexNumber(2, 2), new ComplexNumber(3, 3).sub(new ComplexNumber(1, 1)));
		assertEquals(new ComplexNumber(1.14, 2.5), new ComplexNumber(2.14, 3).sub(new ComplexNumber(1, 0.5)));
		assertEquals(new ComplexNumber(Math.sqrt(3), 0), new ComplexNumber(0, 0).sub(new ComplexNumber(-Math.sqrt(3), 0)));
		assertEquals(new ComplexNumber(Math.sqrt(2), 1), new ComplexNumber(3 * Math.sqrt(2) / 2, 3).sub(new ComplexNumber(Math.sqrt(2) / 2, 2)));
		assertEquals(new ComplexNumber(Math.sqrt(3), Math.sqrt(2)), new ComplexNumber(5 * Math.sqrt(3) / 3, Math.sqrt(2)).sub(new ComplexNumber(2 * Math.sqrt(3) / 3, 0)));
	}
	
	@Test
	public void testSubNaNData() {
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, 1).sub(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, Double.NaN).sub(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).sub(new ComplexNumber(Double.NaN, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).sub(new ComplexNumber(1, Double.NaN)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, Double.NaN).sub(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).sub(new ComplexNumber(Double.NaN, Double.NaN)));
	}
	
	@Test
	public void testSubNull() {
		assertThrows(NullPointerException.class, () -> new ComplexNumber(1, 1).sub(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(Double.NaN, 1).sub(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(1, Double.NaN).sub(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(Double.NaN, Double.NaN).sub(null));
	}
	
	@Test
	public void testMulFiniteAndNotNaNData() {
		assertEquals(new ComplexNumber(23, 14), new ComplexNumber(4, -3).mul(new ComplexNumber(2, 5)));
		assertEquals(new ComplexNumber(-26, -78), new ComplexNumber(7, -9).mul(new ComplexNumber(4, -6)));
		assertEquals(new ComplexNumber(6.626379275, -1.044556221), new ComplexNumber(Math.sqrt(3), Math.sqrt(2)).mul(new ComplexNumber(Math.sqrt(4), -Math.sqrt(5))));
		assertEquals(new ComplexNumber(5, 0), new ComplexNumber(Math.sqrt(2), Math.sqrt(3)).mul(new ComplexNumber(Math.sqrt(2), -Math.sqrt(3))));
		assertEquals(new ComplexNumber(9, 0), new ComplexNumber(Math.sqrt(4), Math.sqrt(5)).mul(new ComplexNumber(Math.sqrt(4), -Math.sqrt(5))));
	}
	
	@Test
	public void testMulNaNData() {
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, 1).mul(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, Double.NaN).mul(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).mul(new ComplexNumber(Double.NaN, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).mul(new ComplexNumber(1, Double.NaN)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, Double.NaN).mul(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).mul(new ComplexNumber(Double.NaN, Double.NaN)));
	}
	
	@Test
	public void testMulInfiniteData() {
		assertEquals(ComplexNumber.INFINITE, new ComplexNumber(Double.POSITIVE_INFINITY, 1).mul(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.INFINITE, new ComplexNumber(1, Double.POSITIVE_INFINITY).mul(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.INFINITE, new ComplexNumber(1, 1).mul(new ComplexNumber(Double.NEGATIVE_INFINITY, 1)));
		assertEquals(ComplexNumber.INFINITE, new ComplexNumber(1, 1).mul(new ComplexNumber(1, Double.NEGATIVE_INFINITY)));
		assertEquals(ComplexNumber.INFINITE, new ComplexNumber(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).mul(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.INFINITE, new ComplexNumber(1, 1).mul(new ComplexNumber(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)));
	}
	
	@Test
	public void testMulNull() {
		assertThrows(NullPointerException.class, () -> new ComplexNumber(1, 1).mul(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(Double.NaN, 1).mul(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(1, Double.NaN).mul(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(Double.NaN, Double.NaN).mul(null));
	}
	
	@Test
	public void testDivFiniteAndNotNaNData() {
		assertEquals(new ComplexNumber(-7.0f / 29, -26.0f / 29), new ComplexNumber(4, -3).div(new ComplexNumber(2, 5)));
		assertEquals(new ComplexNumber(41.0f / 26, 3.0f / 26), new ComplexNumber(7, -9).div(new ComplexNumber(4, -6)));
		assertEquals(new ComplexNumber(0.033535995, 0.7446011634), new ComplexNumber(Math.sqrt(3), Math.sqrt(2)).div(new ComplexNumber(Math.sqrt(4), -Math.sqrt(5))));
		assertEquals(new ComplexNumber(-1.0f / 5, 0.9797958971), new ComplexNumber(Math.sqrt(2), Math.sqrt(3)).div(new ComplexNumber(Math.sqrt(2), -Math.sqrt(3))));
		assertEquals(new ComplexNumber(-1.0f / 9, 0.99380799), new ComplexNumber(Math.sqrt(4), Math.sqrt(5)).div(new ComplexNumber(Math.sqrt(4), -Math.sqrt(5))));
	}
	
	@Test
	public void testDivNaNData() {
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, 1).div(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, Double.NaN).div(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).div(new ComplexNumber(Double.NaN, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).div(new ComplexNumber(1, Double.NaN)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, Double.NaN).div(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).div(new ComplexNumber(Double.NaN, Double.NaN)));
	}
	
	@Test
	public void testDivInfiniteData() {
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.POSITIVE_INFINITY, 1).div(new ComplexNumber(1, 1)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, Double.POSITIVE_INFINITY).div(new ComplexNumber(1, 1)));
		
		assertEquals(ComplexNumber.ZERO, new ComplexNumber(1, 1).div(new ComplexNumber(Double.NEGATIVE_INFINITY, 1)));
		assertEquals(ComplexNumber.ZERO, new ComplexNumber(1, 1).div(new ComplexNumber(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)));
		assertEquals(ComplexNumber.ZERO, new ComplexNumber(1, 1).div(new ComplexNumber(Double.NEGATIVE_INFINITY, 1)));
	}
	
	@Test
	public void testDivZero() {
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 1).div(new ComplexNumber(0, 0)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(0, 1).div(new ComplexNumber(0, 0)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(0, 0).div(new ComplexNumber(0, 0)));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, 0).div(new ComplexNumber(0, 0)));
	}
	
	@Test
	public void testDivNull() {
		assertThrows(NullPointerException.class, () -> new ComplexNumber(1, 1).div(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(Double.NaN, 1).div(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(1, Double.NaN).div(null));
		assertThrows(NullPointerException.class, () -> new ComplexNumber(Double.NaN, Double.NaN).div(null));
	}
	
	@Test
	public void testPowerFiniteAndNotNaNData() {
		assertEquals(new ComplexNumber(-7 * Math.sqrt(2), 3 * Math.sqrt(3)), new ComplexNumber(Math.sqrt(2), Math.sqrt(3)).power(3));
		assertEquals(new ComplexNumber(-142, -65), new ComplexNumber(2, 5).power(3));
		assertEquals(new ComplexNumber(3.0f / 25, -4.0f / 25), new ComplexNumber(2, 1).power(-2));
		assertEquals(new ComplexNumber(-142, -65), new ComplexNumber(Math.sqrt(4), 5).power(3));
		assertEquals(new ComplexNumber(0, -8), new ComplexNumber(1, 1).power(6));
	}
	
	@Test
	public void testPowerNaNData() {
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, 1).power(1));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(1, Double.NaN).power(2));
		assertEquals(ComplexNumber.NaN, new ComplexNumber(Double.NaN, Double.NaN).power(-2));
	}
	
	@Test
	public void testPowerInfiniteData() {
		assertEquals(ComplexNumber.INFINITE, new ComplexNumber(Double.POSITIVE_INFINITY, 1).power(1));
		assertEquals(ComplexNumber.INFINITE, new ComplexNumber(1, Double.POSITIVE_INFINITY).power(-2));
		assertEquals(ComplexNumber.INFINITE, new ComplexNumber(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).power(2));
	}
	
	@Test
	public void testRootFiniteAndNotNaNData() {
		ComplexNumber[] results = new ComplexNumber[] {
			new ComplexNumber(1.25103577, 0.38063821),
			new ComplexNumber(-0.95516024, 0.89310965),
			new ComplexNumber(-0.29587552, -1.27374786)
		};
		assertArrayEquals(results, new ComplexNumber(Math.sqrt(2), Math.sqrt(3)).root(3));
		
		results = new ComplexNumber[] {
				new ComplexNumber(1.61663888, 0.67734447),
				new ComplexNumber(-1.39491696, 1.06137810),
				new ComplexNumber(-0.22172191, -1.73872258)
		};
		assertArrayEquals(results, new ComplexNumber(2, 5).root(3));
		
		results = new ComplexNumber[] {
				new ComplexNumber(1.45534669, 0.34356074),
				new ComplexNumber(-1.45534669, -0.34356074),
		};
		assertArrayEquals(results, new ComplexNumber(2, 1).root(2));
		
		results = new ComplexNumber[] {
				new ComplexNumber(1.61663888, 0.67734447),
				new ComplexNumber(-1.39491696, 1.06137810),
				new ComplexNumber(-0.22172191, -1.73872258)
		};
		assertArrayEquals(results, new ComplexNumber(Math.sqrt(4), 5).root(3));
		
		results = new ComplexNumber[] {
				new ComplexNumber(1.05039924, 0.13828768),
				new ComplexNumber(0.40543897, 0.97881626),
				new ComplexNumber(-0.64496026, 0.84052858),
				new ComplexNumber(-1.05039924, -0.13828768),
				new ComplexNumber(-0.40543897, -0.97881626),
				new ComplexNumber(0.64496026, -0.84052858)
		};
		assertArrayEquals(results, new ComplexNumber(1, 1).root(6));
	}
	
	@Test
	public void testRootNaNData() {
		ComplexNumber[] NaNOnly = new ComplexNumber[] {
			ComplexNumber.NaN	
		};
		assertArrayEquals(NaNOnly, new ComplexNumber(Double.NaN, 1).root(1));
		assertArrayEquals(NaNOnly, new ComplexNumber(1, Double.NaN).root(2));
		assertArrayEquals(NaNOnly, new ComplexNumber(Double.NaN, Double.NaN).root(2));
	}
	
	@Test
	public void testRootInfiniteData() {
		ComplexNumber[] infOnly = new ComplexNumber[] {
				ComplexNumber.INFINITE	
		};
		assertArrayEquals(infOnly, new ComplexNumber(Double.POSITIVE_INFINITY, 1).root(1));
		assertArrayEquals(infOnly, new ComplexNumber(1, Double.POSITIVE_INFINITY).root(2));
		assertArrayEquals(infOnly, new ComplexNumber(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).root(2));
	}
	
	@Test
	public void testRootNegativeArgument() {
		assertThrows(IllegalArgumentException.class, () -> new ComplexNumber(1, 1).root(0));
		assertThrows(IllegalArgumentException.class, () -> new ComplexNumber(1, 1).root(-1));
		assertThrows(IllegalArgumentException.class, () -> new ComplexNumber(Double.NaN, 1).root(0));
		assertThrows(IllegalArgumentException.class, () -> new ComplexNumber(Double.POSITIVE_INFINITY, 1).root(-1));
	}
	
}
