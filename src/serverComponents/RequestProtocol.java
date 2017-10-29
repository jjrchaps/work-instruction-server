package serverComponents;

import containers.POUIContainer;
import customTypes.Images;
import customTypes.ServerPOUI;

public class RequestProtocol {
	private POUIContainer pouiContainer;

	public RequestProtocol(POUIContainer container) {
		pouiContainer = container;
	}


	public Images processRequest(String input) {
		ServerPOUI poui = pouiContainer.getPOUI(input);
		if (poui != null) {
			return poui.getImages();
		} else {
			return null;
		}
	}
}
