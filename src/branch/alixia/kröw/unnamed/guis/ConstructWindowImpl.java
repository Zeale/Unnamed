package branch.alixia.kröw.unnamed.guis;

import branch.alixia.guis.UWindowBase;
import branch.alixia.msapi.Construct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class ConstructWindowImpl extends UWindowBase {

	private final static ObservableList<Construct> CONSTRUCT_LIST = FXCollections
			.synchronizedObservableList(FXCollections.observableArrayList());

	private final ListView<Construct> center = new ListView<>(CONSTRUCT_LIST);
	private final VBox right = new VBox(15);

	{

		getStylesheets().add("/branch/alixia/kröw/unnamed/guis/ConstructWindowImpl.css");

		setCenter(center);
		setRight(right);
		right.setMinWidth(150);
		right.setMaxWidth(450);

	}

}
