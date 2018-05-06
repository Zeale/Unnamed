package branch.alixia.unnamed;

import branch.alixia.kröw.unnamed.guis.constructs.ConstructWindowImpl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class MSGUIs {

	private MSGUIs() {

	}

	public static Parent openConstructWindow(Stage stage) {
		return new ConstructWindowImpl(stage);
	}

	public static Parent openConstructWindow() {
		return new ConstructWindowImpl(null);
	}

	public static void showHomeWindow() {
		Unnamed.showHome();
	}

	public static void setScene(Scene scene) {
		Unnamed.setScene(scene);
	}

}
