package customTypes;

import java.util.LinkedList;

import javax.swing.ImageIcon;

/**
 * Stores an Event e and a string identifier in a pair to be stored
 * within the EventContainer class.
 * @author jameschapman
 */

public class idImagePair<sedid, images> {
		private String productID;
		private LinkedList<ImageIcon> images;
		
		public idImagePair(String id, LinkedList<ImageIcon> images) {
			this.productID = id;
			this.images = images;
		}
		
		public String getProductID() { return this.productID; }
		public LinkedList<ImageIcon> getImages() { return this.images; }
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof idImagePair)) return false;
			@SuppressWarnings("unchecked")
			idImagePair<String, LinkedList<ImageIcon>> compareTo = (idImagePair<String, LinkedList<ImageIcon>>) o;
			return this.productID.equals(compareTo.getProductID());
		}
}