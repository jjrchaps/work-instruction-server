package fileAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	public String retrieveAllRawTimes(String productID) {
		// catches blank input
		if (productID.equals("")) {
			return "";
		}
		// Since the current date will not be viewable, we can simply ignore the file with the
		// current days date. As well, with this method we don't have to worry about clashing
		// with other thread that could be writing concurrently to these calculations.
		Calendar calendar = Calendar.getInstance();
		String fileToIgnore =calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + 
				"." + calendar.get(Calendar.YEAR) + ".txt";
		String results = "";
		String pathToTimingFolder = this.pathToParentFolder + "/.timings/" + productID;
		File timingsFolder = new File(pathToTimingFolder);
		if (!(timingsFolder.exists())) {
			return "No timings available for " + productID;
		}

		// If there is only one file and it is from the current date, return early as to not
		// interfere with potential storing of other data.
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
				String filePath = pathToTimingFolder + "/" + fileName;
				try {
					in = new BufferedReader(new FileReader(filePath));
					// Add date the data was captured above the raw times when before they're displayed
					results += "Date: " + fileName.subSequence(0, fileName.length()-4) + "\n";
					results += "----\n";
					// Begin reading from file to list the raw times
					String nextLine = in.readLine();
					int counter = 1;
					float buildTime = 0;
					while (nextLine != null) {
						results = results + "Step " + counter + ": " + nextLine + "s\n";
						buildTime += Float.parseFloat(nextLine);
						if (counter == numberOfSteps) {
							results += "Build Time: " + buildTime + " seconds";
							results += "\n----\n";
							counter = 0;
							buildTime = 0;
						}
						nextLine = in.readLine();
						counter++;
					}
					results += "\n";
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	/**
	 * Goes to specified product and lists every available day that timings were captured.
	 * @param productID The product that the available days of timings are desired
	 * @return A formatted string containing the dates of all days timings were captured.
	 */
	public String listAvailableDays(String productID) {
		String pathToTimingsFolder = this.pathToParentFolder + ".timings/" + productID + "/";
		File timingsFolder = new File(pathToTimingsFolder);
		if (timingsFolder.exists() && timingsFolder.isDirectory()) {
			String formattedAvailableDays = "";
			boolean firstDay = true;
			for (File timingFile : timingsFolder.listFiles()) {
				if (firstDay) {
					formattedAvailableDays += timingFile.getName().substring(0, 
							timingFile.getName().length()-4);
					firstDay = false;
				}
				else {
					formattedAvailableDays += "\n" + timingFile.getName().substring(0, 
							timingFile.getName().length()-4);
				}
			}
			// If there were in fact timings captured, return the formatted string
			if (!(formattedAvailableDays.equals(""))) {
				return formattedAvailableDays;
			}
		}
		// If the string is empty of the timings folder doesn't exist, return null.
		return null;
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

	public static void main(String[] args) {
		TimeRetrieval test = new TimeRetrieval("/Users/jameschapman/Projects/Images/");
		System.out.println(test.retrieveAllRawTimes("test"));
		System.out.println(test.listAvailableDays("test"));
	}
}
