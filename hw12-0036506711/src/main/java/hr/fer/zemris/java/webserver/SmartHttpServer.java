package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Represents web server that provides some basic functionalities.
 * Dynamic websites are accomplished using SmartScript language that is parsed
 * with {@link SmartScriptParser} and executed by {@link SmartScriptEngine}.
 * 
 * @author Filip Husnjak
 */
public class SmartHttpServer {
	
	/**
	 * Default mime type, when mime type could not be recognized
	 */
	private static final String DEFAULT_MIME = "application/octet-stream";

	/**
	 * Server address
	 */
	private String address;
	
	/**
	 * Server domain name
	 */
	private String domainName;
	
	/**
	 * Port that server listens to
	 */
	private int port;
	
	/**
	 * Number of threads that will perform client requests
	 */
	private int workerThreads;
	
	/**
	 * How long user session lasts
	 */
	private int sessionTimeout;
	
	/**
	 * Map which holds mime types this server can recognize
	 */
	private Map<String, String> mimeTypes = new HashMap<>();
	
	/**
	 * Main server thread
	 */
	private ServerThread serverThread = new ServerThread();
	
	/**
	 * Thread pool which executes workers
	 */
	private ExecutorService threadPool;
	
	/**
	 * Root which should be used to load files, any file that is below this root
	 * cannot be accessed via web.
	 */
	private Path documentRoot;
	
	/**
	 * Map that holds workers mapped to their paths
	 */
	private Map<String, IWebWorker> workersMap = new HashMap<>();

	/**
	 * Flag used to signal server thread to stop.
	 */
	private volatile boolean running;
	
	/**
	 * Map that maps session ID with its {@link SessionMapEntry}
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	
	/**
	 * Thread that cleans {@link #sessions} map each 5 minutes.
	 */
	private Thread cleaner = new Thread(() -> {
		while (running) {
			try {
				Thread.sleep(300_000);
			} catch (InterruptedException ignorable) {}
			long currentTime = System.currentTimeMillis() / 1000;
			// Since we iterate over sessions map which is used by multiple threads
			// we have to synchronize it.
			synchronized (sessions) {
				Iterator<SessionMapEntry> it = sessions.values().iterator();
				while (it.hasNext()) {
					if (it.next().validUntil < currentTime) {
						it.remove();
					}
				}
			}
		}
	});
	
	/**
	 * Constructs new {@link SmartHttpServer}, loads properties from the given
	 * path.
	 * 
	 * @param configFileName
	 *        path of the server-configuration file
	 */
	public SmartHttpServer(String configFileName) {
		try {
			Path configPath = Paths.get(configFileName);
			loadServerProperties(configPath);
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot open file properties!");
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Cannot find class specified in workers config file!");
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Cannot instantiate worker class!");
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot acces given file!");
		} catch (InvocationTargetException | NoSuchMethodException e) {
			throw new IllegalArgumentException("Cannot invoke proper methods!");
		} catch (Exception e) {
			throw new IllegalArgumentException("Something went wrong!");
		}
	}

	/**
	 * Loads server properties using given configuration path. It also loads
	 * all existing mimes and workers.
	 * 
	 * @param configPath
	 *        path of the configuration file
	 * @throws Exception if error occurs reading the file
	 */
	private void loadServerProperties(Path configPath) throws Exception {
		// Load server properties
		Properties prop = new Properties();
		prop.load(Files.newInputStream(configPath));
		address = prop.getProperty("server.address");
		domainName = prop.getProperty("server.domainName");
		port = Integer.parseInt(prop.getProperty("server.port"));
		workerThreads = Integer.parseInt(prop.getProperty("server.workerThreads"));
		documentRoot = Paths.get(prop.getProperty("server.documentRoot"));
		sessionTimeout = Integer.parseInt(prop.getProperty("session.timeout"));
		
		// Load worker objects
		Path workerConfigPath = Paths.get(prop.getProperty("server.workers"));
		loadServerWorkers(workerConfigPath);
		
		// Load mime map
		Path mimeConfigPath = Paths.get(prop.getProperty("server.mimeConfig"));
		loadMimeProperties(mimeConfigPath);
	}
	
