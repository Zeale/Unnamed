package branch.alixia.msapi;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.Instant;
import java.util.UUID;

import branch.alixia.msapi.tools.PropertyVerifier;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Construct implements Externalizable {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;
	private static final long CLASS_VERSION = 1L;

	/*
	 * ********** PROPERTIES **********
	 * 
	 * Any of these that do not have a default value should not be given a
	 * PropertyVerifier with the default implementation.
	 * 
	 */

	private final StringProperty name = new SimpleStringProperty("Unnamed"),
			description = new SimpleStringProperty("Empty description.");
	private final SimpleObjectProperty<Instant> birthDate = new SimpleObjectProperty<>(Instant.now());
	private final ReadOnlyObjectWrapper<UUID> uniqueID = new ReadOnlyObjectWrapper<>(UUID.randomUUID());

	{
		PropertyVerifier.attachStringVerifier(name);
		PropertyVerifier.attachStringVerifier(description);
		PropertyVerifier.attachNullVerifier(birthDate);
	}

	public Construct() {
	}

	public Construct(String name, String description, Instant birthday) {
		setName(name);
		setDescription(description);
		setBirthDate(birthday);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(CLASS_VERSION);

		out.writeObject(uniqueID.get());
		out.writeUTF(name.getValueSafe());
		out.writeUTF(description.getValueSafe());
		out.writeObject(birthDate.get());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		if (in.readLong() != CLASS_VERSION)
			throw new IOException("Incompatible Construct Version");

		try {
			uniqueID.set((UUID) in.readObject());
			name.set(in.readUTF());
			description.set(in.readUTF());
			birthDate.set((Instant) in.readObject());
		} catch (IOException e) {
			// We've reached the end of this construct's data. (The construct is a different
			// version and doesn't have any values after this). Everything below here gets
			// initialized to its default value.
		}
	}

	public final StringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}

	public final StringProperty descriptionProperty() {
		return this.description;
	}

	public final String getDescription() {
		return this.descriptionProperty().get();
	}

	public final void setDescription(final String description) {
		this.descriptionProperty().set(description);
	}

	public final SimpleObjectProperty<Instant> birthDateProperty() {
		return this.birthDate;
	}

	public final Instant getBirthDate() {
		return this.birthDateProperty().get();
	}

	public final void setBirthDate(final Instant birthDate) {
		this.birthDateProperty().set(birthDate);
	}

	public final javafx.beans.property.ReadOnlyObjectProperty<java.util.UUID> uniqueIDProperty() {
		return this.uniqueID.getReadOnlyProperty();
	}

	public final UUID getUniqueID() {
		return this.uniqueIDProperty().get();
	}

	@Override
	public boolean equals(Object obj) {
		// Return true if both are constructs and have equal UUIDs. IDs are supposed to
		// be "unique" and *they are*, but when loading a construct from the file
		// system, via a "reload" method, perhaps, constructs that have been already
		// loaded will get loaded again, so the constructs that are already loaded are
		// detected and discarded. This equals method is used for comparison to detect
		// already loaded constructs.
		return obj instanceof Construct ? ((Construct) obj).getUniqueID().equals(getUniqueID()) : false;
	}

}
