import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(8080);
			System.out.println("Server started.\nListening for connections on port : " + 8080 + " ...\n");

			// we listen until user halts server execution
			while (true) {
				Socket socket = serverSocket.accept();
				HttpRequest request = new HttpRequest(socket.getInputStream());
				String upgradeHeaderValue = request.getHeader("Upgrade");
				if (upgradeHeaderValue!=null&&upgradeHeaderValue.equals("websocket")) {
					Thread thread = new Thread(new WebSocketHandler(request,socket));
					thread.start();
				}else {
					Thread thread = new Thread(new FileRequestHandler(request,socket));
					thread.start();
				}
			}
		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}

}
