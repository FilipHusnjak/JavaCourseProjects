package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents context of the single request. This class provides all necessary 
 * information about currently active request.
 * 
 * @author Filip Husnjak
 */
public class RequestContext {

	/**
	 * Output stream used to send response
	 */
	private OutputStream outputStream;
	
	/**
	 * Charset used to transform bytes to text
	 */
	private Charset charset;
	
	/**
	 * Encoding used to transform bytes to text
	 */
	private String encoding = "UTF-8";
	
	/**
	 * Status code to be sent as response
	 */
	private int statusCode = 200;
	
	/**
	 * Status text to be sent as response
	 */
	private String statusText = "OK";
	
	/**
	 * MIME type of the response
	 */
	private String mimeType = "text/html";
	
	/**
	 * Length of the response in bytes
	 */
	private Long contentLength;
	
	/**
	 * Request parameters
	 */
	private Map<String, String> parameters;
	
	/**
	 * Temporary parameters used mostly between scripts
	 */
	private Map<String, String> temporaryParameters = new HashMap<>();
	
	/**
	 * Parameters bound to user session
	 */
	private Map<String, String> persistentParameters;
	
	/**
	 * List of cookies to be sent
	 */
	private List<RCCookie> outputCookies;
	
	/**
	 * Dispatcher used to dispatch requests
	 */
	private IDispatcher dispatcher;
	
	/**
	 * Tells whether header was generated
	 */
	private boolean headerGenerated;
	
	/**
	 * Session ID of this request
	 */
	private String sessionID;
	
	/**
	 * Constructs new {@link RequestContext} with specified parameters.
	 * 
	 * @param outputStream
	 *        output stream to the client
	 * @param parameters
	 *        request parameters
	 * @param persistentParameters
	 *        session parameters
	 * @param outputCookies
	 *        request cookies
	 * @throws NullPointerException if either outputstream, persistent parameters
	 *         or output cookies are {@code null}
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this.outputStream = Objects.requireNonNull(
				outputStream, 
				"Given outputstream cannot be null!");
		this.parameters = Collections.unmodifiableMap(
				parameters != null ?
				parameters : Collections.emptyMap());
		this.persistentParameters = Objects.requireNonNull(
				persistentParameters, 
				"Given persistent parameters cannot be null!");
		this.outputCookies = Objects.requireNonNull(
				outputCookies, 
				"Given output cookies cannot be null!");
	}
	
	/**
	 * Constructs new {@link RequestContext} with specified parameters.
	 * 
	 * @param outputStream
	 *        output stream to the client
	 * @param parameters
	 *        request parameters
	 * @param persistentParameters
	 *        session parameters
	 * @param outputCookies
	 *        request cookies
	 * @param temporaryParameters
	 *        temporary parameters used by scripts
	 * @param dispatcher
	 *        dispatcher used to dispatch requests
	 * @param sessionID
	 *        session ID of this request
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher, String sessionID) {
		this.outputStream = Objects.requireNonNull(
				outputStream, 
				"Given outputstream cannot be null!");
		this.parameters = Collections.unmodifiableMap(
				parameters != null ?
				parameters : Collections.emptyMap());
		this.persistentParameters = Objects.requireNonNull(
				persistentParameters, 
				"Given persistent parameters cannot be null!");
		this.outputCookies = Objects.requireNonNull(
				outputCookies, 
				"Given output cookies cannot be null!");
		this.temporaryParameters = Objects.requireNonNull(
				temporaryParameters, 
				"Given temporary parameters cannot be null!");
		this.dispatcher = Objects.requireNonNull(
				dispatcher, 
				"Given dispatcher cannot be null!");
		this.sessionID = Objects.requireNonNull(
				sessionID, 
				"Given session id cannot be null!");
	}

	/**
	 * Returns parameter with the given name. If parameter with the given name
	 * does not exist, {@code null} is returned.
	 * 
	 * @param name
	 *        name of the parameter to be returned
	 * @return parameter with the given name
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Returns unmodifiable collection of the parameter names.
	 * 
	 * @return unmodifiable collection of the parameter names
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Returns persistent parameter with the given name. If persistent parameter 
	 * with the given name does not exist, {@code null} is returned.
	 * 
	 * @param name
	 *        name of the parameter to be returned
	 * @return parameter with the given name
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Returns unmodifiable collection of the persistent parameter names.
	 * 
	 * @return unmodifiable collection of the persistent parameter names
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Maps the value of persistent parameter with the specified name.
	 * 
	 * @param name
	 *        name of the persistent parameter
	 * @param value
	 *        value of the persistent parameter
	 * @throws NullPointerException if the given name is {@code null}
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(Objects.requireNonNull(name), value);
	}
	
	/**
	 * Removes the persistent parameter with the specified name.
	 * 
	 * @param name
	 *        name of the persistent parameter
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Returns temporary parameter with the given name. If temporary parameter 
	 * with the given name does not exist, {@code null} is returned.
	 * 
	 * @param name
	 *        name of the parameter to be returned
	 * @return temporary parameter with the given name
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Returns unmodifiable collection of the temporary parameter names.
	 * 
	 * @return unmodifiable collection of the temporary parameter names
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Maps the value of temporary parameter with the specified name.
	 * 
	 * @param name
	 *        name of the temporary parameter
	 * @param value
	 *        value of the temporary parameter
	 * @throws NullPointerException if the given name is {@code null}
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(Objects.requireNonNull(name), value);
	}
	
	/**
	 * Removes the temporary parameter with the specified name.
	 * 
	 * @param name
	 *        name of the temporary parameter
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * Returns session ID of this request.
	 * 
	 * @return session ID of this request
	 */
	public String getSessionID() {
		return sessionID;
	}
	
