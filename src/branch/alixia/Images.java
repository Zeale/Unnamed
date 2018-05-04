package branch.alixia;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class Images {

	private final static Queue<LoadItem> loadQueue = new LinkedList<>();

	public final static class LoadItem {

		private final Consumer<Image> action;
		private final InputStream input;

		private LoadItem(ImageView view, InputStream inputStream) {
			this(view::setImage, inputStream);
		}

		private LoadItem(Consumer<Image> action, InputStream input) {
			this.input = input;
			this.action = action;
		}

		public void unqueue() {
			loadQueue.remove(this);
		}

	}

	private static boolean running;

	private static Thread imageLoaderThread = new Thread(new Runnable() {

		@Override
		public synchronized void run() {
			while (!loadQueue.isEmpty()) {
				LoadItem item = loadQueue.poll();
				System.out.println("Started Loading");
				try {
					Image image = new Image(item.input);
					System.out.println("Finished Loading");
					item.action.accept(image);
				} catch (Throwable e) {
				}
			}
			imageLoaderThread = new Thread(this);
			running = false;
		}
	});

	private static void start() {
		if (!running)
			imageLoaderThread.start();
	}

	private Images() {
	}

	public static void loadImageInBackground(ImageView view, String absolutePath) {
		loadImageInBackground(view, Images.class.getResourceAsStream(absolutePath));
	}

	public static void loadImageInBackground(ImageView view, InputStream input) {
		loadQueue.add(new LoadItem(view, input));
		start();
	}

	public static void loadImageInBackground(Consumer<Image> action, InputStream input) {
		loadQueue.add(new LoadItem(action, input));
		start();
	}

}
