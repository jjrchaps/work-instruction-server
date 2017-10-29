package serverComponents;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import customTypes.Images;

/**
 * Request thread extends thread, and handles client-server communication
 * Uses a RequestProtocol class to process requests and generate responses
 * to be sent back to client
 * @author jameschapman
 *
 */
public class RequestThread extends Thread {
	private Socket socket;

	public RequestThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// create input and output to communicate with client
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {
				String input = in.readLine();
				RequestProtocol reqProtocol = new RequestProtocol();
				Images images = reqProtocol.processRequest(input);
				out.writeObject(images);
				out.flush();
			}
			// if there's a problem, break out of loop and let thread be destroyed
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Terminating connection");
		}
	}
}