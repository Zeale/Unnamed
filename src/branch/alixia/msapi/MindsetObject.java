package branch.alixia.msapi;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

import branch.alixia.msapi.tools.MSSTringProperty;
import branch.alixia.msapi.tools.PropertyVerifier;
import javafx.beans.property.ReadOnlyObjectWrapper;

public abstract class MindsetObject implements Externalizable {

	protected final MSSTringProperty name = new MSSTringProperty("Unnamed");
	protected final ReadOnlyObjectWrapper<UUID> uniqueID = new ReadOnlyObjectWrapper<UUID>(UUID.randomUUID());
	private static final long CLASS_VERSION = 1L;

	{
		PropertyVerifier.attachStringVerifier(name);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// Write the class version
		out.writeLong(CLASS_VERSION);

		out.writeObject(uniqueID.get());
		out.writeObject(name);
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

	public final javafx.beans.property.ReadOnlyObjectProperty<java.util.UUID> uniqueIDProperty() {
		return this.uniqueID.getReadOnlyProperty();
	}

	public final UUID getUniqueID() {
		return this.uniqueIDProperty().get();
	}

	public final MSSTringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}

}
