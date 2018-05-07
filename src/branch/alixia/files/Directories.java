package branch.alixia.files;

import java.io.File;

public final class Directories {

	private Directories() {
	}

	public static boolean makeDirectory(File directory) {
		if (!directory.isDirectory()) {
			directory.delete();
			if (!directory.mkdirs())
				return false;
		}

		return directory.canRead() && directory.canWrite();
	}

}
