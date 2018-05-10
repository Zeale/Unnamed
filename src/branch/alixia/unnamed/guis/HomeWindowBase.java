package branch.alixia.unnamed.guis;

import branch.alixia.kr�w.unnamed.DefaultMenuBar;
import branch.alixia.kr�w.unnamed.tools.FXTools;
import branch.alixia.unnamed.fx.nodes.ItemBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class HomeWindowBase extends UWindowBase {

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
		// Set Menu Bar
		setMenuBar(new DefaultMenuBar());

		String stylesheet = "/branch/alixia/kr�w/unnamed/stylesheet.css";
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

		getMenuBar().setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 2, 0))));
		setBorder(FXTools.getBorderFromColor(Color.BLACK, 2));
		rightRoot.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 0, 2))));
		leftRoot.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 2, 0, 0))));
		bottomRoot.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 0, 0, 0))));

	}

}
