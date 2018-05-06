package branch.alixia.unnamed;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class Unnamed extends Application {

	public static final Color DEFAULT_WINDOW_COLOR = new Color(0.34, 0.34, 0.34, 1);
	public static final Color BASE_COLOR = Color.BLUE;

	public static final File PROGRAM_ROOT = new File(System.getProperty("user.home"), "Unnamed");

	static {
		if (!PROGRAM_ROOT.isDirectory()) {
			PROGRAM_ROOT.delete();
			if (!PROGRAM_ROOT.mkdirs()) {
				System.out.println("Failed to create the program directory!");
			}
		}

		if (!PROGRAM_ROOT.canRead()) {
			System.out.println("Unable to read program directory!");
			if (PROGRAM_ROOT.isDirectory())
				System.out.println("It is a directory however (i.e. it's not a file), and it exists.");
		}

		if (!PROGRAM_ROOT.canWrite()) {
			System.out.println("Unable to write to program directory!");
			if (PROGRAM_ROOT.isDirectory())
				System.out.println("It is a directory however (i.e. it's not a file), and it exists.");
		}

	}

	private static Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		stage = primaryStage;
		primaryStage.setTitle("Unnamed");
		primaryStage.initStyle(StageStyle.TRANSPARENT);

		showHome();

		// The home window will need to verify that everything has loaded before it lets
		// the user click buttons that involve loaded items.
		if (!PROGRAM_ROOT.isDirectory()) {
			// TODO Load things and have the HomeWindow reflect what has been loaded.
		}

	}

	public static void showHome() {
		HomeWindow root = new HomeWindow();
		stage.setScene(new Scene(root, Color.TRANSPARENT));
		stage.centerOnScreen();
		stage.show();
	}

	public static void setScene(Scene scene) {
		stage.setScene(scene);
	}
	
	static Stage getStage() {
		return stage;
	}

}
