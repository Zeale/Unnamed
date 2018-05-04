package branch.alixia.guis;

import branch.alixia.kröw.unnamed.guis.ConstructWindowImpl;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class MSGUIs {

	private MSGUIs() {

	}

	public static void openConstructWindow(Stage stage) {
		stage.setScene(new Scene(new ConstructWindowImpl(), Color.TRANSPARENT));
	}

}
