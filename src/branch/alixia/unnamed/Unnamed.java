package branch.alixia.unnamed;

import java.io.File;

import branch.alixia.unnamed.api.Version;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class Unnamed extends Application {

	public static final Color DEFAULT_WINDOW_COLOR = new Color(0.34, 0.34, 0.34, 1);
	public static final Color ITEM_BORDER_COLOR = Color.BLUE;
	public static final Color SECONDARY_WINDOW_BORDER_COLOR = ITEM_BORDER_COLOR.interpolate(DEFAULT_WINDOW_COLOR, 0.5);
	private final static String PROGRAM_VERSION_IDENTIFIER = "0.2.2b0-nightly";
	public static final Version PROGRAM_VERSION = new Version(PROGRAM_VERSION_IDENTIFIER);

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

	}

	public static void showHome() {
		HomeWindow root = new HomeWindow(stage);
		stage.setScene(new Scene(root, Color.TRANSPARENT));
		stage.setMinHeight(400);
		stage.setMinWidth(600);
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
