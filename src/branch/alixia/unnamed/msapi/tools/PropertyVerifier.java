package branch.alixia.unnamed.msapi.tools;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

/**
 * <p>
 * This class acts as a quick {@link ChangeListener}.
 * <p>
 * To make a {@link PropertyVerifier}, you must pass in a {@link WritableValue}
 * to its constructor when the PropertyVerifier is created. This WritableValue
 * will have any changes to its value, "verified" by this verifier.
 * <p>
 * Whenever the WritableValue changes, the change is sent to this
 * {@link PropertyVerifier}. If this {@link PropertyVerifier}'s
 * {@link #checkValue(Object)} method returns <code>false</code> when it's given
 * the <code>new value</code>, this {@link PropertyVerifier} will set its
 * WritableValue back to {@link #defaultValue}.
 * 
 * @author Zeale
 *
 * @param <T>
 *            The type of whatever can be written to the {@link WritableValue}.
 */
public class PropertyVerifier<T> implements ChangeListener<T> {

	private boolean run = true;

	private T defaultValue;
	private final WritableValue<T> property;

	/**
	 * Constructs a new {@link PropertyVerifier} with the given
	 * {@link WritableValue}.
	 * 
	 * @param property
	 *            The {@link WritableValue} that will back this
	 *            {@link PropertyVerifier}.
	 */
	public PropertyVerifier(WritableValue<T> property) throws IllegalArgumentException {
		setDefaultValue(property.getValue());
		this.property = property;
	}

	public void setDefaultValue(T defaultValue) throws IllegalArgumentException {
		if (!checkValue(defaultValue))
			throw new IllegalArgumentException(
					"The specified default value for this property verifier is not a valid value.");
		this.defaultValue = defaultValue;
	}

	/**
	 * The default implementation of this method simply verifies that its value is
	 * not null.
	 * 
	 * @param value
	 *            The value to check.
	 * @return <code>true</code> if this value is allowed, <code>false</code>
	 *         otherwise.
	 */
	protected boolean checkValue(T value) {
		return value != null;
	}

	/**
	 * Prevents this {@link PropertyVerifier} from do anything until it is turned
	 * back {@link #turnOn() on}. This method does nothing if this verifier was
	 * already off.
	 */
	public void turnOff() {
		run = false;
	}

	/**
	 * Turns this {@link PropertyVerifier} back on, allowing it to verify its
	 * property again. This method does nothing if this verifier was not already
	 * {@link #turnOff() off}.
	 */
	public void turnOn() {
		run = true;
	}

	/**
	 * Indicates whether or not this {@link PropertyVerifier} is on.
	 * 
	 * @return <code>true</code> if this {@link PropertyVerifier} is on,
	 *         <code>false</code> otherwise.
	 */
	public boolean isOn() {
		return run;
	}

	@Override
	public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
		if (run && !checkValue(newValue))
			property.setValue(defaultValue);
	}

	public static <P extends WritableValue<T> & ObservableValue<T>, T> PropertyVerifier<T> attachNullVerifier(
			P property) {
		PropertyVerifier<T> verifier = new PropertyVerifier<>(property);
		property.addListener(verifier);
		return verifier;
	}

	public static <P extends WritableValue<String> & ObservableValue<String>> StringPropertyVerifier attachStringVerifier(
			P property) {
		StringPropertyVerifier verifier = new StringPropertyVerifier(property);
		property.addListener(verifier);
		return verifier;
	}

}
