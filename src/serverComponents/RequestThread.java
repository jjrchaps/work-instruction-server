package serverComponents;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import containers.POUIContainer;
//TODO: Proper commenting required throughout class
/**
 * Request thread extends thread, and handles client-server communication
 * Uses a RequestProtocol class to process requests and generate responses
 * to be sent back to client
 * @author jameschapman
 *
 */
public class RequestThread extends Thread {
	/**
	 * Contains all POUIs currently known to the server.
	 */
	private POUIContainer pouiContainer;
	
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
	 * Creates a new thread with the specified socket and the container of POUIs that 
	 * is needed to satisfy all requests.
	 * @param socket The socket containing the connection with the cleint.
	 * @param container THe container with all POUIs known to the server.
	 */
	public RequestThread(Socket socket, POUIContainer container) {
		this.socket = socket;
		this.pouiContainer = container;
	}
	
	/**
	 * Starts the threads and begins listening for a request to satisfy.
	 */
	public void run() {
		try {
			// create input and output to communicate with client
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {
				String input = in.readLine();
				RequestProtocol reqProtocol = new RequestProtocol(pouiContainer);
				out.writeObject(reqProtocol.processRequest(input));
				out.flush();
			}
			// if there's a problem, break out of loop and let thread be destroyed
		} catch (IOException|NullPointerException e) {
			// do nothing, a user has disconnected and thread should be terminated.
		}
	}
	
	/**
	 * Closes the input and output streams.
	 */
	public void shutdown() {
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			// do nothing, we just want this thread dead anyways
		}
	}
}