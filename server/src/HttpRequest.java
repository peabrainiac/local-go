import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

	public String firstLine;
	public List<String> headers;
	public String body;

	/**
	 * Reads an/a HTTP-Request from an InputStream.
	 * 
	 * @param in
	 * @throws IOException
	 */
	public HttpRequest(InputStream in) throws IOException {
		this();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		this.firstLine = reader.readLine();
		
		String line = reader.readLine();
		while (line != null && !line.equals("")) {
			headers.add(line);
			line = reader.readLine();
		}
		/*line = reader.readLine();
		System.out.println("KEKSE");
		while (line != null) {
			if (!body.equals("")) {
				body += "/n";
			}
			body += line;
			System.out.println("Line: "+line);
			line = reader.readLine();
		}
		
		System.out.println("Body");*/

	}
	/**
	 * Sends the HTTP-Request to an OutputStream.
	 * 
	 * @param out
	 */
	public void send(OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		writer.println(firstLine);
		for (String header : headers) {
			writer.println(header);
		}
		writer.println();
		writer.println(body);
		writer.flush();
	}

	public HttpRequest() {
		firstLine = "";
		headers = new ArrayList<String>();
		body = "";
		
	}
	
	/**
	 * Sets the specified header to the given value.
	 * @param header name
	 * @param value
	 */
	public void setHeader(String header, String value) {
		boolean foundHeader = false;
		for(int i=0;i<headers.size();i++) {
			if(headers.get(i).startsWith(header+":")) {
				headers.set(i,header+": "+value);
				foundHeader = true;
			}
		}
		if (!foundHeader) {
			headers.add(header+": "+value);
		}
	}
	
	/**
	 * Returns a specific header value, or null if the header doesn't exist.
	 * @param header name
	 * @return value
	 */
	public String getHeader(String header) {
		for (String h : headers) {
			if(h.startsWith(header+":")) {
				return h.substring(header.length()+2);
			}
		}
		return null;
	}
	
	
}
