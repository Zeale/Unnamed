package branch.alixia.kröw.unnamed.guis.updater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.Datamap;
import branch.alixia.unnamed.Images;
import branch.alixia.unnamed.Unnamed;
import branch.alixia.unnamed.guis.UWindowBase;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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
import javafx.util.Duration;

public class UpdateWindowImpl extends UWindowBase {

	private final UpdateOverlay overlay = new UpdateOverlay();
	{
		overlay.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY)
				promptInstall();
		});
	}

	final static File UPDATE_DOWNLOADS_ROOT = new File(Unnamed.PROGRAM_ROOT, "Updates");
	final static File LATEST_UPDATE = new File(UPDATE_DOWNLOADS_ROOT, "Update.jar");
	static final File CURRENT_PROGRAM_LOCATION;
	static {
		File location;
		try {
			location = new File(UpdateWindowImpl.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			location = null;
		}
		CURRENT_PROGRAM_LOCATION = location;
	}

	private void checkDownload() {
		if (LATEST_UPDATE.isFile()) {
			overlay.show();
			overlay.play();
		}
	}

	static {
		UPDATE_DOWNLOADS_ROOT.mkdirs();
	}

	public UpdateWindowImpl(boolean checkForUpdates) {
		if (checkForUpdates)
			checkForUpdates();
	}

	private boolean checkingForUpdates;

	private Version latest;

	public void checkForUpdates() {
		// INIT
		checkingForUpdates = true;
		overlay.pause();
		if (latest != null)
			latest.dispose();

		// Update Checking
		setMinSize(1206, 726);
		showUpdateChecker();
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
					System.out.println(map);
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
						latest = new Version(map);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			checkingForUpdates = false;
			checkDownload();
		}).start();

	}

	/*
	 * Update-ready Overlay
	 */

	/*
	 * Loading Screen
	 */
	private final ProgressBar updateCheckerProgress = new ProgressBar(0);
	private final Text status = new Text();
	private final Button continueButton = new Button("Continue");

	private final VBox updateCheckerWrapper = new VBox(40, updateCheckerProgress, status, continueButton);

	/*
	 * Actual Screen
	 */
	private final VBox versions = new VBox();
	private final ScrollPane versionsWrapper = new ScrollPane(versions);

	private final StackPane content = new StackPane();
	private final AnchorPane center = new AnchorPane(content, overlay);
	private InstallationPrompt installationPrompt = new InstallationPrompt(this);

	private void setCenterContent(Node node) {
		content.getChildren().clear();
		if (node != null)
			content.getChildren().add(node);
	}

	{

		/*
		 * Child Initialization
		 */

		setCenter(center);
		content.setAlignment(Pos.CENTER);
		AnchorPane.setRightAnchor(content, 0d);
		AnchorPane.setTopAnchor(content, 0d);
		AnchorPane.setLeftAnchor(content, 0d);
		AnchorPane.setBottomAnchor(content, 0d);

		versionsWrapper.setBorder(FXTools.getBorderFromColor(Color.BLACK));
		versionsWrapper.setMinWidth(200);
		FXTools.setDefaultBackground(versionsWrapper);

		updateCheckerWrapper.setAlignment(Pos.CENTER);
		updateCheckerWrapper.setFillWidth(false);
		updateCheckerProgress.setPrefWidth(400);
		FXTools.styleBasicInput(updateCheckerProgress, continueButton);

		continueButton.setOnAction(event -> {
			if (checkingForUpdates)
				return;
			showVersions();
		});

		status.setFont(Font.font(28));

	}

	private void emptyWindow() {
		setLeft(null);
		setRight(null);
		setBorder(null);
		setCenterContent(null);
		checkDownload();
	}

	private void showVersions() {
		emptyWindow();
		setLeft(versionsWrapper);
		setCenterContent(getNoVersionSelectedText());
	}

	private final static Text getNoVersionSelectedText() {
		Text noVersionSelectedText = new Text("No version selected");
		styleText(noVersionSelectedText);
		return noVersionSelectedText;
	}

	private void showUpdateChecker() {
		emptyWindow();
		setCenterContent(updateCheckerWrapper);
	}

	private void promptInstall() {
		getScene().setRoot(installationPrompt);
	}

	private Node getCenterContent() {
		return content.getChildren().get(0);
	}

	private final class Version {

		public void dispose() {
			versions.getChildren().remove(listing);
			if (getCenterContent() == contentPane)
				setCenterContent(getNoVersionSelectedText());
		}

		private int backgroundAnimationIterator = 1;

		private final ImageView listingIcon = new ImageView();
		private final StackPane listingIconWrapper = new StackPane(listingIcon);
		private final Text contentName = new Text();
		private final Button view = new Button("View");
		private final HBox wrapper = new HBox(listingIconWrapper, contentName, view);

		private final ImageView icon = new ImageView();
		private final Text name = new Text(), version = new Text(), description = new Text();
		private final HBox nameWrap = new HBox(name, version);
		private final Button download = new Button("Download");
		private final ProgressBar downloadProgress = new ProgressBar(0);
		private final VBox content = new VBox(20, icon, nameWrap, description, download, downloadProgress);

		private final ImageView backgroundScreenshot1 = new ImageView(), backgroundScreenshot2 = new ImageView();

		private final List<Image> screenshots = new LinkedList<>();

		// This assumes that
		private final Timeline backgroundAnimator = new Timeline();
		{
			double maxOpacity = 0.4;
			backgroundAnimator.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO, new KeyValue(backgroundScreenshot1.opacityProperty(), maxOpacity),
							new KeyValue(backgroundScreenshot2.opacityProperty(), 0)),
					new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							// Make sure that there are still enough images for the animation to proceed.
							if (screenshots.size() < 2)
								backgroundAnimator.pause();
							// BG1 just faded out. Change its image.

							// Set the iterator to point to the next screenshot.
							if (++backgroundAnimationIterator == screenshots.size())
								backgroundAnimationIterator = 0;
							// Set the faded out, BG1's image to be the determined, next image.
							backgroundScreenshot1.setImage(screenshots.get(backgroundAnimationIterator));
						}
					}, new KeyValue(backgroundScreenshot1.opacityProperty(), 0),
							new KeyValue(backgroundScreenshot2.opacityProperty(), maxOpacity)),
					new KeyFrame(Duration.seconds(6), new KeyValue(backgroundScreenshot1.opacityProperty(), 0),
							new KeyValue(backgroundScreenshot2.opacityProperty(), maxOpacity)),
					new KeyFrame(Duration.seconds(9), event -> {

						if (screenshots.size() < 2)
							backgroundAnimator.pause();

						if (++backgroundAnimationIterator == screenshots.size())
							backgroundAnimationIterator = 0;
						backgroundScreenshot2.setImage(screenshots.get(backgroundAnimationIterator));

					}, new KeyValue(backgroundScreenshot1.opacityProperty(), maxOpacity),
							new KeyValue(backgroundScreenshot2.opacityProperty(), 0)),
					new KeyFrame(Duration.seconds(12),
							new KeyValue(backgroundScreenshot1.opacityProperty(), maxOpacity),
							new KeyValue(backgroundScreenshot2.opacityProperty(), 0)));
			backgroundAnimator.setCycleCount(Animation.INDEFINITE);
		}

		private final AnchorPane listing = new AnchorPane(wrapper),
				contentPane = new AnchorPane(backgroundScreenshot1, backgroundScreenshot2, content);

		private final URL downloadLocation;

		private final branch.alixia.unnamed.api.Version internalVersion;

		private boolean blockDownload;

		{

			description.wrappingWidthProperty().bind(UpdateWindowImpl.this.content.widthProperty().multiply(3d / 4));

			version.setTranslateY(-5);// Gives a superscript-like look
			nameWrap.setAlignment(Pos.CENTER);

			backgroundScreenshot1.setFitHeight(710);
			backgroundScreenshot1.setFitWidth(810);
			backgroundScreenshot2.fitHeightProperty().bind(backgroundScreenshot1.fitHeightProperty());
			backgroundScreenshot2.fitWidthProperty().bind(backgroundScreenshot1.fitWidthProperty());

			AnchorPane.setTopAnchor(content, 0d);
			AnchorPane.setRightAnchor(content, 0d);
			AnchorPane.setBottomAnchor(content, 0d);
			AnchorPane.setLeftAnchor(content, 0d);

			wrapper.setAlignment(Pos.CENTER);
			wrapper.setSpacing(40);
			wrapper.setFillHeight(false);

			FXTools.styleBasicInput(view, download);

			icon.imageProperty().bindBidirectional(listingIcon.imageProperty());

			icon.setFitHeight(128);
			icon.setFitWidth(128);

			listingIcon.setFitHeight(64);
			listingIcon.setFitWidth(64);

			contentName.setFont(Font.font(20));
			contentName.setFill(Color.WHITE);
			contentName.textProperty().bindBidirectional(name.textProperty());

			description.setFont(Font.font(16));
			description.setFill(Color.BLACK);

			styleText(name);

			content.setAlignment(Pos.CENTER);

			download.getStyleClass().remove("basic-input");
			download.setFont(Font.font("Consolas", FontWeight.BOLD, -1));
			download.setTextFill(Color.WHITE);

			downloadProgress.setVisible(false);

			view.setOnAction(event -> setCenterContent(contentPane));

			download.setOnAction(new EventHandler<ActionEvent>() {

				private void handleException(Exception e) {

					// Download unsuccessful

					// Run on FX thread.
					if (!Platform.isFxApplicationThread()) {
						Platform.runLater(() -> handleException(e));
						return;
					}

					downloadProgress.setVisible(false);

					download.setText("Download failed");
					download.setTextFill(Color.RED);
					// This may have an issue if the user clicks download multiple times.
					new Thread(() -> {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						Platform.runLater(() -> {
							download.setText("Download");
							download.setTextFill(Color.WHITE);
							blockDownload = false;
							checkDownload();
						});
					}).start();
					e.printStackTrace();

				}

				@Override
				public void handle(ActionEvent event) {
					if (blockDownload)
						return;
					overlay.pause();
					blockDownload = true;

					download.setText("Downloading...");
					download.setTextFill(Color.GREEN);
					downloadProgress.setVisible(true);

					try {
						downloadProgress.setProgress(0.04);
						HttpURLConnection connection = (HttpURLConnection) downloadLocation.openConnection();
						int responseCode = connection.getResponseCode();
						if (responseCode == 404)
							throw new FileNotFoundException(
									"The download file could not be found on the remote server.");
						else if (responseCode != 200)
							throw new RuntimeException("HTTP Response Code was not 200. It was " + responseCode + ".");
						downloadProgress.setProgress(0.1);

						InputStream input = connection.getInputStream();
						FileOutputStream output = new FileOutputStream(LATEST_UPDATE);

						new Thread(() -> {
							try {
								int bufferLength = 1024;
								double currentProgress = downloadProgress.getProgress();
								long totalReadData = 0;

								byte[] buffer = new byte[bufferLength];
								int amount;
								while ((amount = input.read(buffer)) != -1) {
									totalReadData += amount;
									output.write(buffer, 0, amount);
									downloadProgress.setProgress(currentProgress
											+ connection.getContentLength() / totalReadData * (1 - currentProgress));
								}
								output.close();

								downloadProgress.setVisible(false);

								// Download successful

								Platform.runLater(() -> {
									download.setText("Success!");
									download.setTextFill(Color.BLUE);
									new Thread(() -> {
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e1) {
											e1.printStackTrace();
										}
										Platform.runLater(() -> {
											download.setText("Download");
											download.setTextFill(Color.WHITE);
											blockDownload = false;
											checkDownload();
										});
									}).start();
								});

								checkDownload();

							} catch (Exception e2) {
								handleException(e2);
							}
						}).start();

					} catch (Exception e) {
						handleException(e);
					}
				}
			});

			wrapper.setPadding(new Insets(5, 20, 10, 5));
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
				internalVersion = null;
			} else {
				Datamap downloadInfo = Datamap.read(downloadInfoLocation.openStream());

				if (!downloadInfo.containsKey("version-name"))
					name.setFill(Color.RED);
				name.setText(downloadInfo.getOrDefault("version-name", "Unknown"));
				Datamap map = null;
				if (data.containsKey("version"))
					map = data;
				else if (downloadInfo.containsKey("version"))
					map = downloadInfo;
				if (map != null) {
					String versionName = map.get("version");
					version.setText("v" + versionName);
					version.setFill(Color.ORANGE);
				internalVersion = new branch.alixia.unnamed.api.Version(versionName);
				} else
					internalVersion = null;

				if (!downloadInfo.containsKey("version-description"))
					description.setFill(Color.RED);
				description.setText(downloadInfo.getOrDefault("version-description", "Missing Description"));
				Images.loadImageInBackground(icon, new URL(downloadInfo.get("version-icon")));

				if (downloadInfo.containsKey("screenshots")) {
					String locations[] = downloadInfo.get("screenshots").split(",");
					for (String s : locations) {
						s = s.trim();
						Images.loadImageInBackground((BiConsumer<Image, Boolean>) (t, u) -> {
							if (u) {
								screenshots.add(t);
								if (screenshots.size() == 1)
									backgroundScreenshot1.setImage(t);
								else if (screenshots.size() == 2) {
									backgroundScreenshot2.setImage(t);
									backgroundAnimator.play();
								}
							}
						}, new URL(s));
					}
				}
			}

			// If the version could not be determined or the version is this version (or
			// before this version), don't allow downloading.
			if (internalVersion == null || internalVersion.compareTo(Unnamed.PROGRAM_VERSION) <= 0)
				download.setDisable(true);
		}
	}

	private static final void styleText(Text text) {
		text.setFill(Color.WHITE);
		text.setFont(Font.font(24));
	}

}
