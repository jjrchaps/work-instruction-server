package serverComponents;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Request thread extends thread, and handles client-server communication
 * Uses a RequestProtocol class to process requests and generate responses
 * to be sent back to client
 * @author jameschapman
 *
 */
public class RequestThread extends Thread {
	private Socket socket = null;

	public RequestThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// create input and output to communicate with client
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {
				String input = in.readLine();
				RequestProtocol reqProtocol = new RequestProtocol();
				out.println(reqProtocol.processRequest(input));
				out.flush();
			}
			// if there's a problem, break out of loop and let thread be destroyed
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Terminating connection");
		}
	}
}