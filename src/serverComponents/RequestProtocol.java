package serverComponents;

import java.io.File;

import customTypes.Images;

/**
 * RequestProtocol receives all the requests made by a client, determines what the proper way to handle is, 
 * and executes accordingly.
 * @author jameschapman
 */
public class RequestProtocol {
	private String pathToParentFolder;

	/**
	 * Initializes pathToParentFolder with the given parameter
	 * @param pathToParentFolder The path to the folder where all POUI's are stored.
	 */
	public RequestProtocol(String pathToParentFolder) {
		this.pathToParentFolder = pathToParentFolder;
	}

	/**
	 * Processes requests from a client. Requests should be formatted "[request type]:[add. data if needed]". If 
	 * a request is received that is unknown to this method, it will return null.
	 * @param input The input that was received from the client.
	 * @return The respective object to satisfy the request, or null if request is unknown.
	 */
	public Object processRequest(String input) {
		input = input.toLowerCase();
		String[] splitInput = input.split(":");
		String request = splitInput[0];
		if (request.equals("pouirequest")) {
			return pouiRequest(splitInput[1]);
		}
		if (request.equals("productlist")) {
			return productListRequest();
		}
		if (request.equals("reporttimings")) {
			return reportTimings(splitInput);
		}
		else {
			return null;
		}
	}

	/**
	 * Fetches POUI images from local instance of pouiContainer.
	 * @param input The product id, name, etc of the desired instruction set
	 * @return The images for the POUIs, null if the POUI does not exist (shouldn't happen)
	 */
	private Images pouiRequest(String input) {
		// properly format the path to where the images are stored.
		String pathToAssemblyImages = pathToParentFolder + "/" + input + "/";
		Images pouiImages = new Images(pathToAssemblyImages);
		return pouiImages;
	}

	/**
	 * Creates a list of all products within the parent POUI folder and returns as a string, 
	 * with each of the different product IDs being separated by a semicolon.
	 * @return A string containing all the available products.
	 */
	private String productListRequest() {
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
		return productNames.substring(0, productNames.length());
	}

	/**
	 * Accepts an array of strings that represent the recorded timings from a client upon
	 * completion of the product.
	 * @param splitInput All the timings, as string, separated by a colon.
	 * @return True if the timings were successfully handled, false otherwise.
	 */
	private boolean reportTimings(String[] splitInput) {
		//TODO:Implement the storing of these results instead of just printing them to console.
		for (String time : splitInput) {
			if (!(time.equals("reporttimings"))) {
				System.out.println(time);
			}
		}
		return true;
	}
}
