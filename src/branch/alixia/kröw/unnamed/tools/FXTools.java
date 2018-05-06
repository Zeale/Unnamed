package branch.alixia.kröw.unnamed.tools;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public final class FXTools {

	public static final double COMMON_BORDER_WIDTH = 2;

	public static Background getBackgroundFromColor(Paint color) {
		return new Background(new BackgroundFill(color, null, null));
	}

	public static Border getBorderFromColor(Paint color) {
		return getBorderFromColor(color, 2);
	}

	public static Border getBorderFromColor(Paint color, double width) {
		return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, null, new BorderWidths(width)));
	}

	public static void applyColorwheelTransition(Shape shape, Duration duration, Color... colors) {
		shape.setFill(colors[0]);
		FillTransition[] transitions = new FillTransition[colors.length];
		for (int i = 0; i < colors.length - 1;)
			transitions[i] = new FillTransition(duration, shape, colors[i], colors[++i]);
	
		transitions[transitions.length - 1] = new FillTransition(duration, shape, colors[colors.length - 1], colors[0]);
	
		SequentialTransition repeater = new SequentialTransition(transitions);
	
		repeater.setCycleCount(Transition.INDEFINITE);
	
		shape.setOnMouseEntered(event -> repeater.play());
		shape.setOnMouseExited(event -> repeater.pause());
	}
}
