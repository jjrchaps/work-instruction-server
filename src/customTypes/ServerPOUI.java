package customTypes;

/**
 * Stores an Event e and a string identifier in a pair to be stored
 * within the EventContainer class.
 * @author jameschapman
 */

public class ServerPOUI {
	private String productID;
	private Images images;

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