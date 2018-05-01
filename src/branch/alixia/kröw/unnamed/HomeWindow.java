package branch.alixia.kröw.unnamed;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.UWindowBase;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class HomeWindow extends UWindowBase {

	private static final double DEFAULT_BORDER_WIDTH = 3;

	protected final TilePane content = new TilePane();
	protected final ScrollPane root = new ScrollPane(content);

	{

		root.getStylesheets().add("/branch/alixia/kröw/unnamed/stylesheet.css");

		content.setPrefColumns(4);
		root.setBackground(null);
		content.setBackground(null);

		setCenter(root);

		content.setPadding(new Insets(90, 80, 0, 80));

	}

	private final DoubleProperty minimumItemSizeProperty = new SimpleDoubleProperty(140);

	public abstract class Item extends StackPane {

		{
			minWidthProperty().bind(minimumItemSizeProperty);
			minHeightProperty().bind(minimumItemSizeProperty);
		}

		private final class ItemAnimation extends Transition {

			private Color fromValue = new Color(0.8, 0, 0, 1), toValue = new Color(1, 1, 1, 1);

			{
				setCycleDuration(Duration.seconds(0.6));
			}

			public void setFromValue(Color fromValue) {
				this.fromValue = fromValue;
			}

			public void setToValue(Color toValue) {
				this.toValue = toValue;
			}

			@Override
			protected void interpolate(double frac) {
				setBorder(new Border(new BorderStroke(fromValue.interpolate(toValue, frac), BorderStrokeStyle.SOLID,
						new CornerRadii(getWidth() / 10 * frac), new BorderWidths(DEFAULT_BORDER_WIDTH))));

			}

		}

		private final ItemAnimation animation = new ItemAnimation();

		private final VBox wrapper = new VBox(15, this);

		{
			setOnMouseEntered(event -> {
				animation.setRate(1);
				animation.play();
			});

			setOnMouseExited(event -> {
				animation.setRate(-1);
				animation.play();
			});

			// Must be called underneath wrapper's definition.
			add();

			Color color = new Color(1, 1, 1, 1);
			setBorderColor(color);
			setBorderToColor(color);

			DropShadow value = new DropShadow(5, 30, 30, Color.BLACK);
			setEffect(value);

		}

		protected abstract void activate();

		public void setBorderColor(Color color) {
			setBorder(FXTools.getBorderFromColor(color, DEFAULT_BORDER_WIDTH));
			animation.setFromValue(color);
		}

		public void setBorderToColor(Color color) {
			animation.setToValue(color);
		}

		public void add() {
			content.getChildren().add(wrapper);
		}

		public void remove() {
			content.getChildren().remove(wrapper);
		}

		public final void setPos(int pos) {
			content.getChildren().remove(wrapper);
			content.getChildren().add(pos, wrapper);
		}

		public final int getPos() {
			return content.getChildren().indexOf(wrapper);
		}

	}

}
