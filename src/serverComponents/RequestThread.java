package serverComponents;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import containers.POUIContainer;

/**
 * Request thread extends thread, and handles client-server communication
 * Uses a RequestProtocol class to process requests and generate responses
 * to be sent back to client
 * @author jameschapman
 *
 */
public class RequestThread extends Thread {
	private POUIContainer pouiContainer;
	private Socket socket;

	public RequestThread(Socket socket, POUIContainer container) {
		this.socket = socket;
		this.pouiContainer = container;
	}

	public void run() {
		try {
			// create input and output to communicate with client
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {
				String input = in.readLine();
				RequestProtocol reqProtocol = new RequestProtocol(pouiContainer);
				out.writeObject(reqProtocol.processRequest(input));
				out.flush();
			}
			// if there's a problem, break out of loop and let thread be destroyed
		} catch (IOException e) {
			System.out.println("Terminating connection");
		}
	}
}