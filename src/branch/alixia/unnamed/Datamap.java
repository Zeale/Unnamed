package branch.alixia.unnamed;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Datamap extends HashMap<String, String> {

	@Override
	public String put(String key, String value) {
		String lineSeparator = System.getProperty("line.separator");
		if (key.contains("=") || key.contains(lineSeparator) || value.contains(lineSeparator))
			throw new IllegalArgumentException();
		return super.put(key, value);
	}

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public static void save(Datamap datamap, OutputStream output) {
		PrintWriter writer = new PrintWriter(output);
		for (Entry<String, String> e : datamap.entrySet())
			writer.println(e.getKey() + "=" + e.getValue());
	}

	public static Datamap read(InputStream inputStream) {

		Datamap map = new Datamap();

		try (Scanner scanner = new Scanner(inputStream)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (!line.contains("="))
					throw new RuntimeException("Couldn't find a key value pair for a line.");
				String[] strings = line.split("=");
				String key = strings[0], value = strings[1];

				map.put(key, value);
			}
		}

		return map;
	}

}
