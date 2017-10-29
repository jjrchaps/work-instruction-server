package serverComponents;

import java.util.LinkedList;

import customTypes.Images;
import customTypes.idImagePair;

public class RequestProtocol {
	/**
	 * Reference to a collection of POUI's that can be accessed by a client through this protocol
	 */
	private LinkedList<idImagePair<String, Images>> pouiCollection;

	public RequestProtocol() {
		pouiCollection = new LinkedList<idImagePair<String, Images>>();
		Images images = new Images("/Users/jameschapman/Projects/SED Projects/poui-server/Sample Images/");
		idImagePair<String, Images> newPair = new idImagePair<String, Images>("test", images);
		pouiCollection.add(newPair);
	}


	public Images processRequest(String input) {
			int offset = pouiCollection.indexOf(new idImagePair<String, Images>(input, null));
			if (offset == -1) {
				return null;
			}
			else {
				return pouiCollection.get(offset).getImages();
			}
	}
}
