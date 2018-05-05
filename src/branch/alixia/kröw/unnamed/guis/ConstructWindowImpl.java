package branch.alixia.kröw.unnamed.guis;

import java.io.IOException;
import java.util.Date;

import branch.alixia.guis.UWindowBase;
import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.msapi.Construct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ConstructWindowImpl extends UWindowBase {

	private final static ObservableList<Construct> CONSTRUCT_LIST = FXCollections
			.synchronizedObservableList(FXCollections.observableArrayList());

	private @FXML TableView<Construct> constructs = null;
	private @FXML TableColumn<Construct, String> name = null, description = null;
	private @FXML TableColumn<Construct, Date> birthday = null;

	private final ScrollPane centerWrapper = new ScrollPane();
	private final VBox right = new VBox(15);

	{
		/*
		 * Load in the various FXML nodes.
		 */

		FXMLLoader loader = new FXMLLoader(getClass().getResource("ConstructsView.fxml"));
		loader.setController(this);
		try {
			TableView<Construct> view = loader.load();
			AnchorPane pane = new AnchorPane(view);
			centerWrapper.setContent(pane);
			AnchorPane.setBottomAnchor(constructs, 0d);
			AnchorPane.setTopAnchor(constructs, 0d);
			AnchorPane.setLeftAnchor(constructs, 0d);
			AnchorPane.setRightAnchor(constructs, 0d);
			pane.setBackground(null);
			// loader.load() sets #constructs to the loaded TableView AND returns the SAME
			// loaded TableView.
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	{

		/*
		 * Layout
		 */

		getStylesheets().addAll("/branch/alixia/kröw/unnamed/guis/ConstructWindowImpl.css",
				"/branch/alixia/kröw/unnamed/stylesheet.css");

		setCenter(centerWrapper);
		centerWrapper.setFitToHeight(true);
		centerWrapper.setFitToWidth(true);
		centerWrapper.setBackground(null);
		centerWrapper.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null,
				new BorderWidths(0, COMMON_BORDER_WIDTH, 0, 0))));

		setRight(right);
		right.setMinWidth(150);
		right.setMaxWidth(450);

		/*
		 * Init
		 */

		constructs.setItems(CONSTRUCT_LIST);

		/*
		 * Cell Value Factories
		 */
		name.setCellValueFactory(param -> param.getValue().nameProperty());
		description.setCellValueFactory(param -> param.getValue().descriptionProperty());
		birthday.setCellValueFactory(param -> param.getValue().birthDateProperty());

		/*
		 * Cell Factories
		 */
		name.setCellFactory(param -> new StringCell());
		description.setCellFactory(param -> new StringCell());
		birthday.setCellFactory(param -> new ConstructCell<Date>() {
			protected void updateItem(Date item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.toString());
			};
		});

		constructs.setRowFactory(param -> new ConstructRow());

		constructs.setBackground(null);
		constructs.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 0, 3))));

		/*
		 * Functional Buttons (Right Box)
		 * 
		 * (Testing Code)
		 */

		Button e = new Button("Add");
		e.setOnAction(event -> CONSTRUCT_LIST.add(new Construct("Kröw", "This is a construct!", new Date())));
		right.getChildren().add(e);

		e = new Button("Remove");
		e.setOnAction(event -> {
			if (!CONSTRUCT_LIST.isEmpty())
				CONSTRUCT_LIST.remove(CONSTRUCT_LIST.size() - 1);
		});

		right.getChildren().add(e);
		right.setAlignment(Pos.CENTER);

	}

	private class ConstructCell<T> extends TableCell<Construct, T> {

		{
			setTextFill(Color.WHITE);
		}

		@Override
		protected void updateItem(T item, boolean empty) {
			super.updateItem(item, empty);

			if (item == null || empty)
				setText(null);

		}
	}

	private class ConstructRow extends TableRow<Construct> {

		@Override
		protected void updateItem(Construct item, boolean empty) {
			super.updateItem(item, empty);

			setBackground(FXTools.getBackgroundFromColor(
					item == null || empty ? ((getIndex() & 1) == 0 ? Color.BLACK.interpolate(Color.TRANSPARENT, 0.865)
							: Color.BLACK.interpolate(Color.TRANSPARENT, 0.7)) : Color.GRAY));

		}
	}

	private class StringCell extends ConstructCell<String> {
		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			setText(item);
		}
	}

}
