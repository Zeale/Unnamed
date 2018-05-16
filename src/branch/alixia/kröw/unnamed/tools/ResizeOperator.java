package branch.alixia.kröw.unnamed.tools;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class ResizeOperator implements EventHandler<MouseEvent> {

	private final BooleanProperty enabled = new SimpleBooleanProperty(true);

	public final class Drag {
		public final boolean top, bottom, left, right;
		public double sx, sy;

		public Drag(final MouseEvent click) {
			top = top(click);
			bottom = bottom(click);
			left = left(click);
			right = right(click);

			sx = click.getX();
			sy = click.getY();

			click.consume();
		}
	}

	public enum Side {
		TOP, BOTTOM, LEFT, RIGHT;
	}

	private final Region region;
	private int resizeMargin = 8;

	private final Resizable mover;

	private Drag drag;

	public ResizeOperator(final Region region, final Resizable mover) {
		this.region = region;
		this.mover = mover;
		setup();
	}

	public ResizeOperator(final Region region, final Resizable mover, final int resizeMargin) {
		this(region, mover);
		this.resizeMargin = resizeMargin;
	}

	protected boolean bottom(final MouseEvent e) {
		return e.getY() >= region.getHeight() - resizeMargin;
	}

	protected boolean bottomleft(final MouseEvent e) {
		return bottom(e) && left(e);
	}

	protected boolean bottomright(final MouseEvent e) {
		return bottom(e) && right(e);
	}

	@Override
	public void handle(final MouseEvent event) {
		if (!enabled.get())
			return;

		if (event.getEventType().equals(MouseEvent.MOUSE_MOVED))
			handleMove(event);
		if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED))
			handlePress(event);
		if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED))
			handleDrag(event);
		if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
			handleRelease(event);
	}

	protected void handleDrag(final MouseEvent e) {
		if (drag != null) {
			if (drag.top)
				updateTop(e);
			if (drag.bottom)
				updateBottom(e);
			if (drag.left)
				updateLeft(e);
			if (drag.right)
				updateRight(e);
			e.consume();
		}

	}

	protected void handleMove(final MouseEvent e) {
		if (top(e))
			region.setCursor(left(e) ? Cursor.NW_RESIZE : right(e) ? Cursor.NE_RESIZE : Cursor.N_RESIZE);
		else if (bottom(e))
			region.setCursor(left(e) ? Cursor.SW_RESIZE : right(e) ? Cursor.SE_RESIZE : Cursor.S_RESIZE);
		else
			region.setCursor(left(e) ? Cursor.W_RESIZE : right(e) ? Cursor.E_RESIZE : Cursor.DEFAULT);
	}

	protected void handlePress(final MouseEvent e) {
		if (inRange(e))
			drag = new Drag(e);// Also consumes event.
	}

	protected void handleRelease(final MouseEvent e) {
		drag=null;
	}

	private boolean inRange(final MouseEvent event) {
		return top(event) || bottom(event) || left(event) || right(event);
	}

	protected boolean left(final MouseEvent e) {
		return e.getX() <= resizeMargin;
	}

	protected boolean right(final MouseEvent e) {
		return e.getX() >= region.getWidth() - resizeMargin;
	}

	private void setup() {
		// Added to handle any event, but only handles move, press, drag, and release by
		// handle method. Subclasses may change this by overriding handle method.
		region.addEventFilter(MouseEvent.ANY, this);
	}

	protected boolean top(final MouseEvent e) {
		return e.getY() <= resizeMargin;
	}

	protected boolean topleft(final MouseEvent e) {
		return top(e) && left(e);
	}

	protected boolean topright(final MouseEvent e) {
		return top(e) && right(e);
	}

	private void updateBottom(final MouseEvent e) {
		mover.expandVer(e.getY() - drag.sy);
		drag.sy = e.getY();// drag.sy needs to be updated here.
	}

	private void updateLeft(final MouseEvent e) {
		mover.moveX(e.getX() - drag.sx);
		mover.expandHor(-e.getX() + drag.sx);
	}

	private void updateRight(final MouseEvent e) {
		mover.expandHor(e.getX() - drag.sx);
		drag.sx = e.getX();
	}

	private void updateTop(final MouseEvent event) {
		// They are dragging the top up and down. Lock the bottom, left, and right.
		mover.moveY(event.getY() - drag.sy);// They are dragging out of the node, so move downwards by y. (y is negative
		// since they are going up. Up is negative.) This moves the Resizable upwards.
		mover.expandVer(-event.getY() + drag.sy);// Expand it by how much they moved, or shrink it by the amount.

		// If the user drags upwards, (meaning they want to enlarge the item), then this
		// method will be called and it will move the item upwards, while expanding it
		// by the same amount vertically. This simulates the bottom being anchored.
		//
		// Since both operations are executed separately, the window jitters often.

	}

	public final BooleanProperty enabledProperty() {
		return this.enabled;
	}

	public final boolean isEnabled() {
		return this.enabledProperty().get();
	}

	public final void setEnabled(final boolean enabled) {
		this.enabledProperty().set(enabled);
	}

}
