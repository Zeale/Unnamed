package branch.alixia.msapi;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

import branch.alixia.msapi.tools.PropertyVerifier;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class MindsetObject implements Externalizable {

	protected final StringProperty name = new SimpleStringProperty("Unnamed");
	protected final ReadOnlyObjectWrapper<UUID> uniqueID = new ReadOnlyObjectWrapper<UUID>(UUID.randomUUID());
	private static final long CLASS_VERSION = 1L;

	{
		PropertyVerifier.attachStringVerifier(name);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(CLASS_VERSION);

		out.writeObject(uniqueID.get());
		out.writeUTF(name.getValueSafe());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		if (in.readLong() != CLASS_VERSION)
			throw new IOException("Incompatible Construct Version");

		try {
			uniqueID.set((UUID) in.readObject());
			name.set(in.readUTF());
		} catch (Exception e) {
		}
	}

	public MindsetObject() {
	}

}
