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
		input = input.toLowerCase();
		String[] splitInput = input.split(":");
		if (splitInput[0].equals("pouirequest")) {
			return pouiRequest(splitInput[1]);
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
}
