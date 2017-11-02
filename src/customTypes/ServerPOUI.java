package customTypes;

/**
 * Stores an Event e and a string identifier in a pair to be stored
 * within the EventContainer class.
 * @author jameschapman
 */
public class ServerPOUI {
	private String productID;
	private Images images;
	
	/**
	 * Creates a new instance of ServerPOUI which is the server side version of a Point Of Use Instruction.
	 * @param id The given identification of a POUI, could be a model name, number, product ID, etc.
	 * @param images The image versions of all stages of the POUI.
	 */
	public ServerPOUI(String id, Images images) {
		this.productID = id;
		this.images = images;
	}

	public String getProductID() { 
		return this.productID; 
	}

	public Images getImages() { 
		return this.images; 
	}
}