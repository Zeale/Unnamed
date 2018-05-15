package branch.alixia.kröw.unnamed.guis.updater;

import branch.alixia.kröw.unnamed.tools.FXTools;
import javafx.animation.Transition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

final class UpdateOverlay extends AnchorPane {
	private final Text updateReadyText = new Text("Update Ready!");
	private final ImageView updateReadyIcon = new ImageView();
	private final HBox updateReadyWrapper = new HBox(updateReadyIcon, updateReadyText);
	private final Transition updateReadyTextTransition = FXTools.buildColorwheelTransition(updateReadyText,
			Duration.seconds(0.4));

	public void show() {
		setVisible(true);
	}

	public void hide() {
		setVisible(false);
	}

	public void playFrom(Duration time) {
		updateReadyTextTransition.playFrom(time);
	}

	public void play() {
		updateReadyTextTransition.play();
	}

	public void playFromStart() {
		updateReadyTextTransition.playFromStart();
	}

	public void pause() {
		updateReadyTextTransition.pause();
	}

	{
		getChildren().add(updateReadyWrapper);

		AnchorPane.setRightAnchor(this, 0d);
		updateReadyText.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 18));
		AnchorPane.setTopAnchor(updateReadyWrapper, 7d);
		AnchorPane.setBottomAnchor(updateReadyWrapper, 7d);
		AnchorPane.setLeftAnchor(updateReadyWrapper, 10d);
		AnchorPane.setRightAnchor(updateReadyWrapper, 10d);
		setBorder(FXTools.getBorderFromColor(Color.BLACK.brighter(), -0.1, 5));
		setBackground(FXTools.getBackgroundFromColor(Color.DARKGRAY.darker(), 5));
		setVisible(false);
	}
}
