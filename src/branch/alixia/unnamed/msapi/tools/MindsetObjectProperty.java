package branch.alixia.unnamed.msapi.tools;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javafx.beans.property.SimpleObjectProperty;

public class MindsetObjectProperty<T> extends SimpleObjectProperty<T> implements Externalizable {

	public MindsetObjectProperty() {
	}

	public MindsetObjectProperty(T initialValue) {
		super(initialValue);
	}

	public MindsetObjectProperty(Object bean, String name) {
		super(bean, name);
	}

	public MindsetObjectProperty(Object bean, String name, T initialValue) {
		super(bean, name, initialValue);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			set((T) in.readObject());
		} catch (ClassCastException e) {
			throw new IOException(e);
		}
	}

}
