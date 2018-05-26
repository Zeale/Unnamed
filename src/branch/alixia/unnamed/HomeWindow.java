package branch.alixia.unnamed;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

		class Item extends HomeWindowBase.Item {

			private final Runnable onActivated;

			private Item(String name, String iconLocation, Runnable onActivated) {
				super(name);
				addImage(iconLocation);
				this.onActivated = onActivated;
			}

			@Override
			protected void activate() {
				if (onActivated != null)
					onActivated.run();
			}
		}

		new Item("Constructs", Images.GRAPHICS_LOCATION + "Construct Icon-1024px.png", () -> {
			UWindowBase constructs = new ConstructWindowImpl(getBoundStage());
			getScene().setRoot(constructs);
			constructs.setBoundStage(getBoundStage());
			setBoundStage(null);// Lose reference
		});

		new Item("Laws", Images.GRAPHICS_LOCATION + "Law Icon-1024px.png", null);
		new Item("Complexes", Images.GRAPHICS_LOCATION + "Complex Icon-1024px.png", null);
		new Item("Updates", Images.GRAPHICS_LOCATION + "Updates Icon-1024px.png",
				() -> MSGUIs.setScene(new Scene(new UpdateWindowImpl(getBoundStage()), Color.TRANSPARENT)))
						.setColor(Color.RED);
		new Item("Website", "http://dusttoash.org/favicon.png", () -> {
			if (Desktop.isDesktopSupported())
				if (Desktop.getDesktop().isSupported(Action.BROWSE))
					try {
						Desktop.getDesktop().browse(new URI("http://dusttoash.org"));
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}
		}).setColor(Color.GOLD);

		new Item("Modules", Images.GRAPHICS_LOCATION + "Modules Icon-1024px.png", null)
				.setColor(Color.MEDIUMSPRINGGREEN);

	}

}
