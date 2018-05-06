package branch.alixia.unnamed;

import branch.alixia.Images;
import branch.alixia.guis.HomeWindowBase;

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
	}
}
