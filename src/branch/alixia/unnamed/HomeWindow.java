package branch.alixia.unnamed;

import branch.alixia.kröw.unnamed.guis.updater.UpdateWindowImpl;
import branch.alixia.unnamed.guis.HomeWindowBase;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

final class HomeWindow extends HomeWindowBase {
	{
		new Item("Constructs") {

			{
				addImage(Images.GRAPHICS_LOCATION + "Construct Icon-1024px.png");
			}

			@Override
			protected void activate() {
				getScene().setRoot(MSGUIs.openConstructWindow());
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
				MSGUIs.setScene(new Scene(new UpdateWindowImpl(true), Color.TRANSPARENT));
			}

		};

	}
}
