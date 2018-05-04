package branch.alixia.guis;

import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.unnamed.MenuBar;
import branch.alixia.unnamed.Unnamed;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class UWindowBase extends BorderPane {

	private final ObjectProperty<MenuBar> menuBar = new SimpleObjectProperty<>(new MenuBar());

	{
		topProperty().bind(menuBar);
	}

	private static final Color DEFAULT_BACKGROUND_COLOR = Unnamed.DEFAULT_WINDOW_COLOR;
	private static final Color DEFAULT_BORDER_COLOR = DEFAULT_BACKGROUND_COLOR.darker().darker();

	{
		setBackground(FXTools.getBackgroundFromColor(DEFAULT_BACKGROUND_COLOR));
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
