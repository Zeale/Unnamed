package branch.alixia.unnamed.gui;

import branch.alixia.kröw.unnamed.DefaultMenuBar;
import branch.alixia.kröw.unnamed.tools.FXTools;
import branch.alixia.kröw.unnamed.tools.Resizable;
import branch.alixia.kröw.unnamed.tools.ResizeOperator;
import branch.alixia.unnamed.MenuBar;
import branch.alixia.unnamed.Unnamed;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;

public class UWindowBase extends BorderPane implements Resizable {

	public static final Color DEFAULT_WINDOW_BACKGROUND_COLOR = Unnamed.DEFAULT_WINDOW_COLOR;
	public static final Color ITEM_BORDER_COLOR = Unnamed.ITEM_BORDER_COLOR;
	public static final double COMMON_BORDER_WIDTH = FXTools.COMMON_BORDER_WIDTH;
	public static final Color SECONDARY_WINDOW_BORDER_COLOR = Unnamed.SECONDARY_WINDOW_BORDER_COLOR;

	private final ObjectProperty<MenuBar> menuBar = new SimpleObjectProperty<>(new DefaultMenuBar());
	private final ObjectProperty<Stage> boundStage = new SimpleObjectProperty<>();

	public UWindowBase(Stage stage) {
		boundStage.set(stage);
	}

	public UWindowBase() {
	}

	{
		topProperty().bind(menuBar);
		menuBar.addListener(new ChangeListener<MenuBar>() {

			private double dx, dy;

			private final EventHandler<? super MouseEvent> pressHandler = event -> {
				if (boundStage.get() == null || boundStage.get().isMaximized() || boundStage.get().isFullScreen())
					return;
				dx = boundStage.get().getX() - event.getScreenX();
				dy = boundStage.get().getY() - event.getScreenY();
				event.consume();
			};

			private final EventHandler<? super MouseEvent> dragHandler = event -> {
				if (boundStage.get() == null || boundStage.get().isMaximized() || boundStage.get().isFullScreen())
					return;
				boundStage.get().setX(event.getScreenX() + dx);
				boundStage.get().setY(event.getScreenY() + dy);
				event.consume();
			};

			{
				if (menuBar.get() != null) {
					menuBar.get().addEventFilter(MouseEvent.MOUSE_PRESSED, pressHandler);
					menuBar.get().addEventFilter(MouseEvent.MOUSE_DRAGGED, dragHandler);
				}
			}

			@Override
			public void changed(ObservableValue<? extends MenuBar> observable, MenuBar oldValue, MenuBar newValue) {

				if (oldValue != null) {
					oldValue.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressHandler);
					oldValue.removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragHandler);
				}
				if (newValue != null) {
					newValue.addEventFilter(MouseEvent.MOUSE_PRESSED, pressHandler);
					newValue.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragHandler);
				}
			}
		});

	}

	private final ResizeOperator resizer = new ResizeOperator(this, this);
	private final BooleanProperty resizeEnabled = resizer.enabledProperty();

	protected static final Color DEFAULT_BORDER_COLOR = DEFAULT_WINDOW_BACKGROUND_COLOR.darker().darker();

	{
		setBackground(FXTools.getBackgroundFromColor(DEFAULT_WINDOW_BACKGROUND_COLOR));
		setBorder(FXTools.getBorderFromColor(DEFAULT_BORDER_COLOR));

		if (getMenuBar() != null)
			getMenuBar().setBorder(new Border(new BorderStroke(DEFAULT_BORDER_COLOR, BorderStrokeStyle.SOLID, null,
					new BorderWidths(0, 0, 2, 0))));
	}

	protected final ObjectProperty<MenuBar> menuBarProperty() {
		return this.menuBar;
	}

	protected final MenuBar getMenuBar() {
		return menuBarProperty().get();
	}

	protected final void setMenuBar(final MenuBar menuBar) {
		menuBarProperty().set(menuBar);
	}

	@Override
	public void expandHor(double amount) {
		Window window = getScene().getWindow();
		if (window instanceof Stage && window.getWidth() + amount < ((Stage) window).getMinWidth())
			return;
		window.setWidth(window.getWidth() + amount);
	}

	@Override
	public void expandVer(double amount) {
		Window window = getScene().getWindow();
		if (window instanceof Stage && window.getHeight() + amount < ((Stage) window).getMinHeight())
			return;
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

	public final ObjectProperty<Stage> boundStageProperty() {
		return this.boundStage;
	}
	

	public final Stage getBoundStage() {
		return this.boundStageProperty().get();
	}
	

	public final void setBoundStage(final Stage boundStage) {
		this.boundStageProperty().set(boundStage);
	}
	

}
