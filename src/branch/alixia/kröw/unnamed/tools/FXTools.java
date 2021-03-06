package branch.alixia.kr�w.unnamed.tools;

import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.GOLD;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

import branch.alixia.unnamed.Unnamed;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public final class FXTools {

	public static final double COMMON_BORDER_WIDTH = 2;

	public static Background getBackgroundFromColor(Paint color) {
		return new Background(new BackgroundFill(color, null, null));
	}

	public static Background getBackgroundFromColor(Paint color, double radius) {
		return new Background(new BackgroundFill(color, radius < 0 ? null : new CornerRadii(radius), null));
	}

	public static Border getBorderFromColor(Paint color) {
		return getBorderFromColor(color, 2);
	}

	public static Border getBorderFromColor(Paint color, double width) {
		return new Border(
				new BorderStroke(color, BorderStrokeStyle.SOLID, null, width < 0 ? null : new BorderWidths(width)));
	}

	public static Border getBorderFromColor(Paint color, double width, double radii) {
		return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, radii < 0 ? null : new CornerRadii(radii),
				width < 0 ? null : new BorderWidths(width)));
	}

	public static Transition applyHoverColorAnimation(Shape shape, Duration duration, Color... colors) {

		Transition repeater = buildColorwheelTransition(shape, duration, colors);

		shape.setOnMouseEntered(event -> repeater.play());
		shape.setOnMouseExited(event -> repeater.pause());

		return repeater;
	}

	public static Transition buildColorwheelTransition(Shape shape, Duration duration, Color... colors) {
		shape.setFill(colors[0]);
		FillTransition[] transitions = new FillTransition[colors.length];
		for (int i = 0; i < colors.length - 1;)
			transitions[i] = new FillTransition(duration, shape, colors[i], colors[++i]);

		transitions[transitions.length - 1] = new FillTransition(duration, shape, colors[colors.length - 1], colors[0]);

		SequentialTransition repeater = new SequentialTransition(transitions);

		repeater.setCycleCount(Transition.INDEFINITE);
		return repeater;
	}

	public static Transition buildColorwheelTransition(Shape shape, Color... colors) {
		return buildColorwheelTransition(shape, DEFAULT_COLORWHEEL_TRANSITION_DURATION, colors);
	}

	public static Transition buildColorwheelTransition(Shape shape, Duration duration) {
		return buildColorwheelTransition(shape, duration, DEFAULT_COLORWHEEL_TRANSITION_COLORS);
	}

	public static Transition buildColorwheelTransition(Shape shape) {
		return buildColorwheelTransition(shape, DEFAULT_COLORWHEEL_TRANSITION_COLORS);
	}

	private static final Color[] DEFAULT_COLORWHEEL_TRANSITION_COLORS = new Color[] { RED, GOLD, GREEN, BLUE };
	private static final Duration DEFAULT_COLORWHEEL_TRANSITION_DURATION = Duration.seconds(0.5);

	public static Transition applyHoverColorAnimation(Shape shape, Color... colors) {
		return applyHoverColorAnimation(shape, DEFAULT_COLORWHEEL_TRANSITION_DURATION, colors);
	}

	public static Transition applyHoverColorAnimation(Shape shape) {
		return applyHoverColorAnimation(shape, DEFAULT_COLORWHEEL_TRANSITION_DURATION,
				DEFAULT_COLORWHEEL_TRANSITION_COLORS);
	}

	public static void styleBasicInput(Region... inputs) {
		for (Region r : inputs) {
			r.setBackground(getBackgroundFromColor(Unnamed.DEFAULT_WINDOW_COLOR.interpolate(Color.BLACK, 0.25)));
			r.setBorder(getBorderFromColor(Unnamed.ITEM_BORDER_COLOR));
			r.getStylesheets().add("branch/alixia/kr�w/unnamed/tools/basic-input.css");
			r.getStyleClass().add("basic-input");

			final Color toColor = r instanceof Button ? RED : GREEN;
			(r instanceof Button ? ((Button) r).armedProperty() : r.focusedProperty())
					.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> r
							.setBorder(getBorderFromColor(newValue ? toColor : Unnamed.ITEM_BORDER_COLOR)));

		}
	}

	public static void setDefaultBackground(Region item) {
		item.setBackground(getBackgroundFromColor(Unnamed.DEFAULT_WINDOW_COLOR));
		if (item instanceof ScrollPane) {
			item.getStylesheets().add("branch/alixia/kr�w/unnamed/tools/default-background.css");
			item.getStyleClass().add("default-background");
		}
	}

}
