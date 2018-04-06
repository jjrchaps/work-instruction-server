package serverComponents;

import java.io.File;
import java.util.Scanner;

import storage.TimeRetrieval;

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

	/**
	 * Path to parent folder containing all assemblies
	 */
	private String pathToParentFolder;

	/**
	 * Creates new instance of server interface, which will be the method of user interaction
	 * with the work instruction server.
	 * @param in An instance of Scanner to read input from the terminal
	 * @param handler An instance of ConnectionHandler that manages all client threads
	 * @param pathToAssembly The absolute path to the parent folder containing all assemblies.
	 */
	public ServerInterface(Scanner in, ConnectionHandler handler, String pathToParentFolder) {
		this.in = in;
		this.handler = handler;
		this.pathToParentFolder = pathToParentFolder;
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
					advanceScanner();
					returnToMenu();
				}
				else if (input == 2) {
					startServer();
					advanceScanner();
					returnToMenu();
				}
				else if (input == 3) {
					advanceScanner();
					getRawTimes();
					returnToMenu();
				}
				else if (input == 4) {
					shutdown();
				}
				else {
					System.out.println("Invalid selection");
					// advance the scanner
					advanceScanner();
				}
			}
			else {
				System.out.println("Invalid entry");
				advanceScanner();
			}
		}
	}

	private void displayOptions() {
		// formatting for top of poui controls
		System.out.println("\n--------------------");
		System.out.println("POUI Server Controls");
		System.out.println("--------------------\n");

		// list available options
		System.out.println("1: Pause Server");
		System.out.println("2: Start Server");
		System.out.println("3: Get Captured Build Time Data");
		System.out.println("4: Shutdown Server");

		// query for user input
		System.out.print("\nSelection: ");
	}

	/**
	 * Stops the server from running and ends the program.
	 */
	private void shutdown() {
		System.out.println("Server shutting down...");
		in.close();
		handler.shutdown();
	}

	/**
	 * Pauses the server from accepting new connections and communicating with clients
	 */
	private void pauseServer() {
		handler.pauseServer();
		System.out.println("Server Paused");
	}

	/**
	 * Restarts the server listening for incoming connections
	 */
	private void startServer() {
		handler.startServer();
		System.out.println("Server Restarted");
	}
	
	/** 
	 * Displays the raw times stored within timings folder, after querying user for
	 * the desired product ID
	 */
	private void getRawTimes() {
		System.out.println("Available Assemblies: ");
		listAssemblies();
		System.out.print("Select assembly: ");
		String productID = in.nextLine();
		TimeRetrieval timeRetriever = new TimeRetrieval(pathToParentFolder);
		System.out.println(timeRetriever.retrieveRawTimes(productID));
	}
	
	/**
	 * Lists all assembly folders in the parent folder.
	 */
	private void listAssemblies() {
		File parentFolder = new File(pathToParentFolder);
		if (parentFolder.isDirectory()) {
			for (File pouiFolder : parentFolder.listFiles()) {
				// if the file is a directory and it's name doesn't start with a period,
				// that means it's an assembly and it should be listed.
				if (pouiFolder.isDirectory() && (!pouiFolder.getName().substring(0, 1).equals("."))) {
					System.out.println(" - " + pouiFolder.getName());
				}
			}
		}
	}
	
	/**
	 * Advances the input stream to the the next newline character.
	 */
	private void advanceScanner() {
		in.nextLine();
	}
	
	/**
	 * Waits for the user to press the enter key, then returns to main menu.
	 */
	private void returnToMenu() {
		System.out.println("Press enter to return to main menu...");
		in.nextLine();
	}
}