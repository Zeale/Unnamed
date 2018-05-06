package branch.alixia.kröw.unnamed.guis.constructs;

import javafx.scene.control.TableCell;
import javafx.util.Callback;

class QuickTableCell<T, I> extends TableCell<T, I> {

	private final Callback<I, String> stringGenerator;

	public QuickTableCell(Callback<I, String> stringGenerator) {
		this.stringGenerator = stringGenerator;
	}

	@Override
	protected void updateItem(I item, boolean empty) {

		if (getItem() == item)
			return;

		super.updateItem(item, empty);

		if (item == null || empty) {
			setText(null);
			setGraphic(null);
		} else {
			setText(stringGenerator.call(item));
			setGraphic(null);
		}

	}

}
