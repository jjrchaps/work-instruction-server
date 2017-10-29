package customTypes;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Images acts solely as a container for the LinkedList of POUI images to provide
 * easier type checking when sending between client and server.
 * @author jameschapman
 */
public class Images implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<ImageIcon> images;
	
	/**
	 * Constructs the image icon with the images located in the specified folder. Image
	 * should be named step1, step2, ..., stepN where n is the total amount of steps 
	 * for the product.
	 * @param folderLocation The path to where all the images are stored.
	 */
	public Images(String folderPath) {
		int numberOfImages = new File(folderPath).listFiles().length;
		images = new LinkedList<ImageIcon>();
		for (int i = 1; i <= numberOfImages; i++) {
			String fileName = folderPath + "step" + Integer.toString(i) + ".jpg";
			try {
				Image image = ImageIO.read(new File(fileName));
				ImageIcon img = new ImageIcon(image);
				images.add(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public LinkedList<ImageIcon> getImages() {
		return this.images;
	}
}
