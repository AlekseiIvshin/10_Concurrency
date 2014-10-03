package concurrency.queuestorages.files;

import java.io.File;

/**
 * Interface for getting file from queue.
 * @author Aleksei_Ivshin
 *
 */
public interface FileGetter {
	/**
	 * Get next file.
	 * @return next file. if return null it means that queue is closed (or interrupted)
	 */
	File getNextFile();
}
