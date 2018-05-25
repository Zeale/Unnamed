package branch.alixia.unnamed.fx.nodes;

import java.net.MalformedURLException;
import java.net.URL;

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
		try {
			Images.loadImageInBackground(this, new URL(url));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
