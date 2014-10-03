package concurrency.queuestorages.files;

import java.io.File;

public interface FileSetter {

	/**
	 * Set file to queue.
	 * @param f new file
	 * @return true - all right, false - queue was closed(interrupted)
	 */
	boolean setFile(File f);
}
