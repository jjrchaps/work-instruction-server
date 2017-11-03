package serverComponents;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * ConnectionHandler will handle initial incoming connections and create a thread
 * for each connection that will handle requests.
 * @author jameschapman
 */
public class ConnectionHandler extends Thread {
	/**
	 * An instance of serverSocket to be used for client communication
	 */
	private ServerSocket serverSocket;
	
	/**
	 * The path to the parent folder containing all available POUIs.
	 */
	private String pathToParentFolder;
	
	/**
	 * Constructs ConnectionHandler with the given port and folder path that is storing all POUIs.
	 * @param port The desired port that the server is to use.
	 * @param container The path to the folder containing all the POUIs.
	 */
	public ConnectionHandler(int port, String pathToParentFolder) {
		this.pathToParentFolder = pathToParentFolder;
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Listens for client requests and creates a new RequestThread instance for each
	 * incoming connection
	 */
	public void run() {
		while (true) {
			try {
				Socket clientSocket = this.serverSocket.accept();
				RequestThread clientRequestThread = new RequestThread(clientSocket, pathToParentFolder);
				clientRequestThread.start();
			} catch (SocketException e) {
				// do nothing, let the client's connection be terminated
			} catch (IOException e) {
				// do nothing, client will disconnect. This error does not have to be handled at this level.
			}
		}
	}
	
	public void shutdown() {
		try {
			serverSocket.close();
			System.exit(0);
		} catch (IOException e) {
			// do nothing, we expect to see some errors -- they don't need to be handled in this case.
		}
	}
}