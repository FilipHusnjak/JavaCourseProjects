package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

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

	private int getParam(String parameter, int defaultValue) {
		if (parameter == null) return defaultValue;
		try {
			return Integer.parseInt(parameter);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
}
