package branch.alixia.unnamed.gui;

import branch.alixia.kr�w.unnamed.DefaultMenuBar;
import branch.alixia.kr�w.unnamed.tools.FXTools;
import branch.alixia.kr�w.unnamed.tools.Resizable;
import branch.alixia.kr�w.unnamed.tools.ResizeOperator;
import branch.alixia.unnamed.MenuBar;
import branch.alixia.unnamed.Unnamed;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Window;

public class UWindowBase extends BorderPane implements Resizable {

	public static final Color DEFAULT_WINDOW_BACKGROUND_COLOR = Unnamed.DEFAULT_WINDOW_COLOR;
	public static final Color ITEM_BORDER_COLOR = Unnamed.ITEM_BORDER_COLOR;
	public static final double COMMON_BORDER_WIDTH = FXTools.COMMON_BORDER_WIDTH;
	public static final Color SECONDARY_WINDOW_BORDER_COLOR = Unnamed.SECONDARY_WINDOW_BORDER_COLOR;

	private final ObjectProperty<MenuBar> menuBar = new SimpleObjectProperty<>(new DefaultMenuBar());

	{
		topProperty().bind(menuBar);
	}

	private final ResizeOperator resizer = new ResizeOperator(this, this);
	private final BooleanProperty resizeEnabled = resizer.enabledProperty();

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

	@Override
	public void expandHor(double amount) {
		Window window = getScene().getWindow();
		window.setWidth(window.getWidth() + amount);
	}

	@Override
	public void expandVer(double amount) {
		Window window = getScene().getWindow();
		window.setHeight(window.getHeight() + amount);
	}

	@Override
	public void moveX(double amount) {
		Window window = getScene().getWindow();
		window.setX(amount + window.getX());
	}

	@Override
	public void moveY(double amount) {
		Window window = getScene().getWindow();
		window.setY(amount + window.getY());
	}

	public final BooleanProperty resizeEnabledProperty() {
		return this.resizeEnabled;
	}

	public final boolean isResizeEnabled() {
		return this.resizeEnabledProperty().get();
	}

	public final void setResizeEnabled(final boolean resizeEnabled) {
		this.resizeEnabledProperty().set(resizeEnabled);
	}

}
