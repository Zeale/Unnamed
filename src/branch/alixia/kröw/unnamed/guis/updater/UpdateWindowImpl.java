package branch.alixia.kröw.unnamed.guis.updater;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.Datamap;
import branch.alixia.unnamed.guis.UWindowBase;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
	private final ScrollPane versionsWrapper = new ScrollPane(versions);

	private final class Version {

		private final ImageView listingIcon = new ImageView();
		private final StackPane listingIconWrapper = new StackPane(listingIcon);
		private final Text contentName = new Text();
		private final Button view = new Button("View");
		private final HBox wrapper = new HBox(listingIconWrapper, contentName, view);

		private final ImageView icon = new ImageView();
		private final Text name = new Text(), description = new Text();
		private final Button download = new Button("Download");
		private final VBox content = new VBox(20, icon, name, description, download);

		private final ImageView backgroundScreenshot1 = new ImageView(), backgroundScreenshot2 = new ImageView();
		private final List<ImageView> screenshots = new LinkedList<>();

		private final AnchorPane listing = new AnchorPane(wrapper), contentPane = new AnchorPane(content);

		private final URL downloadLocation;

		private boolean downloading;

		// TODO Add a transition that shows screenshots as the background or something.

		{
			wrapper.setAlignment(Pos.CENTER);
			wrapper.setSpacing(40);
			wrapper.setFillHeight(false);

			FXTools.styleBasicInput(view, download);

			icon.imageProperty().bindBidirectional(listingIcon.imageProperty());
			listingIcon.fitHeightProperty().bind(listingIconWrapper.heightProperty());
			listingIcon.fitWidthProperty().bind(listingIconWrapper.widthProperty());
			contentName.setFont(Font.font(20));
			contentName.setFill(Color.WHITE);
			contentName.textProperty().bindBidirectional(name.textProperty());

			description.setFont(Font.font(16));
			description.setFill(Color.WHITE);

			styleText(name);

			AnchorPane.setTopAnchor(backgroundScreenshot1, 0d);
			AnchorPane.setRightAnchor(backgroundScreenshot1, 0d);
			AnchorPane.setBottomAnchor(backgroundScreenshot1, 0d);
			AnchorPane.setLeftAnchor(backgroundScreenshot1, 0d);

			AnchorPane.setTopAnchor(backgroundScreenshot2, 0d);
			AnchorPane.setRightAnchor(backgroundScreenshot2, 0d);
			AnchorPane.setBottomAnchor(backgroundScreenshot2, 0d);
			AnchorPane.setLeftAnchor(backgroundScreenshot2, 0d);

			AnchorPane.setTopAnchor(content, 0d);
			AnchorPane.setRightAnchor(content, 0d);
			AnchorPane.setBottomAnchor(content, 0d);
			AnchorPane.setLeftAnchor(content, 0d);
			content.setAlignment(Pos.CENTER);

			download.getStyleClass().remove("basic-input");
			download.setFont(Font.font("Consolas", FontWeight.BOLD, -1));
			download.setTextFill(Color.WHITE);

			view.setOnAction(event -> setCenter(contentPane));
			download.setOnAction(event -> {
				if (downloading)
					return;
				download.setText("Downloading...");
				download.setTextFill(Color.GREEN);

				try {
					downloading = true;
					// TODO Attempt to download
				} catch (Exception e) {
					download.setText("Download failed");
					download.setTextFill(Color.RED);
					new Thread(() -> {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						Platform.runLater(() -> {
							download.setText("Download");
							download.setTextFill(Color.WHITE);
						});
					}).start();

				}
			});

			wrapper.setPadding(new Insets(5, 5, 10, 5));
			listing.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null,
					new BorderWidths(0, 0, FXTools.COMMON_BORDER_WIDTH, 0))));

			versions.getChildren().add(listing);
		}

		public Version(Datamap data) throws IOException {

			downloadLocation = new URL(data.get("download-location"));

			// Location of an info file for the update
			URL downloadInfoLocation = new URL(data.get("download-info"));
			HttpURLConnection connection = (HttpURLConnection) downloadInfoLocation.openConnection();
			int response = connection.getResponseCode();

			if (response != 200) {
				name.setFill(Color.RED);
				name.setText("Unknown...");
			} else {
				Datamap downloadInfo = Datamap.read(downloadInfoLocation.openStream());
				if (!downloadInfo.containsKey("version-name"))
					name.setFill(Color.RED);
				name.setText(downloadInfo.getOrDefault("version-name", "Unknown"));
				if (!downloadInfo.containsKey("version-description"))
					description.setFill(Color.RED);
				description.setText(downloadInfo.getOrDefault("version-description", "Missing Description"));

			}
		}
	}

	{
		/*
		 * Child Initialization
		 */

		versions.setBorder(FXTools.getBorderFromColor(Color.BLACK));

		wrapper.setAlignment(Pos.CENTER);
		wrapper.setFillWidth(false);
		updateCheckerProgress.setPrefWidth(400);
		FXTools.styleBasicInput(updateCheckerProgress, continueButton);
		continueButton.setOnAction(event -> {
			setCenter(null);
			setLeft(versions);
			Text noVersionSelectedText = new Text("No version selected");
			styleText(noVersionSelectedText);
			setCenter(noVersionSelectedText);
		});

		status.setFont(Font.font(28));

	}

	private static final void styleText(Text text) {
		text.setFill(Color.WHITE);
		text.setFont(Font.font(24));
	}

}
