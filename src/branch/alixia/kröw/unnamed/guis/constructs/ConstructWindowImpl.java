package branch.alixia.kröw.unnamed.guis.constructs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import branch.alixia.files.Directories;
import branch.alixia.guis.UWindowBase;
import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.msapi.Construct;
import branch.alixia.msapi.Construct.Class;
import branch.alixia.msapi.Construct.ClassData;
import branch.alixia.unnamed.MenuBar;
import branch.alixia.unnamed.Unnamed;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConstructWindowImpl extends UWindowBase {

	public ConstructWindowImpl() {
	}

	private final static ObservableList<Construct> CONSTRUCT_LIST = FXCollections
			.synchronizedObservableList(FXCollections.observableArrayList());

	public static ObservableList<Construct> getConstructList() {
		return FXCollections.unmodifiableObservableList(CONSTRUCT_LIST);
	}

	private static boolean saveAvailable;

	private static final File CONSTRUCT_DIRECTORY = new File(Unnamed.PROGRAM_ROOT, "Constructs");

	static {
		refresh();
	}

	private static boolean isSaveAvailable() {
		return saveAvailable;
	}

	public static List<Construct> loadConstructs() throws IOException {
		if (!Directories.makeDirectory(CONSTRUCT_DIRECTORY))
			throw new IOException("The save directory is not available.");

		File[] files = CONSTRUCT_DIRECTORY.listFiles();
		List<Construct> constructs = new LinkedList<>();

		for (File f : files)
			if (f.getName().toLowerCase().endsWith(".msc"))
				try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(f))) {
					constructs.add((Construct) input.readObject());
				} catch (Exception e) {
					e.printStackTrace();
				}

		return constructs;

	}

	private static final void refresh() {
		try {
			for (Construct construct : loadConstructs())
				if (!CONSTRUCT_LIST.contains(construct))
					CONSTRUCT_LIST.add(construct);
			saveAvailable = true;
		} catch (IOException e) {
			e.printStackTrace();
			saveAvailable = false;
		}
	}

	public boolean addConstruct(Construct construct) {
		if (CONSTRUCT_LIST.contains(construct))
			return false;
		CONSTRUCT_LIST.add(construct);
		if (!isSaveAvailable())
			notifySaveUnavailable();
		else
			try {
				saveConstruct(construct);
			} catch (IOException e) {
				e.printStackTrace();
				showDialog(
						"Failed to save the construct. (The construct will still exist inside the program so you can save it to a custom location).",
						"Failed to Save Construct", "Save Failure");
			}
		return true;
	}

	private void saveConstruct(Construct construct) throws IOException {
		File file;
		long numb = 0;

		// There is literally like, a zero % chance that the "uniqueID" will not be
		// unique when its generated, but I like the while loop I made.
		while ((file = new File(CONSTRUCT_DIRECTORY, construct.getUniqueID() + "_" + numb++ + ".msc")).exists())
			;
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
			output.writeObject(construct);
		}
	}

	public final void showDialog(String content, String header, String title) {
		Dialog<Void> notification = new Dialog<>();
		notification.initOwner(getScene().getWindow());
		notification.setContentText(content);
		notification.setHeaderText(header);
		notification.setTitle(title);
		notification.showAndWait();
	}

	private void notifySaveUnavailable() {
		showDialog(
				"Saving and loading constructs to/from the default directory is unavailable. Please save to a specific location when you are finished.",
				"Default Save Location Unavailable", "Save Location");
	}

	public void removeConstruct(Construct construct) {
		CONSTRUCT_LIST.remove(construct);
	}

	private @FXML TableView<Construct> constructs;
	private @FXML TableColumn<Construct, String> name, description;
	private @FXML TableColumn<Construct, Instant> birthday;
	private @FXML TableColumn<Construct, Construct.ClassData> classData;
	private @FXML TableColumn<Construct, ClassData> classes;
	private @FXML TableColumn<Construct, Double> gradientValue;

	private final ScrollPane centerWrapper = new ScrollPane();

	private final MenuBar rightToolBar = new MenuBar();
	private final AnchorPane menu = new AnchorPane();
	private final VBox rightWrapper = new VBox(0, rightToolBar, menu);

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

		getStylesheets().addAll("/branch/alixia/kröw/unnamed/guis/constructs/ConstructWindowImpl.css",
				"/branch/alixia/kröw/unnamed/stylesheet.css");

		setCenter(centerWrapper);
		centerWrapper.setFitToHeight(true);
		centerWrapper.setFitToWidth(true);
		centerWrapper.setBackground(null);
		centerWrapper.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null,
				new BorderWidths(0, COMMON_BORDER_WIDTH, 0, 0))));

		setRight(rightWrapper);
		rightWrapper.setMinWidth(150);
		rightWrapper.setPrefWidth(300);
		rightWrapper.setMaxWidth(450);

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
		classes.setCellValueFactory(param -> param.getValue().classDataProperty());
		gradientValue.setCellValueFactory(param -> Bindings.createObjectBinding(
				() -> param.getValue().getClassData().getGradientFraction(), param.getValue().classDataProperty()));

		/*
		 * Cell Factories
		 */
		name.setCellFactory(param -> new StringCell());
		description.setCellFactory(param -> new StringCell());
		birthday.setCellFactory(param -> new ConstructCell<Instant>() {
			protected void updateItem(Instant item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.toString());
			};
		});
		classes.setCellFactory(param -> new ConstructCell<ClassData>() {
			protected void updateItem(ClassData item, boolean empty) {
				super.updateItem(item, empty);
				if (empty)
					return;
				setText(item.getClasses().toString());
				if (item.getClasses().contains(Class.BIG_DADDY))
					setTextFill(Color.FIREBRICK);
				else if (item.getClasses().contains(Class.ELEVATED_TITAN)
						|| item.getClasses().contains(Class.HONORARY_TITAN))
					if (item.getClasses().contains(Class.ELEVATED_TITAN)
							&& item.getClasses().contains(Class.HONORARY_TITAN))
						setTextFill(Color.AQUAMARINE);
					else
						setTextFill(Color.GREEN);
				else if (item.getClasses().contains(Class.PROPER_TITAN))
					setTextFill(Color.BLUE);
				else if (item.getClasses().contains(Class.PERSONALITY))
					setTextFill(Color.PURPLE.interpolate(Color.LIME, item.getGradientFraction() / 1000));
			};
		});
		gradientValue.setCellFactory(param -> {
			ConstructCell<Double> cell = new ConstructCell<Double>() {
				protected void updateItem(Double item, boolean empty) {

					super.updateItem(item, empty);
					if (empty)
						return;
					if (item < 0 || item > 1000) {
						setText("N/A");
						setTextFill(Color.RED);
					} else {
						setTextFill(Color.PURPLE.interpolate(Color.LIME, item / 1000));
						setText(item / 1000 + "");
					}
				};
			};
			return cell;
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

		Text newTab = getFormattedMenuText("New");
		rightToolBar.new MenuItem((Node) newTab);

		newTab.setOnMouseClicked(event -> showNewMenu());

		rightToolBar.setPrefHeight(30);
		rightToolBar.setRightMenuPadding(15);
		rightToolBar.setRightMenuSpacing(10);

	}

	private static Text getFormattedMenuText(String text) {
		Text t = new Text(text);
		t.setFont(Font.font(null, FontWeight.BOLD, -1));

		FXTools.applyColorwheelTransition(t);

		return t;
	}

	private Stage newWindow;

	private void showNewMenu() {
		if (newWindow == null) {
			newWindow = new Stage(StageStyle.TRANSPARENT);
			newWindow.initOwner(getScene().getWindow());
			newWindow.setAlwaysOnTop(true);
		}
		if (newWindow.isShowing())
			return;
		newWindow.setScene(new Scene(new NewConstructWindowImpl(this, newWindow), Color.TRANSPARENT));
		newWindow.show();
	}

	private class ConstructCell<T> extends TableCell<Construct, T> {

		protected final Color DEFAULT_CELL_TEXT_FILL = Color.WHITE;

		{
			setTextFill(DEFAULT_CELL_TEXT_FILL);
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

			setBackground(
					FXTools.getBackgroundFromColor(
							item == null || empty
									? ((getIndex() & 1) == 0 ? Color.BLACK.interpolate(Color.TRANSPARENT, 0.865)
											: Color.BLACK.interpolate(Color.TRANSPARENT, 0.7))
									: new Color(0.4, 0.4, 0.4, 1)));

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
