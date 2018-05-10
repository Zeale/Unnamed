package branch.alixia.kröw.unnamed.guis.updater;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import branch.alixia.guis.UWindowBase;
import branch.alixia.kröw.unnamed.tools.FXTools;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UpdateWindowImpl extends UWindowBase {

	public UpdateWindowImpl(boolean checkForUpdates) {
		if (checkForUpdates)
			checkForUpdates();
	}

	public void checkForUpdates() {
		setCenter(wrapper);
		status.setText("Attempting to connect to update repository...");
		try {

			final URL url = new URL("http://dusttoash.org/Unnamed/latest/version");
			int code = ((HttpURLConnection) (url.openConnection())).getResponseCode();
			if (code != 200) {
				status.setFill(Color.RED);
				status.setText("Failed to grab update information... HTTP Response code: " + code);
			} else {
				updateCheckerProgress.setProgress(1);
				status.setFill(Color.GREEN);
				status.setText("Successfully grabbed update information!");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Loading Screen
	 */
	private final ProgressBar updateCheckerProgress = new ProgressBar(0);
	private final Text status = new Text();
	private final Button continueButton = new Button("Continue");

	private final VBox wrapper = new VBox(40, updateCheckerProgress, status, continueButton);

	/*
	 * Actual Screen
	 */
	private final VBox versions = new VBox();
	private final VBox selectedVersion = new VBox(40);

	{
		/*
		 * Child Initialization
		 */

		wrapper.setAlignment(Pos.CENTER);
		wrapper.setFillWidth(false);
		updateCheckerProgress.setPrefWidth(400);
		FXTools.styleBasicInput(updateCheckerProgress, continueButton);
		continueButton.setOnAction(event -> {
			setCenter(null);
			setLeft(versions);
			setRight(selectedVersion);
		});

		status.setFont(Font.font(28));

		selectedVersion.setFillWidth(false);
		selectedVersion.setAlignment(Pos.CENTER);
		selectedVersion.setPrefWidth(800);

	}

}
