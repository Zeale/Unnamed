package branch.alixia.unnamed;

import branch.alixia.kröw.unnamed.HomeWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class Unnamed extends Application {

	public static final Color DEFAULT_WINDOW_COLOR = new Color((double) 0x35 / 255, (double) 0x35 / 255,
			(double) 0x35 / 255, 1);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Unnamed");
		primaryStage.initStyle(StageStyle.TRANSPARENT);

		primaryStage.setScene(new Scene(new HomeWindow()));
		primaryStage.getScene().setFill(Color.TRANSPARENT);

		primaryStage.setWidth(1000);
		primaryStage.setHeight(600);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

}
