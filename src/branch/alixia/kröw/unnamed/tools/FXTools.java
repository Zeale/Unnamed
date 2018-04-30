package branch.alixia.kröw.unnamed.tools;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Paint;

public final class FXTools {
	public static Background getBackgroundFromColor(Paint color) {
		return new Background(new BackgroundFill(color, null, null));
	}

	public static Border getBorderFromColor(Paint color) {
		return getBorderFromColor(color, 2.5);
	}

	public static Border getBorderFromColor(Paint color, double width) {
		return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, null, new BorderWidths(width)));
	}
}
