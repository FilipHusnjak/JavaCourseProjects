package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Web worker which calculates the sum of the given parameters 'a' and 'b'.
 * If parameters are not given values '1' and '2' are used by default.
 * 
 * @author Filip Husnjak
 */
public class SumWorker implements IWebWorker {
	
	@Override
	public void processRequest(RequestContext context) throws Exception {
		int a = getParam(context.getParameter("a"), 1);
		int b = getParam(context.getParameter("b"), 2);
		int sum = a + b;
		context.setTemporaryParameter("varA", String.valueOf(a));
		context.setTemporaryParameter("varB", String.valueOf(b));
		context.setTemporaryParameter("zbroj", String.valueOf(sum));
		context.setTemporaryParameter("imgName", sum % 2 == 0 ? "evenSum" : "oddSum");
		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
	}

	/**
	 * Returns parameter if its not {@code null} and can be parsed to {@link Integer},
	 * otherwise this method returns default value.
	 * 
	 * @param parameter
	 *        parameter to be checked
	 * @param defaultValue
	 *        default value to be returned if the parameter is illegal
	 * @return parameter if its not {@code null} and can be parsed to {@link Integer},
	 *         otherwise this method returns default value
	 */
	private int getParam(String parameter, int defaultValue) {
		if (parameter == null) return defaultValue;
		try {
			return Integer.parseInt(parameter);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
}
