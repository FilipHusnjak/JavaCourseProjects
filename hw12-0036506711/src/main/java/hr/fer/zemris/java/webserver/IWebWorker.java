package hr.fer.zemris.java.webserver;

/**
 * Represents web worker which performs a job upon a given {@link RequestContext}
 * object.
 * 
 * @author Filip Husnjak
 */
@FunctionalInterface
public interface IWebWorker {

	/**
	 * Processes the request specified by the given {@link RequestContext}
	 * object.
	 * 
	 * @param context
	 *        context representing the request
	 * @throws Exception if an error occurs
	 */
	public void processRequest(RequestContext context) throws Exception;
	
}
