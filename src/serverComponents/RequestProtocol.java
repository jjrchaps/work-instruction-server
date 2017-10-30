package serverComponents;

import containers.POUIContainer;
import customTypes.Images;
import customTypes.ServerPOUI;

public class RequestProtocol {
	private POUIContainer pouiContainer;

	public RequestProtocol(POUIContainer container) {
		pouiContainer = container;
	}


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
