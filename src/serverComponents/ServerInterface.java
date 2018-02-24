package serverComponents;

import java.util.Scanner;

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
	public void getInput() {
		// loop until quit received from user. Then, trigger connectionHandler shutdown method.
		while (true) {
			displayOptions();
			if (in.hasNextInt()) {
				int input = in.nextInt();
				if (input == 1) {
					pauseServer();
				}
				else if (input == 2) {
					startServer();
				}
				else if (input == 3) {
					shutdown();
				}
				else {
					System.out.println("Invalid selection");
					// advance the scanner
					in.nextLine();
				}
			}
			else {
				System.out.println("Invalid entry");
				// advance the scanner
				in.nextLine();
			}
		}
	}

	private void displayOptions() {
		// formatting for top of poui controls
		System.out.println("--------------------");
		System.out.println("POUI Server Controls");
		System.out.println("--------------------\n");

		// list available options
		System.out.println("1: Pause Server");
		System.out.println("2: Start Server");
		System.out.println("3: Shutdown Server");
		
		// query for user input
		System.out.println("\nSelection: ");
	}

	/**
	 * Stops the server from running and ends the program.
	 */
	private void shutdown() {
		in.close();
		handler.shutdown();
	}
	
	/**
	 * Pauses the server from accepting new connections and communicating with clients
	 */
	private void pauseServer() {
		handler.pauseServer();
	}
	
	/**
	 * Restarts the server listening for incoming connections
	 */
	private void startServer() {
		handler.startServer();
	}
}
