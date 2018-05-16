package branch.alixia.unnamed;

import java.io.InputStream;
import java.net.URL;
import java.util.Stack;
import java.util.function.BiConsumer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class Images {

	private final static Stack<LoadItem> loadQueue = new Stack<>();

	// I may publicize this later, if needed.
	public final static class LoadItem {

		private final BiConsumer<Image, Boolean> action;
		private final InputStream input;
		private final URL location;
		private double width, height;

		private static final BiConsumer<Image, Boolean> convertViewToConsumer(ImageView view) {
			return (t, u) -> view.setImage(t);
		}

		private LoadItem(ImageView view, InputStream inputStream) {
			this(convertViewToConsumer(view), inputStream);
		}

		private LoadItem(BiConsumer<Image, Boolean> action, InputStream input) {
			this.input = input;
			this.action = action;
			location = null;
		}

		private LoadItem(BiConsumer<Image, Boolean> action, URL location) {
			this.action = action;
			this.location = location;
			input = null;
		}

		private LoadItem(ImageView view, URL location) {
			action = convertViewToConsumer(view);
			this.location = location;
			input = null;
		}

		public void setWidth(double width) {
			this.width = width;
		}

		public void setHeight(double height) {
			this.height = height;
		}

		/**
		 * @return <code>true</code> if this {@link LoadItem} was queued.
		 */
		public boolean unqueue() {
			return loadQueue.remove(this);
		}

	}

	private static boolean suppressWarnings = true;

	public static void setSuppressWarnings(boolean suppressWarnings) {
		Images.suppressWarnings = suppressWarnings;
	}

	private static Thread imageLoaderThread = new Thread(new Runnable() {

		@Override
		public void run() {

			while (!loadQueue.isEmpty()) {
				try {
					LoadItem item = loadQueue.pop();
					item.action.accept(new Image(item.input == null ? item.location.openStream() : item.input,
							item.width, item.height, false, true), true);
				} catch (Throwable e) {
					if (!suppressWarnings)
						e.printStackTrace();
				}
			}
			imageLoaderThread = new Thread(this);
		}
	});

	private static void start() {
		if (!imageLoaderThread.isAlive())
			imageLoaderThread.start();
	}

	private Images() {
	}

	public static LoadItem loadImageInBackground(ImageView view, String absolutePath) {
		return loadImageInBackground(view, Images.class.getResourceAsStream(absolutePath));
	}

	/**
	 * Loads an {@link Image} in the background, (on a dedicated thread), and gives
	 * the specified {@link ImageView} a randomly selected
	 * <code>Missing Texture</code> icon for the meantime. Once the requested image
	 * is loaded, the specified ImageView is given the loaded image.
	 * <p>
	 * <b>Please note that there is no guarantee that the missing texture icon will
	 * be given to the specified {@link ImageView}</b>. This can occur when
	 * {@link #getRandomMissingTextureIcon()} returns null, due to no missing
	 * texture icon being available.
	 * 
	 * @param view
	 *            The {@link ImageView} to load the {@link Image} into.
	 * @param input
	 *            An {@link InputStream} that the {@link Image} can be read from.
	 */
	public static LoadItem loadImageInBackground(ImageView view, InputStream input) {
		return loadImageInBackground((BiConsumer<Image, Boolean>) (t, u) -> view.setImage(t), input);
	}

	public static LoadItem loadImageInBackground(ImageView view, URL location) {
		return loadImageInBackground((BiConsumer<Image, Boolean>) (t, u) -> view.setImage(t), location);
	}

	public static LoadItem loadImageInBackground(BiConsumer<Image, Boolean> action, URL location) {
		Image randomMissingTextureIcon = getRandomMissingTextureIcon();
		if (randomMissingTextureIcon != null)
			action.accept(randomMissingTextureIcon, false);

		LoadItem item = new LoadItem(action, location);
		loadQueue.push(item);
		start();
		return item;
	}

	public static LoadItem loadImageInBackground(BiConsumer<Image, Boolean> action, URL location, double width,
			double height) {
		LoadItem item = loadImageInBackground(action, location);
		item.setWidth(width);
		item.setHeight(height);
		return item;
	}

	/**
	 * <p>
	 * Loads an {@link Image} in the background, (on a dedicated thread). As soon as
	 * this method is called, the specified {@link BiConsumer} is called with a
	 * randomly selected <code>Missing Texture</code> {@link Image} as its first
	 * argument, and <code>false</code> as its second argument. The
	 * <code>false</code> indicates that this image is not the loaded image.
	 * <p>
	 * Once the requested image is done loading in the background, the specified
	 * {@link BiConsumer} is called with the newly loaded image, as its first
	 * argument, and <code>true</code> as its second argument. The <code>true</code>
	 * signifies that the BiConsumer is being called with the requested image.
	 * <p>
	 * <b>Please note that there is no guarantee that the missing texture icon will
	 * be given to the specified {@link BiConsumer}</b>. This can occur when
	 * {@link #getRandomMissingTextureIcon()} returns null, due to no missing
	 * texture icon being available.
	 * 
	 * @param action
	 *            The {@link BiConsumer} that will handle the images.
	 * @param input
	 *            An {@link InputStream} which will be used to obtain the image
	 *            loaded in the background.
	 */
	public static LoadItem loadImageInBackground(BiConsumer<Image, Boolean> action, InputStream input) {
		Image randomMissingTextureIcon = getRandomMissingTextureIcon();
		if (randomMissingTextureIcon != null)
			action.accept(randomMissingTextureIcon, false);

		LoadItem item = new LoadItem(action, input);
		loadQueue.push(item);
		start();
		return item;

	}

	private static Image[] getMissingTextureIcons() {
		return MISSING_TEXTURE_IMAGES;
	}

	public static final String GRAPHICS_LOCATION = "/branch/alixia/kröw/unnamed/resources/graphics/";

	// This array's initialization may need to be run in a dedicated thread if too
	// many large images are being used as Missing Texture Graphics.
	private static final Image[] MISSING_TEXTURE_IMAGES = new Image[] {
			new Image(GRAPHICS_LOCATION + "Missing Texture 1.png"),
			new Image(GRAPHICS_LOCATION + "Missing Texture 2.png") };

	public static Image getRandomMissingTextureIcon() {
		Image[] missingTextureIcons = getMissingTextureIcons();

		return missingTextureIcons.length == 0 ? null
				: missingTextureIcons[(int) (Math.random() * missingTextureIcons.length)];
	}

}
