package serverComponents;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

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
		// Get the productID from the received request.
		String productID = splitInput[1];

		// check to see if this product ID already has a folder. If not, we create one.
		if (!(new File(this.pathToParentFolder + "/.timings/" + productID)).exists()) {
			if (!(new File(pathToParentFolder + "/.timings/" + productID).mkdir())) {
				System.out.println("Failed to create timings folder for " + productID);
				return false;
			}
		}
		else {
			if (!(new File(pathToParentFolder + "/.timings/" + productID).isDirectory())) {
				System.out.println("File with name timings found, but it's not a directory.");
				return false;
			}
		}
		
		// create the filename for the text file that will contain all the timings.
		// file names are formatted by the date (all builds that took place on the same day
		// will be in the same file). The date is represented by DAY.MONTH.YEAR without the forward
		// slashes.
		Calendar calendar = Calendar.getInstance();
		String fileName = this.pathToParentFolder + ".timings/" + productID + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "." + 
				(calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + ".txt";
		
		// create the file to be written, and then write to it
		try {
			// check to see if this file has been created already, and if so, add a newline between these timings
			// and previous timings
			boolean newFile = !(new File(fileName).exists());
			File file = new File(fileName);
			PrintWriter out = new PrintWriter(new FileOutputStream(file, true));
			if (!newFile) {
				out.write("\n");
			}
			for (int i = 2; i < splitInput.length; i++) {
				out.println("Step" + (i - 1) + ":" + splitInput[i]);
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to create file for product " + productID);
			//TODO: Add custom exception
		}

		return true;
	}
}
