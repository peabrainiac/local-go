import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class WebSocketHandler implements Runnable{

	public HttpRequest request;
	public Socket socket;
	
	public WebSocketHandler(HttpRequest request, Socket socket) {
		this.request = request;
		this.socket = socket;
	}

	public void run(){
		HttpRequest response = new HttpRequest();
		response.firstLine = "HTTP/1.1 101 Switching Protocols";
		response.setHeader("Upgrade", "websocket");
		response.setHeader("Connection", "Upgrade");
		response.setHeader("Sec-WebSocket-Accept", deriveAcceptHeader(request));
		try {
			response.send(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String deriveAcceptHeader(HttpRequest request) {
		String key = request.getHeader("Sec-WebSocket-Key");
        try {
        	MessageDigest MSGdIGEST = MessageDigest.getInstance("SHA-1");
        	String hashInput = key+"258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
			MSGdIGEST.update(hashInput.getBytes("UTF-8"), 0, hashInput.length());
			return Base64.getEncoder().encodeToString(MSGdIGEST.digest());
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return null;
	}
	
}
