package statistics;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * TimingInfoGenerator will generate timing information captured by the
 * client(s) in aggregate and display averages for each step, steps that
 * take the most time and least.
 * @author jameschapman
 */
public class TimingInfoGenerator {
	public String generateAverages(String pathToTimings) {
		File timingsFolder = new File(pathToTimings);
		LinkedList<Integer> timingAverages = new LinkedList<Integer>();
		for (File timingFile : timingsFolder.listFiles()) {
			Scanner in = new Scanner(timingFile.getAbsolutePath());
			while (in.hasNext()) {
				
			}
		}
		String result = "";
		
		
		
		
		return result;
	}
}
