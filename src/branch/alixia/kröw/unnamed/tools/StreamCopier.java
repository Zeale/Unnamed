package branch.alixia.kröw.unnamed.tools;

import java.io.InputStream;
import java.io.OutputStream;

import javafx.beans.value.WritableValue;

public class StreamCopier {

	private final WritableValue<Number> progress;
	private final InputStream input;
	private final OutputStream output;
	private final double inputSize;

	public StreamCopier(double inputSize, WritableValue<Number> progress, InputStream input, OutputStream output) {
		this.progress = progress;
		this.input = input;
		this.output = output;
		this.inputSize = inputSize;

		start();

	}

	private Throwable exception;

	public Throwable getException() {
		return exception;
	}

	private boolean started;

	private final Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {

			try {

				int bufferLength = 1024;
				long totalReadData = 0;

				byte[] buffer = new byte[bufferLength];
				int amount;
				while ((amount = input.read(buffer)) != -1) {
					totalReadData += amount;
					output.write(buffer, 0, amount);
					progress.setValue(totalReadData / inputSize);
				}
			} catch (Throwable e) {
				exception = e;
			}
		}
	});

	public void start() {
		if (!started) {
			started = true;
			thread.start();
		}
	}

}
