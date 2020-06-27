import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        startServer();
    }

    /**
     * Startet den Server
     */
    public static void startServer() {
        //Inintialisiert einen Server Socket auf port 8080 (Muss in extra thread für multiple connections)
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            Socket connectionSocket = serverSocket.accept();

            //Streams für Kommunikation
            InputStream inputToServer = connectionSocket.getInputStream();
            OutputStream outputFromServer = connectionSocket.getOutputStream();
            
            //Scanner bekommt input vom Server.inputStream
            Scanner inputScanner = new Scanner(inputToServer, "UTF-8");
            //Print Writer für den Output vom Server
            PrintWriter outputWriter = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

            outputWriter.println("Standardnachricht, 'exit' fuer das Verlassen");

            //Infinite loop fürs scannen von nachrichten
            boolean done = false;
            //Muss auch in extra thread für 2 connections
            	//vielleicht das while auseinanderziehen in ein wile(!done) und ein if(scanner.hasNext) und dann in das if nen Wait um nicht zu oft zu checken ob neue Nachrichten da sind
            while(!done && inputScanner.hasNextLine()) {
                String line = inputScanner.nextLine();
                outputWriter.println("Echo vom Server: " + line);
                //wenn line = exit, break;
                if(line.toLowerCase().trim().equals("exit")) {
                    done = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}