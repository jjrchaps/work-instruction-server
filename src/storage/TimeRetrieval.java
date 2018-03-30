package storage;

import java.io.BufferedReader;
import java.io.File;
import java.util.Calendar;

/**
 * TimeRetrieval will allow the access of timing files that have been stored. All captured times
 * besides those of the current date will be available. Available will be raw times, average total
 * build time, and average time per step.
 * @author jameschapman
 */
public class TimeRetrieval {
	/**
	 * Path to the folder containing all POUIs, as all timings are stored in hidden timings folder
	 * in this location
	 */
	private String pathToParentFolder;

	public TimeRetrieval(String pathToParentFolder) {
		this.pathToParentFolder = pathToParentFolder;
	}

	/**
	 * Will retrieve all raw times and format properly to be displayed to user.
	 * @param productID The product ID of the desired times
	 * @return A formatted string ready for display to the user
	 */
	public String retrieveRawTimes(String productID) {
		// Since the current date will not be viewable, we can simply ignore the file with the
		// current days date. As well, with this method we don't have to worry about clashing
		// with other thread that could be writing concurrently to these calculations.
		Calendar calendar = Calendar.getInstance();
		String fileToIgnore =calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + 
				"." + calendar.get(Calendar.YEAR) + ".txt";
		String results = null;
		String pathToTimingFolder = this.pathToParentFolder + "/.timings/ + productID";
		File timingsFolder = new File(pathToTimingFolder);
		if (!(timingsFolder.exists())) {
			return "No timings available for " + productID;
		}

		String[] fileNames = timingsFolder.list();
		if (fileNames.length == 1) {
			if (fileNames[0].equals(fileToIgnore)) {
				return "No timings available for " + productID;
			}
		}
		
		int numberOfSteps = getNumberOfSteps(productID);
		BufferedReader in;
		for (String fileName : fileNames) {
			if (!(fileName.equals(fileToIgnore))) {
				in = new BufferedReader(new FileReader())
			}
		}



		return results;
	}
	
	/**
	 * Returns the number of steps in an assembly given the product ID
	 * @param productID The product in questions
	 * @return The total number of assembly steps in productID's assembly
	 */
	private int getNumberOfSteps(String productID) {
		String pathToAssemblyImages = this.pathToParentFolder + "/" + productID;
		int numberOfImages = new File(pathToAssemblyImages).listFiles().length;
		String pathToInspections = pathToAssemblyImages + "/inspections.txt";
		if (new File(pathToInspections).exists()) {
			// subtract one from the total image count since we now know that one of the files
			// included is a text file, not an image.
			numberOfImages--;
		}
		return numberOfImages;
	}
}
