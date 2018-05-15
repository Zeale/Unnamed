package branch.alixia.kröw.unnamed;

import javafx.scene.Node;
import javafx.scene.Parent;

public final class Debug {
	public static void list(Parent parent, int level) {
		for (Node n : parent.getChildrenUnmodifiable()) {
			System.out.println("Parent: " + parent + ", Node: " + n + ", Level: " + level);
			if (n instanceof Parent) {
				list((Parent) n, level + 1);
				System.out.println();
			}
		}
	}
}
