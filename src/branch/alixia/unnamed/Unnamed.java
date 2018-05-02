package branch.alixia.unnamed;

import branch.alixia.kr�w.unnamed.HomeWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class Unnamed extends Application {

	public static final Color DEFAULT_WINDOW_COLOR = new Color(0.34, 0.34, 0.34, 1);
	public static final Color BASE_COLOR = Color.BLUE;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Unnamed");
		primaryStage.initStyle(StageStyle.TRANSPARENT);

		HomeWindow root = new HomeWindow();
		root.new Item("Updates") {

			{
				addImage(new Image("/branch/alixia/kr�w/unnamed/resources/graphics/Blue C-1024px.png"));
			}

			@Override
			protected void activate() {

				double distance = Math.random() * 150 + 50;
				root.setShadowX(distance);
				root.setShadowY(distance);

			}

		};

		primaryStage.setScene(new Scene(root));
		primaryStage.getScene().setFill(Color.TRANSPARENT);

		primaryStage.centerOnScreen();
		primaryStage.show();

	}

}
