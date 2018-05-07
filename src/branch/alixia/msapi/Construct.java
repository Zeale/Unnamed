package branch.alixia.msapi;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.Instant;

import branch.alixia.msapi.tools.PropertyVerifier;
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

	private transient final StringProperty name = new SimpleStringProperty("Unnamed"),
			description = new SimpleStringProperty("Empty description.");
	private transient final SimpleObjectProperty<Instant> birthDate = new SimpleObjectProperty<>(Instant.now());

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

		out.writeUTF(name.getValueSafe());
		out.writeUTF(description.getValueSafe());
		out.writeObject(birthDate.get());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		if (in.readLong() != CLASS_VERSION)
			throw new IOException("Incompatible Construct Version");

		name.set(in.readUTF());
		description.set(in.readUTF());
		birthDate.set((Instant) in.readObject());
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

}
