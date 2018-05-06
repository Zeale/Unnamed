package branch.alixia.unnamed;

import branch.alixia.kröw.unnamed.guis.constructs.ConstructWindowImpl;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class MSGUIs {

	private MSGUIs() {

	}

	public static Parent openConstructWindow() {
		return new ConstructWindowImpl();
	}

	public static void showHomeWindow() {
		Unnamed.showHome();
	}

	public static void setScene(Scene scene) {
		Unnamed.setScene(scene);
	}

}
