package serverComponents;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import containers.POUIContainer;
//TODO: Proper commenting required throughout class
/**
 * ConnectionHandler will handle initial incoming connections and create a thread
 * for each connection that will handle requests.
 * @author jameschapman
 *
 */
public class ConnectionHandler extends Thread {
	/**
	 * An instance of serverSocket to be used for client communication
	 */
	private ServerSocket serverSocket;
	
	/**
	 * An instance of POUIContainer that will be used to fulfill client requests
	 */
	protected POUIContainer pouiContainer;
	
	private ArrayList<RequestThread> clientThreads;
	
	public ConnectionHandler(int port, POUIContainer container) {
		pouiContainer = container;
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		clientThreads = new ArrayList<RequestThread>();
	}

	/**
	 * Listens for client requests and creates a new RequestThread instance for each
	 * incoming connection
	 */
	public void run() {
		while (true) {
			try {
				Socket clientSocket = this.serverSocket.accept();
				RequestThread clientRequestThread = new RequestThread(clientSocket, pouiContainer);
				clientThreads.add(clientRequestThread);
				clientRequestThread.start();
			} catch (SocketException e) {
				//TODO: Decide proper handling of SocketException (for now it's nothing)
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void shutdown() {
		try {
			System.out.println("Shutting down...");
			for (RequestThread client : clientThreads) {
				client.shutdown();
			}
			serverSocket.close();
			System.exit(0);
		} catch (IOException e) {
			System.exit(1);
		}
	}
}