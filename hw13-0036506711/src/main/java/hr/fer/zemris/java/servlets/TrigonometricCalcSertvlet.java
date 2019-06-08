package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to calculate trigonometric values of range specified by the given
 * parameters. Parameters are:
 * <ul>
 * <li> a - initial value whose sine and cosine are to be calculated
 * <li> b - last value whose sine and cosine are to be calculated
 * </ul>
 * Parameters are treated as degrees.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/trigonometric")
public class TrigonometricCalcSertvlet extends HttpServlet {

	private static final long serialVersionUID = -1174800810120543005L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		int a = getValue(0, req.getParameter("a"));
		int b =  getValue(360, req.getParameter("b"));
		// If a is greater swap a and b
		if (a > b) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		if (b > a + 720) b = a + 720;
		List<SinCosValues> trigValues = new LinkedList<>();
		for (int i = a; i <= b; ++i) {
			trigValues.add(new SinCosValues(i));
		}
		// Store values as list
		req.setAttribute("trigValues", trigValues);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
	/**
	 * Returns the given value if its legal or default value otherwise.
	 * 
	 * @param defaultValue
	 *        default value to be returned if the given value is illegal
	 * @param value
	 *        {@link String} to be transformed to int if possible
	 * @return the given value if its legal or default value otherwise
	 */
	private int getValue(int defaultValue, String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * Helper class which holds sine and cosine value of the specified angle.
	 * 
	 * @author Filip Husnjak
	 */
	public static class SinCosValues {
		
		/**
		 * Angle whose sine and cosine value are calculated
		 */
		private final int angle;
		
		/**
		 * Sine value of the angle
		 */
		private final double sin;
		
		/**
		 * Cosine value of the angle
		 */
		private final double cos;

		/**
		 * Constructs new {@link SinCosValues} from specified angle.
		 * 
		 * @param angle
		 *        angle whose sine and cosine values are to be calculated
		 */
		private SinCosValues(int angle) {
			this.angle = angle;
			this.sin = Math.sin(Math.toRadians(angle));
			this.cos = Math.cos(Math.toRadians(angle));
		}

		/**
		 * Returns an angle whose sine and cosine values are calculated.
		 * 
		 * @return an angle whose sine and cosine values are calculated
		 */
		public int getAngle() {
			return angle;
		}
		
		/**
		 * Returns sine value of the angle.
		 * 
		 * @return sine value of the angle
		 */
		public double getSin() {
			return sin;
		}

		/**
		 * Returns cosine value of the angle.
		 * 
		 * @return cosine value of the angle
		 */
		public double getCos() {
			return cos;
		}
		
	}
	
}
