package branch.alixia.guis;

import branch.alixia.kr�w.unnamed.guis.ConstructWindowImpl;
import javafx.scene.Parent;

public final class MSGUIs {

	private MSGUIs() {

	}

	public static Parent openConstructWindow() {
		return new ConstructWindowImpl();
	}

}
