package branch.alixia.kröw.unnamed;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.UWindowBase;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class HomeWindow extends UWindowBase {

	private static final double DEFAULT_BORDER_WIDTH = 3;
	private final static String UNNAMED_STRING = new String("Unnamed");
	private final static DropShadow DEFAULT_ITEM_SHADOW_EFFECT = new DropShadow(5, 30, 30, Color.BLACK);
	private static final Color DEFAULT_ITEM_COLOR = Color.BLUE;

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

		// Does SimpleStringProperty guarantee to return the EXACT same String you give
		// it? Or just an equal String? Probably the former, but whatever. This works
		// too.
		private final ObjectProperty<String> name = new SimpleObjectProperty<>(UNNAMED_STRING);

		public Item() {
			this(UNNAMED_STRING);
		}

		public Item(String name) {
			this.name.set(name);
		}

		private final class ItemAnimation extends Transition {

			private Color fromValue = DEFAULT_ITEM_COLOR, toValue = DEFAULT_ITEM_COLOR;

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

		private final Text nameText = new Text();
		private final VBox wrapper = new VBox(15, this, nameText);

		{

			minWidthProperty().bind(minimumItemSizeProperty);
			minHeightProperty().bind(minimumItemSizeProperty);

			wrapper.setFillWidth(true);
			wrapper.setAlignment(Pos.CENTER);

			nameText.setFont(Font.font("Brush Script MT", 32));
			nameText.setTextAlignment(TextAlignment.CENTER);
			nameText.textProperty().bind(name);

			setOnMouseEntered(event -> {
				animation.setRate(1);
				animation.play();
			});

			setOnMouseExited(event -> {
				animation.setRate(-1);
				animation.play();
			});

			setOnMouseClicked(event -> activate());

			// Must be called underneath wrapper's definition.
			add();

			Color color = DEFAULT_ITEM_COLOR;
			setBorderColor(color);
			setBorderToColor(color);

			DropShadow value = DEFAULT_ITEM_SHADOW_EFFECT;
			wrapper.setEffect(value);

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
