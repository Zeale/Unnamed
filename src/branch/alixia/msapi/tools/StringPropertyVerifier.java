package branch.alixia.msapi.tools;

import javafx.beans.value.WritableValue;

public class StringPropertyVerifier extends PropertyVerifier<String> {

	public StringPropertyVerifier(WritableValue<String> property) {
		super(property);
	}

	@Override
	protected boolean checkValue(String value) {
		return super.checkValue(value) && !value.isEmpty();
	}

}
