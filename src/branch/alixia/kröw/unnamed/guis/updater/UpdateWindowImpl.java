package branch.alixia.kröw.unnamed.guis.updater;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.Datamap;
import branch.alixia.unnamed.guis.UWindowBase;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

				HttpURLConnection connection = (HttpURLConnection) (new URL(
						"http://dusttoash.org/Unnamed/latest/version").openConnection());
				int code = connection.getResponseCode();

				// Deal with the response code and show stuff to the user.
				Platform.runLater(() -> {
					if (code != 200) {
						status.setFill(Color.RED);
						status.setText("Failed to grab update information... HTTP Response code: " + code);
					} else {
						updateCheckerProgress.setProgress(1);
						status.setFill(Color.GREEN);
						status.setText("Successfully located update information!");
					}
				});

				if (code == 200) {
					// Get the map of urls for this version.
					Datamap map = Datamap.read(connection.getInputStream());

					if (!map.containsKey("download-location")) {
						Platform.runLater(() -> {
							status.setFill(Color.RED);
							status.setText("Failed to obtain the download location of the update...");
						});
					} else if (!map.containsKey("download-info")) {
						Platform.runLater(() -> {
							status.setFill(Color.RED);
							status.setText("Failed to obtain the version of the update.");
						});
					} else {
						new Version(map);
					}
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

	private final class Version {

		private final ImageView icon = new ImageView();
		private final StackPane iconWrapper = new StackPane(icon);
		private final Text name = new Text();
		private final Button view = new Button("View");
		private final HBox wrapper = new HBox(iconWrapper, name, view);

		private final AnchorPane pane = new AnchorPane(wrapper);

		private final List<ImageView> screenshots = new LinkedList<>();

		private final URL fileLocation;

		// TODO Add a transition that shows screenshots as the background or something.

		{
			wrapper.setAlignment(Pos.CENTER);
			wrapper.setSpacing(40);
			wrapper.setFillHeight(false);

			FXTools.styleBasicInput(view);

			icon.fitHeightProperty().bind(iconWrapper.heightProperty());
			icon.fitWidthProperty().bind(iconWrapper.widthProperty());
			name.setFont(Font.font(20));
			name.setFill(Color.WHITE);
		}

		public Version(Datamap data) throws IOException {

			fileLocation = new URL(data.get("download-location"));

			URL downloadInfoLocation = new URL(data.get("download-info"));
			HttpURLConnection connection = (HttpURLConnection) downloadInfoLocation.openConnection();
			int response = connection.getResponseCode();

			if (response != 200) {
				name.setFill(Color.RED);
				name.setText("Unknown...");
			} else {

			}
		}
	}

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
