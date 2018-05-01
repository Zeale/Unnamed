package branch.alixia.unnamed;

import branch.alixia.kr�w.unnamed.tools.FXTools;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class MenuBar extends AnchorPane {

	protected final HBox buttonLayout = new HBox(2);
	protected final ObjectProperty<Image> icon = new SimpleObjectProperty<>();
	private final ImageView iconView = new ImageView();

	public final ObjectProperty<Image> iconProperty() {
		return this.icon;
	}

	public final Image getIcon() {
		return this.iconProperty().get();
	}

	public final void setIcon(final Image icon) {
		this.iconProperty().set(icon);
	}

	public class MenuItem {

		private final StackPane button = new StackPane();

		public void add() {
			buttonLayout.getChildren().add(button);
		}

		public void remove() {
			buttonLayout.getChildren().remove(button);
		}

		public void addShape(Shape shape) {
			shape.setMouseTransparent(true);
		}

		public void removeShape(Shape shape) {
			shape.setMouseTransparent(false);
		}

		public final void setOnMouseClicked(EventHandler<? super MouseEvent> value) {
			button.setOnMouseClicked(value);
		}

	}

	{
		setBackground(FXTools.getBackgroundFromColor(new Color(0, 0, 0, 0.5)));
		setPrefHeight(20);

		AnchorPane.setRightAnchor(buttonLayout, 0d);
		AnchorPane.setBottomAnchor(buttonLayout, 0d);
		AnchorPane.setTopAnchor(buttonLayout, 0d);

		iconView.imageProperty().bind(icon);
		AnchorPane.setLeftAnchor(iconView, 0d);
		AnchorPane.setTopAnchor(iconView, 0d);
		AnchorPane.setBottomAnchor(iconView, 0d);
		iconView.fitWidthProperty().bind(iconView.fitHeightProperty());

	}

}
