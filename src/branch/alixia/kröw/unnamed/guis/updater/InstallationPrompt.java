package branch.alixia.kröw.unnamed.guis.updater;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.guis.UWindowBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

final class InstallationPrompt extends UWindowBase {

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
	final VBox wrapper = new VBox(25, installationWarningsWrapper, installationPromptButtonHolder);

	{

		setCenter(wrapper);

		installationWarning.setFont(Font.font(20));
		installationNotice.setFont(Font.font(16));
		installationNotice.setFill(Color.ORANGE);
		installationWarning.setWrappingWidth(600);
		installationNotice.setWrappingWidth(500);
		installationWarning.setTextAlignment(TextAlignment.CENTER);
		installationNotice.setTextAlignment(TextAlignment.CENTER);

		installationWarningsWrapper.setAlignment(Pos.CENTER);
		installationWarningsWrapper.setFillWidth(false);
		installationPromptButtonHolder.setAlignment(Pos.CENTER);
		installationPromptButtonHolder.setFillHeight(false);
		wrapper.setAlignment(Pos.CENTER);
		wrapper.setFillWidth(false);

		FXTools.styleBasicInput(installationPromptYes, installationPromptNo);

		installationPromptYes.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

			}
		});
		installationPromptNo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getScene().setRoot(instance);
			}
		});
	}

	private final UpdateWindowImpl instance;

	InstallationPrompt(UpdateWindowImpl instance) {
		this.instance = instance;
	}

}
