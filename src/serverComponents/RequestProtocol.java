package serverComponents;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import auxiliary.Images;

/**
 * RequestProtocol receives all the requests made by a client, determines what the proper way to handle is, 
 * and executes accordingly.
 * @author jameschapman
 */
public class RequestProtocol {
	/**
	 * A path to the parent folder containing all POUIs that are available to a client.
	 */
	private String pathToParentFolder;

	/**
	 * A queue that will be used for storing timings in a thread safe manner.
	 */
	private BlockingQueue<String> queue;

	/**
	 * Local instance of the output stream to the client to return responses from the request
	 */
	private ObjectOutputStream out;

	/**
	 * Initializes pathToParentFolder with the given parameter
	 * @param pathToParentFolder The path to the folder where all POUI's are stored.
	 */
	public RequestProtocol(String pathToParentFolder, BlockingQueue<String> queue, 
			ObjectOutputStream out) {
		this.pathToParentFolder = pathToParentFolder;
		this.queue = queue;
		this.out = out;
	}

	/**
	 * Processes requests from a client. Requests should be formatted "[request type]:[add. data if needed]". If 
	 * a request is received that is unknown to this method, it will return null.
	 * @param input The input that was received from the client.
	 */
	public void processRequest(String input) throws IOException {
		String[] splitInput = input.split(":");
		String request = splitInput[0].toLowerCase();
		if (request.equals("pouiimagerequest")) {
			pouiImageRequest(splitInput[1]);
		}
		else if (request.equals("pouiinspectionrequest")) {
			pouiInspectionRequest(splitInput[1]);
		}
		else if (request.equals("productlist")) {
			productListRequest();
		}
		else if (request.equals("reporttimings")) {
			reportTimings(input);
		}
		else if (request.equals("inspectioncheckrequest")) {
			inspectionCheckRequest(input);
		}
		else {
			sendResponse("Request Not Supported");
		}
	}

	/**
	 * Fetches POUI images from local instance of pouiContainer.
	 * @param input The product id, name, etc of the desired instruction set
	 * @IOException Thrown if there are any issues writing to the ObjectOutputStream
	 */
	private void pouiImageRequest(String input) throws IOException {
		// properly format the path to where the images are stored.
		String pathToAssemblyImages = pathToParentFolder + "/" + input + "/";
		Images pouiImages = new Images(pathToAssemblyImages);
		sendResponse(pouiImages);	
	}

	/**
	 * Generates and sends an array of booleans representing which steps in the poui (specified
	 * by the input from the client) require level 3 inspections
	 * @param input The productID of the poui of which the inspections are desired.
	 * @IOException Thrown if there are any issues writing to the ObjectOutputStream
	 */
	private void pouiInspectionRequest(String input) throws IOException {
		String pathToAssemblyImages = pathToParentFolder + "/" + input + "/";
		// get the number of images in the folder. Folder contains images and the text file
		// of inspections, so 1 is subtracted.
		int numberOfImages = new File(pathToAssemblyImages).listFiles().length -1;
		String pathToInspections = pathToParentFolder + "/" + input + "/inspections.txt";
		boolean[] inspectionRequired = new boolean[numberOfImages];
		if (new File(pathToInspections).exists()) {
			File inspectionFile = new File(pathToInspections);
			Scanner in = new Scanner(inspectionFile);
			for (int i = 0; i < numberOfImages; i++) {
				// split the input by a semicolon delimiter, as in the text file we store the approved users
				// on the same line as the yes identifying it as requiring inspection.
				String[] splitString = in.nextLine().split(";");
				String inspectionRequiredFromSplit = splitString[0];
				if (inspectionRequiredFromSplit.equalsIgnoreCase("yes") || inspectionRequiredFromSplit.equalsIgnoreCase("true")) {
					inspectionRequired[i] = true;
				}
				else {
					inspectionRequired[i] = false;
				}
			}
			in.close();
			sendResponse(inspectionRequired);
		}
		else {
			for (int i = 0; i < numberOfImages; i++) {
				inspectionRequired[i] = false;
			}
			sendResponse(inspectionRequired);
		}

	}

	/**
	 * Creates a list of all products within the parent POUI folder and returns as a string, 
	 * with each of the different product IDs being separated by a semicolon.
	 */
	private void productListRequest() throws IOException {
		String productNames = "";
		// if the path given is in fact a directory, loop through directory and discover available assemblies.
		File parentFolder = new File(pathToParentFolder);
		if (parentFolder.isDirectory()) {
			for (File pouiFolder : parentFolder.listFiles()) {
				if (pouiFolder.isDirectory()) {
					String productName = pouiFolder.getName();
					if (!(productName.substring(0, 1).equals("."))) {
						productNames += productName + ";";
					}
				}
			}
		}
		// return the formatted string, and remove the last semicolon that was appended above
		sendResponse(productNames.substring(0, productNames.length()));
	}

	/**
	 * Adds the reported timings to a queue to the be stored.
	 * @param timings The string of timings that has been reported from the client
	 * @IOException Thrown if there are any issues writing to the ObjectOutputStream
	 */
	private void reportTimings(String timings) throws IOException {
		try {
			queue.put(timings);
		} catch (InterruptedException e) {
			sendResponse(false);
		}
		sendResponse(true);
	}
	
	/**
	 * Takes string formatted as "request:userID:productID:currentStep" and checks the associated inspection file
	 * to find whether or not the userID is in the approved list of approvers.
	 * @param checkRequest The request detailing the assembly to be checked for the userID
	 * @throws IOException If there's an issue sending a response, an IOException is thrown.
	 */
	private void inspectionCheckRequest(String checkRequest) throws IOException {
		String[] splitInput = checkRequest.split(":");
		String userID = splitInput[1];
		String productID = splitInput[2];
		int currentStep = Integer.parseInt(splitInput[3]);
		boolean approved = false;
		String pathToInspections = pathToParentFolder + "/" + productID + "/inspections.txt";
		File inspectionFile = new File(pathToInspections);
		try {
			Scanner in = new Scanner(inspectionFile);
			for (int i = 0; i < currentStep - 1; i++) {
				in.nextLine();
			}
			String relevantInspectionLine = in.nextLine();
			// Inspection line is split with semicolons, thus we use that to split it up. Then check for the presence of 
			// userID in the array, and if found the user is approved. If not, they are not.
			String[] arrayOfApprovedInspectors = relevantInspectionLine.split(";");
			if (Arrays.asList(arrayOfApprovedInspectors).contains(userID)) {
				approved = true;
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find " + pathToInspections);
		}
		sendResponse(approved);
	}
	
	/**
	 * Sends given object to the client that had sent the initial request.
	 * @param response The object to be sent to the client that just made a request
	 * @throws IOException If there is an issue writing to the client, IOException is thrown.
	 */
	private void sendResponse(Object response) throws IOException {
		out.writeObject(response);
		out.flush();
	}
}
