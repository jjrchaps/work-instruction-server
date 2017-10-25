package serverComponents;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import customTypes.idImagePair;

public class RequestProtocol {
	/**
	 * Reference to a collection of POUI's that can be accessed by a client through this protocol
	 */
	private LinkedList<idImagePair<String, LinkedList<ImageIcon>>> pouiCollection;

	public RequestProtocol() {
		pouiCollection = new LinkedList<idImagePair<String, LinkedList<ImageIcon>>>();
		LinkedList<ImageIcon> images = new LinkedList<ImageIcon>();
		for (int i = 1; i <= 3; i++) {
			String fileName = "/Users/jameschapman/Projects/SED Projects/poui-server/Sample Images/" + "step" + Integer.toString(i) + ".jpg";
			Image image;
			try {
				image = ImageIO.read(new File(fileName));
				ImageIcon img = new ImageIcon(image);
				images.add(img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		idImagePair<String, LinkedList<ImageIcon>> newPair = new idImagePair<String, LinkedList<ImageIcon>>("test", images);
		pouiCollection.add(newPair);
	}


	public LinkedList<ImageIcon> processRequest(String input) {
			int offset = pouiCollection.indexOf(new idImagePair<String, LinkedList<ImageIcon>>("test", null));
			if (offset == -1) {
				return null;
			}
			else {
				return pouiCollection.get(offset).getImages();
			}
	}
}
