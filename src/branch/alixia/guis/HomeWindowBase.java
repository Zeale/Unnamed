package branch.alixia.guis;

import branch.alixia.kröw.unnamed.DefaultMenuBar;
import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.Unnamed;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class HomeWindowBase extends UWindowBase {

	private static final double DEFAULT_BORDER_WIDTH = 3;
	private final static String UNNAMED_STRING = new String("Unnamed");
	private final DropShadow shadowEffect = new DropShadow(5, 80, 180, new Color(0.1, 0.1, 0.1, 1));
	private static final Color DEFAULT_ITEM_COLOR = Unnamed.BASE_COLOR;

	private final DoubleProperty shadowX = shadowEffect.offsetXProperty(), shadowY = shadowEffect.offsetYProperty();

	protected final TilePane center = new TilePane();

	protected final VBox right = new VBox(), left = new VBox();
	// protected final ScrollBox bottom = new ScrollBox();// TODO Define

	protected final ScrollPane centerRoot = new ScrollPane(center), rightRoot = new ScrollPane(right),
			leftRoot = new ScrollPane(left), bottomRoot = new ScrollPane();

	{
		// Set Menu Bar
		setMenuBar(new DefaultMenuBar());

		String stylesheet = "/branch/alixia/kröw/unnamed/stylesheet.css";
		centerRoot.getStylesheets().add(stylesheet);
		rightRoot.getStylesheets().add(stylesheet);
		leftRoot.getStylesheets().add(stylesheet);
		bottomRoot.getStylesheets().add(stylesheet);

		center.setHgap(50);
		center.setVgap(80);

		center.setEffect(shadowEffect);

		rightRoot.setBackground(null);
		leftRoot.setBackground(null);
		bottomRoot.setBackground(null);
		centerRoot.setBackground(null);
		center.setBackground(null);

		setCenter(centerRoot);
		setLeft(leftRoot);
		setRight(rightRoot);
		setBottom(bottomRoot);

		center.setPrefWidth(1000);
		centerRoot.setPrefViewportHeight(600);
		centerRoot.setHbarPolicy(ScrollBarPolicy.NEVER);
		center.setMinSize(400, 400);

		rightRoot.setMinWidth(100);
		leftRoot.setMinWidth(100);
		bottomRoot.setMinHeight(100);

		center.setPadding(new Insets(90, 80, 0, 80));

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

	private final DoubleProperty itemSize = new SimpleDoubleProperty(140);

	public abstract class Item extends StackPane {

		// Does SimpleStringProperty guarantee to return the EXACT same String you give
		// it? Or just an equal String? Probably the former, but whatever. This works
		// too.
		private final ObjectProperty<String> name = new SimpleObjectProperty<>(UNNAMED_STRING);
		private final Text defaultTextIcon = new Text(name.get().substring(0, 1).toUpperCase());
		private final StringProperty defaultTextIconFontFamily = new SimpleStringProperty("Brush Script MT");
		{
			defaultTextIcon.textProperty()
					.bind(Bindings.createStringBinding(() -> name.get().substring(0, 1).toUpperCase(), name));
			defaultTextIcon.fontProperty().bind(Bindings
					.createObjectBinding(() -> Font.font(defaultTextIconFontFamily.get(), itemSize.get()), itemSize));
		}

		public Item() {
			this(UNNAMED_STRING);
		}

		public Item(String name) {
			this.name.set(name);
		}

		public void addImage(Image image) {
			ImageView view = new ImageView(image);
			view.fitHeightProperty().bind(itemSize);
			view.fitWidthProperty().bind(itemSize);
			getChildren().add(view);
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
				nameText.setFill(Color.BLACK.interpolate(DEFAULT_ITEM_COLOR, frac));

			}

		}

		private final ItemAnimation animation = new ItemAnimation();

		private final Text nameText = new Text();
		private final VBox wrapper = new VBox(15, this, nameText);

		{

			prefWidthProperty().bind(itemSize);
			prefHeightProperty().bind(itemSize);

			wrapper.maxWidthProperty().bind(itemSize);
			nameText.wrappingWidthProperty().bind(itemSize);

			wrapper.setAlignment(Pos.CENTER);

			nameText.setFont(Font.font("Brush Script MT", 32));
			nameText.setTextAlignment(TextAlignment.CENTER);
			nameText.textProperty().bind(name);

			wrapper.setOnMouseEntered(event -> {
				animation.setRate(1);
				animation.play();
			});

			wrapper.setOnMouseExited(event -> {
				animation.setRate(-1);
				animation.play();
			});

			wrapper.setOnMouseClicked(event -> activate());

			// Must be called underneath wrapper's definition.
			add();

			setBorderColor(Color.BLACK);
			setBorderToColor(Color.BLACK);

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
			center.getChildren().add(wrapper);
		}

		public void remove() {
			center.getChildren().remove(wrapper);
		}

		public final void setPos(int pos) {
			center.getChildren().remove(wrapper);
			center.getChildren().add(pos, wrapper);
		}

		public final int getPos() {
			return center.getChildren().indexOf(wrapper);
		}

	}

	public final DoubleProperty shadowXProperty() {
		return this.shadowX;
	}

	public final double getShadowX() {
		return this.shadowXProperty().get();
	}

	public final void setShadowX(final double shadowX) {
		this.shadowXProperty().set(shadowX);
	}

	public final DoubleProperty shadowYProperty() {
		return this.shadowY;
	}

	public final double getShadowY() {
		return this.shadowYProperty().get();
	}

	public final void setShadowY(final double shadowY) {
		this.shadowYProperty().set(shadowY);
	}

}
