package serverComponents;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import storage.timingStorer;

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
	 * Boolean that tracks whether or not the server is currently paused or accepting
	 * connections
	 */
	private boolean serverOpen;
	
	/**
	 * The port that is to be used to listen for incoming connections
	 */
	private int serverPort;

	/**
	 * Constructs ConnectionHandler with the given port and folder path that is storing all POUIs.
	 * @param port The desired port that the server is to use.
	 * @param container The path to the folder containing all the POUIs.
	 */
	public ConnectionHandler(int port, String pathToParentFolder) {
		this.serverPort = port;
		this.pathToParentFolder = pathToParentFolder;
		try {
			this.serverSocket = new ServerSocket(port);
			this.serverOpen = true;
		} catch (IOException e) {
			exitWithError(e);
		}
	}

	/**
	 * Listens for client requests and creates a new RequestThread instance for each
	 * incoming connection
	 */
	public void run() {
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		timingStorer timingStorer = new timingStorer(queue, pathToParentFolder);
		timingStorer.start();
		while (true) {
			if (serverOpen) {
				try {
					Socket clientSocket = this.serverSocket.accept();
					RequestThread clientRequestThread = new RequestThread(clientSocket, pathToParentFolder, queue);
					clientRequestThread.start();
				} catch (SocketException e) {
					// do nothing, let the client's connection be terminated
				} catch (IOException e) {
					// do nothing, client will disconnect. This error does not have to be handled at this level.
				}
			}
		}
	}

	/**
	 * Pauses the server and ceases all communication with clients.
	 */
	public void pauseServer() {
		if (!(serverSocket.isClosed())) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// do nothing, as this error isn't unexpected.
			}
		}
	}
	
	public void startServer() {
		if (serverSocket.isClosed()) {
			try {
				serverSocket = new ServerSocket(serverPort);
			} catch (IOException e) {
				exitWithError(e);
			}
		}
	}

	/**
	 * Closes the Server Socket opened and exits the system. Ignores IOException as this is a result
	 * of closing the socket.
	 */
	public void shutdown() {
		try {
			serverSocket.close();
			System.exit(0);
		} catch (IOException e) {
			// do nothing, we expect to see some errors -- they don't need to be handled in this case.
		}
	}
	
	/**
	 * Prints stack trace and exits with exit status one, signaling that an error occurred.
	 * @param e The exception that was thrown before this method was called.
	 */
	private void exitWithError(Exception e) {
		e.printStackTrace();
		System.exit(1);
	}
}