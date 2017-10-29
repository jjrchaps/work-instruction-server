package main;

import java.util.Scanner;

import containers.POUIContainer;
import serverComponents.ConnectionHandler;

/**
 * This will function as the main entry point to the system. It will start a new instance
 * of ConnectionManager as well as an instance of EventManager.
 * @author jameschapman
 */
public class Server {

	/**
	 * Serves as the entry point/starting method of the server software. Creates a new
	 * ConnectionHandler instance to listen for incoming connections and handle them
	 * appropriately.
	 * @param args standard argument for PSV main functions in java
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the path to the folder containing all POUIs: ");
		String pathToPouis = in.nextLine();
		POUIContainer container = new POUIContainer(pathToPouis);
		
		System.out.println("Please enter a port number to use: ");

		int port;
		while (true) {
			try {
				port = in.nextInt();
				in.nextLine();
				break;
			} catch (Exception e) {
				in.nextLine();
				System.out.println("Please enter an integer.");
			}
		}

		ConnectionHandler connectionHandler = new ConnectionHandler(port, container);
		System.out.println("Starting POUI Server on port " + port);
		connectionHandler.start();
		
		

		// loop until quit received from user. Then, trigger connectionHandler shutdown method.
		while (true) {
			if (in.hasNext()) {
				if (in.nextLine().equalsIgnoreCase("quit")) {
					in.close();
					connectionHandler.shutdown();
				}
				else {
					System.out.println("To terminate server, enter \"quit\"");
					in.nextLine();
				}
			}
		}
	}
}
