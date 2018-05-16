package branch.alixia.kröw.unnamed;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.MSGUIs;
import branch.alixia.unnamed.MenuBar;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DefaultMenuBar extends MenuBar {

	protected final class Item extends MenuItem {
		private final ObjectProperty<Color> hoverColor = new SimpleObjectProperty<Color>(new Color(0, 0, 0, 0.6));
		{
			setOnMouseEntered(event -> getButton().setBackground(FXTools.getBackgroundFromColor(hoverColor.get())));
			setOnMouseExited(event -> getButton().setBackground(null));
		}

		public Item() {
		}

		public Item(Node... nodes) {
			super(nodes);
		}

		public Item(Shape... shapes) {
			super(shapes);
		}

		public final ObjectProperty<Color> hoverColorProperty() {
			return this.hoverColor;
		}

		public final Color getHoverColor() {
			return this.hoverColorProperty().get();
		}

		public final void setHoverColor(final Color hoverColor) {
			this.hoverColorProperty().set(hoverColor);
		}

	}

	private final Text homeText = new Text("H");
	private final MenuItem home = new Item(homeText);

	private final Rectangle rightCloseBar = new Rectangle(), leftCloseBar = new Rectangle();
	private final MenuItem close = new Item(rightCloseBar, leftCloseBar);

	{

		setRightMenuPadding(0);
		setRightMenuSpacing(8);

		rightCloseBar.heightProperty().bind(close.getButton().heightProperty().multiply(0.75));
		leftCloseBar.heightProperty().bind(rightCloseBar.heightProperty());
		rightCloseBar.widthProperty().bind(rightCloseBar.heightProperty().divide(6));
		leftCloseBar.widthProperty().bind(leftCloseBar.heightProperty().divide(6));

		rightCloseBar.setRotate(45);
		leftCloseBar.setRotate(-45);

		rightCloseBar.setFill(Color.RED);
		leftCloseBar.setFill(Color.RED);

		close.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY)
				getScene().getWindow().hide();
		});

		homeText.setFill(Color.BLUE);
		homeText.setFont(Font.font(null, FontWeight.BOLD, -1));

		home.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY)
				MSGUIs.showHomeWindow();
		});

	}

}
