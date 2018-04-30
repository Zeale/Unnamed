package branch.alixia.unnamed;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class Unnamed extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Unnamed");
		primaryStage.initStyle(StageStyle.TRANSPARENT);

		primaryStage.setScene(new Scene(new UWindowBase()));
		primaryStage.getScene().setFill(Color.TRANSPARENT);

		primaryStage.setWidth(1000);
		primaryStage.setHeight(600);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

}
