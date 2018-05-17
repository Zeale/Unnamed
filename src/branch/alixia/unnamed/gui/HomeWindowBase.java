package branch.alixia.unnamed.gui;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.fx.nodes.ItemBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HomeWindowBase extends UWindowBase {

	public HomeWindowBase(Stage stage) {
		super(stage);
	}

	public HomeWindowBase() {
	}

	protected final VBox right = new VBox(), left = new VBox();
	protected final ItemBox center = new ItemBox();

	public abstract class Item extends ItemBox.Item {

		public Item() {
			center.super();
		}

		public Item(String name) {
			center.super(name);
		}

	}

	// protected final ScrollBox bottom = new ScrollBox();// TODO Define

	protected final ScrollPane rightRoot = new ScrollPane(right), leftRoot = new ScrollPane(left),
			bottomRoot = new ScrollPane();

	{

		String stylesheet = "/branch/alixia/kröw/unnamed/stylesheet.css";
		rightRoot.getStylesheets().add(stylesheet);
		leftRoot.getStylesheets().add(stylesheet);
		bottomRoot.getStylesheets().add(stylesheet);

		rightRoot.setBackground(null);
		leftRoot.setBackground(null);
		bottomRoot.setBackground(null);

		setCenter(center);
		setLeft(leftRoot);
		setRight(rightRoot);
		setBottom(bottomRoot);

		rightRoot.setMinWidth(100);
		leftRoot.setMinWidth(100);
		bottomRoot.setMinHeight(100);

		setBorder(FXTools.getBorderFromColor(Color.BLACK));

		center.resize(1000, 0);// This makes sure that the window doesn't start out too wide.

		if (getMenuBar() != null)
			getMenuBar().setBorder(new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 2, 0))));

		rightRoot.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 0, 2))));
		leftRoot.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 2, 0, 0))));
		bottomRoot.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 0, 0, 0))));

	}

}
