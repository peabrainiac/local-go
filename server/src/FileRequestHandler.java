import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.StringTokenizer;

// The tutorial can be found just here on the SSaurel's Blog : 
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// Each Client Connection will be managed in a dedicated Thread
public class FileRequestHandler implements Runnable {

	static final File WEB_ROOT = new File("../client");
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String METHOD_NOT_SUPPORTED = "not_supported.html";
	// port to listen connection
	static final int PORT = 8080;

	// verbose mode
	static final boolean verbose = true;

	private HttpRequest request;
	private Socket socket;

	// Constructor
	public FileRequestHandler(HttpRequest request, Socket socket) {
		this.request = request;
		this.socket = socket;
	}

	@Override
	public void run() {
		// we manage our particular client connection
		String fileRequested = null;

		try {
			// we parse the request with a string tokenizer
			StringTokenizer parse = new StringTokenizer(request.firstLine); // ERROR wenn man von der Seite ohne /index
																			// (Also
			// localhost:8080) auf die seite mit /index geht (Also
			// Localhost:8080/index.html)
			String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
			// we get file requested
			fileRequested = parse.nextToken().toLowerCase();

			System.out.println("_____________");
			request.send(System.out);
			System.out.println("_____________");

			HttpRequest response = new HttpRequest();

			// we support only GET and HEAD methods, we check
			if (!method.equals("GET") && !method.equals("HEAD")) {
				if (verbose) {
					System.out.println("501 Not Implemented : " + method + " method.");
				}

				// we return the not supported file to the client
				File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);

				// we send HTTP Headers with data to client
				response.firstLine = "HTTP/1.1 501 Not Implemented";
				response.setHeader("Server", "idk lol");
				response.setHeader("Date", "" + new Date());
				response.setHeader("Content-type", "text/html");
				response.setHeader("Content-length:", "" + file.length());
				response.body = readFileData(file);

			} else {
				// GET or HEAD method
				if (fileRequested.endsWith("/")) {
					fileRequested += DEFAULT_FILE;
				}

				File file = new File(WEB_ROOT, fileRequested);
				String content = getContentType(fileRequested);

				if (method.equals("GET")) { // GET method so we return content

					// send HTTP Headers
					response.firstLine = "HTTP/1.1 200 OK";
					response.setHeader("Server", "idk lol");
					response.setHeader("Date", "" + new Date());
					response.setHeader("Content-type", "" + content);
					response.setHeader("Content-length", "" + file.length());

					response.body = readFileData(file);
				}

				if (verbose) {
					System.out.println("File " + fileRequested + " of type " + content + " returned");
				}

			}
			response.send(socket.getOutputStream());
		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(socket.getOutputStream(), fileRequested);
			} catch (IOException ioe) {
				System.err.println("Error with file not found exception : " + ioe.getMessage());
			}

		} catch (IOException ioe) {
			System.err.println("Server error : " + ioe);
		} finally {
			try {
				socket.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			}

			if (verbose) {
				System.out.println("Connection closed.\n");
			}
		}

	}

	private static String readFileData(File file) throws IOException {
		int fileLength = (int) file.length();
		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];

		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null)
				fileIn.close();
		}

		return new String(fileData, Charset.forName("UTF-8"));
	}

	// return supported MIME Types
	private static String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")) {
			return "text/html";
		}
		if (fileRequested.endsWith(".css")) {
			return "text/css";
		}
		if (fileRequested.endsWith(".js")) {
			return "text/javascript";
		} else {
			return "text/plain";
		}
	}

	private static void fileNotFound(OutputStream out, String fileRequested) throws IOException {
		File file = new File(WEB_ROOT, FILE_NOT_FOUND);
		int fileLength = (int) file.length();
		String content = "text/html";

		HttpRequest response = new HttpRequest();

		response.firstLine = "HTTP/1.1 404 File Not Found";
		response.setHeader("Server", "IDK lol");
		response.setHeader("Date", "" + new Date());
		response.setHeader("Content-type", "" + content);
		response.setHeader("Content-length", "" + fileLength);
		
		response.body = readFileData(file);

		if (verbose) {
			System.out.println("File " + fileRequested + " not found");
		}

		response.send(out);

	}

}
