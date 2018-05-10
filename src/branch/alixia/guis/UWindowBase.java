package branch.alixia.guis;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.MenuBar;
import branch.alixia.unnamed.Unnamed;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class UWindowBase extends BorderPane {

	public static final Color DEFAULT_WINDOW_BACKGROUND_COLOR = Unnamed.DEFAULT_WINDOW_COLOR;
	public static final Color ITEM_BORDER_COLOR = Unnamed.ITEM_BORDER_COLOR;
	public static final double COMMON_BORDER_WIDTH = FXTools.COMMON_BORDER_WIDTH;
	public static final Color SECONDARY_WINDOW_BORDER_COLOR = Unnamed.SECONDARY_WINDOW_BORDER_COLOR;

	private final ObjectProperty<MenuBar> menuBar = new SimpleObjectProperty<>(new MenuBar());

	{
		topProperty().bind(menuBar);
		setPrefSize(1206, 726);
	}

	protected static final Color DEFAULT_BORDER_COLOR = DEFAULT_WINDOW_BACKGROUND_COLOR.darker().darker();

	{
		setBackground(FXTools.getBackgroundFromColor(DEFAULT_WINDOW_BACKGROUND_COLOR));
		setBorder(FXTools.getBorderFromColor(DEFAULT_BORDER_COLOR));
	}

	protected final ObjectProperty<MenuBar> menuBarProperty() {
		return this.menuBar;
	}

	protected final MenuBar getMenuBar() {
		return this.menuBarProperty().get();
	}

	protected final void setMenuBar(final MenuBar menuBar) {
		this.menuBarProperty().set(menuBar);
	}

}
