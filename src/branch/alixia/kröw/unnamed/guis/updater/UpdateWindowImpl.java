package branch.alixia.kröw.unnamed.guis.updater;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.guis.UWindowBase;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
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

		// Start a new thread to check the website so that the window doesn't lag.
		new Thread(() -> {
			try {

				// Create the connection
				final URL url = new URL("http://dusttoash.org/Unnamed/latest/version");
				HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
				int code = connection.getResponseCode();

				// Deal with the response code and show stuff to the user.
				Platform.runLater(() -> {
					if (code != 200) {
						status.setFill(Color.RED);
						status.setText("Failed to grab update information... HTTP Response code: " + code);
					} else {
						updateCheckerProgress.setProgress(1);
						status.setFill(Color.GREEN);
						status.setText("Successfully grabbed update information!");
					}
				});

				if (code == 200) {
					// Get the map of urls for this version.
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
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
