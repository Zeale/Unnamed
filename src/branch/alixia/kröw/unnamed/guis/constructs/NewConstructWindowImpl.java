package branch.alixia.kröw.unnamed.guis.constructs;

import branch.alixia.guis.UWindowBase;
import branch.alixia.kröw.unnamed.tools.FXTools;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class NewConstructWindowImpl extends UWindowBase {

	private final TextField nameInput = new TextField();
	private final DatePicker birthdayInput = new DatePicker();
	private final TextArea descriptionInput = new TextArea();

	private final VBox innerContentWrapper = new VBox(20, nameInput, birthdayInput, descriptionInput);
	private final Button doneButton = new Button("Done");
	private final VBox content = new VBox(50, innerContentWrapper, doneButton);

	private final AnchorPane wrapper = new AnchorPane(content);

	private final Text cancel = new Text("Cancel");

	private final Stage stage;

	{

		/*
		 * Pane Layout
		 */
		setMinSize(800, 600);
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

		FXTools.styleBasicInput(nameInput, birthdayInput, descriptionInput, doneButton);

		nameInput.setPrefWidth(80);
		descriptionInput.setPrefSize(200, 200);

		innerContentWrapper.setAlignment(Pos.CENTER);
		innerContentWrapper.setFillWidth(false);

	}

	private final ConstructWindowImpl owner;

	public NewConstructWindowImpl(ConstructWindowImpl owner, Stage stage) {
		this.owner = owner;
		this.stage = stage;
	}

}