	/**
	 * Loads mime properties from given mime configuration file path.
	 * 
	 * @param mimeConfigPath
	 *        path of the mime configuration file
	 * @throws IOException if error occurs reading the file
	 */
	private void loadMimeProperties(Path mimeConfigPath) throws IOException {
		Properties prop = new Properties();
		prop.load(Files.newInputStream(mimeConfigPath));
		// For every key in properties map it with the proper value
		for (Object key: prop.keySet()) {
			String k = (String) key;
			mimeTypes.put(k, prop.getProperty(k));
		}
	}
	
	/**
	 * Loads worker objects defined in workers configuration file whose path
	 * if given as an argument. This method fills {@link #workersMap} with
	 * appropriate worker objects.
	 * 
	 * @param workersPath
	 *        path of the workers configuration file
	 * @throws Exception if error occurs reading the file
	 */
	private void loadServerWorkers(Path workersPath) throws Exception {
		// Read worker lines
		List<String> lines = Files.readAllLines(workersPath);
		for (String line: lines) {
			if (line.isBlank()) continue;
			String[] lineParts = line.split("=");
			Class<?> referenceToClass = 
					this.getClass().getClassLoader().loadClass(lineParts[1].trim());
			Object newObject = referenceToClass.getConstructor().newInstance();
			IWebWorker iww = (IWebWorker)newObject;
			workersMap.put(lineParts[0].trim(), iww);
		}
	}

	/**
	 * Starts the server thread. Initializes thread pool and also starts
	 * the appropriate cleaner thread.
	 */
	protected synchronized void start() {
		if (!running) {
			running = true;
			cleaner.setDaemon(true);
			threadPool = Executors.newFixedThreadPool(workerThreads);
			serverThread.start();
			cleaner.start();
		}
	}

	/**
	 * Stops all threads used by this server, including the main server thread.
	 */
	protected synchronized void stop() {
		running = false;
		threadPool.shutdown();
	}

