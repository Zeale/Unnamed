package branch.alixia.msapi.tools;

public class MSSTringProperty extends MindsetObjectProperty<String> {

	public MSSTringProperty() {
	}

	public MSSTringProperty(String initialValue) {
		super(initialValue);
	}

	public MSSTringProperty(Object bean, String name) {
		super(bean, name);
	}

	public MSSTringProperty(Object bean, String name, String initialValue) {
		super(bean, name, initialValue);
	}

	public String getValueSafe() {
		String value = get();
		return value == null ? "" : value;
	}

}
