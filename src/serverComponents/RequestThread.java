package serverComponents;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Request thread extends thread, and handles client-server communication
 * Uses a RequestProtocol class to process requests and generate responses
 * to be sent back to client
 * @author jameschapman
 */
public class RequestThread extends Thread {
	/**
	 * Path to the parent folder containing all the POUIs.
	 */
	private String pathToParentFolder;

	/**
	 * Instance of socket that will be used to store the connection between server and client.
	 */
	private Socket socket;

	/**
	 * Output stream that will be used to write objects to the client, be it strings, images, etc.
	 */
	private ObjectOutputStream out;

	/**
	 * The input stream that will be used to received text requests from the client.
	 */
	private BufferedReader in;
	
	/**
	 * A queue to write timings to be stored to, to prevent multiple threads causing issues with one another.
	 */
	private BlockingQueue<String> queue;

	/**
	 * Creates a new thread with the specified socket and the container of POUIs that 
	 * is needed to satisfy all requests.
	 * @param socket The socket containing the connection with the client.
	 * @param container THe container with all POUIs known to the server.
	 */
	public RequestThread(Socket socket, String pathToParentFolder, BlockingQueue<String> queue) {
		this.socket = socket;
		this.pathToParentFolder = pathToParentFolder;
		this.queue = queue;
	}

	/**
	 * Starts the threads and begins listening for a request to satisfy.
	 */
	public void run() {
		try {
			// create input and output to communicate with client
			out = new ObjectOutputStream(socket.getOutputStream());
			// flush for the client to establish ObjectInputStream
			out.flush();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			RequestProtocol reqProtocol = new RequestProtocol(pathToParentFolder, queue, out);
			
			while (true) {
				reqProtocol.processRequest(in.readLine());
			}
			// if there's a problem, break out of loop and let thread be destroyed
		} catch (IOException|NullPointerException e) {
			// do nothing, a user has disconnected and thread should be terminated.
		}
	}
}