package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Web worker which opens the main page and sets the background color.
 * 
 * @author Filip Husnjak
 */
public class HomeWorker implements IWebWorker {
	
	/**
	 * Default background color used when background color is not specified
	 */
	private final String DEFAULT_BACKGROUND = "7F7F7F";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgColor = context.getPersistentParameter("bgcolor");
        if (bgColor == null) {
        	bgColor = DEFAULT_BACKGROUND;
        }
        context.setTemporaryParameter("background", bgColor);
        context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
	}

}
