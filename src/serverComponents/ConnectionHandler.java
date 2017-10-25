package serverComponents;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;



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
	 * @param port the desired port number for the server socket to use
	 */
	public ConnectionHandler(int port) {
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
				RequestThread clientRequestThread = new RequestThread(clientSocket);
				clientRequestThread.start();
			} catch (SocketException e) {
				// do nothing, we're in the midst of shutting down the system
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void shutdown() {
		try {
			System.out.println("Shutting down");
			this.serverSocket.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}