	/**
	 * Main server thread that 
	 * 
	 * @author Filip Husnjak
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			try (ServerSocket serverSocket = new ServerSocket()) {
				serverSocket.bind(new InetSocketAddress(address, port));
				while (running) {
					Socket toClient = serverSocket.accept();
					ClientWorker cw = new ClientWorker(toClient);
					threadPool.submit(cw);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ClientWorker implements Runnable, IDispatcher {

		private Socket csocket;
		
		private PushbackInputStream istream;
		
		private OutputStream ostream;
		
		private String version;
		
		private String method;
		
		private String host;
		
		private Map<String, String> params = new HashMap<>();
		
		private Map<String, String> tempParams = new HashMap<>();
		
		private Map<String, String> permPrams = new HashMap<>();
		
		private List<RCCookie> outputCookies = new ArrayList<>();
		
		private String SID;
		
		private RequestContext context;

		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}
		
		private void sendError(int statusCode, String statusText) throws IOException {
            ostream.write(
                    ("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                    "Server: simple java server\r\n" +
                    "Content-Type: text/plain;charset=UTF-8\r\n" +
                    "Content-Length: 0\r\n" +
                    "Connection: close\r\n" +
                    "\r\n").getBytes(StandardCharsets.US_ASCII)
            );
        }

		@Override
		public void run() {
			try {
				// Get input and output streams for receiving request and sending response.
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());
				
				// Request in bytes
				byte[] request = readRequest(istream);
				if(request==null) {
					sendError(400, "Bad request");
					return;
				}
				String requestStr = new String(
					request, 
					StandardCharsets.US_ASCII
				);
				
				// List of header lines
				List<String> headers = extractHeaders(requestStr);
				
				// First line splitted into parts
				String[] firstLine = headers.isEmpty() ? null : headers.get(0).split(" ");
				if(firstLine==null || firstLine.length != 3) {
					sendError(400, "Bad request");
					return;
				}
				
				method = firstLine[0].toUpperCase();
				version = firstLine[2].toUpperCase();
				// If method or version are not odd, send error back to client
				if(!method.equals("GET") || !version.equals("HTTP/1.1")) {
					sendError(400, "Bad request");
					return;
				}
				
				// Find host if it exists
				for (int i = 1; i < headers.size(); ++i) {
					if (headers.get(i).startsWith("Host: ")) {
						if (!parseHost(headers.get(i))) {
							sendError(400, "Bad request: Host header not defined properly!");
							return;
						}
						break;
					}
				}
				
				// Checks current session and updates SID accordingly
				checkSession(headers);
				
				// Requested path that contains parameter and file path
				String requestedPath = firstLine[1];
				String[] pathParts = requestedPath.split("\\?");
				// Parse parameters and fill parameters map
				String paramString = pathParts.length < 2 ? null : pathParts[1];
				parseParameters(paramString);
				
				// Process request and deliver response
				internalDispatchRequest(pathParts[0], true);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					csocket.close();
				} catch (IOException e) {
					System.out.println("Socket could not be closed!");
				}
			}
		}

		private void checkSession(List<String> headers) {
			synchronized (sessions) {
				String sidCandidate = null;
				for (String header: headers) {
					// We are looking for cookies
					if (!header.startsWith("Cookie:")) continue;
					// Remove prefix 'Cookie='
					String cookies = header.substring(7);
					for (String cookie: cookies.split(";")) {
						// We are looking for session id
						if (!cookie.trim().startsWith("sid")) continue;
						String tmpSid = cookie.split("=")[1].trim();
						tmpSid = tmpSid.substring(1, tmpSid.length() - 1);
						// Check if retrieved session id is valid
						if (!checkHost(tmpSid)) continue;
						sidCandidate = tmpSid;
					}
				}
				long currentTime = System.currentTimeMillis() / 1000;
				SessionMapEntry entry = sessions.get(sidCandidate);
				if (entry == null) {
					entry = addNewEntry(sidCandidate, currentTime);
				} else {
					SID = sidCandidate;
					entry.validUntil = currentTime + sessionTimeout;
				}
				permPrams = entry.map;
				outputCookies.add(new RCCookie(
						"sid",
						SID,
						null,
						host == null ? domainName : host,
						"/"));
			}
		}
		
		private boolean checkHost(String sidCandidate) {
			long currentTime = System.currentTimeMillis() / 1000;
			SessionMapEntry entry = sessions.get(sidCandidate);
			return entry != null && entry.host.equals(host) && entry.validUntil > currentTime;
		}

		private SessionMapEntry addNewEntry(String sidCandidate, long currentTime) {
			sessions.remove(sidCandidate);
			SID = UUID.randomUUID().toString();
			SessionMapEntry newEntry = new SessionMapEntry(
					SID,
					host, 
					currentTime + sessionTimeout,
					new ConcurrentHashMap<>());
			sessions.put(SID, newEntry);
			return newEntry;
		}

		private String getExtension(String reqPath) {
			String extension = "";
			int i = reqPath.lastIndexOf('.');
			if (i > 0) {
			    extension = reqPath.substring(i+1);
			}
			return extension;
		}

		private boolean isChild(Path child, Path parent) {
			return child.normalize().toAbsolutePath().startsWith(
					parent.normalize().toAbsolutePath());
		}

		private void parseParameters(String paramString) {
			if (paramString == null) return;
			String[] parts = paramString.split("&");
			for (String part: parts) {
				String[] keyValue = part.split("=");
				if (keyValue.length != 2) continue;
				params.put(keyValue[0], keyValue[1]);
			}
		}

		private boolean parseHost(String possibleHost) {
			String[] hostLine = possibleHost.split(" ");
			if (hostLine.length != 2) {
				return false;
			}
			// Remove port from hostname
			host = hostLine[1].split(":")[0];
			return true;
		}

		private List<String> extractHeaders(String requestStr) throws IOException {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for (String s : requestStr.split("\n")) {
				if (s.isEmpty()) break;
				char c = s.charAt(0);
				if (c == 9 || c == 32) {
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}

		private byte[] readRequest(InputStream cis) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			// Deterministic finite state machine that parses header of HTML request
	l:		while (true) {
				int b = cis.read();
				if (b == -1) return null;
				if(b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0: 
					if (b == 13) { state = 1; } else if (b == 10) state = 3;
					break;
				case 1:
					if (b == 10) { state = 2; } else state = 0;
					break;
				case 2: 
					if (b == 13) { state = 3; } else state = 0;
					break;
				case 3: 
					if (b == 10) { break l; } else state = 0;
					break;
				}
			}
			return bos.toByteArray();
		}
		
		public void internalDispatchRequest(String urlPath, boolean directCall)
				 throws Exception {
			// Creates context if it does not exist
			createContext();
			// If its direct call to private path, send error
			if (urlPath.startsWith("/private") && directCall) {
				sendError(404, "Page not found!");
				return;
			}
			// If worker should be loaded dynamically, load it and execute
			if (urlPath.startsWith("/ext/")) {
				String classPath = "hr.fer.zemris.java.webserver.workers.";
				getWorker(classPath + urlPath.substring(5)).processRequest(context);
				return;
			}
			// If its the call for statically loaded workers, execute it
			if (workersMap.containsKey(urlPath)) {
                workersMap.get(urlPath).processRequest(context);
                return;
            }
			// Create requested path based on documentRoot and URL
			Path reqPath = documentRoot.resolve(Paths.get(urlPath.substring(1)));
			// If its not sub path of document root, send error
			if (!isChild(reqPath, documentRoot)) {
				sendError(403, "Forbidden");
				return;
			}
			// If the file does not exist or is not readable, send error
			if (!Files.isReadable(reqPath) || !Files.isRegularFile(reqPath)) {
				sendError(404, "File not found");
				return;
			}
			// Get extension from requested URL
			String extension = getExtension(reqPath.getFileName().toString()).trim();
			if (extension.equals("smscr")) {
				// If extension is 'smscr' execute the script
				executeFile(reqPath);
			} else {
				// Find appropriate mime type for extension
				context.setMimeType(mimeTypes.getOrDefault(extension, DEFAULT_MIME));
				// Set content length based on Files.size method. Since we already
				// checked if the file is regular we can rely on this method
				context.setContentLength(Files.size(reqPath));
				// Read the file to output stream
				readFile(reqPath);
			}
		}
		
		private IWebWorker getWorker(String classPath) throws Exception {
			Class<?> referenceToClass = 
					this.getClass().getClassLoader().loadClass(classPath.trim());
			Object newObject = referenceToClass.getConstructor().newInstance();
			return (IWebWorker)newObject;
		}

		private void createContext() {
			if (context == null) {
				context = new RequestContext(
						ostream, 
						params, 
						permPrams,
						outputCookies,
						tempParams,
						this,
						SID);
			}
		}

		private void executeFile(Path reqPath) throws IOException {
			String docBody = Files.readString(reqPath);
			new SmartScriptEngine(
					new SmartScriptParser(docBody).getDocumentNode(),
					context).execute();
		}

		private void readFile(Path reqPath) throws IOException {
			// Read file and write response
			InputStream fis = new BufferedInputStream(Files.newInputStream(reqPath));
			byte[] buff = new byte[1024];
			while (true) {
				int r = fis.read(buff);
				if (r < 1) break;
				context.write(buff, 0, r);
			}
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			// Dispatch request without direct call
			internalDispatchRequest(urlPath, false);
		}
	}
	
	private static class SessionMapEntry {
		
		@SuppressWarnings("unused")
		String sid;
		
		String host;
		
		long validUntil;
		
		Map<String, String> map;

		public SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
			this.map = map;
		}
		
	}
	
	public static void main(String[] args) {
		try {
			new SmartHttpServer("./config/server.properties").start();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

}
