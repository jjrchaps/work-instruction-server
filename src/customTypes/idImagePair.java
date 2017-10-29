package customTypes;

/**
 * Stores an Event e and a string identifier in a pair to be stored
 * within the EventContainer class.
 * @author jameschapman
 */

public class idImagePair<sedid, images> {
		private String productID;
		private Images images;
		
		public idImagePair(String id, Images images) {
			this.productID = id;
			this.images = images;
		}
		
		public String getProductID() { return this.productID; }
		public Images getImages() { return this.images; }
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof idImagePair)) return false;
			@SuppressWarnings("unchecked")
			idImagePair<String, Images> compareTo = (idImagePair<String, Images>) o;
			return this.productID.equals(compareTo.getProductID());
		}
}