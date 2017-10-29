package serverComponents;

import containers.POUIContainer;
import customTypes.Images;

public class RequestProtocol {
	private POUIContainer pouiContainer;
	
	public RequestProtocol(POUIContainer container) {
		pouiContainer = container;
	}


	public Images processRequest(String input) {
		return pouiContainer.getPOUI(input).getImages();
	}
}
