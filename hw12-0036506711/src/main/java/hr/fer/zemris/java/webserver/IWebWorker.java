package hr.fer.zemris.java.webserver;

public interface IWebWorker {

	public void processRequest(RequestContext context) throws Exception;
	
}
