package branch.alixia.unnamed;

import branch.alixia.guis.HomeWindowBase;
import javafx.scene.image.Image;

final class HomeWindow extends HomeWindowBase {
	{
		new Item("Constructs") {

			{
				addImage(new Image("/branch/alixia/kröw/unnamed/resources/graphics/Construct Icon-1024px.png"));
			}

			@Override
			protected void activate() {
				System.out.println(0);
			}

		};

		new Item("Laws") {

			{
				addImage(new Image("/branch/alixia/kröw/unnamed/resources/graphics/Law Icon-1024px.png"));
			}

			@Override
			protected void activate() {

			}
		};

		new Item("Complexes") {

			{
				addImage(new Image("/branch/alixia/kröw/unnamed/resources/graphics/Complex Icon-1024px.png"));
			}

			@Override
			protected void activate() {

			}
		};
	}
}
