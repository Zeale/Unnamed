package branch.alixia.kröw.unnamed.guis.constructs;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import branch.alixia.guis.UWindowBase;
import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.msapi.Construct;
import branch.alixia.msapi.Construct.ClassData;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
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

				// Sets classes properly.
				ClassData classData = minor.isSelected() ? ClassData.getMinorClass()
						: honoraryTitan.isSelected() || elevatedTitan.isSelected()
								? ClassData.getClass(elevatedTitan.isSelected(), honoraryTitan.isSelected())
								: perMech.isSelected()
										? ClassData.getPersonalityAndMechanical(perMechDegrees.getValue())
										: properTitan.isSelected() ? ClassData.getProperTitanClass()
												: ClassData.getDefaultClass();
				construct.setClassData(classData);

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

	private final Button back = new Button("<--");

	private final ToggleGroup overallClass = new ToggleGroup();
	private final RadioButton minor = new RadioButton("Minor"), major = new RadioButton("Major");
	private final CheckBox properTitan = new CheckBox("Proper Titan"), elevatedTitan = new CheckBox("Elevated Titan"),
			honoraryTitan = new CheckBox("Honorary Titan"), perMech = new CheckBox("Personality/Mechanical");
	private final TilePane classWrapper = new TilePane(minor, major, perMech, properTitan, elevatedTitan,
			honoraryTitan);

	private final Slider perMechDegrees = new Slider(0, 1000, 0);

	private final Separator classSplitter = new Separator();

	private final VBox optionsContent = new VBox(20, classWrapper, perMechDegrees, classSplitter);
	private final AnchorPane optionsWrapper = new AnchorPane(optionsContent, back);
	private final ScrollPane optionsScrollWrapper = new ScrollPane(optionsWrapper);

	private void setDisable(boolean disabled, Node... nodes) {
		for (Node n : nodes)
			n.setDisable(disabled);
	}

	{

		/*
		 * Class Selector Setup
		 */

		// Make sure that you can't select both major and minor simulataneously.
		overallClass.getToggles().addAll(minor, major);

		// If the major is not selected, disable the classes that can only be selected
		// if major is selected.
		major.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue,
				newValue) -> setDisable(!newValue, properTitan, elevatedTitan, honoraryTitan, perMech));

		properTitan.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue,
				newValue) -> setDisable(newValue, elevatedTitan, honoraryTitan, perMech));

		perMech.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			perMechDegrees.setDisable(!newValue);
			setDisable(newValue, elevatedTitan, honoraryTitan, properTitan);
		});

		/*
		 * Honorary and Elevated Titan classes.
		 */
		ChangeListener<Boolean> multiTitans = (observable, oldValue, newValue) -> {
			if (newValue)
				setDisable(true, perMech, properTitan);
			else if (!elevatedTitan.isSelected() && !honoraryTitan.isSelected())
				setDisable(false, perMech, properTitan);
		};
		elevatedTitan.selectedProperty().addListener(multiTitans);
		honoraryTitan.selectedProperty().addListener(multiTitans);

		perMechDegrees.setDisable(true);
		major.setSelected(true);

		/*
		 * Window Initialization
		 */

		classSplitter.prefWidthProperty().bind(optionsContent.widthProperty());

		AnchorPane.setTopAnchor(optionsContent, 110d);
		AnchorPane.setRightAnchor(optionsContent, 0d);
		AnchorPane.setLeftAnchor(optionsContent, 0d);
		AnchorPane.setBottomAnchor(optionsContent, 0d);
		optionsContent.setAlignment(Pos.CENTER);
		optionsContent.setFillWidth(false);

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
