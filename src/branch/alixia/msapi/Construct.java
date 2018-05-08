package branch.alixia.msapi;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.Instant;

import branch.alixia.msapi.tools.MSSTringProperty;
import branch.alixia.msapi.tools.MindsetObjectProperty;
import branch.alixia.msapi.tools.PropertyVerifier;
import javafx.beans.property.ObjectProperty;

public class Construct extends MindsetObject {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * ********** PROPERTIES **********
	 * 
	 * Any of these that do not have a default value should not be given a
	 * PropertyVerifier with the default implementation.
	 * 
	 */

	private final MSSTringProperty description = new MSSTringProperty("Empty description.");
	private final ObjectProperty<Instant> birthDate = new MindsetObjectProperty<>(Instant.now());

	{
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
		out.writeUTF(description.getValueSafe());
		out.writeObject(birthDate.get());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		try {
			description.set(in.readUTF());
			birthDate.set((Instant) in.readObject());
		} catch (IOException e) {
			// We've reached the end of this construct's data. (The construct is a different
			// version and doesn't have any values after this). Everything below here gets
			// initialized to its default value.
		}
	}

	public final ObjectProperty<Instant> birthDateProperty() {
		return this.birthDate;
	}

	public final Instant getBirthDate() {
		return this.birthDateProperty().get();
	}

	public final void setBirthDate(final Instant birthDate) {
		this.birthDateProperty().set(birthDate);
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

	public final MSSTringProperty descriptionProperty() {
		return this.description;
	}

	public final String getDescription() {
		return this.descriptionProperty().get();
	}

	public final void setDescription(final String description) {
		this.descriptionProperty().set(description);
	}

}
