package branch.alixia.kröw.unnamed.guis;

import branch.alixia.guis.UWindowBase;
import branch.alixia.msapi.Construct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ConstructWindowImpl extends UWindowBase {

	private final static ObservableList<Construct> CONSTRUCT_LIST = FXCollections
			.synchronizedObservableList(FXCollections.observableArrayList());

	private final TableView<Construct> left = new TableView<>(CONSTRUCT_LIST), right = new TableView<>(CONSTRUCT_LIST);

	private final VBox editPane = new VBox();
	private final ScrollPane editCenterScroll = new ScrollPane(editPane);
	private final Tab edit = new Tab("Edit", editCenterScroll);
	private final TabPane pane = new TabPane(edit);

	{
		setLeft(left);
		setRight(right);
		setCenter(pane);

		left.setBackground(null);
		left.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null,
				new BorderWidths(0, COMMON_BORDER_WIDTH, 0, 0))));

		right.setBackground(null);
		right.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null,
				new BorderWidths(0, 0, 0, COMMON_BORDER_WIDTH))));

	}

}
