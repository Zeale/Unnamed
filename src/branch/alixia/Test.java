package branch.alixia;

import branch.alixia.unnamed.Images;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Test extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		ImageView view = new ImageView(
				// Loading this image with a size smaller than it's actual size (800x600 vs
				// 1024x1024) will help show the issue where the non-resizable ImageView class
				// stops the pane it's in from being shrunk (as well as not shrinking, itself)
				// when the window is resized.
				//
				// This issue just occurs when the window is resized to be smaller than the
				// image itself which the ImageView is layered on top of. This means that
				// whatever size the Image is loaded with has an effect on the ImageView that
				// renders it.
				new Image(Images.GRAPHICS_LOCATION + "Construct Icon-1024px.png", 800, 600, false, false));
		StackPane sizer = new StackPane();
		AnchorPane pane = new AnchorPane(view, sizer);
		primaryStage.setScene(new Scene(new BorderPane(pane)));

		primaryStage.setWidth(800);
		primaryStage.setHeight(600);

		AnchorPane.setTopAnchor(sizer, 0d);
		AnchorPane.setBottomAnchor(sizer, 0d);
		AnchorPane.setLeftAnchor(sizer, 0d);
		AnchorPane.setRightAnchor(sizer, 0d);

		view.fitHeightProperty().bind(sizer.heightProperty());
		view.fitWidthProperty().bind(sizer.widthProperty());

		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
