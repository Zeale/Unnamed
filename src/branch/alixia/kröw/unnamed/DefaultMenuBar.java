package branch.alixia.kröw.unnamed;

import branch.alixia.unnamed.MSGUIs;
import branch.alixia.unnamed.MenuBar;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DefaultMenuBar extends MenuBar {

	private final Text homeText = new Text("H");
	private final MenuItem home = new MenuItem(homeText);

	private final Rectangle rightCloseBar = new Rectangle(), leftCloseBar = new Rectangle();
	private final MenuItem close = new MenuItem(rightCloseBar, leftCloseBar);

	{

		setRightMenuPadding(0);
		setRightMenuSpacing(8);

		rightCloseBar.heightProperty().bind(close.getButton().heightProperty().multiply(0.75));
		leftCloseBar.heightProperty().bind(rightCloseBar.heightProperty());
		rightCloseBar.widthProperty().bind(rightCloseBar.heightProperty().divide(6));
		leftCloseBar.widthProperty().bind(leftCloseBar.heightProperty().divide(6));

		rightCloseBar.setRotate(45);
		leftCloseBar.setRotate(-45);

		rightCloseBar.setFill(Color.RED);
		leftCloseBar.setFill(Color.RED);

		close.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY)
				getScene().getWindow().hide();
		});

		homeText.setFill(Color.BLUE);
		homeText.setFont(Font.font(null, FontWeight.BOLD, -1));

		home.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY)
				MSGUIs.showHomeWindow();
		});

	}

}
