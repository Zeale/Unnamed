package branch.alixia.unnamed.api;

/**
 * <p>
 * This class represents the string representation of a {@link Version} of this
 * program, and will be composed of the following terms, as specified in the
 * following list.
 * <p>
 * <ol>
 * <li>A sequence of numbers and decimal points that represent the numerical
 * version of an instance of the program (where no two decimals are directly
 * next to each other).</li>
 * <li>An optional build number represented by a <code>b</code> followed
 * immediately by the build number. The <code>b</code> will be the first
 * occurrence of a letter in the version, if this term exists in it.</li>
 * <li>An optional string of text determining whether this version is an alpha
 * release or beta release. This term will consist of a dash/hyphen character,
 * (<code>-</code>), followed immediately by the word <code>alpha</code> or the
 * word <code>beta</code>, depending on whether or not this is an alpha or beta
 * release.</li>
 * </ol>
 * All terms will be immediately followed by the next term, if the next term
 * exists.
 * <p>
 * Following are some examples of valid versions in String form.
 *
 * <pre>
 * <code>0.3.9</code>
 * </pre>
 *
 * This string is very simple. It only contains numbers denoting the version it
 * represents. This is not a beta nor an alpha build, and it does not contain a
 * build number.
 *
 * <pre>
 * <code>1.18.9.4b3</code>
 * </pre>
 *
 * This string represents a build number, as well as a version of the program.
 * Since this string is not followed by a hyphen/dash and the word "beta" or
 * "alpha", it does not represent an alpha or beta build.
 *
 * <pre>
 * <code>14.3.12b73-alpha</code>
 * </pre>
 *
 * This string contains all possible terms, and in the correct order.
 *
 * @author Zeale
 *
 */
public final class Version implements Comparable<Version> {

	/**
	 * This enum contains objects that denote the type of a build of a program. The
	 * contents of this enum are ordered accurately i.e. they can be correctly used
	 * by {@link #compareTo(BuildType)} calls. The earlier in the time of a
	 * program's lifecycle, that an object in this enum denotes, the "less" it is,
	 * in comparison to other objects in this enum. For example:
	 *
	 * <pre>
	 * <code> {@link #NIGHTLY}.{@link #compareTo(BuildType) compareTo(ALPHA)} </code>
	 * </pre>
	 *
	 * will return {@code -1}, since nightly builds come before alpha builds in the
	 * life of a program's development.
	 *
	 * @author Zeale
	 *
	 */
	public enum BuildType {
		NIGHTLY, ALPHA, BETA,
		/**
		 * This type should not be specified inside a version string; it is implied
		 * through the lack of a specified version type.
		 */
		REGULAR;

		@Override
		public String toString() {
			final String name = name().toLowerCase();
			final char firstChar = name.charAt(0);
			return name.replaceFirst("" + firstChar, "" + Character.toUpperCase(firstChar));
		}
	}

	private static boolean hasBuildNumber(final String point) {
		// The last point of a version string gets passed into this method. This point
		// should contain any sequence of numbers, immediately followed by a 'b' and the
		// build number.
		//
		// The BuildType, if included in the version string, MUST come after the build
		// number, so, while iterating through the version string below, if we find a
		// '-' before a 'b', then we know we have found a BuildType, so there can't be a
		// build number after it.

		for (final char c : point.toCharArray())
			if (c == 'b')
				// This method won't catch a 'b' followed by nothing, which IS an error; you
				// can't declare that your version has a build number and then not include the
				// number in your version string!
				return true;
			else if (!Character.isDigit(c))
				// We return false if we find something, like '-', before we find 'b'.
				return false;
		return false;
	}

	public static boolean hasBuildType(final String point) {
		return point.contains("-");
	}

	public final String version;

	private final int[] points;

	public final int buildNumber;

	public final BuildType buildType;

	public Version(final String version) throws IllegalArgumentException {
		this.version = version;
		final String[] points = version.split("\\.");
		final String lastPoint = points[points.length - 1];
		this.points = new int[points.length];
		try {
			for (int i = 0; i < points.length - 1; i++)
				this.points[i] = Integer.parseInt(points[i]);
		} catch (final NumberFormatException e) {
			throw new IllegalArgumentException("Failed to parse a version from the string, " + version + ".", e);
		}

		String buildNumberText = lastPoint;
		if (hasBuildType(lastPoint)) {
			final String[] parts = lastPoint.split("-");
			buildType = BuildType.valueOf(parts[1].toUpperCase());
			buildNumberText = parts[0];
		} else
			buildType = BuildType.REGULAR;
		if (hasBuildNumber(lastPoint)) {
			final String[] parts = buildNumberText.split("b");
			buildNumber = Integer.parseInt(parts[1]);
			this.points[this.points.length - 1] = Integer.parseInt(parts[0]);
		} else {
			buildNumber = 0;
			this.points[this.points.length - 1] = Integer.parseInt(buildNumberText);
		}

	}

	/**
	 * <p>
	 * Returns a negative number, (<code>< 0</code>), if <b>this</b> object
	 * represents a release <b>before</b> that specified by the parameter,
	 * <code>o</code>.
	 * <p>
	 * Returns a positive number, (<code>> 0</code>), if <b>this</b> object
	 * represents a release <b>after</b> that specified by the parameter.
	 * <p>
	 * Returns <code>0</code> if this object represents the same release as the
	 * parameter.<br>
	 * <br>
	 * <p>
	 * Do note that <b>this method <i>WON'T</i> always return either a 0, 1, or
	 * -1</b>.
	 */
	@Override
	public int compareTo(final Version o) {
		// First we compare the build types.
		if (o.buildType != buildType)
			return buildType.compareTo(o.buildType);

		// Iterate over each point in common.
		final int min = Math.min(o.points.length, points.length);
		for (int i = 0; i < min; i++) {
			final int comparison = ((Integer) points[i]).compareTo(o.points[i]);
			if (comparison == 0)
				continue;
			else
				return comparison;
		}

		// If one version's points extend farther than the others, that version is
		// "greater," unless that version's extra points are all numerically zero...
		//
		// v0.2.5.0.0.0.0b3-BETA
		//
		// is the same as
		//
		// v0.2.5b3-BETA
		//
		//
		// As can be seen, all the points that the first displayed version have more
		// than the second, are 0. This would make the two versions equal. If any of
		// those 0s was to be a different number, the first version would be greater
		// than the second...
		//
		// v0.2.5.0.0.1
		//
		// is greater than
		//
		// v0.2.5
		//
		//
		// The first of these is a slightly later release than the second.

		if (o.points.length != points.length) {
			boolean oMore = o.points.length > points.length;
			int[] pointArr = oMore ? o.points : points;
			for (int i = min; i < pointArr.length; i++)
				if (pointArr[i] > 0)
					return oMore ? -1 : 1;
		}

		// Every pair of points contains equal points, so we continue on to the build
		// number.
		return buildNumber - o.buildNumber;// ((Integer) buildNumber).compareTo(o.buildNumber);

	}

	public String getVersionPoints() {
		String points = "";
		for (int i = 0; i < this.points.length - 1; i++)
			points += this.points[i] + ".";
		return points + this.points[this.points.length - 1];
	}

	@Override
	public String toString() {
		String output = getClass().getCanonicalName() + "[points=[";
		for (int i = 0; i < points.length - 1; i++)
			output += points[i] + ", ";
		return output + points[points.length - 1] + "], buildNumber=" + buildNumber + ", buildType=" + buildType
				+ ", Super={" + super.toString() + "}]";
	}

}
