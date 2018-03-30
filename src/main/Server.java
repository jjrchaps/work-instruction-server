package main;

import java.io.File;
import java.util.Scanner;

import serverComponents.ConnectionHandler;
import serverComponents.ServerInterface;

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
		String pathToParentFolder = in.nextLine();
		
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
		
		// if the timings folder doesn't already exists, create a new one
		// this will only take place on fresh installs or if something went horribly wrong.
		if (!(new File(pathToParentFolder + "/.timings").exists())) {
			if (!(new File(pathToParentFolder + "/.timings").mkdir())) {
				System.out.println("Failed to create timings directory.");
				System.exit(1);
			}
		}
		
		ConnectionHandler connectionHandler = new ConnectionHandler(port, pathToParentFolder);
		System.out.println("Starting POUI Server on port " + port);
		connectionHandler.start();

		ServerInterface textInterface = new ServerInterface(in, connectionHandler, pathToParentFolder);
		textInterface.getInput();
	}
}