	/**
	 * Adds the given cookie to the collection of output cookies.
	 * 
	 * @param cookie
	 *        cookie to be added
	 * @throws NullPointerException if the given cookie is {@code null}
	 */
	public void addRCCookie(RCCookie cookie) {
		outputCookies.add(Objects.requireNonNull(cookie));
	}
	
	/**
	 * Writes given byte array to the output stream of this {@link RequestContext}.
	 * 
	 * @param data
	 *        data to be written
	 * @return this instance of {@link RequestContext}
	 * @throws IOException if an I/O error occurs
	 * @throws NullPointerException if the given array is {@code null}
	 */
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}
	
	/**
	 * Writes given data to the output stream of this {@link RequestContext}.
	 * Data is written from the specified offset with specified length.
	 * 
	 * @param data
	 *        data to be written
	 * @param offset
	 *        offset in an array
	 * @param len
	 *        length of a data sequence to be written
	 * @return this instance of {@link RequestContext}
	 * @throws IOException if an I/O error occurs
	 * @throws NullPointerException if the given array is {@code null}
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		Objects.requireNonNull(data, "Given byte array cannot be null!");
		// Generate header if its not generated
		if (!headerGenerated) {
			String header = generateHeader();
			outputStream.write(header.getBytes(StandardCharsets.ISO_8859_1));
			headerGenerated = true;
		}
		outputStream.write(data, offset, len);
		outputStream.flush();
		return this;
	}

	/**
	 * Writes given text to the output stream of this {@link RequestContext}.
	 * Text is transformed to byte array using encoding defined in this
	 * {@link RequestContext} instance. If encoding was not defined 'UTF-8' is
	 * used by default.
	 * 
	 * @param text
	 *        text to be written
	 * @return this instance of {@link RequestContext}
	 * @throws IOException
	 * @throws NullPointerException if the given {@link String} is {@code null}
	 */
	public RequestContext write(String text) throws IOException {
		// Transform string to bytes using appropriate charset
		charset = Charset.forName(encoding);
		byte[] data = text.getBytes(charset);
 		return write(data);
	}
	
	/**
	 * Generates and returns header for the response.
	 * 
	 * @return header for the response
	 */
	private String generateHeader() {
		return "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
			   "Content-Type: " + getMimeType() +
			   getContentLength() +
			   getCookies() +
			   "\r\n";
	}

	/**
	 * Returns cookie header lines generated from outputCookies list.
	 * 
	 * @return cookie header lines
	 */
	private String getCookies() {
		StringBuilder cookies = new StringBuilder();
		for (RCCookie cookie: outputCookies) {
			cookies.append(extractValues(cookie));
		}
		return cookies.toString();
	}

	/**
	 * Returns header line generated for specified cookie.
	 * 
	 * @param cookie
	 *        cookie whose header line is to be generated
	 * @return header line generated for specified cookie
	 */
	private String extractValues(RCCookie cookie) {
		return "Set-Cookie: " + cookie.name + "=\"" + cookie.value + "\"" +
				(cookie.domain != null ? ";Domain=" + cookie.domain : "") +
				(cookie.path != null ? ";Path =" + cookie.path : "") +
				(cookie.maxAge != null ? ";Max-Age =" + cookie.maxAge : "") +
				"\r\n";
	}

	/**
	 * Returns header line representing content length. If content length is
	 * not specified empty string is returned.
	 * 
	 * @return header line representing content length
	 */
	private String getContentLength() {
		return contentLength != null ?
				"Content-Length: " + contentLength + "\r\n" : "";
	}

	/**
	 * Returns MIME type in proper form.
	 * 
	 * @return MIME type in proper form
	 */
	private String getMimeType() {
		return (mimeType.startsWith("text/") ?
				mimeType + ";charset=" + encoding : mimeType) + "\r\n";
	}

	/**
	 * Sets encoding of this {@link RequestContext} to the specified one.
	 * 
	 * @param encoding
	 *        new encoding used by this {@link RequestContext}
	 * @throws NullPointerException if the given encoding is {@code null}
	 * @throws RuntimeException if the header is already generated
	 */
	public void setEncoding(String encoding) {
		checkHeaderGenerated();
		this.encoding = Objects.requireNonNull(encoding);
	}

	/**
	 * Sets status code of this {@link RequestContext} to the specified one.
	 * 
	 * @param statusCode
	 *        new statusCode
	 * @throws NullPointerException if the given statusCode is {@code null}
	 * @throws RuntimeException if the header is already generated
	 */
	public void setStatusCode(int statusCode) {
		checkHeaderGenerated();
		this.statusCode = Objects.requireNonNull(statusCode);
	}

	/**
	 * Sets status text of this {@link RequestContext} to the specified one.
	 * 
	 * @param statusCode
	 *        new status text
	 * @throws NullPointerException if the given status text is {@code null}
	 * @throws RuntimeException if the header is already generated
	 */
	public void setStatusText(String statusText) {
		checkHeaderGenerated();
		this.statusText = Objects.requireNonNull(statusText);
	}

	/**
	 * Sets mime type of this {@link RequestContext} to the specified one.
	 * 
	 * @param mimeType
	 *        new mime type
	 * @throws NullPointerException if the given mimeType is {@code null}
	 * @throws RuntimeException if the header is already generated
	 */
	public void setMimeType(String mimeType) {
		checkHeaderGenerated();
		this.mimeType = Objects.requireNonNull(mimeType);
	}
	
	/**
	 * Sets content length of this {@link RequestContext} to the specified one.
	 * 
	 * @param contentLength
	 *        new content length
	 * @throws RuntimeException if the header is already generated
	 */
	public void setContentLength(Long contentLength) {
		checkHeaderGenerated();
		this.contentLength = contentLength;
	}

	/**
	 * Checks whether header is already generate, and if it is throws
	 * {@link RuntimeException}.
	 * 
	 * @throws RuntimeException if the header is already generated
	 */
	private void checkHeaderGenerated() {
		if (headerGenerated) {
			throw new RuntimeException("Header is already generated!");
		}
	}
	
	/**
	 * Returns dispatcher used by this {@link RequestContext}.
	 * 
	 * @return dispatcher used by this {@link RequestContext}
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Represents cookie sent to client. Currently cookies are used to determine
	 * which session is active and which session performed the request.
	 * 
	 * @author Filip Husnjak
	 */
	public static class RCCookie {
		
		/**
		 * Name of the cookie
		 */
		private String name;
		
		/**
		 * Value of the cookie
		 */
		private String value;
		
		/**
		 * Domain of the cookie
		 */
		private String domain;
		
		/**
		 * Path of the cookie
		 */
		private String path;
		
		/**
		 * Max age of the cookie
		 */
		private Integer maxAge;

		/**
		 * Constructs new {@link RCCookie} with specified parameters.
		 * 
		 * @param name
		 *        name of the cookie
		 * @param value
		 *        value of the cookie
		 * @param maxAge
		 *        max age of the cookie
		 * @param domain
		 *        domain of the cookie
		 * @param path
		 *        path of the cookie
		 * @throws NullPointerException if the given name or value are {@code null}
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = Objects.requireNonNull(name, "Name cannot be null!");
			this.value = Objects.requireNonNull(value, "Value cannot be null!");
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Returns name of this cookie.
		 * 
		 * @return name of this cookie.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns value of this cookie.
		 * 
		 * @return value of this cookie.
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Returns domain of this cookie.
		 * 
		 * @return domain of this cookie.
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Returns path of this cookie.
		 * 
		 * @return path of this cookie.
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Returns max age of this cookie.
		 * 
		 * @return max age of this cookie.
		 */
		public int getMaxAge() {
			return maxAge;
		}
		
	}
	
}
