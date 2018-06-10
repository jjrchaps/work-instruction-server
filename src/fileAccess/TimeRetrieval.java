package fileAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

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

		results = "";
		for (String fileName : fileNames) {
			if (!(fileName.equals(fileToIgnore))) {
				results += formatSingleDaysTimingInfo(productID, fileName);
			}
		}
		return results;
	}

	//TODO: Properly sort the order that the days are displayed to the user,
	// Maybe make 2-tuple array, one with file name to be selected and the other with the names
	// reformatted so it is displayed to the user properly?
	/**
	 * Goes to specified product and lists every available day that timings were captured.
	 * @param productID The product that the available days of timings are desired
	 * @return A formatted string containing the dates of all days timings were captured, 
	 * null if no timing information available for supplied productID
	 */
	public String listAvailableDays(String productID) {
		String pathToTimingsFolder = this.pathToParentFolder + ".timings/" + productID + "/";
		File timingsFolder = new File(pathToTimingsFolder);
		if (timingsFolder.exists() && timingsFolder.isDirectory()) {
			String[] files = timingsFolder.list();
			Arrays.sort(files, new Comparator<String>() {
				public int compare(String first, String second) {
					// split the file names into string arrays, and then convert to integers.
					String[] firstString = first.split("\\.");
					int[] firstNumbers = {0,0,0};
					for (int i = 0; i < 3; i++) {
						firstNumbers[i] = Integer.parseInt(firstString[i]);
					}
					String[] secondString = second.split("\\.");
					int[] secondNumbers = {0,0,0};
					for (int i = 0; i < 3; i++) {
						secondNumbers[i] = Integer.parseInt(secondString[i]);
					}
					// compare years, check to see if years are different
					if (firstNumbers[2] < secondNumbers[2]) {
						return 1;
					}
					else if (firstNumbers[2] > secondNumbers[2]) {
						return -1;
					}
					// if years aren't different, compare months
					else {
						if (firstNumbers[1] < secondNumbers[1]) {
							return 1;
						}
						else if (firstNumbers[1] > secondNumbers[1]) {
							return -1;
						}
						// if months aren't different, then compare days.
						else {
							if (firstNumbers[0] < secondNumbers[0]) {
								return 1;
							}
							else if (firstNumbers[0] > secondNumbers[0]) {
								return -1;
							}
							else {
								return 0;
							}
						}
					}
				}
			});
			String formattedAvailableDays = "";
			boolean firstDay = true;
			for (String timingFile : files) {
				if (firstDay) {
					formattedAvailableDays += timingFile.substring(0, 
							timingFile.length()-4);
					firstDay = false;
				}
				else {
					formattedAvailableDays += "\n" + timingFile.substring(0, 
							timingFile.length()-4);
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
	 * Gets the raw timing information for a specific day selected by the user
	 * @param productID The id of the product the timing information is needed for
	 * @param day The day in question selected by the user.
	 * @return The days timing information if available/exists, null otherwise.
	 */
	public String retrieveSpecificDayRawTimes(String productID, String day) {
		// add .txt extension to day string as the user will not be made to enter it.
		day += ".txt";
		String pathToSelectedDay = this.pathToParentFolder + ".timings/" + productID + "/" + day;
		File selectedDay = new File(pathToSelectedDay);
		if (selectedDay.exists()) {
			return formatSingleDaysTimingInfo(productID, day);
		}
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

	/**
	 * Formats the timing results for the product matching the id on the specified day
	 * @param productID The product being inquired about
	 * @param date The date in question
	 * @return The timing results formatted to be displayed, null if they don't exists or couldn't
	 * be accessed.
	 */
	private String formatSingleDaysTimingInfo(String productID, String date) {
		String filePath = pathToParentFolder + ".timings/" + productID + "/" + date;
		BufferedReader in;
		String results = "";
		try {
			in = new BufferedReader(new FileReader(filePath));
			// Add date the data was captured above the raw times when before they're displayed
			results += "Date: " + date.subSequence(0, date.length()-4) + "\n";
			results += "----\n";
			// Begin reading from file to list the raw times
			String nextLine = in.readLine();
			int counter = 1;
			float buildTime = 0;
			while (nextLine != null) {
				results = results + "Step " + counter + ": " + nextLine + "s\n";
				buildTime += Float.parseFloat(nextLine);
				if (counter == getNumberOfSteps(productID)) {
					BigDecimal roundedTime = new BigDecimal(Float.toString(buildTime));
					roundedTime = roundedTime.setScale(2, BigDecimal.ROUND_HALF_UP);
					results += "Build Time: " + roundedTime.floatValue() + " seconds";
					results += "\n----\n";
					counter = 0;
					buildTime = 0;
				}
				nextLine = in.readLine();
				counter++;
			}
			in.close();
			return results;
		} catch (IOException e) {
			e.printStackTrace();
		}
		// if it's come to this point, an exception has been thrown. Return null as no data
		// could be successfully retrieved.
		return null;
	}

	public static void main(String[] args) {
		TimeRetrieval test = new TimeRetrieval("/Users/jameschapman/Projects/Images/");
		System.out.println(test.retrieveAllRawTimes("test"));
		System.out.println(test.listAvailableDays("test"));
	}
}
