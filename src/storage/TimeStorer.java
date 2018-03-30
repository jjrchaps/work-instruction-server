package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;

/**
 * timingStorer is responsible for the saving of recorded times that have been
 * reported to the server. It pulls strings from a common queue and creates the
 * appropriate files/folders to store times within.
 * @author jameschapman
 */
public class TimeStorer extends Thread {
	/**
	 * A queue to pull timing information to store to file.
	 */
	private BlockingQueue<String> queue;

	/**
	 * Path to the folder containing all POUIs, as all timings will be stored in a hidden folder within
	 * this parent folder.
	 */
	private String pathToParentFolder;

	public TimeStorer(BlockingQueue<String> queue, String pathToParentFolder) {
		this.queue = queue;
		this.pathToParentFolder = pathToParentFolder;
	}

	public void run() {
		while (true) {
			try {
				reportTimings(queue.take());
			} catch (InterruptedException e) {
				// even if interrupted we require thread to continue working, so do nothing with
				// this error and keep storing items in the queue.
			}
		}
	}

	/**
	 * Accepts an array of strings that represent the recorded timings from a client upon
	 * completion of the product.
	 * @param splitInput All the timings, as string, separated by a colon.
	 * @return True if the timings were successfully handled, false otherwise.
	 */
	private boolean reportTimings(String input) {
		String[] splitInput = input.split(":");
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
			File file = new File(fileName);
			PrintWriter out = new PrintWriter(new FileOutputStream(file, true));
			for (int i = 2; i < splitInput.length; i++) {
				out.println(splitInput[i]);
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to create file for product " + productID);
		}
		
		return true;
	}
}
