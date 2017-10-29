package containers;

import java.io.File;
import java.util.LinkedList;

import customTypes.Images;
import customTypes.ServerPOUI;

/**
 * POUIContainer is a container class to store all of the poui's for future
 * client access. Goes through parentFolder and creates ServerPOUI Objects with
 * the directories within.
 * @author jameschapman
 *
 */
public class POUIContainer {
	private LinkedList<ServerPOUI> pouis;
	
	/**
	 * Creates a new instance of POUIContainer. For all folders within folderOfPouis, 
	 * generate a new ServerPOUI object and store in pouis.
	 * @param folderOfPouis The parent folder containing all POUI's that will be accessible to clients
	 */
	public POUIContainer(String folderOfPouis) {
		pouis = new LinkedList<ServerPOUI>();
		
		// if the path given is in fact a directory, loop through directory and 
		// generate ServerPOUI objects
		File parentFile = new File(folderOfPouis);
		if (parentFile.isDirectory()) {
			for (File pouiFolder : parentFile.listFiles()) {
				if (pouiFolder.isDirectory()) {
					Images pouiImages = new Images(pouiFolder.getAbsolutePath());
					String productID = pouiFolder.getName();
					ServerPOUI poui = new ServerPOUI(productID, pouiImages);
					pouis.add(poui);
				}
			}
		}
	}
	
	public ServerPOUI getPOUI(String productID) {
		// iterate through pouis to find a match, and if found return it.
		for (ServerPOUI poui : pouis) {
			if (poui.getProductID().equals(productID)) {
				return poui;
			}
		}
		// if we get to this point it means that we didn't find a match. Return null in this case.
		return null;
	}
}
