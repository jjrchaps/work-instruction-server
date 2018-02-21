package auxiliary;

import java.util.Scanner;

import serverComponents.ConnectionHandler;

/**
 * Server interface is the class that handles user interactions. The
 * interface will be text based and will be started after the server's
 * initial startup.
 * @author jameschapman
 */
public class ServerInterface {
	/**
	 * Local instance of scanner that was created on start of program
	 */
	private Scanner in;

	/**
	 * Local instance of ConnectionHandler that will be needed for general
	 * control of the server.
	 */
	private ConnectionHandler handler;

	public ServerInterface(Scanner in, ConnectionHandler handler) {
		this.in = in;
		this.handler = handler;
	}

	/**
	 * Start is the function that will begin the ServerInterface
	 * listening for interactions from the user. It will
	 * receive/parse input and also control server components.
	 */
	public void start() {
		System.out.println("Available commands are:");
		// loop until quit received from user. Then, trigger connectionHandler shutdown method.
		while (true) {
			if (in.hasNextInt()) {
				int input = in.nextInt();
				if (input == 1) {
					shutdown();
				}
				else {
					System.out.println("Invalid selection");
					in.nextLine();
				}
			}
		}
	}
	
	/**
	 * Stops the server from running and ends the program.
	 */
	private void shutdown() {
		in.close();
		handler.shutdown();
	}
}
