package branch.alixia.unnamed;

import branch.alixia.kröw.unnamed.tools.FXTools;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;

public class MenuBar extends AnchorPane {

	protected final HBox buttonLayout = new HBox(2);
	protected final ObjectProperty<Image> icon = new SimpleObjectProperty<>();

	private final ImageView iconView = new ImageView();
	private final HBox leftLayout = new HBox(5, iconView);

	private final DoubleProperty rightMenuPadding = new SimpleDoubleProperty(10),
			rightMenuSpacing = new SimpleDoubleProperty(5);

	{
		getChildren().addAll(leftLayout, buttonLayout);
	}

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

		{
			add();
		}

		public final void setOnMouseEntered(EventHandler<? super MouseEvent> value) {
			button.setOnMouseEntered(value);
		}

		public final void setOnMouseExited(EventHandler<? super MouseEvent> value) {
			button.setOnMouseExited(value);
		}

		public void add() {
			buttonLayout.getChildren().add(button);
		}

		public void remove() {
			buttonLayout.getChildren().remove(button);
		}

		public MenuItem(Shape... shapes) {
			for (Shape s : shapes)
				addShape(s);
		}

		public MenuItem() {
		}

		public MenuItem(Node... nodes) {
			for (Node n : nodes)
				addChild(n);
		}

		public void addChild(Node child) {
			button.getChildren().add(child);
		}

		public void removeChild(Node child) {
			button.getChildren().remove(child);
		}

		public void addShape(Shape shape) {
			shape.setMouseTransparent(true);
			button.getChildren().add(shape);
		}

		public void removeShape(Shape shape) {
			shape.setMouseTransparent(false);
			button.getChildren().remove(shape);
		}

		public final void setOnMouseClicked(EventHandler<? super MouseEvent> value) {
			button.setOnMouseClicked(value);
		}

	}

	{

		setBackground(FXTools.getBackgroundFromColor(new LinearGradient(0.5, 0, 0.5, 1, true, CycleMethod.NO_CYCLE,
				new Stop(0.3, Color.TRANSPARENT), new Stop(1, Unnamed.DEFAULT_WINDOW_COLOR.darker().darker()))));
		setPrefHeight(20);

		AnchorPane.setRightAnchor(buttonLayout, 0d);
		AnchorPane.setBottomAnchor(buttonLayout, 0d);
		AnchorPane.setTopAnchor(buttonLayout, 0d);
		buttonLayout.spacingProperty().bind(rightMenuSpacing);
		rightMenuPadding.addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> AnchorPane
				.setRightAnchor(buttonLayout, newValue.doubleValue()));

		iconView.imageProperty().bind(icon);
		AnchorPane.setLeftAnchor(leftLayout, 0d);
		AnchorPane.setTopAnchor(leftLayout, 0d);
		AnchorPane.setBottomAnchor(leftLayout, 0d);
		iconView.fitWidthProperty().bind(iconView.fitHeightProperty());
		iconView.fitHeightProperty().bind(leftLayout.heightProperty().subtract(4));
		leftLayout.setAlignment(Pos.CENTER);

	}

	public final DoubleProperty rightMenuPaddingProperty() {
		return this.rightMenuPadding;
	}

	public final double getRightMenuPadding() {
		return this.rightMenuPaddingProperty().get();
	}

	public final void setRightMenuPadding(final double rightMenuPadding) {
		this.rightMenuPaddingProperty().set(rightMenuPadding);
	}

	public final DoubleProperty rightMenuSpacingProperty() {
		return this.rightMenuSpacing;
	}

	public final double getRightMenuSpacing() {
		return this.rightMenuSpacingProperty().get();
	}

	public final void setRightMenuSpacing(final double rightMenuSpacing) {
		this.rightMenuSpacingProperty().set(rightMenuSpacing);
	}

}
