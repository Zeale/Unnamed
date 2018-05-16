package branch.alixia.kröw.unnamed.guis.updater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;

import javax.xml.ws.http.HTTPException;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.Datamap;
import branch.alixia.unnamed.Images;
import branch.alixia.unnamed.Unnamed;
import branch.alixia.unnamed.gui.UWindowBase;
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

	private static final String PROGRAM_VERSION_LIST_SERVER_LOCATION = "http://dusttoash.org/Unnamed/program_versions.php";
	private static final String PROGRAM_VERSION_LIST_DIRECTORY = "http://dusttoash.org/Unnamed/";

	private static List<URL> getVersions() throws MalformedURLException, IOException {
		HttpURLConnection connection = (HttpURLConnection) (new URL(PROGRAM_VERSION_LIST_SERVER_LOCATION)
				.openConnection());
		int code = connection.getResponseCode();

		if (code != 200)
			throw new HTTPException(code);
		else {

			List<URL> locations = new ArrayList<>();
			Scanner scanner = new Scanner(connection.getInputStream());

			while (scanner.hasNextLine())
				try {
					locations.add(new URL(PROGRAM_VERSION_LIST_DIRECTORY + "/" + scanner.nextLine() + "/version.info"));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

			scanner.close();

			return locations;
		}
	}

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

	public UpdateWindowImpl() {
		refresh();
	}

	private boolean refreshing;

	private void refresh() {
		if (refreshing)
			return;

		setMinSize(1206, 726);

		continueButton.setDisable(true);
		showUpdateChecker();
		updateCheckerStatus.setText("Checking remote update list...");

		refreshing = true;

		// Clear the current list of versions.
		List<Version> clone = new ArrayList<>(versions);
		for (Version v : clone)
			v.dispose();

		// Check remote server for new list.
		new Thread(() -> {
			try {

				// Get list of version info repos.
				List<URL> versionLinks = getVersions();

				// Notify user of progress.
				Platform.runLater(() -> updateCheckerStatus.setText("Gathering update information..."));

				// Gather information.
				for (int i = 0; i < versionLinks.size(); i++) {
					URL url = versionLinks.get(i);
					try {
						new Version(url);
						updateCheckerProgress.setProgress((i + 1d) / versionLinks.size());
					} catch (IOException e) {
						System.err.println("Failed to show a version (URL: [" + url + "]):");
						// TODO Show an error directly to the user.
						e.printStackTrace();
					}
				}

				Platform.runLater(() -> {
					updateCheckerStatus.setText("Done!");
					updateCheckerStatus.setFill(Color.GREEN);
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			checkDownload();

			refreshing = false;
			Platform.runLater(() -> continueButton.setDisable(false));
		}).start();

	}

	/*
	 * Update-ready Overlay
	 */

	/*
	 * Loading Screen
	 */
	private final ProgressBar updateCheckerProgress = new ProgressBar(0);
	private final Text updateCheckerStatus = new Text();
	private final Button continueButton = new Button("Continue");

	private final VBox updateCheckerWrapper = new VBox(40, updateCheckerProgress, updateCheckerStatus, continueButton);

	/*
	 * Actual Screen
	 */
	private final VBox versionList = new VBox();
	private final ScrollPane versionsWrapper = new ScrollPane(versionList);

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
			showVersions();
		});

		updateCheckerStatus.setFont(Font.font(28));

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

	private final List<Version> versions = new ArrayList<>();

	private final class Version {

		public void add() {
			if (versions.contains(this))
				return;

			versions.add(this);
			versionList.getChildren().add(listing);
		}

		public void remove() {
			if (!versions.contains(this))
				return;

			versionList.getChildren().remove(listing);
			if (getCenterContent() == contentPane)
				setCenterContent(getNoVersionSelectedText());
			versions.remove(this);
		}

		public void dispose() {
			remove();
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

		private final StackPane backgroundImageSizer = new StackPane();
		private final AnchorPane listing = new AnchorPane(wrapper), contentPane = new AnchorPane(backgroundScreenshot1,
				backgroundScreenshot2, backgroundImageSizer, content);

		private final URL downloadLocation;

		private final branch.alixia.unnamed.api.Version internalVersion;

		private boolean blockDownload;

		{

			description.wrappingWidthProperty().bind(UpdateWindowImpl.this.content.widthProperty().multiply(3d / 4));

			version.setTranslateY(-5);// Gives a superscript-like look
			nameWrap.setAlignment(Pos.CENTER);

			AnchorPane.setTopAnchor(backgroundImageSizer, 0d);
			AnchorPane.setRightAnchor(backgroundImageSizer, 0d);
			AnchorPane.setBottomAnchor(backgroundImageSizer, 0d);
			AnchorPane.setLeftAnchor(backgroundImageSizer, 0d);

			backgroundScreenshot1.fitHeightProperty().bind(backgroundImageSizer.heightProperty());
			backgroundScreenshot1.fitWidthProperty().bind(backgroundImageSizer.widthProperty());
			backgroundScreenshot2.fitHeightProperty().bind(backgroundImageSizer.heightProperty());
			backgroundScreenshot2.fitWidthProperty().bind(backgroundImageSizer.widthProperty());

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

			add();
		}

		public Version(Datamap data) throws IOException {

			downloadLocation = new URL(data.get("download-location"));

			// Name
			if (!data.containsKey("version-name"))
				name.setFill(Color.RED);
			name.setText(data.getOrDefault("version-name", "Unknown"));

			// Version ID
			if (data.containsKey("version")) {
				String versionName = data.get("version");
				version.setText("v" + versionName);
				version.setFill(Color.ORANGE);
				internalVersion = new branch.alixia.unnamed.api.Version(versionName);
			} else
				internalVersion = null;

			// Description
			if (!data.containsKey("version-description"))
				description.setFill(Color.RED);
			description.setText(data.getOrDefault("version-description", "Missing Description"));

			// Icon
			Images.loadImageInBackground(icon, new URL(data.get("version-icon")));

			// Screenshots
			if (data.containsKey("screenshots")) {
				String locations[] = data.get("screenshots").split(",");
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
						// ~~ ImageView issue workaround ~~
					}, new URL(s), 700, 700);
				}
			}

			// If the version could not be determined or the version is this version (or
			// before this version); don't allow downloading.
			if (internalVersion == null || internalVersion.compareTo(Unnamed.PROGRAM_VERSION) <= 0)
				download.setDisable(true);
		}

		public Version(URL location) throws IOException {
			this(Datamap.read(location.openStream()));
		}
	}

	private static final void styleText(Text text) {
		text.setFill(Color.WHITE);
		text.setFont(Font.font(24));
	}

}
