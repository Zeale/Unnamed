package branch.alixia.msapi;

import java.util.Date;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Construct {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;
	private static final long CLASS_VERSION = 1L;

	private transient final StringProperty name = new SimpleStringProperty("Unnamed"),
			description = new SimpleStringProperty("Empty description.");
	private transient final SimpleObjectProperty<Date> birthDate = new SimpleObjectProperty<>();

	public Construct() {
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

	public final SimpleObjectProperty<Date> birthDateProperty() {
		return this.birthDate;
	}

	public final Date getBirthDate() {
		return this.birthDateProperty().get();
	}

	public final void setBirthDate(final Date birthDate) {
		this.birthDateProperty().set(birthDate);
	}

}
