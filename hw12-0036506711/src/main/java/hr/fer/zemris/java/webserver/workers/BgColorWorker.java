package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Web worker which changes the background color of the home page and generate
 * appropriate message.
 * 
 * @author Filip Husnjak
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgColor = context.getParameter("bgcolor");
		// If background color is not properly written, do not set it
		if (bgColor == null || bgColor.length() != 6 || !bgColor.matches("-?[0-9a-fA-F]+")) {
			context.setTemporaryParameter("message", "Color was not updated!");
		} else {
			context.setPersistentParameter("bgcolor", bgColor);
			context.setTemporaryParameter("message", "Color was updated!");
		}
		context.getDispatcher().dispatchRequest("/private/pages/color.smscr");
	}

}
