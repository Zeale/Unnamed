package branch.alixia.unnamed;

import branch.alixia.kröw.unnamed.guis.constructs.ConstructWindowImpl;
import branch.alixia.kröw.unnamed.guis.updater.UpdateWindowImpl;
import branch.alixia.unnamed.gui.HomeWindowBase;
import branch.alixia.unnamed.gui.UWindowBase;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

final class HomeWindow extends HomeWindowBase {

	public HomeWindow(Stage stage) {
		super(stage);
	}

	{
		new Item("Constructs") {

			{
				addImage(Images.GRAPHICS_LOCATION + "Construct Icon-1024px.png");
			}

			@Override
			protected void activate() {
				UWindowBase constructs = new ConstructWindowImpl(getBoundStage());
				getScene().setRoot(constructs);
				constructs.setBoundStage(getBoundStage());
				setBoundStage(null);// Lose reference
			}

		};

		new Item("Laws") {

			{
				addImage(Images.GRAPHICS_LOCATION + "Law Icon-1024px.png");
			}

			@Override
			protected void activate() {

			}
		};

		new Item("Complexes") {

			{
				addImage(Images.GRAPHICS_LOCATION + "Complex Icon-1024px.png");
			}

			@Override
			protected void activate() {

			}
		};

		new Item("Updates") {

			{
				addImage(Images.GRAPHICS_LOCATION + "Updates Icon-1024px.png");
				setTextToColor(Color.RED);
			}

			@Override
			protected void activate() {
				MSGUIs.setScene(new Scene(new UpdateWindowImpl(getBoundStage()), Color.TRANSPARENT));
			}

		};

	}

}
