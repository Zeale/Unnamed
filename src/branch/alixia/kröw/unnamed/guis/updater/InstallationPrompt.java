package branch.alixia.kröw.unnamed.guis.updater;

import branch.alixia.kröw.unnamed.tools.FXTools;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

final class InstallationPrompt extends VBox {

	/*
	 * Installation Prompt Screen
	 */

	final Text installationWarning = new Text(
			"Are you sure you want to attempt to install the latest update? The program will rewrite itself in this process."),
			installationNotice = new Text(
					"If anything goes wrong, you'll probably need to download the program off the internet again. Your data will remain intact, however.");
	final VBox installationWarningsWrapper = new VBox(5, installationWarning, installationNotice);
	final Button installationPromptYes = new Button("Continue" + (Math.random() > 0.85 ? " (heil yes)" : "")),
			installationPromptNo = new Button("No");
	final HBox installationPromptButtonHolder = new HBox(50, installationPromptYes, installationPromptNo);

	{

		getChildren().addAll(installationWarningsWrapper, installationPromptButtonHolder);

		installationWarning.setFont(Font.font(20));
		installationNotice.setFont(Font.font(16));
		installationNotice.setFill(Color.ORANGE);
		installationWarning.setWrappingWidth(600);
		installationNotice.setWrappingWidth(500);

		installationWarningsWrapper.setAlignment(Pos.CENTER);
		installationWarningsWrapper.setFillWidth(false);
		installationPromptButtonHolder.setAlignment(Pos.CENTER);
		installationPromptButtonHolder.setFillHeight(false);
		setAlignment(Pos.CENTER);
		setFillWidth(false);

		AnchorPane.setTopAnchor(this, 0d);
		AnchorPane.setBottomAnchor(this, 0d);
		AnchorPane.setLeftAnchor(this, 0d);
		AnchorPane.setRightAnchor(this, 0d);

		FXTools.styleBasicInput(installationPromptYes, installationPromptNo);
	}

	private final UpdateWindowImpl instance;

	InstallationPrompt(UpdateWindowImpl instance) {
		super(25);
		this.instance = instance;
	}

}
