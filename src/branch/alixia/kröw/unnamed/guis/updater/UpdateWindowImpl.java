package branch.alixia.kröw.unnamed.guis.updater;

import branch.alixia.guis.UWindowBase;
import branch.alixia.kröw.unnamed.tools.FXTools;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class UpdateWindowImpl extends UWindowBase {

	public UpdateWindowImpl(boolean checkForUpdates) {
		if (checkForUpdates)
			checkForUpdates();
	}

	public void checkForUpdates() {
		setCenter(loadingScreen);
	}

	/*
	 * Loading Screen
	 */
	private final ProgressBar updateCheckerProgress = new ProgressBar(0);
	private final AnchorPane loadingScreen = new AnchorPane(updateCheckerProgress);

	/*
	 * Actual Screen
	 */
	private final AnchorPane updateWindow = new AnchorPane();

	{
		/*
		 * Initialization
		 */

		setCenter(updateWindow);

		/*
		 * Child Initialization
		 */

		AnchorPane.setBottomAnchor(updateCheckerProgress, 400d);
		AnchorPane.setLeftAnchor(updateCheckerProgress, 80d);
		AnchorPane.setRightAnchor(updateCheckerProgress, 80d);

		FXTools.styleBasicInput(updateCheckerProgress);
	}

}
