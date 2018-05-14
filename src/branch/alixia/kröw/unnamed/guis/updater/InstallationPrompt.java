package branch.alixia.kröw.unnamed.guis.updater;

import branch.alixia.kröw.unnamed.tools.FXTools;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

final class InstallationPrompt extends VBox {

	/*
	 * Installation Prompt Screen
	 */

	private final Text installationWarning = new Text(
			"Are you sure you want to attempt an install? The program will rewrite itself in this process."),
			installationNotice = new Text(
					"If anything goes wrong, you'll need to download the program off the internet again. Your data won't be lost.");
	private final VBox installationWarningsWrapper = new VBox(5, installationWarning, installationNotice);
	private final Button installationPromptYes = new Button("Continue" + (Math.random() > 0.85 ? " (heil yes)" : "")),
			installationPromptNo = new Button("No");
	private final HBox installationPromptButtonHolder = new HBox(50, installationPromptYes, installationPromptNo);

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

		FXTools.styleBasicInput(installationPromptYes, installationPromptNo);
	}

	private final UpdateWindowImpl instance;

	public InstallationPrompt(UpdateWindowImpl instance) {
		super(25);
		this.instance = instance;
	}

}
