package branch.alixia.kröw.unnamed.guis.constructs;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import branch.alixia.guis.UWindowBase;
import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.msapi.Construct;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class NewConstructWindowImpl extends UWindowBase {

	public NewConstructWindowImpl(ConstructWindowImpl owner, Stage stage) {
		this.owner = owner;
		this.stage = stage;
	}

	private final Stage stage;
	private final ConstructWindowImpl owner;

	/*
	 * Main Content
	 */
	private final Button moreOptions = new Button("...");

	private final TextField nameInput = new TextField();
	private final DatePicker birthdayInput = new DatePicker();
	private final TextArea descriptionInput = new TextArea();

	private final VBox innerContentWrapper = new VBox(20, nameInput, birthdayInput, descriptionInput);
	private final Button doneButton = new Button("Create");
	private final VBox content = new VBox(50, innerContentWrapper, doneButton);

	private final AnchorPane wrapper = new AnchorPane(content, moreOptions);

	private final Text cancel = new Text("Cancel");

	{

		/*
		 * Pane Layout
		 */
		wrapper.setPrefSize(800, 600);
		setCenter(wrapper);
		setBorder(FXTools.getBorderFromColor(ITEM_BORDER_COLOR.interpolate(DEFAULT_WINDOW_BACKGROUND_COLOR, 0.5)));

		/*
		 * Center Content Node
		 */
		content.setBackground(null);
		content.setFillWidth(false);
		content.setAlignment(Pos.CENTER);

		AnchorPane.setTopAnchor(content, 0d);
		AnchorPane.setRightAnchor(content, 0d);
		AnchorPane.setBottomAnchor(content, 0d);
		AnchorPane.setLeftAnchor(content, 50d);

		/*
		 * Menu Bar Setup
		 */
		getMenuBar().new MenuItem((Node) cancel);
		FXTools.applyColorwheelTransition(cancel);
		cancel.setFont(Font.font(null, FontWeight.BOLD, -1));
		cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.close();
			}
		});

		/*
		 * Center Content Initialization
		 */
		nameInput.setPromptText("Construct Name");
		descriptionInput.setPromptText("Description");

		FXTools.styleBasicInput(nameInput, birthdayInput, descriptionInput, doneButton, moreOptions);

		nameInput.setPrefWidth(80);
		descriptionInput.setMinSize(200, 200);

		innerContentWrapper.setAlignment(Pos.CENTER);
		innerContentWrapper.setFillWidth(false);

		doneButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = nameInput.getText(), description = descriptionInput.getText();
				LocalDate date = birthdayInput.getValue();
				Instant time = date == null ? null : date.atStartOfDay(ZoneId.systemDefault()).toInstant();

				Construct construct = new Construct(name, description, time);

				if (!owner.addConstruct(construct)) {
					owner.showDialog("This construct already exists!", "Duplicate Construct error", "Dupe Construct");
					return;
				}

				stage.close();
			}
		});

		AnchorPane.setRightAnchor(moreOptions, 35d);
		AnchorPane.setTopAnchor(moreOptions, 35d);
		moreOptions.setOnAction(event -> showExtraProperties());

	}

	private void showNormalProperties() {
		stage.setWidth(800);
		stage.setHeight(600);
		stage.centerOnScreen();
		setCenter(wrapper);
	}

	/*
	 * Extra Properties Window
	 */

	private final ToggleGroup minorMajorGroup = new ToggleGroup(), pMTGroup = new ToggleGroup();
	private final RadioButton minor = new RadioButton("Minor"), major = new RadioButton("Major"),
			perMech = new RadioButton("Personality/Mechanical"), properTitan = new RadioButton("Proper Titan"),
			elevatedTitan = new RadioButton("Elevated Titan"), honoraryTitan = new RadioButton("Honorary Titan");

	private final TilePane classWrapper = new TilePane(minor, major, perMech, properTitan, elevatedTitan,
			honoraryTitan);

	private final Button back = new Button("<--");

	private final VBox optionsContent = new VBox(20, classWrapper);
	private final AnchorPane optionsWrapper = new AnchorPane(optionsContent, back);
	private final ScrollPane optionsScrollWrapper = new ScrollPane(optionsWrapper);

	{

		AnchorPane.setTopAnchor(optionsContent, 110d);
		AnchorPane.setRightAnchor(optionsContent, 0d);
		AnchorPane.setLeftAnchor(optionsContent, 0d);
		AnchorPane.setBottomAnchor(optionsContent, 0d);

		minorMajorGroup.getToggles().addAll(minor, major);
		pMTGroup.getToggles().addAll(perMech, properTitan, elevatedTitan, honoraryTitan);

		FXTools.styleBasicInput(minor, major, perMech, properTitan, elevatedTitan, honoraryTitan);

		AnchorPane.setTopAnchor(back, 35d);
		AnchorPane.setLeftAnchor(back, 35d);

		classWrapper.setHgap(15);
		classWrapper.setVgap(5);
		classWrapper.setAlignment(Pos.CENTER);

		FXTools.styleBasicInput(back);
		back.setOnAction(event -> showNormalProperties());

		FXTools.setDefaultBackground(optionsScrollWrapper);
		optionsScrollWrapper.setFitToWidth(true);

	}

	private void showExtraProperties() {
		stage.setWidth(1400);
		stage.setHeight(800);
		stage.centerOnScreen();
		setCenter(optionsScrollWrapper);
	}

}
