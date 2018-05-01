package branch.alixia.kröw.unnamed;

import branch.alixia.unnamed.UWindowBase;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class HomeWindow extends UWindowBase {

	protected final TilePane root = new TilePane();

	{

		root.setPrefColumns(4);

		setCenter(root);
	}

	public final class Item extends StackPane {

		private final VBox wrapper = new VBox(15, this);

		public void add() {
			root.getChildren().add(wrapper);
		}

		public void remove() {
			root.getChildren().remove(wrapper);
		}

		public final void setPos(int pos) {
			root.getChildren().remove(wrapper);
			root.getChildren().add(pos, wrapper);
		}

		public final int getPos() {
			return root.getChildren().indexOf(wrapper);
		}

	}

}
