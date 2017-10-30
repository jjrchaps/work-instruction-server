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
					String productID = pouiFolder.getName();
					// ignore any folders starting with a period as they're not 
					// valid for our purposes
					if (!(productID.substring(0, 1).equals("."))) {
						// append a slash as we're passing a path to a folder and will require
						// it in the next step (accessing the images within the folder)
						Images pouiImages = new Images(pouiFolder.getAbsolutePath() + "/");
						ServerPOUI poui = new ServerPOUI(productID, pouiImages);
						pouis.add(poui);
					}
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
	
	/**
	 * Returns all product id's concatenated down to single string, each separated by a 
	 * semicolon.
	 * @return All IDs of current available product POUIs
	 */
	public String getAllProductID() {
		String pouiString = "";
		
		for (ServerPOUI product : this.pouis) {
			pouiString = pouiString + product.getProductID() + ";";
		}
		// strip the last semicolon off the end.
		pouiString = pouiString.substring(0, pouiString.length());
		return pouiString;
	}
}
