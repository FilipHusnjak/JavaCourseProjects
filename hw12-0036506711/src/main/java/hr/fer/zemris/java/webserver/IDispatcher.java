package hr.fer.zemris.java.webserver;

/**
 * Represents dispatcher used to dispatch requests upon {@link RequestContext}
 * object. Request performed through this interface are not considered direct.
 * 
 * @author Filip Husnjak
 */
@FunctionalInterface
public interface IDispatcher {

	/**
	 * Dispatches the request with specified URL path.
	 * 
	 * @param urlPath
	 *        URL path of the request
	 * @throws Exception if an error occurs
	 */
	void dispatchRequest(String urlPath) throws Exception;
	
}
