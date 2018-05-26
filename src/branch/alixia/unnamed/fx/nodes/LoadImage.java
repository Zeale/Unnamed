package branch.alixia.unnamed.fx.nodes;

import branch.alixia.unnamed.Images;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoadImage extends ImageView {

	public LoadImage() {
	}

	public LoadImage(Image image) {
		super(image);
	}

	public LoadImage(String url) {
		Images.loadImageInBackground(this, url);
	}

}
