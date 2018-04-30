package branch.alixia.unnamed;

import branch.alixia.kr�w.unnamed.tools.FXTools;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class UWindowBase extends BorderPane {

	protected final MenuBar menuBar = new MenuBar();
	{
		setTop(menuBar);
	}

	private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0.37, 0.37, 0.37, 1);
	private static final Color DEFAULT_BORDER_COLOR = DEFAULT_BACKGROUND_COLOR.darker().darker();

	{
		setBackground(FXTools.getBackgroundFromColor(DEFAULT_BACKGROUND_COLOR));
		setBorder(FXTools.getBorderFromColor(DEFAULT_BORDER_COLOR));
	}

}
