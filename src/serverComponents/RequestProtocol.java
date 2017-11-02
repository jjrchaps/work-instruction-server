package serverComponents;

import containers.POUIContainer;
import customTypes.Images;
import customTypes.ServerPOUI;

/**
 * RequestProtocol receives all the requests made by a client, determines what the proper way to handle is, 
 * and executes accordingly.
 * @author jameschapman
 */
public class RequestProtocol {
	/**
	 * A local instance of all the POUI available to the server.
	 */
	private POUIContainer pouiContainer;
	
	/**
	 * Constructs a new instance of RequestProtocol with a container of all POUIs
	 * @param container The container containing all POUIs known to the server.
	 */
	public RequestProtocol(POUIContainer container) {
		pouiContainer = container;
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
		ServerPOUI poui = pouiContainer.getPOUI(input);
		if (poui != null) {
			return poui.getImages();
		}
		else {
			return null;
		}
	}
	
	private String productListRequest() {
		return pouiContainer.getAllProductID();
	}
}
