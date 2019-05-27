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

public class RequestContext {

	private OutputStream outputStream;
	
	private Charset charset;
	
	private String encoding = "UTF-8";
	
	private int statusCode = 200;
	
	private String statusText = "OK";
	
	private String mimeType = "text/html";
	
	private Long contentLength;
	
	private Map<String, String> parameters;
	
	private Map<String, String> temporaryParameters = new HashMap<>();
	
	private Map<String, String> persistentParameters;
	
	private List<RCCookie> outputCookies;
	
	private IDispatcher dispatcher;
	
	private boolean headerGenerated;
	
	private String sessionID;
	
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

	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	public String getSessionID() {
		return sessionID;
	}
	
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	public void addRCCookie(RCCookie cookie) {
		outputCookies.add(cookie);
	}
	
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}
	
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
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

	public RequestContext write(String text) throws IOException {
		// Transform string to bytes using appropriate charset
		charset = Charset.forName(encoding);
		byte[] data = text.getBytes(charset);
 		return write(data);
	}
	
	private String generateHeader() {
		return "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
			   "Content-Type: " + getMimeType() +
			   getContentLength() +
			   getCookies() +
			   "\r\n";
	}

	private String getCookies() {
		StringBuilder cookies = new StringBuilder();
		for (RCCookie cookie: outputCookies) {
			cookies.append(extractValues(cookie));
		}
		return cookies.toString();
	}

	private String extractValues(RCCookie cookie) {
		return "Set-Cookie: " + cookie.name + "=\"" + cookie.value + "\"" +
				(cookie.domain != null ? ";Domain=" + cookie.domain : "") +
				(cookie.path != null ? ";Path =" + cookie.path : "") +
				(cookie.maxAge != null ? ";Max-Age =" + cookie.maxAge : "") +
				"\r\n";
	}

	private String getContentLength() {
		return contentLength != null ?
				"Content-Length: " + contentLength + "\r\n" : "";
	}

	private String getMimeType() {
		return (mimeType.startsWith("text/") ?
				mimeType + ";charset=" + encoding : mimeType) + "\r\n";
	}

	public void setEncoding(String encoding) {
		checkHeaderGenerated();
		this.encoding = encoding;
	}

	public void setStatusCode(int statusCode) {
		checkHeaderGenerated();
		this.statusCode = statusCode;
	}

	public void setStatusText(String statusText) {
		checkHeaderGenerated();
		this.statusText = statusText;
	}

	public void setMimeType(String mimeType) {
		checkHeaderGenerated();
		this.mimeType = mimeType;
	}
	
	public void setContentLength(Long contentLength) {
		checkHeaderGenerated();
		this.contentLength = contentLength;
	}

	private void checkHeaderGenerated() {
		if (headerGenerated) {
			throw new RuntimeException("Header is already generated!");
		}
	}
	
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	public static class RCCookie {
		
		private String name;
		
		private String value;
		
		private String domain;
		
		private String path;
		
		private Integer maxAge;

		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = Objects.requireNonNull(name, "Name cannot be null!");
			this.value = Objects.requireNonNull(value, "Value cannot be null!");
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String getDomain() {
			return domain;
		}

		public String getPath() {
			return path;
		}

		public int getMaxAge() {
			return maxAge;
		}
		
	}
	
}
